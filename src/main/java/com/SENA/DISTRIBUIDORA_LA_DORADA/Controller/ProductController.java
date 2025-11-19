package com.SENA.DISTRIBUIDORA_LA_DORADA.Controller;

import com.SENA.DISTRIBUIDORA_LA_DORADA.DTO.ProductStockDto;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Product;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Stock;
import com.SENA.DISTRIBUIDORA_LA_DORADA.IService.IProductService;
import com.SENA.DISTRIBUIDORA_LA_DORADA.IService.IStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private IProductService productService;

    @Autowired
    private IStockService stockService;

    @GetMapping
    public List<Product> getAll() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Product> findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping
    public Product save(@RequestBody Product product) {
        return productService.save(product);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }

    @PostMapping("/create-with-stock")
    public Product createProductWithStock(@RequestBody Product product) {
        // Busca por nombre + diseño, nunca por unitPrice
        return productService.findByNameAndModel(product.getName(), product.getModel())
                .map(existing -> {
                    int stockToAdd = product.getInitialStock() != null ? product.getInitialStock() : 0;
                    if (stockToAdd > 0) stockService.saveOrUpdateStock(existing, stockToAdd);
                    return existing;
                })
                .orElseGet(() -> productService.save(product));
    }


    @GetMapping("/with-stock")
    public List<ProductStockDto> getAllProductsWithStock() {
        List<ProductStockDto> dtos = new ArrayList<>();
        List<Product> products = productService.findAll();

        for (Product product : products) {
            if (!product.getActive()) continue;

            // Obtener stock total sumando todos los registros de stock
            int totalStock = stockService.findTotalStockByProductId(product.getId());

            ProductStockDto dto = new ProductStockDto();
            dto.setProductId(product.getId());
            dto.setName(product.getName());
            dto.setModel(product.getModel());
            dto.setUnitPrice(product.getUnitPrice());
            dto.setDescription(product.getDescription());
            dto.setCurrentStock(totalStock);
            dto.setTotalStock(totalStock);

            dtos.add(dto);
        }
        return dtos;
    }


}
