package UQBank.model.interfaces;

import UQBank.model.users.Client;

public interface ICashierManagement {
    Client registerClient(String id, String name, String password);
    void generateClientReport(Client client);
}