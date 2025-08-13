package com.SENA.DISTRIBUIDORA_LA_DORADA.Service;

import com.SENA.DISTRIBUIDORA_LA_DORADA.DTO.ProductStockDto;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Product;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Stock;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Repository.ProductRepository;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductStockService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    public List<ProductStockDto> getAllProductsWithStock() {
        List<Product> products = productRepository.findAll();
        List<Stock> stocks = stockRepository.findAll();

        // Mapear productId a stock actual para acceso rápido
        Map<Long, Integer> stockMap = stocks.stream()
                .collect(Collectors.toMap(s -> s.getProduct().getId(), Stock::getCurrentStock));

        List<ProductStockDto> dtos = new ArrayList<>();
        for (Product p : products) {
            Integer stock = stockMap.getOrDefault(p.getId(), 0);

            ProductStockDto dto = new ProductStockDto();
            dto.setProductId(p.getId());
            dto.setName(p.getName());
            dto.setUnitPrice(p.getUnitPrice());
            dto.setCurrentStock(stock);

            dtos.add(dto);
        }
        return dtos;
    }
}
