package shop.app;

import java.util.List;
import shop.StudentInfo;
import shop.model.DigitalProduct;
import shop.model.PhysicalProduct;
import shop.model.Product;
import shop.notification.*;
import shop.payment.BankTransferPayment;
import shop.payment.CreditCardPayment;
import shop.payment.EWalletPayment;
import shop.payment.PaymentMethod;
import shop.service.CheckoutService;
import shop.shipping.ExpressShipping;
import shop.shipping.ShippingService;

public class TugasApp {
    public static void main(String[] args) {
        System.out.println("Tugas oleh " + StudentInfo.NAME + " (" + StudentInfo.NIM + ")");

        Product coffee = new PhysicalProduct("KOPI-001", "Kopi Arabika Gayo 250g", 85_000, 300);
        Product rendang = new PhysicalProduct("RDG-002", "Rendang Kemasan 250g", 120_000, 400);
        Product ebook = new DigitalProduct("EBK-003", "E-book Resep Kue Nusantara", 50_000,
                "https://toko-umkm.example/unduh/ebk-003");
        List<Product> cart = List.of(coffee, rendang, ebook);

        // ===== T1 =====
        CheckoutService checkout = new CheckoutService();
        ShippingService express = new ExpressShipping();
        EWalletPayment wallet = new EWalletPayment("DompetKu", 1_000_000);
        PaymentMethod transfer = new BankTransferPayment("Bank Nagari");
        CreditCardPayment card = new CreditCardPayment("Siti Rahma", 1_000_000);

        System.out.println("\n== T1: Total dengan biaya admin (Ekspres) ==");
        printTotal(checkout, cart, express, wallet);
        printTotal(checkout, cart, express, transfer);
        printTotal(checkout, cart, express, card);

        System.out.println("\nKebijakan kartu kredit : " + card.policy());
        System.out.println("Kebijakan e-wallet     : " + wallet.policy());

        // ===== T2 =====
        System.out.println("\n== T2: Notifikasi status pesanan ==");
        NotificationService service = new NotificationService();
        service.register(new EmailChannel());
        service.register(new SmsChannel(StudentInfo.SMS_MAX_LENGTH));
        service.register(new WhatsAppChannel());
        service.register(new NotificationChannel() { // anonymous class: kanal log konsol
            @Override
            public String channelName() {
                return "Log";
            }

            @Override
            public boolean send(CustomerContact contact, String message) {
                System.out.println("[LOG] " + contact.getName() + ": " + message);
                return true;
            }
        });

        String message = "Pesanan ORD-0005 sudah dikirim dengan kurir Ekspres. "
                + "Nomor resi akan muncul di aplikasi dalam 1x24 jam. "
                + "Terima Kasih Telah Berbelanja Product Kami! "
                + "Hubungi layanan pelanggan kami jika ada pertanyaan lebih lanjut.";

        CustomerContact dennis = new CustomerContact("Siti Rahma", "siti@example.com", "081234567890");
        CustomerContact akmal = new CustomerContact("Budi", null, "+6281298765432");

        System.out.println("Terkirim ke Dennis lewat " + service.broadcast(dennis, message) + " kanal");
        System.out.println("Terkirim ke Akmal lewat " + service.broadcast(akmal, message) + " kanal");
    }

    private static void printTotal(CheckoutService checkout, List<Product> cart,
            ShippingService shipping, PaymentMethod payment) {
        long total = checkout.total(cart, shipping);
        long grand = checkout.grandTotal(cart, shipping, payment);
        System.out.println(payment.getName() + " : Rp" + grand
                + " (admin Rp" + payment.adminFee(total) + ")");
    }
}