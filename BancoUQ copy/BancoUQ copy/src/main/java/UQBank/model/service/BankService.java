package UQBank.model.service;

import UQBank.model.accounts.BankAccount;
import UQBank.model.accounts.CheckingAccount;
import UQBank.model.accounts.SavingAccount;
import UQBank.model.accounts.BusinessAccount;
import UQBank.model.users.User;
import UQBank.model.users.Admin;
import UQBank.model.users.Cashier;
import UQBank.model.users.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.time.LocalDateTime;

public class BankService {

    private static BankService instance;

    private final List<User> users;
    private final List<BankAccount> accounts;

    private User currentUser;
    private final AtomicLong nextUserId = new AtomicLong(100);
    private final AtomicLong nextAccountNumber = new AtomicLong(1000);

    private BankService() {
        this.users = new ArrayList<>();
        this.accounts = new ArrayList<>();
        initializeData();
    }

    public static BankService getInstance() {
        if (instance == null) {
            instance = new BankService();
        }
        return instance;
    }

    private void initializeData() {
        users.add(new Admin("A-001", "John Medina", "john2007", "john.medina@bankuq.com"));

        users.add(new Cashier("C-002", "Juan Camilo", "quindas2008", "juan.camilo@bankuq.com"));

        Client client1 = new Client("CL-003", "Samuel Franco", "salchipapa", "samuel.franco@correo.com");

        BankAccount acc1 = new SavingAccount("ACC-1000", 5000.00);
        BankAccount acc2 = new CheckingAccount("ACC-1001", 1500.00);

        client1.addAccount(acc1);
        client1.addAccount(acc2);

        users.add(client1);
        accounts.add(acc1);
        accounts.add(acc2);

        nextUserId.set(4);
        nextAccountNumber.set(1002);
    }

    public User login(String email, String password) {
        User user = findUserByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            this.currentUser = user;
            return user;
        }
        return null;
    }

    public void logout() {
        this.currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public User findUserByEmail(String email) {
        if (email == null) return null;
        return users.stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }

    public User findUserById(String id) {
        if (id == null) return null;
        return users.stream()
                .filter(user -> user.getUserId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public boolean deleteUser(String userId) {
        if (currentUser != null && currentUser.getUserId().equals(userId)) {
            return false;
        }
        return users.removeIf(user -> user.getUserId().equals(userId));
    }

    public List<User> getUsers() {
        return users;
    }

    public String openAccount(Client client, String accountType, double initialDeposit) {
        if (client == null || accountType == null) return null;

        String accountNumber = generateNextAccountNumber();
        BankAccount newAccount = null;

        if ("Saving".equalsIgnoreCase(accountType)) {
            newAccount = new SavingAccount(accountNumber, initialDeposit);
        } else if ("Checking".equalsIgnoreCase(accountType)) {
            newAccount = new CheckingAccount(accountNumber, initialDeposit);
        } else if ("Business".equalsIgnoreCase(accountType)) {
            newAccount = new BusinessAccount(accountNumber, initialDeposit);
        } else {
            return null;
        }

        if (newAccount != null) {
            client.addAccount(newAccount);
            accounts.add(newAccount);
            return accountNumber;
        }
        return null;
    }

    public BankAccount findAccountByNumber(String accountNumber) {
        if (accountNumber == null) return null;
        return accounts.stream()
                .filter(acc -> acc.getAccountNumber().equalsIgnoreCase(accountNumber))
                .findFirst()
                .orElse(null);
    }

    public String generateNextId(String role) {
        String prefix = switch (role.toUpperCase()) {
            case "ADMIN" -> "A-";
            case "CASHIER" -> "C-";
            case "CLIENT" -> "CL-";
            default -> "";
        };
        return prefix + String.format("%03d", nextUserId.getAndIncrement());
    }

    public String generateNextAccountNumber() {
        return "ACC-" + nextAccountNumber.getAndIncrement();
    }

    public String processEndOfMonth() {
        int count = 0;
        for (BankAccount account : accounts) {
            if (account instanceof SavingAccount) {
                double balance = account.getBalance();
                double interest = balance * 0.001;
                account.deposit(interest);
                count++;
            }
        }
        return String.format("Proceso de Fin de Mes completado. %d cuentas actualizadas. Hora: %s",
                count, LocalDateTime.now());
    }


    public boolean transfer(String sourceAccountNumber, String targetAccountNumber, double amount) {

        BankAccount source = findAccountByNumber(sourceAccountNumber);
        BankAccount target = findAccountByNumber(targetAccountNumber);

        if (source == null || target == null) {
            return false;
        }

        if (amount <= 0 || source.getBalance() < amount) {
            return false;
        }


        if (!source.withdrawForTransfer(amount)) {
            return false;
        }


        if (!target.deposit(amount)) {


            source.forceDeposit(amount);

            System.err.println("Transferencia revertida por límite de depósito.");
            return false;
        }

        return true;
    }


}


