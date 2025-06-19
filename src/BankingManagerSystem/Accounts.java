package  BankingManagerSystem;
import java.io.BufferedReader;
import java.io.IOException;
import  java.sql.*;
public class Accounts {

    private  Connection con;
    private  BufferedReader br;
    // ParameterizedConstructor
    public Accounts (Connection con, BufferedReader br) {
        this.con=con;
        this.br=br;
    }

    public long open_account(String email){
      if(!account_exists(email)){
          String Open_Account="INSERT INTO ACCOUNTS(FULL_NAME,EMAIL,BALANCE,SECURITY_PIN) VALUES(?,?,?,?)";
          try{
              System.out.print("Enter Full Name:");
              String full_name= br.readLine();
              System.out.print("Enter Initial Amount :");
              double balance = Double.parseDouble(br.readLine());
              System.out.println("Enter Security Pin :");
              String security_pin= br.readLine();
              try {
                  PreparedStatement preparedStatement = con.prepareStatement(Open_Account,Statement.RETURN_GENERATED_KEYS);
                  preparedStatement.setString(1, full_name);
                  preparedStatement.setString(2,email);
                  preparedStatement.setDouble(3,balance);
                  preparedStatement.setString(4,security_pin);
                  int rowsAffected=preparedStatement.executeUpdate();
                  if(rowsAffected>0)
                  {
                      ResultSet rs=preparedStatement.getGeneratedKeys();//helps in retrieving autoincrement or auto generated data
                        if(rs.next()) {
                          long account_no=  rs.getLong(1);
                            System.out.println(" Account Open SuccessFull ");
                            System.out.println("Welcome BANK OF INDIA");
                            System.out.println("Your Account_no :"+account_no);
                            return account_no;
                        }
                  }
                  else {
                      System.out.println("Error in opening Bank Account");
                  }
              } catch (SQLException e) {
                  throw new RuntimeException(e);
              }
          } catch (IOException e) {
              throw new RuntimeException(e);
          }

      }
        return -1;
    }

    public long getAccount_number(String email){
       String Query ="SELECT ACCOUNT_NO FROM ACCOUNTS WHERE EMAIL= ?";
       try
       {
           PreparedStatement preparedStatement= con.prepareStatement(Query);
           preparedStatement.setString(1,email);
           ResultSet rs = preparedStatement.executeQuery();
           if(rs.next())
           {
               return  rs.getLong("ACCOUNT_NO");
           }
       }catch (SQLException e)
       {
           e.printStackTrace();
       }
       try{

       } catch (Exception e) {
           throw new RuntimeException(e);
       }
        throw new RuntimeException("Account Number Doesn't Exist");
    }

    public boolean account_exists (String email){
        String query ="SELECT ACCOUNT_NO FROM ACCOUNTS WHERE EMAIL =?";
        try{
            PreparedStatement preparedStatement =con.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next())
            {
                return true;
            }else{
                return false;
            }

        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
