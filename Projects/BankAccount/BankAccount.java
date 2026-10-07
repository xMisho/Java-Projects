package BankAccount;

import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {

    private int accountID;
    private String ownerName;
    private double balance;

    private final ReentrantLock lock = new ReentrantLock();

    public BankAccount(int accountID, String ownerName, double balance) {
        this.accountID = accountID;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    public int getAccountID() {
        return accountID;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public double getBalance() {
    lock.lock();
    try {
        return balance;
    } finally {
        lock.unlock();
    }
}

    public void deposit(double amount) {
        lock.lock();
        try {
            if (amount > 0) {
                balance += amount;
            } else {
                System.out.println("Deposit amount must be positive.");
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean withdraw(double amount) {
        lock.lock();
        try {
            if (amount > 0 && amount <= balance) {
                balance -= amount;
                return true;
            } else {
                System.out.println("Insufficient funds or invalid withdrawal amount.");
                return false;
            }
        } finally {
            lock.unlock();
        }
    }

    ReentrantLock getLock() {
    return lock;
}


}
