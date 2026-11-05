package review.fixed;

import java.util.ArrayList;
import java.util.List;

/*
 * T3 - Versi perbaikan BadShop.
 * Setiap bagian diberi tanda PERBAIKAN sesuai nomor di tabel code review.
 */

// PERBAIKAN 1: interface gemuk dipecah menjadi kontrak kecil
interface Payable {
    boolean pay(long amount);

    String printInvoice();
}

interface Refundable extends Payable {
    boolean refund(long amount);
}

interface Shippable {
    long ship(int weightGrams);
}

// Voucher hanya bisa dibayar; tidak ada refund() atau ship() yang menipu
class GiftVoucher implements Payable {
    private long balance = 100_000; // uang memakai long (rupiah)

    @Override
    public boolean pay(long amount) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    @Override
    public String printInvoice() {
        return "sisa Rp" + balance;
    }
}

// PERBAIKAN 2 dan 3: abstract method untuk tarif, rumus dikunci final
abstract class Courier {
    protected abstract long ratePerKg();

    public final long calculateCost(int kilograms) {
        return kilograms * ratePerKg();
    }
}

class RegularCourier extends Courier {
    @Override
    protected long ratePerKg() {
        return 10_000;
    }
}

class ExpressCourier extends Courier {
    @Override
    protected long ratePerKg() {
        return 18_000;
    }
}

class Cart {
    private final List<Item> items = new ArrayList<>();

    // PERBAIKAN 4: static nested class karena tidak membaca field Cart
    static class Item {
        private final String name;
        private final long price;
        private final int quantity;

        Item(String name, long price, int quantity) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        long subtotal() {
            return price * quantity;
        }

        String getName() {
            return name;
        }
    }

    void add(Item item) {
        items.add(item);
    }

    long total() {
        long total = 0;
        for (Item item : items) {
            total += item.subtotal();
        }
        return total;
    }
}

// PERBAIKAN 5: Address immutable (final class, field private final, tanpa setter)
final class Address {
    private final String street;
    private final String city;

    Address(String street, String city) {
        this.street = street;
        this.city = city;
    }

    // mengubah kota = membuat objek Address baru
    Address withCity(String newCity) {
        return new Address(street, newCity);
    }

    @Override
    public String toString() {
        return street + ", " + city;
    }
}

class SimpleOrder {
    private final String id;
    private Address address;

    SimpleOrder(String id, Address address) {
        this.id = id;
        this.address = address;
    }

    Address getAddress() {
        return address;
    }

    void changeAddress(Address newAddress) {
        this.address = newAddress;
    }

    @Override
    public String toString() {
        return id + " -> " + address;
    }
}

// PERBAIKAN 6: tanpa instanceof; tiap diskon hanya menentukan persentasenya
interface Discount {
    int percent();

    default long apply(long price) {
        return price - price * percent() / 100;
    }
}

class MemberDiscount implements Discount {
    @Override
    public int percent() {
        return 5;
    }
}

class FlashSaleDiscount implements Discount {
    @Override
    public int percent() {
        return 30;
    }
}

public class BadShopRefactored {
    public static void main(String[] args) {
        Cart cart = new Cart();
        cart.add(new Cart.Item("Kopi Gayo", 85_000, 2));
        System.out.println("Total cart: " + cart.total());

        Courier courier = new ExpressCourier();
        System.out.println("Ongkir Ekspres 2 kg: " + courier.calculateCost(2));

        Address home = new Address("Jl. Sudirman 10", "Padang");
        SimpleOrder first = new SimpleOrder("ORD-1", home);
        SimpleOrder second = new SimpleOrder("ORD-2", home);
        second.changeAddress(second.getAddress().withCity("Bukittinggi"));
        System.out.println(first);
        System.out.println(second);

        Discount flashSale = new FlashSaleDiscount();
        System.out.println("Harga flash sale: " + flashSale.apply(100_000));

        Payable voucher = new GiftVoucher();
        System.out.println(voucher.pay(30_000) + " | " + voucher.printInvoice());
    }
}
