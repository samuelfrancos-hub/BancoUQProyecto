package UQBank.model.users;

public abstract class User {

    protected String userId;
    protected String fullName;
    protected String password;
    protected String email;

    public User(String userId, String fullName, String password, String email) {
        this.userId = userId;
        this.fullName = fullName;
        this.password = password;
        this.email = email;
    }

    public String getFullName() { return fullName; }
    public String getPassword() { return password; }
    public String getUserId() { return userId; }
    public String getEmail() { return email; }

    public abstract String getRole();
}
