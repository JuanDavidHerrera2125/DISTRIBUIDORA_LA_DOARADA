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

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product save(Product product) {
        Double price = calculateUnitPrice(product.getName(), product.getModel());
        product.setUnitPrice(price);
        Product savedProduct = productRepository.save(product);

        if (product.getInitialStock() != null && product.getInitialStock() > 0) {
            saveOrUpdateStock(savedProduct, product.getInitialStock());
        }

        return savedProduct;
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
    public Stock getStockByProductId(Long productId) {
        return stockRepository.findByProductId(productId).orElse(null);
    }

    @Override
    public Stock findByProductId(Long productId) {
        return stockRepository.findByProductId(productId).orElse(null);
    }

    @Override
    public void delete(Long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setDescription(product.getDescription());
                    existingProduct.setModel(product.getModel());
                    Double price = calculateUnitPrice(product.getName(), product.getModel());
                    existingProduct.setUnitPrice(price);
                    existingProduct.setRegistrationDate(product.getRegistrationDate());
                    existingProduct.setActive(product.getActive());
                    return productRepository.save(existingProduct);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
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
        stockRepository.findByProductId(product.getId())
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
    public void decreaseStock(Long productId, int quantity) {
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("No se encontró stock para el producto con ID " + productId));

        if (stock.getCurrentStock() < quantity) {
            throw new RuntimeException("Stock insuficiente para el producto con ID " + productId);
        }

        stock.setCurrentStock(stock.getCurrentStock() - quantity);
        stockRepository.save(stock);
    }

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
        Double basePrice = BASE_PRICES.getOrDefault(name, 0.0);
        Double designPrice = DESIGN_PRICES.getOrDefault(model, 0.0);
        return basePrice + designPrice;
    }
}
