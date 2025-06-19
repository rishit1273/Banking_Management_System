package  BankingManagerSystem;
import java.sql.*;
import  java.io.*;

public class User {

    private  Connection con;
    private  BufferedReader br;
// ParameterizedConstructor
    public User(Connection con, BufferedReader br) {
        this.con=con;
        this.br=br;
    }
    public void register(){
       try {
           System.out.print("Enter Full Name:");
           String full_name = br.readLine();
           System.out.println("Enter Email_Id:");
           String email=br.readLine();
           System.out.print("Enter Password: ");
           String password = br.readLine();
           //method
           if(user_exists(email))
           {
               System.out.println("User Already Exists for this Email Address !!!");
               return;
           }
           String register_query="INSERT INTO USERS (FULL_NAME, EMAIL ,PASSWORD) VALUES(?,?,?);";
           try
           {
               PreparedStatement registry =con.prepareStatement(register_query);
               registry.setString(1,full_name);
               registry.setString(2,email);
               registry.setString(3,password);
               int AffectedRows=registry.executeUpdate();
               if(AffectedRows>0)
               {
                   System.out.println("Registration Successful! Welcome To BANK OF INDIA");

               }else {
                   System.out.println("Registration Failed");
               }
               
           } catch (SQLException e) {
               throw new RuntimeException(e);
           }

       } catch (IOException e) {
           throw new RuntimeException(e);
       }
    }
    public String login() {
        try {
            System.out.print("Email:");
            String email = br.readLine();
            System.out.print("Password:");
            String password = br.readLine();
            String login_query="SELECT * FROM USERS WHERE EMAIL=?AND PASSWORD=?";
            try{
                PreparedStatement login = con.prepareStatement(login_query);
                login.setString(1,email);
                login.setString(2,password);
                ResultSet rs=login.executeQuery();
                if(rs.next())
                {
                    return email;
                }else{
                    return null;
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        catch (IOException e)
        {
            System.out.print(e.getMessage());
        }
        return null;
    }
     public boolean user_exists(String email){
        String query ="SELECT * FROM USERS WHERE email =?";
        try{
            PreparedStatement user= con.prepareStatement(query);
            user.setString(1,email);
            ResultSet rs = user.executeQuery();
            if(rs.next())
            {
                return  true;

            }else {
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
