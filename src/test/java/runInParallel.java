import DB.writeToDB;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.aspectj.weaver.ast.And;
import org.junit.experimental.ParallelComputer;
import org.junit.runner.JUnitCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class runInParallel {

    private static int numOfAndroid = 0;
    private static int numOfIOS = 0;
    public static writeToDB app;


    public static void main(String[] args) {

        try {
            getAllDevices();
        } catch (UnirestException e) {
            e.printStackTrace();
        }


        app = new writeToDB();
        app.connect();
        int n = numOfAndroid + numOfIOS;
        Class<?>[] classes = new Class[n];
        for (int i=0; i<numOfAndroid; i++) {
            classes[i] = Android.class;
        }
        for (int i=numOfAndroid; i<n; i++) {
            classes[i] = IOS.class;
        }
        // ParallelComputer(true,true) will run all classes and methods
        // in parallel.  (First arg for classes, second arg for methods)
//        JUnitCore.runClasses(new ParallelComputer(true, true), classes);

        // not in parallel
        JUnitCore.runClasses(classes);

    }

    static getDevicesInfo d1 = new getDevicesInfo();
    static Map<String, String>[] devices = new Map[120];
    private static List<String> androidDevices = new ArrayList<>();
    private static List<String> iOSDevices = new ArrayList<>();

    public static void getAllDevices() throws UnirestException {

        devices = d1.getArrayOfDevices("https://qa-win2016.experitest.com", "eyalk", "Jj123456");
        for (int i = 0; i < devices.length; i++) {
            if (devices[i] != null) {
                if (devices[i].get("displayStatus").equals("Available")) {
                    String id = devices[i].get("udid");
                    switch (devices[i].get("deviceOs")) {
                        case "iOS":
                            iOSDevices.add(id);
                            break;
                        case "Android":
                            androidDevices.add(id);
                            break;
                    }
                }
            }
        }
        System.out.println("ios device are: " + iOSDevices.toString());
        System.out.println("android device are: " + androidDevices.toString());
    }

    public synchronized static String useDeviceFromFilterDevices(String os) {
        String deviceUDID = null;
        if (os=="iOS" && iOSDevices.size()>0) {
            deviceUDID = iOSDevices.get(0);
            iOSDevices.remove(0);
        } else if (os=="Android" && androidDevices.size()>0) {
            deviceUDID = androidDevices.get(0);
            androidDevices.remove(0);
        }
        return deviceUDID;
    }

}
