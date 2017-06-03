package com.mainPackage.database;

import com.mainPackage.util.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
/**
 * Created by claudiu on 02.04.2017.
 */

public class ConnectionToDB {

    private static final String driverClassName = "com.mysql.jdbc.Driver";
    private static final String url = "jdbc:mysql://localhost:3306/LICENTA";
    private static final String dbUsername = "root";
    private static final String dbPassword = "root";
    private static final String ID = "PersonID";
    private static final String USERNAME = "Username";
    private static final String FIRSTNAME = "FirstName";
    private static final String LASTNAME = "LastName";
    private static final String EMAIL = "Email";
    private static final String PASSWORD = "Password";

    private static final String insertSql =

            "INSERT INTO Users (" +

                    "PersonID, " +

                    "LastName, " +

                    "FirstName, " +

                    "Email, " +

                    "Username," +

                    "Password)" +

                    " VALUES (?, ?, ?, ?, ?, ?)";

    private static final String insertShare =
            "INSERT INTO Shares (" +

                    "PersonID, " +

                    "Symbol," +

                    "Name," +

                    "Shares," +

                    "PortofolioName)" +

                    " VALUES (?, ?, ?, ?)";

    private static final String getSqlData =
            "SELECT * FROM Users";

    private static final String lastId = "select " + "PersonId" + " from Users";

    private static final String getRowData = "select * from Users where Username=";

    private static final String getRowId = "select PersonID from Users where Username=";

    private static final String getRows = "select Symbol, Name, Shares from Shares where PersonID=";

    private static final String updateSharesRow = "update Shares set Shares=";

    private static final String deleteSharesRow = "delete from Shares where PersonID=";

    public void testConnection() {
        JdbcTemplate template = new JdbcTemplate(getDataSource());

        List<Map<String, Object>> rows = template.queryForList(getSqlData);

        for (Map<String, Object> map : rows) {
            System.out.print(map.get("PersonID") + " " + map.get("LastName") + " " + map.get("FirstName")
                    + " " + map.get("Username") + "\n");
        }
    }

    public int getNextId() {
        JdbcTemplate template = new JdbcTemplate(getDataSource());

        return template.queryForList(lastId).size() + 1;
    }

    public void saveRecord(int id, String firstName, String lastName, String email,
                                  String userName, String password) {

        JdbcTemplate template = new JdbcTemplate(getDataSource());

        // define query arguments
        Object[] params = new Object[] { id, firstName, lastName, email, userName, password };

        // define SQL types of the arguments
        int[] types = new int[] { Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
                                Types.VARCHAR, Types.VARCHAR};

        // execute insert query to insert the data
        // return number of row / rows processed by the executed query
        int row = template.update(insertSql, params, types);
        System.out.println(row + " row inserted.");

    }

    public void saveShareRecord(int id, String symbol, String name, int shares, String porofolio) {

        JdbcTemplate template = new JdbcTemplate(getDataSource());
        Object[] params = new Object[] { id, symbol, name, shares};

        int[] types = new int[] { Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR};

        // execute insert query to insert the data
        // return number of row / rows processed by the executed query
        int row = template.update(insertShare, params, types);
        System.out.println(row + " row inserted.");

    }

    public User getRecord (String userName) {

        JdbcTemplate template = new JdbcTemplate(getDataSource());
        String sqlQuery = new StringBuilder().append(getRowData)
                .append("\"" + userName + "\"").toString();
        List<Map<String, Object>> rows = template.queryForList(sqlQuery);

        if (rows.size() == 1) {
            Map<String, Object> row = rows.get(0);
            return new User(row.get(ID), row.get(FIRSTNAME), row.get(LASTNAME),
                    row.get(EMAIL), row.get(USERNAME), row.get(PASSWORD));
        }
        return null;
    }

    public int getId(String userName) {
        JdbcTemplate template = new JdbcTemplate(getDataSource());
        String sqlQuery = new StringBuilder().append(getRowId)
                .append("\"" + userName + "\"").toString();

        List<Map<String, Object>> rows = template.queryForList(sqlQuery);

        if (rows.size() == 1) {
            Map<String, Object> row = rows.get(0);

            return (Integer)row.get(ID);
        }
        return -1;
    }

    public List<Map<String, Object>> getShares(int userId) {

        JdbcTemplate template = new JdbcTemplate(getDataSource());
        String sqlQuery = new StringBuilder().append(getRows)
                .append("\"" + userId + "\"").toString();

        return template.queryForList(sqlQuery);
    }

    public List<Map<String, Object>> getShares(int userId, String symbol) {

        JdbcTemplate template = new JdbcTemplate(getDataSource());
        String sqlQuery = new StringBuilder().append(getRows)
                .append("\"" + userId + "\"")
                .append(" and Symbol=\"" + symbol + "\"")
                .toString();

        return template.queryForList(sqlQuery);
    }

    public void deleteSharesRow(int userId, String symbol, String portofolio) {
        JdbcTemplate template = new JdbcTemplate(getDataSource());
        String sqlQuery = new StringBuilder().append(deleteSharesRow)
                .append(userId + " and PorotfolioName=\"")
                .append(portofolio)
                .append("\" and Symbol=\"" + symbol + "\"")
                .toString();

        template.execute(sqlQuery);
    }

    public void updateSharesRow(int userId, String symbol, int shares, String portofolio) {
        JdbcTemplate template = new JdbcTemplate(getDataSource());
        String sqlQuery = new StringBuilder().append(updateSharesRow)
                .append(shares + " where PersonID=")
                .append(userId + " and PortofolioName=\"")
                .append(portofolio)
                .append("\" and Symbol=\"" + symbol + "\"")
                .toString();

        template.execute(sqlQuery);
    }

    public static DriverManagerDataSource getDataSource() {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(driverClassName);

        dataSource.setUrl(url);

        dataSource.setUsername(dbUsername);

        dataSource.setPassword(dbPassword);

        return dataSource;
    }
}
