package shop.payment;

import shop.StudentInfo;

public class CreditCardPayment implements RefundablePayment, InstallmentPayment {
    private final String cardHolder;
    private final long creditLimit;
    private long usedCredit;

    public CreditCardPayment(String cardHolder, long creditLimit) {
        if (cardHolder == null || cardHolder.isBlank()) {
            throw new IllegalArgumentException("Card holder must not be blank");
        }
        if (creditLimit < 0) {
            throw new IllegalArgumentException("Credit limit must not be negative");
        }
        this.cardHolder = cardHolder;
        this.creditLimit = creditLimit;
    }

    @Override
    public String getName() {
        return "Kartu Kredit (" + cardHolder + ")";
    }

    @Override
    public boolean pay(long amount) {
        if (amount <= 0 || usedCredit + amount > creditLimit) {
            return false;
        }
        usedCredit += amount;
        return true;
    }

    @Override
    public int maxInstallmentMonths() {
        return 12;
    }

    @Override
    public boolean refund(long amount) {
        if (amount <= 0 || amount > usedCredit) {
            return false;
        }
        usedCredit -= amount;
        return true;
    }

    @Override
    public long adminFee(long amount) {
        return amount * StudentInfo.CARD_ADMIN_PERCENT / 100;
    }

    @Override
    public String policy() {
        return RefundablePayment.super.policy() + "; " + InstallmentPayment.super.policy();
    }

    public long getUsedCredit() {
        return usedCredit;

    }
}
