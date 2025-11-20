package UQBank.model.accounts;

public class SavingAccount extends BankAccount {

    private final double DAILY_WITHDRAWAL_LIMIT = 1000.00;
    private final double DAILY_DEPOSIT_LIMIT = 2000.00;

    public SavingAccount(String accountNumber, double initialBalance) {
        super(accountNumber, initialBalance);
    }

    @Override
    public boolean deposit(double amount) {
        checkAndResetDailyCounts();

        if (amount > 0 && this.dailyCumulativeDeposit + amount > DAILY_DEPOSIT_LIMIT) {
            System.err.println("Error: El depósito excede el máximo diario acumulado de $" + DAILY_DEPOSIT_LIMIT);
            return false;
        }

        return super.deposit(amount);
    }

    @Override
    public boolean withdraw(double amount) {
        checkAndResetDailyCounts();

        if (this.dailyCumulativeWithdrawal + amount > DAILY_WITHDRAWAL_LIMIT) {
            System.err.println("Error: El retiro excede el máximo diario acumulado de $" + DAILY_WITHDRAWAL_LIMIT);
            return false;
        }

        if (amount > 0 && this.balance >= amount) {
            this.balance -= amount;

            addTransaction(new Transaction("RETIRO AHORRO", amount, accountNumber));
            this.dailyCumulativeWithdrawal += amount;

            return true;
        }
        System.err.println("Error: Saldo insuficiente o monto inválido para retiro de ahorro.");
        return false;
    }
    public boolean withdrawForTransfer(double amount) {
        if (amount > 0 && this.balance >= amount) {
            this.balance -= amount;

            // Registrar movimiento pero marcándolo como transferencia
            addTransaction(new Transaction("TRANSFERENCIA SALIENTE", amount, accountNumber));

            return true;
        }
        return false;
    }

}