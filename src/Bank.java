import java.util.ArrayList;

class Bank {
    private String name;
    private ArrayList<Client> clientList;
    private ArrayList<Account> accountList;

    public Bank(String name){
        this.name = name;
        clientList = new ArrayList<>();
        accountList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Account> getAccountList() {
        return accountList;
    }

    public ArrayList<Client> getClientList() {
        return clientList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Client addClient(Person person) {
        Client client = new Client(person);
        clientList.add(client);
        return client;
    }

    public void addAccount(Account newAccount, Client client) {
        accountList.add(newAccount);
        client.addAccount(newAccount);
    }

    public Account searchAccount(String id) {
        for (Account account : accountList) {
            if (account.getNumber().equals(id)) {
                return account;
            }
        }
        return null;
    }

    public boolean removeClient(String id) {
        Client target = null;
        for (Client client : clientList) {
            if (client.getId().equals(id)) {
                target = client;
                break;
            }
        }

        if (target != null) {
            accountList.removeAll(target.getAccountList());
            clientList.remove(target);
            return true;
        }
        return false;
    }

    public float totalAmount() {
        float total = 0;
        for (Account account : accountList) {
            total += account.getAmount();
        }
        return total;
    }

    public Client searchCustomerDetail(String CNIC) {
        for (Client client : clientList) {
            if (client.getPersonalDetails().getCNIC().equals(CNIC)) {
                return client;
            }
        }
        return null;
    }

    public void displayAllClients() {
        for (Client client : clientList) {
            System.out.println(client);
        }
    }

    @Override
    public String toString() {
        return "Bank Name: " + name;
    }

}
