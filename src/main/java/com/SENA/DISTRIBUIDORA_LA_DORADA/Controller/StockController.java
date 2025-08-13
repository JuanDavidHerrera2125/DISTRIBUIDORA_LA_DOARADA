// StockController.java

package com.SENA.DISTRIBUIDORA_LA_DORADA.Controller;

import com.SENA.DISTRIBUIDORA_LA_DORADA.DTO.StockDto;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Stock;
import com.SENA.DISTRIBUIDORA_LA_DORADA.IService.IStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/stocks")
@CrossOrigin(origins = "*")
public class StockController {

    @Autowired
    private IStockService stockService;

    /**
     * Obtener todos los stocks
     */
    @GetMapping
    public List<Stock> getAll() {
        return stockService.findAll();
    }

    /**
     * Obtener stock por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Stock> findById(@PathVariable Long id) {
        return stockService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtener stock por ID de producto
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<Stock> findByProductId(@PathVariable Long productId) {
        return stockService.findByProductId(productId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Guardar un nuevo stock (usando StockDto)
     */
    @PostMapping
    public ResponseEntity<Stock> save(@RequestBody StockDto dto) {
        try {
            Stock saved = stockService.saveFromDto(dto);
            return ResponseEntity.status(201).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Actualizar stock por ID
     */
    @PutMapping("/{id}")
    public ResponseEntity<Stock> update(@PathVariable Long id, @RequestBody Stock stock) {
        try {
            Stock updated = stockService.updateStock(id, stock);
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Eliminar stock por ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            stockService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}