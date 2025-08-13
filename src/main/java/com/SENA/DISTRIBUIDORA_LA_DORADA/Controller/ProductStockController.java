package com.SENA.DISTRIBUIDORA_LA_DORADA.Controller;

import com.SENA.DISTRIBUIDORA_LA_DORADA.DTO.ProductStockDto;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Service.ProductStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-stock")
@CrossOrigin(origins = "*")
public class ProductStockController {


    @Autowired
    private ProductStockService productStockService;


    @GetMapping
    public List<ProductStockDto> getAllProductStocks() {
        return productStockService.getAllProductsWithStock();
    }


}
