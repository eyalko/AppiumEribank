import DB.writeToDB;

public class selectFromSQL {


    public static void main(String[] args) {
        String query = "select *\n" +
                "from public.execution_report\n" +
                "where device_udid = '00008020-000C1D5A0E89002E' and command_type = 'newSession'";

        String query2 = "select device_udid, command_duration\n" +
                "from public.execution_report\n" +
                "where device_udid = '00008020-000C1D5A0E89002E' and command_type = 'newSession'";

        String query3 = "select *\n" +
                "from public.execution_report";

        String query4 = "select cloud_version, device_udid, command_type, command_xpath, cast(avg(cast(command_duration as float)) as decimal(10,3)) as avg_duration\n" +
                "from public.execution_report\n" +
                "group by cloud_version, device_udid, command_type, command_xpath\n" +
                "order by cloud_version, device_udid, command_type, command_xpath";

        writeToDB app = new writeToDB();
        app.connect();

        app.selectFromDB(query4);
        app.closeConnection();
    }
}
