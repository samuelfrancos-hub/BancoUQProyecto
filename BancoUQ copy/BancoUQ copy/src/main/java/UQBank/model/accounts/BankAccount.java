package UQBank.model.accounts;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class BankAccount {

    protected String accountNumber;
    protected double balance;
    protected List<Transaction> transactions;

    protected double dailyCumulativeWithdrawal;
    protected double dailyCumulativeDeposit;
    protected LocalDate lastTransactionDate;

    public BankAccount(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
        this.dailyCumulativeWithdrawal = 0;
        this.dailyCumulativeDeposit = 0;
        this.lastTransactionDate = LocalDate.now();

        addTransaction(new Transaction("DEPOSITO INICIAL", initialBalance, accountNumber));
    }

    protected void checkAndResetDailyCounts() {
        LocalDate today = LocalDate.now();
        if (!today.equals(this.lastTransactionDate)) {
            this.dailyCumulativeWithdrawal = 0;
            this.dailyCumulativeDeposit = 0;
            this.lastTransactionDate = today;
        }
    }

    public abstract boolean withdraw(double amount);

    public boolean deposit(double amount) {
        if (amount > 0) {
            checkAndResetDailyCounts();

            this.balance += amount;
            this.dailyCumulativeDeposit += amount;
            addTransaction(new Transaction("DEPOSITO", amount, accountNumber));
            return true;
        }
        return false;
    }

    protected void addTransaction(Transaction tx) {
        this.transactions.add(tx);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public String getAccountType() {
        return this.getClass().getSimpleName().replace("Account", "");
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public boolean withdrawForTransfer(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            addTransaction(new Transaction("RETIRO TRANSFERENCIA", amount, accountNumber));
            return true;
        }
        return false;
    }

    public void forceDeposit(double amount) {
        balance += amount;
        addTransaction(new Transaction("REVERSO TRANSFERENCIA", amount, accountNumber));
    }

}



