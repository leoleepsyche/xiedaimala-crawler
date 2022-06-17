//package com.github.hcsp.http;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Properties;
//
//public class JDBCConnectionTest {
//    private Object userName = "root";
//    private Object password = "lg19931019";
//    private Object dbms = "mysql";
//    private Object serverName = "localhost";
//    private Object portNumber = 3306;
//    private Object dbName = "Test";
//    Connection conn = null;
//    //  de-dependency is well.
//    public Connection getConnection() throws SQLException {
//
//
//        Properties connectionProps = new Properties();
//        connectionProps.put("user", this.userName);
//        connectionProps.put("password", this.password);
//
//        if (this.dbms.equals("mysql")) {
//            conn = DriverManager.getConnection(
//                    "jdbc:" + this.dbms + "://" +
//                            this.serverName +
//                            ":" + this.portNumber + "/",
//                    connectionProps);
//        } else if (this.dbms.equals("derby")) {
//            conn = DriverManager.getConnection(
//                    "jdbc:" + this.dbms + ":" +
//                            this.dbName +
//                            ";create=true",
//                    connectionProps);
//        }
//        System.out.println("Connected to database");
//        System.out.println("我成功啦！！！");
//        return conn;
//    }
//
//    public static void main(String[] args) throws SQLException {
//        new JDBCConnectionTest().getConnection();
//    }
//
//    public void createTable() throws SQLException {
//        String createString =
//                "create table SUPPLIERS " + "(SUP_ID integer NOT NULL, " +
//                        "SUP_NAME varchar(40) NOT NULL, " + "STREET varchar(40) NOT NULL, " +
//                        "CITY varchar(20) NOT NULL, " + "STATE char(2) NOT NULL, " +
//                        "ZIP char(5), " + "PRIMARY KEY (SUP_ID))";
//
//
//        try (Statement stmt = conn.createStatement()) {
//            stmt.executeUpdate(createString);
//        } catch (SQLException e) {
//            JDBCTutorialUtilities.printSQLException(e);
//        }
//    }
//
//
//
//
//
//}
