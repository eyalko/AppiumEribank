package DB;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class writeToDB {

    String DBserverURL = "192.168.1.216";
    String DBport = "5432";
    String DBname = "performanceDeep";

    private final String url = "jdbc:postgresql://"+ DBserverURL +":"+ DBport +"/"+DBname;
    private final String user = "postgres";
    private final String password = "Experitest2012";

    public static Connection conn;

    // local DB
//    private final String url = "jdbc:postgresql://localhost:5432/performanceDeep";
//    private final String user = "postgres";
//    private final String password = "adminPassw0rd";

    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    public Connection connect() {
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


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        writeToDB app = new writeToDB();

        app.connect();



//        rowInTable currentRow = new rowInTable("12.8.7777","Windows","32354555ddd41","13.0", "Test iOS","click","//*[@id='passwordTextField']","1.333");
//        long commandID = app.insertCommand(currentRow);
//        System.out.println("c_id row added to table: " + commandID);


    }


//    public static String getCloudVersion() {
//        String CloudVersion="";
//        HttpResponse<String> responseString;
//        String url = "https://qa-win2016.experitest.com/api/v2/status/capabilities";
//        System.out.println("API_Cloud Url is: "+url);
//
//        try {
//            responseString = Unirest.get(url)
//                    .basicAuth("eyalk", "Experitest2012")
//                    .header("content-type", "application/json")
//                    .asString();
//            //   System.out.println(responseString.getBody());
//            CloudVersion = parsingJson(responseString.getBody(),"version");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return CloudVersion;
//
//    }
//
//    public static String parsingJson(String Json_String,String key){
//        System.out.println("Json string is "+Json_String);
//        String returnValue=null;
//        try {
//            JSONObject Json_obj = new JSONObject(Json_String);
//            returnValue = Json_obj.getString(key);
//        }catch (Exception e){
//            System.out.println("Cant parse JSON "+ e.getMessage());
//
//        }
//
//        return returnValue;
//    }


    public void selectFromDB(String query) {

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs;

            rs = stmt.executeQuery(query);


            ResultSetMetaData rsmd = rs.getMetaData();
            System.out.println("querying \"" + query + "\"");
            int columnsNumber = rsmd.getColumnCount();
            String[] row = new String[columnsNumber];
            String format = StringUtils.repeat("%-40s",columnsNumber);
//            String format = "%-15s%-15s%-15s%-45s%-17s%-25s%-25s%-35s%-20s%-35s%-10s";
            for (int i = 1; i <= columnsNumber; i++) {
                row[i-1] = rsmd.getColumnName(i);
            }
            System.out.format(format+"\n", row);

            // ******
            row = new String[columnsNumber];
            for (int i = 1; i <= columnsNumber; i++) {
                row[i - 1] = "----------";
            }
            System.out.format(format+"\n", row);

            while (rs.next()) {
                row = new String[columnsNumber];
                for (int i = 1; i <= columnsNumber; i++) {
                    row[i-1] = rs.getString(i);

//                    if (i > 1) System.out.print(", ");
//                    String columnValue = rs.getString(i);
//                    System.out.print(rsmd.getColumnName(i) + ": " + columnValue);
                }
                System.out.format(format+"\n", row);
            }
            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }



}