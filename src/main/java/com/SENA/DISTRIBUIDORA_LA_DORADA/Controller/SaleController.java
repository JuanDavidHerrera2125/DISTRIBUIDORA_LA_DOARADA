package com.SENA.DISTRIBUIDORA_LA_DORADA.Controller;

import com.SENA.DISTRIBUIDORA_LA_DORADA.DTO.SaleDetailDto;
import com.SENA.DISTRIBUIDORA_LA_DORADA.DTO.SaleRequestDto;
import com.SENA.DISTRIBUIDORA_LA_DORADA.DTO.SaleResponseDto;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.*;
import com.SENA.DISTRIBUIDORA_LA_DORADA.IService.ISaleDetailService;
import com.SENA.DISTRIBUIDORA_LA_DORADA.IService.ISaleService;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Service.ClientService;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Service.ProductService;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Service.SaleService;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Service.StockService;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Converter.SaleDtoConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sales")
public class SaleController {

    @Autowired
    private ISaleDetailService saleDetailService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SaleService saleService;

    @Autowired
    private StockService stockService;

    @Autowired
    private ISaleService service;

    @Autowired
    private SaleDtoConverter saleDtoConverter;

    @GetMapping
    public List<SaleResponseDto> all() {
        return service.findAll()
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public ResponseEntity<SaleResponseDto> findById(@PathVariable Long id) {
        Optional<Sale> optionalSale = service.findById(id);
        if (optionalSale.isPresent()) {
            SaleResponseDto dto = convertToResponseDto(optionalSale.get());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Sale save(@RequestBody Sale sale) {
        return service.save(sale);
    }

    @PutMapping("{id}")
    public Sale update(@PathVariable Long id, @RequestBody Sale sale) {
        return service.updateSale(id, sale);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @PostMapping("/create")
    public ResponseEntity<SaleResponseDto> createSale(@RequestBody SaleRequestDto request) {
        // 1️⃣ Buscar o crear cliente
        Client client = clientService.findByPhoneOrSave(request.getClientPhone(), request);

        // 2️⃣ Procesar nombre y modelo del producto
        String productName = request.getProductName();
        String name;
        String model;

        if (productName.contains(" - ")) {
            String[] parts = productName.split(" - ", 2);
            name = parts[0].trim();
            model = parts[1].trim();
        } else {
            name = productName.trim();
            model = "GEN-" + System.currentTimeMillis();
        }

        // 3️⃣ Buscar producto
        Product product = productService.findByNameAndModel(name, model)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + request.getProductName()));

        // 4️⃣ Validar stock
        Stock stock = stockService.findByProduct(product)
                .orElseThrow(() -> new RuntimeException("Stock no encontrado para el producto"));

        if (stock.getCurrentStock() < request.getQuantity()) {
            throw new RuntimeException("Stock insuficiente para " + product.getName() +
                    ". Disponible: " + stock.getCurrentStock());
        }

        // 5️⃣ Crear venta
        Sale sale = new Sale();
        sale.setClient(client);
        sale.setDate(new Date());
        sale.setTotal(request.getQuantity() * request.getUnitPrice());

        sale = saleService.save(sale);

        // 6️⃣ Crear detalle
        SaleDetail detail = SaleDetail.builder()
                .sale(sale)
                .product(product)
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .subtotal(request.getQuantity() * request.getUnitPrice())
                .build();

        saleDetailService.save(detail);

        // 7️⃣ Actualizar stock
        stock.setCurrentStock(stock.getCurrentStock() - request.getQuantity());
        stockService.save(stock);

        // 8️⃣ Convertir a DTO y devolver
        SaleResponseDto responseDto = convertToResponseDto(sale);

        return ResponseEntity.ok(responseDto);
    }

    private SaleResponseDto convertToResponseDto(Sale sale) {
        List<SaleDetailDto> detailsDto = (sale.getDetails() != null) ?
                sale.getDetails().stream()
                        .map(detail -> {
                            SaleDetailDto dto = new SaleDetailDto();
                            dto.setProductName(detail.getProduct().getName());
                            dto.setProductModel(detail.getProduct().getModel());
                            dto.setQuantity(detail.getQuantity());
                            dto.setUnitPrice(detail.getUnitPrice());
                            dto.setSubtotal(detail.getSubtotal());
                            return dto;
                        })
                        .collect(Collectors.toList()) : new ArrayList<>();

        SaleResponseDto response = new SaleResponseDto();
        response.setId(sale.getId());
        response.setClientName(sale.getClient().getName());
        response.setClientPhone(sale.getClient().getPhone());
        response.setClientAddress(sale.getClient().getAddress());
        response.setDate(sale.getDate().toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDateTime());
        response.setTotal(sale.getTotal());
        response.setDetails(detailsDto);
        response.setStatus(sale.getStatus());
        return response;
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancelSale(@PathVariable Long id) {
        Sale sale = saleService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Venta no encontrada"));

        if ("CANCELLED".equalsIgnoreCase(sale.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La venta ya fue cancelada");
        }

        // ✅ Devolver stock por cada detalle
        for (SaleDetail detail : sale.getDetails()) {
            Product product = detail.getProduct();

            // ✅ Llama directamente a increaseStock en StockService
            stockService.increaseStock(product.getId(), detail.getQuantity());
        }

        // ✅ Marcar como cancelada
        sale.setStatus("CANCELLED");
        saleService.save(sale); // Guarda el estado, pero no debe afectar stock

        return ResponseEntity.ok("Venta cancelada y stock devuelto correctamente");
    }

    // NUEVAS PETICIONES DETALLADAS

    // 🔹 Endpoint para obtener las ventas de hoy
    @GetMapping("/today-sales")
    public ResponseEntity<Long> getTodaySalesCount(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {
        LocalDate targetDate = (date != null) ? date : LocalDate.now();
        List<Sale> sales = saleService.findByDate(targetDate);
        return ResponseEntity.ok((long) sales.size());
    }

    // 🔹 Endpoint para obtener los ingresos de hoy
    @GetMapping("/today-income")
    public ResponseEntity<Double> getTodayIncome(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {
        LocalDate targetDate = (date != null) ? date : LocalDate.now();
        Double income = saleService.findTodayIncome(targetDate);
        return ResponseEntity.ok(income != null ? income : 0.0);
    }

    // 🔹 Endpoint para obtener el número de clientes registrados
    @GetMapping("/clients-count")
    public ResponseEntity<Long> getClientsCount() {
        long count = clientService.countAllClients();
        return ResponseEntity.ok(count);
    }

    // 🔹 Endpoint para obtener el número de productos activos
    @GetMapping("/products-count")
    public ResponseEntity<Long> getActiveProductsCount() {
        long count = productService.countActiveProducts();
        return ResponseEntity.ok(count);
    }

    // 🔹 Endpoint para obtener información rápida
    @GetMapping("/quick-info")
    public ResponseEntity<Map<String, Object>> getQuickInfo() {
        LocalDate today = LocalDate.now();

        Map<String, Object> info = new HashMap<>();
        info.put("stockDisponible", productService.countStockAvailable());
        info.put("productosActivos", productService.countActiveProducts());
        info.put("clientesRegistrados", clientService.countAllClients());
        info.put("ventasDelDia", (long) saleService.findByDate(today).size());
        info.put("ingresosDelDia", saleService.findTodayIncome(today) != null ?
                saleService.findTodayIncome(today) : 0.0);

        return ResponseEntity.ok(info);
    }
}
