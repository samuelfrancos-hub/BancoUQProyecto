package UQBank.model.accounts;

import java.time.LocalDateTime;

public class Transaction {

    private final String type;
    private final double amount;
    private final String accountNumber;
    private final LocalDateTime date;

    public Transaction(String type, double amount, String accountNumber) {
        this.type = type;
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.date = LocalDateTime.now();
    }

    @Override
    public String toString() {
        String sign = type.contains("DEPOSITO") || type.contains("INICIAL") ? "+" : "-";
        String formattedTime = date.toLocalTime().withNano(0).toString();

        return String.format("%s | %s: %s$%.2f", formattedTime, type, sign, amount);
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }
}