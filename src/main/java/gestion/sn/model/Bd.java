package gestion.sn.model;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import java.sql.Connection;
import java.sql.DriverManager;

public class Bd {
    private final String server = "javafx";
    private final String username = "root";
    private final String password = "";
    private final String bd = "javafx";
    private final String url = "jdbc:mysql://localhost:3306/tp1javabd";
    private Connection conn;

    public Bd() {
    }

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/javafx", "root", "");
            System.out.println("Connection a la base reussi ");
        } catch (Exception var2) {
            this.conn = null;
            System.out.print("Erreur de Connection  ");
        }

        return this.conn;
    }
}
