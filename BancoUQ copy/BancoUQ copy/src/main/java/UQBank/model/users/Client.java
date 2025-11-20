package UQBank.model.users;

import UQBank.model.accounts.BankAccount;
import java.util.ArrayList;
import java.util.List;

public class Client extends User {

    private List<BankAccount> accounts;

    public Client(String userId, String fullName, String password, String email) {
        super(userId, fullName, password, email);
        this.accounts = new ArrayList<>();
    }


    @Override
    public String getRole() {
        return "Client";
    }

    public BankAccount getAccount(String accountNumber) {
        if (accountNumber == null) return null;
        return accounts.stream()
                .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElse(null);
    }

    public void addAccount(BankAccount account) {
        this.accounts.add(account);
    }

    public List<BankAccount> getAccounts() {
        return accounts;
    }
}