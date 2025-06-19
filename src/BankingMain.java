import BankingManagerSystem.Accounts;
import BankingManagerSystem.AccountsManager;
import BankingManagerSystem.User;

import java.io.*;
import java.sql.*;

public class BankingMain {
    private static final String url = "jdbc:mysql://localhost:3306/banking_management_system";
    private static final String username = "YourUsername";
    private static final String password = "YourPassword";

    public static void main(String[] args) throws IOException, SQLException {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found", e);
        }

        try {
            // Connect to DB
            Connection con = DriverManager.getConnection(url, username, password);
            System.out.println("Connection successful!");

            // Input reader
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            // Instantiate app logic classes
            User user = new User(con, br);
            Accounts account = new Accounts(con, br);
            AccountsManager accountsManager = new AccountsManager(con, br);

            String email;
            long account_number;

            while (true) {
                System.out.println("\n=== Welcome to Banking System ===");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                String input = br.readLine();
                int choice;
                if (input == null || input.trim().isEmpty()) {
                    choice = -1; // or 0 or any default invalid choice
                } else {
                    choice = Integer.parseInt(input.trim());
                }

                switch (choice) {
                    case 1:
                        user.register(); // Assuming this method exists
                        break;

                    case 2:
                        email = user.login(); // Assuming this returns email if login successful
                        if (email != null) {
                            System.out.println("\nUser Logged In Successfully!");

                            if (!account.account_exists(email)) {
                                System.out.println("\nNo bank account found.");
                                System.out.println("1. Open a new Bank Account");
                                System.out.println("2. Exit");
                                int choice1 = Integer.parseInt(br.readLine());
                                if (choice1 == 1) {
                                    account_number = account.open_account(email);
                                    if (account_number != -1) {
                                        System.out.println("Account created successfully.");
                                        System.out.println("Your Account Number is: " + account_number);
                                    }
                                } else {
                                    break;
                                }
                            }

                            account_number = account.getAccount_number(email);
                            int choice3 = 0;
                            while (choice3 != 5) {
                                System.out.println("\n1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.print("Enter your choice: ");
                                choice3 = Integer.parseInt(br.readLine());

                                switch (choice3) {
                                    case 1:
                                        accountsManager.debit_money(account_number);
                                        break;
                                    case 2:
                                        accountsManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountsManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountsManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        System.out.println("Logging out...");
                                        break;
                                    default:
                                        System.out.println("Enter a valid choice.");
                                        break;
                                }
                            }
                        } else {
                            System.out.println("Incorrect email or login failed.");
                        }
                        break; // <-- FIXED: Add break to stop falling into case 3

                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                        return;

                    default:
                        System.out.println("Please enter a valid choice.");
                        break;
                }
            }

        } catch (SQLException ex) {
            throw new RuntimeException("Database error", ex);
        }
    }
}
