package UQBank.model.interfaces;

import UQBank.model.users.User;

public interface IAdminManagement {
    void registerEmployee(User employee);
    void deleteEmployee(String userId);
    void monitorTransactions();
    void generateTotalActivityReport();
}