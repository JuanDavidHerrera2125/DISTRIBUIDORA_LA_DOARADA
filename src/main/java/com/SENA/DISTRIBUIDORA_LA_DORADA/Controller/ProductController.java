package com.SENA.DISTRIBUIDORA_LA_DORADA.Controller;

import com.SENA.DISTRIBUIDORA_LA_DORADA.DTO.ProductStockDto;
import com.SENA.DISTRIBUIDORA_LA_DORADA.DTO.StockDto;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Product;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Stock;
import com.SENA.DISTRIBUIDORA_LA_DORADA.IService.IProductService;
import com.SENA.DISTRIBUIDORA_LA_DORADA.IService.IStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*") // Ajusta según tu frontend (ej: "http://localhost:8080")
public class ProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private IStockService stockService;


    /**
     * Obtener todos los productos activos
     */
    @GetMapping
    public List<Product> getAll() {
        return productService.findAll();
    }

    /**
     * Obtener un producto por ID
     */
    @GetMapping("/{id}")
    public Optional<Product> findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    /**
     * Guardar un nuevo producto
     */
    @PostMapping
    public Product save(@RequestBody Product product) {
        return productService.save(product);
    }

    /**
     * Actualizar un producto por ID
     */
    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    /**
     * Eliminar un producto por ID
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }

    // Buscar si existe producto igual
    @PostMapping("/create-with-stock")
    public Product createProductWithStock(@RequestBody Product product) {
        // Buscar si existe producto igual
        Optional<Product> existingProduct = productService.findByNameAndUnitPrice(
                product.getName(),
                product.getUnitPrice()
        );

        if (existingProduct.isPresent()) {
            // Si ya existe, actualizamos stock
            Product existing = existingProduct.get();

            // Buscar stock actual
            Stock existingStock = stockService.findByProductId(existing.getId())
                    .orElseThrow(() -> new RuntimeException("Stock no encontrado para el producto existente"));

            // Sumamos el stock inicial enviado
            int stockToAdd = product.getInitialStock() != null ? product.getInitialStock() : 0;
            existingStock.setCurrentStock(existingStock.getCurrentStock() + stockToAdd);
            stockService.save(existingStock);

            return existing; // devolvemos el producto ya existente
        }

        // Si no existe, lo guardamos como nuevo
        Product savedProduct = productService.save(product);

        // Creamos el registro en Stock
        Stock stock = new Stock();
        stock.setProduct(savedProduct);
        stock.setCurrentStock(product.getInitialStock());
        stockService.save(stock);

        return savedProduct;
    }

    @GetMapping("/with-stock")
    public List<ProductStockDto> getAllProductsWithStock() {
        List<ProductStockDto> dtos = new ArrayList<>();

        // Obtener todos los productos (o solo activos)
        List<Product> products = productService.findAll(); // Asegúrate de que este método exista

        for (Product product : products) {
            if (!product.getActive()) continue; // Opcional: filtrar solo activos

            // Buscar stock
            Integer currentStock = 0;
            Optional<Stock> stockOpt = stockService.findByProductId(product.getId());
            if (stockOpt.isPresent()) {
                currentStock = stockOpt.get().getCurrentStock();
            }

            // Mapear a DTO
            ProductStockDto dto = new ProductStockDto();
            dto.setProductId(product.getId());
            dto.setName(product.getName());
            dto.setModel(product.getModel());           // ← Este es el campo que faltaba
            dto.setUnitPrice(product.getUnitPrice());
            dto.setCurrentStock(currentStock);
            dto.setDescription(product.getDescription());
            dto.setTotalStock(currentStock); // Puedes cambiar si hay lógica más compleja

            dtos.add(dto);
        }

        return dtos;
    }
}