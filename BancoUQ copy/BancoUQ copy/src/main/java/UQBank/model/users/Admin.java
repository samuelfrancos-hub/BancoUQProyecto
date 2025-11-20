package UQBank.model.users;

public class Admin extends User {

    public Admin(String userId, String fullName, String password, String email) {
        super(userId, fullName, password, email);
    }

    @Override
    public String getRole() {
        return "Admin";
    }
}
