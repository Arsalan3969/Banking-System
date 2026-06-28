class CurrentAccount extends Account{
    private float overdraftLimit;

    public CurrentAccount(float amount, Client accountHolder, float overdraftLimit) {
        super(amount, accountHolder);
        this.overdraftLimit = overdraftLimit;
    }

    public float getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(float overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public float withdraw(float amount) {
        if ((getAmount() - amount) < -overdraftLimit) {
            System.out.println("Failed Transaction: Overdraft limit of Rs. " + overdraftLimit + " exceeded!");
        } else {
            setAmount(getAmount() - amount);
            System.out.println("Withdrawal successful. Rs. " + amount + " withdrew.");
        }
        return getAmount();
    }

    @Override
    public String toString() {
        return super.toString() + "\nOverdraft Limit: Rs. " + overdraftLimit + "\nAccount Type: Current";
    }
}
