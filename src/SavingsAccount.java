class SavingsAccount extends Account{
    private float interestRate;

    public SavingsAccount(float amount, Client accountHolder, float interestRate){
        super(amount, accountHolder);
        this.interestRate = interestRate;
    }

    public float getInterestRate(){
        return interestRate;
    }

    public void setInterestRate(float interestRate) {
        this.interestRate = interestRate;
    }

    public float applyInterest() {
        float interest = getAmount() * (interestRate / 100);
        deposit(interest);
        return getAmount();
    }

    @Override
    public float withdraw(float amount) {
        if ((getAmount() - amount) < 500) {
            System.out.println("Failed Transaction: Minimum balance of Rs. 500 must be maintained in the account.");
        } else {
            setAmount(getAmount() - amount);
            System.out.println("Withdrawal successful. Rs. " + amount + " withdrew.");
        }
        return getAmount();
    }

    @Override
    public String toString() {
        return super.toString() + "\nInterest Rate: " + interestRate + "%" + "\nAccount Type: Savings";
    }
}
