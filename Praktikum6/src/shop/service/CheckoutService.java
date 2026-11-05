package shop.service;

import java.util.List;
import shop.model.Product;
import shop.payment.PaymentMethod;
import shop.shipping.Shippable;
import shop.shipping.ShippingService;
import shop.payment.RefundablePayment;

public class CheckoutService {

    public long subtotal(List<Product> items) {
        long total = 0;
        for (Product product : items) {
            total += product.getFinalPrice();
        }
        return total;
    }

    public int totalWeight(List<Product> items) {
        int grams = 0;
        for (Product product : items) {
            if (product instanceof Shippable shippable) {
                grams += shippable.getWeightGrams();
            }
        }
        return grams;
    }

    public boolean refund(RefundablePayment payment, long amount) {
        return payment.refund(amount);
    }

    public long total(List<Product> items, ShippingService shipping) {
        long subtotal = subtotal(items);
        int weightGrams = totalWeight(items);
        long shippingCost = shipping.calculateCost(weightGrams);
        return subtotal + shippingCost;
    }

    public long grandTotal(List<Product> items, ShippingService shipping, PaymentMethod payment) {
        long total = total(items, shipping);
        return total + payment.adminFee(total);
    }

    public boolean checkout(List<Product> items, ShippingService shipping, PaymentMethod payment) {
        if (shipping == null || payment == null || items == null || items.isEmpty()) {
            return false;
        }

        long grandTotal = grandTotal(items, shipping, payment);
        return payment.pay(grandTotal);
    }
}
