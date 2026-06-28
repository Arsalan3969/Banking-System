class Account {
    private String number;
    private float amount;
    private Client accountHolder;
    static int count = 1000;

    public Account(float amount, Client accountHolder) {
        this.number = "ACCOUNT" + count++;
        this.amount = amount;
        this.accountHolder = accountHolder;
    }

    public String getNumber() {
        return number;
    }

    public float getAmount() {
        return amount;
    }

    public Client getAccountHolder(){
        return accountHolder;
    }

    public float deposit(float amount) {
        this.amount += amount;
        return this.amount;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setAccountHolder(Client accountHolder) {
        this.accountHolder = accountHolder;
    }

    public float withdraw(float amount) {
        if (amount <= this.amount) {
            this.amount -= amount;
        } else {
            System.out.println("Insufficient Balance!");
        }
        return this.amount;
    }

    @Override
    public String toString() {
        return "Account Number: " + number + "\nBalance: " + amount + "\nAccount Holder: " + accountHolder.getPersonalDetails().getName();
    }
}
