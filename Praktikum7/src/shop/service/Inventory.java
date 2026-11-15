package shop.service;

import java.util.HashMap;
import java.util.Map;
import shop.exception.OutOfStockException;

/** Stock per SKU. Product objects stay immutable; the stock lives here. */
public class Inventory {
    private final Map<String, Integer> stock = new HashMap<>();

    public void addStock(String sku, int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        stock.put(sku, available(sku) + qty);
    }

    public int available(String sku) {
        return stock.getOrDefault(sku, 0);
    }

    public void reserve(String sku, int qty) throws OutOfStockException {
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        int current = available(sku);
        if (qty > current) {
            throw new OutOfStockException(sku, qty, current);
        }
        stock.put(sku, current - qty);
    }

    public void release(String sku, int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        stock.put(sku, available(sku) + qty);
    }
}