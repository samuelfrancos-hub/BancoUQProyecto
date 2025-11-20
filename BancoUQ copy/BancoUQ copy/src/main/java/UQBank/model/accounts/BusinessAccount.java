package UQBank.model.accounts;

public class BusinessAccount extends BankAccount {

    private final double DAILY_WITHDRAWAL_LIMIT = 1000.00;
    private final double DAILY_DEPOSIT_LIMIT = 20000.00;

    public BusinessAccount(String accountNumber, double initialBalance) {
        super(accountNumber, initialBalance);
    }

    @Override
    public boolean deposit(double amount) {
        checkAndResetDailyCounts();

        if (amount > 0 && this.dailyCumulativeDeposit + amount > DAILY_DEPOSIT_LIMIT) {
            System.err.println("Error: El depósito excede el límite diario de $" + DAILY_DEPOSIT_LIMIT);
            return false;
        }

        return super.deposit(amount);
    }

    @Override
    public boolean withdraw(double amount) {
        checkAndResetDailyCounts();

        if (this.dailyCumulativeWithdrawal + amount > DAILY_WITHDRAWAL_LIMIT) {
            System.err.println("Error: El retiro excede el límite diario de $" + DAILY_WITHDRAWAL_LIMIT);
            return false;
        }

        if (amount > 0 && this.balance >= amount) {
            this.balance -= amount;
            addTransaction(new Transaction("RETIRO NEGOCIOS", amount, accountNumber));
            this.dailyCumulativeWithdrawal += amount;
            return true;
        }

        System.err.println("Error: saldo insuficiente o monto inválido.");
        return false;
    }


    @Override
    public boolean withdrawForTransfer(double amount) {
        if (amount > 0 && this.balance >= amount) {
            this.balance -= amount;
            addTransaction(new Transaction("TRANSFERENCIA SALIENTE", amount, accountNumber));
            return true;
        }
        return false;
    }
}
