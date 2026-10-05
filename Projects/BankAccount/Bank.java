import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public final class Bank {

    private final ConcurrentHashMap<Integer, BankAccount> accounts;
    private final ConcurrentLinkedQueue<Transaction> transactionHistory;
    private final AtomicInteger transactionCount;

    public Bank() {
        accounts = new ConcurrentHashMap<>();
        transactionHistory = new ConcurrentLinkedQueue<>();
        transactionCount = new AtomicInteger();
    }

    public boolean addAccount(BankAccount account) {
        BankAccount existing = accounts.putIfAbsent(account.getAccountID(), account);

        if (existing != null) {
            System.out.println("Account with ID " + account.getAccountID() + " already exists.");
            return false;
        }
        return true;
    }

    public BankAccount getAccount(int accountID) {
        return accounts.get(accountID);
    }

    public int getAccountCount() {
        return accounts.size();
    }

    public boolean deposit(int accountID, double amount) {
        BankAccount account = accounts.get(accountID);
        if (account == null || !Double.isFinite(amount) || amount <= 0) {
            return false;
        }

        account.getLock().lock();
        try {
            account.deposit(amount);
            recordTransaction(new Transaction("DEPOSIT", amount, -1, accountID));
            return true;
        } finally {
            account.getLock().unlock();
        }
    }

    public boolean withdraw(int accountID, double amount) {
        BankAccount account = accounts.get(accountID);
        if (account == null) {
            return false;
        }

        account.getLock().lock();
        try {
            if (!account.withdraw(amount)) {
                return false;
            }

            recordTransaction(new Transaction("WITHDRAW", amount, accountID, -1));
            return true;
        } finally {
            account.getLock().unlock();
        }
    }

    public int getTransactionCount() {
        return transactionCount.get();
    }

    private void recordTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
        transactionCount.incrementAndGet();
    }

    public boolean accountExists(int accountID) {
        return accounts.containsKey(accountID);
    }       

    public boolean removeAccount(int accountID) {
        if (accounts.remove(accountID) != null) {
            return true;
        } else {
            System.out.println("Account with ID " + accountID + " does not exist.");
            return false;
        }
    }

    public void displayAllAccounts() {
        for (BankAccount account : accounts.values()) {
            System.out.println("Account ID: " + account.getAccountID() +
                    ", Owner: " + account.getOwnerName() +
                    ", Balance: " + account.getBalance());
        }
    }

    public boolean transfer(int fromAccountID, int toAccountID, double amount) {
        if (amount <= 0) {
            System.out.println("Transfer amount must be positive.");
            return false;
        }

        if (fromAccountID == toAccountID) {
            System.out.println("Cannot transfer to the same account.");
            return false;
        }

        BankAccount fromAccount = accounts.get(fromAccountID);
        BankAccount toAccount = accounts.get(toAccountID);

        if (fromAccount == null || toAccount == null) {
            System.out.println("One or both accounts do not exist.");
            return false;
        }

        BankAccount firstAccount;
        BankAccount secondAccount;

        if (fromAccountID < toAccountID) {
        firstAccount = fromAccount;
        secondAccount = toAccount;
        } else  
        {
        firstAccount = toAccount;
        secondAccount = fromAccount;
        }

        firstAccount.getLock().lock();
        try {
            secondAccount.getLock().lock();
            try {
                if (fromAccount.withdraw(amount)) {
                    toAccount.deposit(amount);

                    recordTransaction(
                            new Transaction("TRANSFER", amount, fromAccountID, toAccountID)
                    );

                    return true;
                }

                System.out.println("Transfer failed due to insufficient funds.");
                return false;
            } finally {
                secondAccount.getLock().unlock();
            }
        } finally {
            firstAccount.getLock().unlock();
        }
    }

    public void displayTransactionHistory() {
    for (Transaction transaction : transactionHistory) {
        System.out.println(
                "Type: " + transaction.getType() +
                ", Amount: " + transaction.getAmount() +
                ", From: " + transaction.getFromAccountID() +
                ", To: " + transaction.getToAccountID()
        );
    }
}

    public void displayTransactionsForAccount(int accountID) {
        for (Transaction transaction : transactionHistory) {
            if (transaction.getFromAccountID() == accountID
                    || transaction.getToAccountID() == accountID) {
                System.out.println(
                        "Type: " + transaction.getType() +
                        ", Amount: " + transaction.getAmount() +
                        ", From: " + transaction.getFromAccountID() +
                        ", To: " + transaction.getToAccountID()
                );
            }
        }
    }

}


