This project allows users to register, log in, manage bank accounts, and perform basic banking operations such as credit, debit, transfer, and balance check.

 Key Features & Modules
1. User Module (User.java)
Register: Users can create an account by providing their name, email, and password.

Login: Existing users log in using email and password.

Duplicate Check: Prevents multiple registrations with the same email.

2. Accounts Module (Accounts.java)
Open Account: After login, users can open a bank account with an initial deposit and security PIN.

Check Account: Checks if a bank account already exists for the user.

Get Account Number: Retrieves the user's account number using email.

3. AccountsManager Module (AccountsManager.java)
Credit Money: Add money to a user’s bank account after validating security PIN.

Debit Money: Withdraw money if sufficient balance and correct PIN.

Transfer Money: Transfer amount between two users' accounts (sender + receiver) securely.

Check Balance: View current balance after providing correct security PIN.

4. Main Driver Class (BankingMain.java)
Connects to MySQL database.

Provides a text-based menu with:

Register

Login → Account creation or management

Exit

Uses BufferedReader for user input.

Executes corresponding methods based on user’s choice.
