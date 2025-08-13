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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
        return service.findAll()  // List<Sale>
                .stream()
                .map(this::convertToResponseDto)  // Convertir cada Sale a SaleResponseDto
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
        System.out.println("Recibido request: " + request);
        // 1. Buscar o crear cliente
        Client client = clientService.findByPhoneOrSave(request.getClientPhone(), request);

        // 2. Procesar el nombre del producto
        String productName = request.getProductName();
        String name;
        String model;

        if (productName.contains(" - ")) {
            String[] parts = productName.split(" - ", 2);
            name = parts[0].trim();
            model = parts[1].trim();
        } else {
            // Si no tiene formato, usar el nombre completo como nombre y generar modelo básico
            name = productName.trim();
            model = "GEN-" + System.currentTimeMillis(); // Modelo generado automáticamente
        }

        // 3. Buscar producto por nombre y modelo
        Product product = productService.findByNameAndModel(name, model)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + request.getProductName()));

        // 4. Validar stock
        Stock stock = stockService.findByProduct(product)
                .orElseThrow(() -> new RuntimeException("Stock no encontrado para el producto"));

        if (stock.getCurrentStock() < request.getQuantity()) {
            throw new RuntimeException("Stock insuficiente para " + product.getName() +
                    ". Disponible: " + stock.getCurrentStock());
        }

        // 5. Crear venta
        Sale sale = new Sale();
        sale.setClient(client);
        sale.setDate(new Date()); // java.util.Date
        sale.setTotal(request.getQuantity() * request.getUnitPrice());

        sale = saleService.save(sale);

        // 6. Crear detalle
        SaleDetail detail = SaleDetail.builder()
                .sale(sale)
                .product(product)
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .subtotal(request.getQuantity() * request.getUnitPrice())
                .build();

        // Guardar el detalle
        saleDetailService.save(detail);

        // 7. Actualizar stock
        stock.setCurrentStock(stock.getCurrentStock() - request.getQuantity());
        stockService.save(stock);

        // 8. Convertir a DTO y devolver
        SaleResponseDto responseDto = convertToResponseDto(sale);

        return ResponseEntity.ok(responseDto);
    }

    // Método para convertir entidad Sale a DTO con detalles convertidos correctamente
    private SaleResponseDto convertToResponseDto(Sale sale) {
        // ✅ Manejo seguro de null para evitar NullPointerException
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

        // Opcional: estado de la venta, si existe en entidad Sale
        response.setStatus(sale.getStatus());

        return response;
    }

    // Cancelar pedido
    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancelSale(@PathVariable Long id) {
        Sale sale = saleService.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        if ("CANCELLED".equalsIgnoreCase(sale.getStatus())) {
            throw new RuntimeException("La venta ya fue cancelada anteriormente");
        }

        // Devolver stock por cada detalle
        for (SaleDetail detail : sale.getDetails()) {
            Product product = detail.getProduct();
            stockService.saveOrUpdateStock(product, detail.getQuantity());
        }

        // Marcar como cancelada
        sale.setStatus("CANCELLED");
        saleService.save(sale);

        return ResponseEntity.ok("Venta cancelada y stock devuelto");
    }
}