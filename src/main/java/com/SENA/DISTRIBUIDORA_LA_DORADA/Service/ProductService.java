package com.SENA.DISTRIBUIDORA_LA_DORADA.Service;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Product;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Stock;
import com.SENA.DISTRIBUIDORA_LA_DORADA.IService.IProductService;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Repository.ProductRepository;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Repository.StockRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockService stockService;

    private static final Map<String, Double> BASE_PRICES = Map.of(
            "Silla Mesedora", 100000.0,
            "Silla Fija", 90000.0,
            "Silla Barra", 110000.0,
            "Silla Sala", 95000.0,
            "Silla Huevo", 85000.0,
            "Silla Columpio", 105000.0,
            "Silla Pequeña", 75000.0,
            "Silla Brazona", 120000.0
    );

    private static final Map<String, Double> DESIGN_PRICES = Map.of(
            "Wuayú", 80000.0,
            "Canasta", 60000.0,
            "Sencilla", 40000.0,
            "Extra Grande", 100000.0,
            "Fútbol", 50000.0,
            "Imagen", 75000.0
    );

    private Double calculateUnitPrice(String name, String model) {
        return BASE_PRICES.getOrDefault(name, 0.0) + DESIGN_PRICES.getOrDefault(model, 0.0);
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Optional<Stock> getStockByProductId(Long productId) {
        return Optional.empty();
    }

    @Override
    public Optional<Stock> findByProductId(Long productId) {
        return Optional.empty();
    }

    @Override
    public Product save(Product product) {
        // Calcula unitPrice
        product.setUnitPrice(calculateUnitPrice(product.getName(), product.getModel()));
        Product saved = productRepository.save(product);

        // Crear stock si inicialStock > 0
        if (product.getInitialStock() != null && product.getInitialStock() > 0) {
            saveOrUpdateStock(saved, product.getInitialStock());
        }
        return saved;
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        return productRepository.findById(id)
                .map(existing -> {
                    existing.setName(product.getName());
                    existing.setDescription(product.getDescription());
                    existing.setModel(product.getModel());
                    existing.setUnitPrice(calculateUnitPrice(product.getName(), product.getModel()));
                    existing.setRegistrationDate(product.getRegistrationDate());
                    existing.setActive(product.getActive());
                    return productRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Optional<Product> findByNameAndUnitPrice(String name, Double unitPrice) {
        return productRepository.findByNameAndUnitPrice(name, unitPrice);
    }

    @Override
    public Optional<Product> findByNameAndModel(String name, String model) {
        return productRepository.findByNameAndModel(name, model);
    }

    @Transactional
    public void saveOrUpdateStock(Product product, int stockToAdd) {
        stockRepository.findByProduct_Id(product.getId())
                .ifPresentOrElse(stock -> {
                    stock.setCurrentStock(stock.getCurrentStock() + stockToAdd);
                    stockRepository.save(stock);
                }, () -> {
                    Stock newStock = new Stock();
                    newStock.setProduct(product);
                    newStock.setCurrentStock(stockToAdd);
                    stockRepository.save(newStock);
                });
    }

    @Transactional
    public void decreaseStock(Long productId, int quantity) {
        Stock stock = stockRepository.findByProduct_Id(productId)
                .orElseThrow(() -> new RuntimeException("Stock no encontrado para el producto con ID: " + productId));

        if (stock.getCurrentStock() < quantity) {
            throw new RuntimeException("Stock insuficiente para el producto con ID: " + productId);
        }

        stock.setCurrentStock(stock.getCurrentStock() - quantity);
        stockRepository.save(stock);
    }
}
