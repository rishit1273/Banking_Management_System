package BankingManagerSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountsManager {

    private final Connection con;
    private final BufferedReader br;

    // ParameterizedConstructor
    public AccountsManager(Connection con, BufferedReader br) {
        this.con = con;
        this.br = br;
    }

    public void credit_money(long account_no) throws SQLException {
        try {
            System.out.print("Enter Amount:");
            double amount = Double.parseDouble(br.readLine());
            System.out.print("Enter Security Pin:");
            String security_pin = br.readLine();
            try {
                con.setAutoCommit(false);
                if (account_no != 0) {
                    PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM ACCOUNTS WHERE ACCOUNT_NO = ? AND  SECURITY_PIN = ?");
                    preparedStatement.setLong(1, account_no);
                    preparedStatement.setString(2, security_pin);
                    ResultSet rs = preparedStatement.executeQuery();

                    if (rs.next()) {
                        String credit_money = "UPDATE ACCOUNTS SET BALANCE =BALANCE + ? WHERE  ACCOUNT_NO= ?";
                        PreparedStatement preparedStatement1 = con.prepareStatement(credit_money);
                        preparedStatement1.setDouble(1, amount);
                        preparedStatement1.setLong(2, account_no);
                        int rowsAffected = preparedStatement1.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Rs" + amount + " is credited Successfully");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed!");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Invalid Security Pin");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        con.setAutoCommit(true);//Reset Default Settings
    }

    public void debit_money(long account_no) throws SQLException {
        try {
            System.out.print("Enter Amount:");
            double amount = Double.parseDouble(br.readLine());
            System.out.print("Enter Pin:");
            String security_pin = br.readLine();
            try {
                con.setAutoCommit(false);
                if (account_no != 0) {
                    PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM Accounts WHERE ACCOUNTS_NO = ? and security_pin = ? ;");
                    preparedStatement.setLong(1, account_no);
                    preparedStatement.setString(2, security_pin);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        double current_Balance = resultSet.getDouble("BALANCE");
                        if (amount <= current_Balance) {
                            String debit_query = "UPDATE ACCOUNTS SET BALANCE =BALANCE - ? WHERE ACCOUNT_NO = ?";
                            PreparedStatement preparedStatement1 = con.prepareStatement(debit_query);
                            preparedStatement1.setDouble(1, amount);
                            preparedStatement1.setLong(2, account_no);
                            int rowsAffected = preparedStatement1.executeUpdate();
                            if (rowsAffected > 0) {
                                System.out.println("Rs" + amount + "debit Successfully");
                                con.commit();
                                con.setAutoCommit(true);
                                return;
                            } else {
                                System.out.println("Transaction Failed!");
                                con.rollback();
                                con.setAutoCommit(true);
                            }

                        } else {
                            System.out.println("Insufficient Balance");
                        }
                    } else {
                        System.out.println("Invalid Pin");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        con.setAutoCommit(true);
    }

    public void transfer_money(long sender_account_no) throws SQLException, IOException {
        try {
            System.out.print("Enter the Receiver Account:");
            long receiver_account = Long.parseLong(br.readLine());
            System.out.print("Enter Amount:");
            double amount = Double.parseDouble(br.readLine());
            System.out.print("Enter Security Pin:");
            String security_pin = br.readLine();
            con.setAutoCommit(false);
            if (sender_account_no != 0 && receiver_account != 0) {
                PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM ACCOUNTS WHERE ACCOUNT_NO = ? AND security_pin = ? ");
                preparedStatement.setLong(1, sender_account_no);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()){
                    double current_balance =resultSet.getDouble("BALANCE");
                    if(amount<=current_balance)
                    {
                        String debit_query = "UPDATE Accounts SET BALANCE = BALANCE - ? WHERE ACCOUNT_NO = ?";
                        String credit_query = "UPDATE Accounts SET BALANCE = BALANCE + ? WHERE  ACCOUNT_NO = ?";

                        PreparedStatement creditPreparedStatement = con.prepareStatement(credit_query);
                        PreparedStatement debitPreparedStatement = con.prepareStatement(debit_query);
                        creditPreparedStatement.setDouble(1, amount);
                        creditPreparedStatement.setLong(2, receiver_account);
                        debitPreparedStatement.setDouble(1, amount);
                        debitPreparedStatement.setLong(2, sender_account_no);
                        int rowsAffected1 = debitPreparedStatement.executeUpdate();
                        int rowsAffected2 = creditPreparedStatement.executeUpdate();
                        if (rowsAffected1 > 0 && rowsAffected2 > 0) {
                            System.out.println("Transaction Successful!");
                            System.out.println("Rs." + amount + " Transferred Successfully");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        }else{
                            System.out.println("Transaction Failed");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                        }else{
                        System.out.println("Insufficient Balance!");
                    }
                    }else {
                    System.out.println("Invalid Security Pin!");
                }


                }else {
                System.out.println("Invalid account number");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        con.setAutoCommit(true);
    }
    public  void getBalance(long account_number){
        try {
        System.out.print("Enter Security Pin :");
        String security_pin=br.readLine();
        try{
            PreparedStatement preparedStatement = con.prepareStatement("SELECT BALANCE FROM ACCOUNTS WHERE ACCOUNT_NO = ? AND SECURITY_PIN = ?");
            preparedStatement.setLong(1, account_number);
            preparedStatement.setString(2, security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                double balance = resultSet.getDouble("balance");
                System.out.println("Balance: " + balance);
            }else {
                System.out.println("Invalid Pin");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    }
