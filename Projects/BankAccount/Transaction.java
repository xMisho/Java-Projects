public class Transaction {
    
    private final String type;
    private final double amount;
    private final int fromAccountID;
    private final int toAccountID;

    public Transaction(String type, double amount, int fromAccountID, int toAccountID) {
        this.type = type;
        this.amount = amount;
        this.fromAccountID = fromAccountID;
        this.toAccountID = toAccountID;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public int getFromAccountID() {
        return fromAccountID;
    }

    public int getToAccountID() {
        return toAccountID;
    }

    @Override
    public String toString() {
        return "Type: " + type +
                ", Amount: " + amount +
                ", From: " + fromAccountID +
                ", To: " + toAccountID;
    }
}