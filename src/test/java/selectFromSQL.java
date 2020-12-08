import DB.writeToDB;

import java.util.*;

public class selectFromSQL {

    public static writeToDB app;

    public static String averageDurationAPIquery(String api_name, String numOfBuilds) {
        System.out.println("* API name: " + api_name);
        return
            "select * from (\n" +
            "    select api_name, cloud_version, cast( AVG(cast(duration as float)) as decimal(10,3)) as build_average_duration, count(*) as num, row_number() over (partition by api_name order by cloud_version desc) as rownum\n" +
            "    \tfrom api_report\n" +
            "    \twhere api_status='200' and api_name='" + api_name + "'\n" +
            "    \tgroup by api_name, cloud_version\n" +
            "    \torder by cloud_version\n" +
            ") as table1\n" +
            "where rownum<=" + numOfBuilds;
    }

    public static String averageDurationTestsQuery(String OS, String test_name, String numOfBuilds) {
        System.out.println("* OS: " + OS + "; Test: " + test_name);
        return
            "select * from (\n" +
            "select device_os, test_name, cloud_version, cast( AVG(cast(duration as float)) as decimal(10,3)) as build_average_duration, count(*) as num, row_number() over (partition by test_name order by cloud_version desc) as rownum\n" +
            "\tfrom execution_report\n" +
            "\twhere device_os='" + OS + "' and test_name='" + test_name + "' and status='Passed' and num_devices_in_run='1'\n" +
            "\tgroup by device_os, test_name, cloud_version\n" +
            "\torder by cloud_version\n" +
            ") as table1\n" +
            "where rownum<=" + numOfBuilds;
    }

    public static String averageDurationCommands(String OS, String command_type, String command_xpath, String numOfBuilds) {
        System.out.println("* OS: " + OS + "; Command: " + command_type + "; XPath: " + command_xpath);
        return
            "select * from (\n" +
            "select cloud_version, device_os, device_udid, command_type, command_xpath, cast( AVG(cast(command_duration as float)) as decimal(10,3)) as build_average_duration, count(*) as num, row_number() over (partition by command_type,command_xpath order by cloud_version desc) as rownum\n" +
            "\tfrom commands_report\n" +
            "\twhere device_os='" + OS + "' and command_type='" + command_type + "' and command_xpath='" + command_xpath + "'\n" +
            "\tgroup by cloud_version, device_os, device_udid, command_type, command_xpath\n" +
            "\torder by cloud_version\n" +
            ") as table1\n" +
            "where rownum<=" + numOfBuilds;
    }

    public static String getAllAPIQuery = "select api_name from api_report\n" +
            "group by api_name";

    public static String getAllTestsQuery = "select device_os, test_name from execution_report\n" +
            "group by device_os, test_name\n" +
            "order by device_os, test_name";

    static String query4 = "select * from execution_report where test_name='SimpleEriBankTest'";

    public static void main(String[] args) {
        app = new writeToDB(); // open connection
        String column = "build_average_duration";
//        newValidateDeviation(averageDurationTestsQuery("ios", "NonInstrumentEribank", "30"), column);
//        newValidateDeviation(averageDurationTestsQuery("ios", "InstrumentEribank"), column);
//        newValidateDeviation(averageDurationTestsQuery("android", "NonInstrumentEribank"), column);
//        newValidateDeviation(averageDurationTestsQuery("android", "NonInstrumentEribank"), column);

//        System.out.println(all_tests_table());
//        System.out.println(all_api_names_table());

        all_tests_table().forEach((n) -> {
            newValidateDeviation(averageDurationTestsQuery(n.split(",")[0] ,n.split(",")[1], "20"), column);
        });

        all_api_names_table().forEach((n) -> {
            newValidateDeviation(averageDurationAPIquery(n, "50"), column);
        });

//        newValidateDeviation(averageDurationCommands("ios", "findElement", "By.xpath: //*[@id=''usernameTextField'']"), column);
//
//        validateDeviation(averageDurationAPIquery("new user"), column);
//        validateDeviation(averageDurationAPIquery("new project"), column);
//        validateDeviation(averageDurationAPIquery("assign user to project"), column);
//        validateDeviation(averageDurationAPIquery("unassign user from project"), column);
//
//        validateDeviation(averageDurationAPIquery("delete user"), column);
//        validateDeviation(averageDurationAPIquery("get applications"), column);
//        validateDeviation(averageDurationAPIquery("upload application dotgame"), column);
//        validateDeviation(averageDurationAPIquery("upload application eribank apk"), column);
//
//        validateDeviation(averageDurationAPIquery("delete application"), column);
//        validateDeviation(averageDurationAPIquery("delete application dotgame"), column);
//        validateDeviation(averageDurationAPIquery("delete application eribank apk"), column);

        app.closeConnection(); // close connection

    }

    private static List<String> all_api_names_table() {
        List<Map<String , String>> tableArray = app.getDataFromTable(getAllAPIQuery);
        List<String> data = new ArrayList<>();
        for (int i=0; i<tableArray.size()-1; i++) {
            data.add(tableArray.get(i).get("api_name"));
        }
        return data;
    }

    private static List<String> all_tests_table() {
        List<Map<String , String>> tableArray = app.getDataFromTable(getAllTestsQuery);
        List<String> data = new ArrayList<>();
        for (int i=0; i<tableArray.size()-1; i++) {
            data.add(tableArray.get(i).get("device_os") + "," + tableArray.get(i).get("test_name"));
        }
        return data;
    }

    /**
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
        String lastBuildDuration = tableArray.get(tableArray.size()-1).get(columnToVerify);
        Boolean ok = checkDeviation(deviation, lastBuildDuration, avg);

        System.out.println("Cloud version: " + tableArray.get(tableArray.size()-1).get("cloud_version"));
        System.out.print("SD: " + String.format("%.3f", deviation));
        System.out.print("; avg: " + String.format("%.3f", avg));
        System.out.println("; last duration: " + lastBuildDuration);
        System.out.println("ok is: " + ok);
        System.out.println();

    }
    **/

    public static void newValidateDeviation(String query, String columnToVerify) {
        // get only data column from query (except the new one)
        List<Map<String , String>> tableArray = app.getDataFromTable(query);
        List<String> data = new ArrayList<>();
        for (int i=0; i<tableArray.size()-1; i++) {
            data.add(tableArray.get(i).get(columnToVerify));
        }
//        data = new ArrayList<>(Arrays.asList("1","1.01","11.9","1.02","1.03","12","1.05","1.04"));
//        System.out.println("all before durations are: " + data.toString() + " size: " + data.size());

        Double sd = calculateSDarray(data);
        Double avg = getAverage(data);
        Iterator<String> dataIterator;
        boolean foundOutlier = true;

        while (foundOutlier) {
            foundOutlier = false;
            dataIterator = data.iterator();
            sd = calculateSDarray(data);
            avg = getAverage(data);
//            System.out.println("new sd is: " + sd + ", new avg is: " + avg);
            while (dataIterator.hasNext()) {
                String element = dataIterator.next();
                boolean check = verifyDeviation(sd, Double.valueOf(element), avg);
                if (!check) { // element is outlier - out of range
                    foundOutlier = true;
//                    System.out.println("removed " + element);
                    dataIterator.remove();
                }
            }
//            System.out.println("new data is: " + data + " size: "+ data.size());
        }
//        System.out.println("all after durations are: " + data.toString() + " size: " + data.size());

        // last cloud build to verify with final SD and AVG
        String lastBuildDuration = tableArray.get(tableArray.size()-1).get(columnToVerify);
        boolean ok = verifyDeviation(sd, Double.valueOf(lastBuildDuration), avg);
        System.out.println("Cloud version: " + tableArray.get(tableArray.size()-1).get("cloud_version"));
        System.out.print("SD: " + String.format("%.3f", sd));
        System.out.print("; avg: " + String.format("%.3f", avg));
        System.out.println("; last duration: " + lastBuildDuration);
        System.err.print("status is: " + ok);
        System.out.println(" compare to: " + data.size() + " builds\n");

    }

    private static boolean verifyDeviation(double sd, double duration, double avg) {
        double diff = Math.abs(duration - avg);
        boolean ok = diff < (sd * 2);
        return ok;
    }


    private static double calculateSDarray(List<String> data) {
        double standardDeviation = 0.0;
        int length = data.size();
        double mean = getAverage(data);

        for (int i=0; i<data.size(); i++) {
            standardDeviation += Math.pow(Double.valueOf(data.get(i)) - mean, 2);
        }
        return Math.sqrt(standardDeviation/length);
    }

    private static double getAverage(List<String> data) {
        double sum = 0.0;
        int length = data.size();
        for (int i=0; i<data.size(); i++) {
            sum += Double.valueOf(data.get(i));
        }
        double mean = sum/length;

        return mean;
    }









    // old methods

    private static double calculateSD(List<Map<String , String>> tableArray, String column) {
        double sum = 0.0, standardDeviation = 0.0;
        int length = tableArray.size()-1;
        for (int i=0; i<tableArray.size()-1; i++) {
            sum += Double.valueOf(tableArray.get(i).get("build_average_duration"));
        }
        double mean = sum/length;

        for (int i=0; i<tableArray.size()-1; i++) {
            standardDeviation += Math.pow(Double.valueOf(tableArray.get(i).get("build_average_duration")) - mean, 2);
        }

        return Math.sqrt(standardDeviation/length);
    }

    private static double calculateAvg(List<Map<String , String>> tableArray, String column) {
        double sum = 0.0;
        int length = tableArray.size()-1;
        for (int i=0; i<tableArray.size()-1; i++) {
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
