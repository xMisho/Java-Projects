public class Main {

    public static void main(String[] args) throws InterruptedException {

        Bank bank = new Bank();

        BankAccount account1 =
                new BankAccount(1, "Mike", 100_000);

        BankAccount account2 =
                new BankAccount(2, "John", 100_000);

        bank.addAccount(account1);
        bank.addAccount(account2);

        Thread depositThread = new Thread(() -> {

            for (int i = 0; i < 10_000; i++) {
                bank.deposit(1, 1);
            }

        });

        Thread withdrawThread = new Thread(() -> {

            for (int i = 0; i < 10_000; i++) {
                bank.withdraw(2, 1);
            }

        });

        Thread transferThread1 = new Thread(() -> {

            for (int i = 0; i < 10_000; i++) {
                bank.transfer(1, 2, 1);
            }

        });

        Thread transferThread2 = new Thread(() -> {

            for (int i = 0; i < 10_000; i++) {
                bank.transfer(2, 1, 1);
            }

        });

        depositThread.start();
        withdrawThread.start();
        transferThread1.start();
        transferThread2.start();

        depositThread.join();
        withdrawThread.join();
        transferThread1.join();
        transferThread2.join();

        System.out.println("=== FINAL RESULTS ===");

        System.out.println(
                "Account 1 balance: " + account1.getBalance()
        );

        System.out.println(
                "Account 2 balance: " + account2.getBalance()
        );

        System.out.println(
                "Transaction count: " + bank.getTransactionCount()
        );

        System.out.println(
                "Account count: " + bank.getAccountCount()
        );
    }
}