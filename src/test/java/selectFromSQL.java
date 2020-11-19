import DB.writeToDB;

import java.util.List;
import java.util.Map;

public class selectFromSQL {

    public static writeToDB app;

    public static String averageDurationAPIquery(String api_name) {
        System.out.println("* API name: " + api_name);
        return
            "select * from (\n" +
            "    select api_name, cloud_version, cast( AVG(cast(duration as float)) as decimal(10,3)) as build_average_duration, count(*) as num, row_number() over (partition by api_name order by cloud_version desc) as rownum\n" +
            "    \tfrom api_report\n" +
            "    \twhere api_status='200' and api_name='" + api_name + "'\n" +
            "    \tgroup by api_name, cloud_version\n" +
            "    \torder by cloud_version\n" +
            ") as table1\n" +
            "where rownum<=50";
    }

    public static String averageDurationQuery(String OS, String test_name) {
        System.out.println("* OS: " + OS + "; Test: " + test_name);
        return
            "select * from (\n" +
            "select device_os, test_name, cloud_version, cast( AVG(cast(duration as float)) as decimal(10,3)) as build_average_duration, count(*) as num, row_number() over (partition by test_name order by cloud_version desc) as rownum\n" +
            "\tfrom execution_report\n" +
            "\twhere device_os='" + OS + "' and test_name='" + test_name + "' and status='Passed' and num_devices_in_run='1'\n" +
            "\tgroup by device_os, test_name, cloud_version\n" +
            "\torder by cloud_version\n" +
            ") as table1\n" +
            "where rownum<=20";
    }

    static String query4 = "select * from execution_report where test_name='SimpleEriBankTest'";

    public static void main(String[] args) {
        app = new writeToDB(); // open connection
        String column = "build_average_duration";
        validateDeviation(averageDurationQuery("ios", "NonInstrumentEribank"), column);
        validateDeviation(averageDurationQuery("ios", "InstrumentEribank"), column);
        validateDeviation(averageDurationQuery("android", "NonInstrumentEribank"), column);
        validateDeviation(averageDurationQuery("android", "NonInstrumentEribank"), column);

        validateDeviation(averageDurationAPIquery("new user"), column);
        validateDeviation(averageDurationAPIquery("new project"), column);
        validateDeviation(averageDurationAPIquery("assign user to project"), column);
        validateDeviation(averageDurationAPIquery("unassign user from project"), column);

        validateDeviation(averageDurationAPIquery("delete user"), column);
        validateDeviation(averageDurationAPIquery("get applications"), column);
        validateDeviation(averageDurationAPIquery("upload application dotgame"), column);
        validateDeviation(averageDurationAPIquery("upload application eribank apk"), column);

        validateDeviation(averageDurationAPIquery("delete application"), column);

        app.closeConnection(); // close connection

    }


    public static void validateDeviation(String query, String columnToVerify) {

        List<Map<String , String>> tableArray = app.getDataFromTable(query);

//        System.out.println("all builds average duration");
//        for (int i=0; i<tableArray.size(); i++) {
//            System.out.print(tableArray.get(i).get(columnToVerify) + ", ");
//        }
//        System.out.println();


        // check deviation status of last build
        Double deviation = calculateSD(tableArray, columnToVerify);
        Double avg = calculateAvg(tableArray, columnToVerify);
        String lastBuild = tableArray.get(tableArray.size()-1).get(columnToVerify);
        Boolean ok = checkDeviation(deviation, lastBuild, avg);

        System.out.println("Cloud version: " + tableArray.get(tableArray.size()-1).get("cloud_version"));
        System.out.print("SD: " + String.format("%.3f", deviation));
        System.out.print("; avg: " + String.format("%.3f", avg));
        System.out.println("; last duration: " + lastBuild);
        System.out.println("ok is: " + ok);
        System.out.println();

    }







    private static double calculateSD(List<Map<String , String>> tableArray, String column) {
        double sum = 0.0, standardDeviation = 0.0;
        int length = tableArray.size();
        for (int i=0; i<tableArray.size(); i++) {
            sum += Double.valueOf(tableArray.get(i).get("build_average_duration"));
        }
        double mean = sum/length;

        for (int i=0; i<tableArray.size(); i++) {
            standardDeviation += Math.pow(Double.valueOf(tableArray.get(i).get("build_average_duration")) - mean, 2);
        }

        return Math.sqrt(standardDeviation/length);
    }

    private static double calculateAvg(List<Map<String , String>> tableArray, String column) {
        double sum = 0.0;
        int length = tableArray.size();
        for (int i=0; i<tableArray.size(); i++) {
            sum += Double.valueOf(tableArray.get(i).get("build_average_duration"));
        }
        double mean = sum/length;

        return mean;
    }

    private static boolean checkDeviation(double sd, String lastBuildDuration, double avg) {
        double diff = Math.abs(Double.valueOf(lastBuildDuration) - avg);
        boolean ok = diff < (sd * 3);
        return ok;
    }
}
