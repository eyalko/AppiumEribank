package DB;

import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class writeToDB {

    String DBserverURL = "192.168.2.80";
    String DBport = "5432";
    String DBname = "testResults";

    private final String url = "jdbc:postgresql://"+ DBserverURL +":"+ DBport +"/"+DBname;
    private final String user = "postgres";
    private final String password = "Experitest2012";

//    private static writeToDB writeToDBInstance = null;
    private static Connection conn;

//    public synchronized static writeToDB getInstance(){
//        if(writeToDBInstance == null){
//            writeToDBInstance = new writeToDB();
//        }
//        return writeToDBInstance;
//    }

    public writeToDB() {
        connect();
    }

    // local DB
//    private final String url = "jdbc:postgresql://localhost:5432/performanceDeep";
//    private final String user = "postgres";
//    private final String password = "adminPassw0rd";

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    private Connection connect() {
//        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully at: " + url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void closeConnection() {
        try {
            conn.close();
            System.out.println("Connection to PostgreSQL closed successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized long insertCommand(rowInTable currentRow) {
        String SQL = "INSERT INTO public.execution_report(cloud_version,dhm_os,device_udid,device_version,test_name,command_type,command_xpath,command_duration,date_time,device_os)"
                + "VALUES(?,?,?,?,?,?,?,?,?,?)";

        long id = 1;
//        System.out.println(currentRow);
        try (PreparedStatement pstmt = conn.prepareStatement(SQL,
                Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, currentRow.cloud_version);
            pstmt.setString(2, currentRow.dhm_os);
            pstmt.setString(3, currentRow.device_udid);
            pstmt.setString(4, currentRow.device_version);
            pstmt.setString(5, currentRow.test_name);
            pstmt.setString(6, currentRow.command_type);
            pstmt.setString(7, currentRow.command_xpath);
            pstmt.setString(8, currentRow.command_duration);
            pstmt.setString(9, new SimpleDateFormat("EEE MMM d yyyy HH:mm:ss z").format(new Date()));
            pstmt.setString(10, StringUtils.capitalize(currentRow.device_os));

            int affectedRows = pstmt.executeUpdate();
            // check the affected rows
            if (affectedRows > 0) {
                // get the ID back
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        id = rs.getLong(1);
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            System.out.println(currentRow.device_udid+ " - row added to the DB: " + id + ", command: " + currentRow.command_type + ", xpath: " + currentRow.command_xpath + ", duration: " + currentRow.command_duration);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return id;
    }


    public List<Map<String , String>> getDataFromTable(String query) {
        List<Map<String , String>> tableArray  = new ArrayList<Map<String,String>>();

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs;
            rs = stmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            //System.out.println("querying \"" + query + "\"");

            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                Map<String,String> RowMap = new HashMap<String, String>();
                for (int i = 1; i <= columnsNumber; i++) {
                    RowMap.put(rsmd.getColumnName(i), rs.getString(i));
                }
                tableArray.add(RowMap);
            }
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
        return tableArray;
    }


}