class Person {
    private String name;
    private String CNIC;
    private String phoneNumber;

    public Person(String name, String CNIC, String phoneNumber){
        this.name = name;
        this.CNIC = CNIC;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getCNIC() {
        return CNIC;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Name: "+name+"\nCNIC: "+CNIC+"\nPhone: "+phoneNumber;
    }
}
