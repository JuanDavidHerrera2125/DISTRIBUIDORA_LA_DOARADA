package com.SENA.DISTRIBUIDORA_LA_DORADA.Service;

import com.SENA.DISTRIBUIDORA_LA_DORADA.DTO.ProductStockDto;
import com.SENA.DISTRIBUIDORA_LA_DORADA.DTO.StockDto;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Product;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Stock;
import com.SENA.DISTRIBUIDORA_LA_DORADA.IService.IStockService;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Repository.ProductRepository;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Repository.StockRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StockService implements IStockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Stock> findAll() {
        return stockRepository.findAll();
    }

    @Override
    public Optional<Stock> findById(Long id) {
        return stockRepository.findById(id);
    }

    @Override
    public Stock save(Stock stock) {
        return stockRepository.save(stock);
    }

    @Override
    public void delete(Long id) {
        if (!stockRepository.existsById(id)) {
            throw new NoSuchElementException("Stock no encontrado con ID: " + id);
        }
        stockRepository.deleteById(id);
    }

    @Override
    public Stock updateStock(Long id, Stock stock) {
        Stock existing = stockRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Stock no encontrado con ID: " + id));

        existing.setCurrentStock(stock.getCurrentStock());
        return stockRepository.save(existing);
    }

    @Override
    public Stock saveFromDto(StockDto dto) {
        if (dto == null || dto.getProductId() == null) {
            throw new IllegalArgumentException("Datos incompletos para crear stock");
        }

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado con ID: " + dto.getProductId()));

        return stockRepository.findByProduct_Id(product.getId())
                .map(existingStock -> {
                    existingStock.setCurrentStock(dto.getCurrentStock());
                    return stockRepository.save(existingStock);
                })
                .orElseGet(() -> {
                    Stock newStock = new Stock();
                    newStock.setProduct(product);
                    newStock.setCurrentStock(dto.getCurrentStock());
                    return stockRepository.save(newStock);
                });
    }

    @Override
    public List<ProductStockDto> getGroupedProductStock() {
        List<Stock> allStocks = stockRepository.findAll();
        Map<String, ProductStockDto> map = new HashMap<>();

        for (Stock stock : allStocks) {
            Product product = stock.getProduct();
            String modelValue = product.getModel() != null ? product.getModel() : "Sin diseño";
            String key = product.getName() + "|" + modelValue;

            ProductStockDto dto = map.get(key);
            if (dto == null) {
                dto = new ProductStockDto();
                dto.setProductId(product.getId());
                dto.setName(product.getName());
                dto.setModel(modelValue);
                dto.setUnitPrice(product.getUnitPrice() != null ? product.getUnitPrice() : 0.0);
                dto.setDescription(product.getDescription() != null ? product.getDescription() : "");
                dto.setTotalStock(0);
                map.put(key, dto);
            }

            Integer current = stock.getCurrentStock() != null ? stock.getCurrentStock() : 0;
            dto.setTotalStock(dto.getTotalStock() + current);
        }

        map.values().forEach(dto -> dto.setCurrentStock(dto.getTotalStock()));
        return new ArrayList<>(map.values());
    }

    @Override
    public Optional<Stock> findByProduct(Product product) {
        return stockRepository.findByProduct(product);
    }

    @Override
    public Optional<Stock> findByProductId(Long productId) {
        return stockRepository.findByProduct_Id(productId);
    }

    @Transactional
    @Override
    public void saveOrUpdateStock(Product product, int stockToAdd) {
        if (product == null) throw new IllegalArgumentException("El producto no puede ser nulo");
        if (stockToAdd < 0) throw new IllegalArgumentException("La cantidad a agregar no puede ser negativa");

        stockRepository.findByProduct_Id(product.getId())
                .ifPresentOrElse(
                        stock -> {
                            stock.setCurrentStock(stock.getCurrentStock() + stockToAdd);
                            stockRepository.save(stock);
                        },
                        () -> {
                            Stock newStock = new Stock();
                            newStock.setProduct(product);
                            newStock.setCurrentStock(stockToAdd);
                            stockRepository.save(newStock);
                        }
                );
    }

    @Transactional
    @Override
    public void decreaseStock(Long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad a descontar debe ser mayor a 0");
        }

        Stock stock = stockRepository.findByProduct_Id(productId)
                .orElseThrow(() -> new RuntimeException("No se encontró stock para el producto con ID: " + productId));

        if (stock.getCurrentStock() < quantity) {
            throw new RuntimeException("Stock insuficiente para el producto con ID: " + productId +
                    ". Disponible: " + stock.getCurrentStock() + ", solicitado: " + quantity);
        }

        stock.setCurrentStock(stock.getCurrentStock() - quantity);
        stockRepository.save(stock);
    }

    @Override
    public int findTotalStockByProductId(Long productId) {
        return stockRepository.findByProduct_Id(productId)
                .map(Stock::getCurrentStock)
                .orElse(0);
    }

    @Transactional
    public void increaseStock(Long productId, Integer quantity) {
        Stock stock = stockRepository.findByProduct_Id(productId)
                .orElseThrow(() -> new RuntimeException("Stock no encontrado para el producto: " + productId));

        stock.setCurrentStock(stock.getCurrentStock() + quantity);
        stockRepository.save(stock);
    }
}
