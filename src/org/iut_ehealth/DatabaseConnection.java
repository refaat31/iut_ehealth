package org.iut_ehealth;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static DatabaseConnection instance = null;

    private String driver = "com.mysql.cj.jdbc.Driver";
    private String url = "jdbc:mysql://sql12.freesqldatabase.com:3306/sql12353692";
    private String username = "sql12353692";
    private String password="NruRn74dY6";
    private Connection myConn_;

    //factory method
    public static DatabaseConnection getInstance(){
        if(instance==null){
            synchronized (DatabaseConnection.class){
                instance = new DatabaseConnection();
            }
        }
        return instance;
    }

    public void connectToDatabase(){
        try{
            Connection myConn = DriverManager.getConnection(url,username,password);
            System.out.println("Connection successful");
            myConn_ = myConn;
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public Connection getConnectionObject(){
        return myConn_;
    }


}
