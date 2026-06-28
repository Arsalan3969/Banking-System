import java.util.ArrayList;

class Client {
    private String id;
    private Person personalDetails;
    private ArrayList<Account> accountList;
    static int count = 1;

    public Client(Person person) {
        this.id = "CLIENT" + count++;
        this.personalDetails = person;
        this.accountList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public Person getPersonalDetails(){
        return personalDetails;
    }

    public ArrayList<Account> getAccountList() {
        return accountList;
    }

    public void addAccount(Account account){
        accountList.add(account);
    }

    public float totalAmount() {
        float total = 0;
        for (Account account : accountList) {
            total += account.getAmount();
        }
        return total;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPersonalDetails(Person personalDetails) {
        this.personalDetails = personalDetails;
    }

    public void withdraw(float amount, String accNo) {
        for (Account account : accountList) {
            if (account.getNumber().equals(accNo)) {
                System.out.println("Remaining Balance: " + account.withdraw(amount));
                return;
            }
        }
        System.out.println("Error!\nAccount not Found.");
    }

    public void deposit(float amount, String accNo) {
        for (Account account : accountList) {
            if (account.getNumber().equals(accNo)) {
                System.out.println("Current Balance: " + account.deposit(amount));
                return;
            }
        }
        System.out.println("Error!\nAccount not Found.");
    }

    @Override
    public String toString() {
        String data = "\nCLIENT\n";
        data += personalDetails.toString();
        data += "\nClient ID: " + id;
        data += "\nAccounts\n";

        for (Account account : accountList) {
            data += account.toString() + "\n";
        }
        return data;
    }
}
