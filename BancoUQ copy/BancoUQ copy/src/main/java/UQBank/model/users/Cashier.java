package UQBank.model.users;

public class Cashier extends User {

    public Cashier(String userId, String fullName, String password, String email) {
        super(userId, fullName, password, email);
    }

    @Override
    public String getRole() {
        return "Cashier";
    }

}
