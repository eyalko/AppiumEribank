import DB.CommandDetails;
import DB.CommandsPerRun;
import DB.rowInTable;
import DB.writeToDB;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import drivers.AndroidExtension;
import Settings.CloudUsers;
import com.experitest.appium.SeeTestClient;
import drivers.IOSExtension;
import io.appium.java_client.AppiumDriver;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;


public class baseTest {

    protected String accessKey, cloudURL, deviceQuery, whichClass;
    protected String testName = "general test name";
    public static CloudUsers cloudUser;
    protected AppiumDriver driver = null;
    DesiredCapabilities dc = new DesiredCapabilities();
    SeeTestClient client;
    String dateFormat = "EEE MMM d yyyy HH_mm_ss z";
    private Integer runID;

    writeToDB app;
    String cloudVersion;
//    String deviceOS = "";

    @Before
    public void setUp() throws MalformedURLException {
        cloudUser = CloudUsers.EyalDeepCloudAdmin;
//        cloudUser = CloudUsers.LocalCloud;
        accessKey = cloudUser.getAccessKey();
        cloudURL = cloudUser.getCloudFullAddress();
        whichClass = getClass().getName();
        System.out.println("this class is: " + whichClass);

        app = new writeToDB();
        app.connect();

        cloudVersion = getCloudVersion();

        setUpClass();
        dc.setCapability("testName", testName);
        dc.setCapability("accessKey", accessKey);
        dc.setCapability("deviceQuery", deviceQuery);
        dc.setCapability("RunNameEyal", "Eyal");

        URL url = new URL(cloudURL + "/wd/hub");
        if (whichClass.equals("Android")) {
            driver = new AndroidExtension(url, dc);
            runID = ((AndroidExtension)driver).getRunId();
        } else if (whichClass.equals("IOS")){
            driver = new IOSExtension(url, dc);
            runID = ((IOSExtension)driver).getRunId();
        }
        client = new SeeTestClient(driver);
    }

    @After
    public void tearDown() {
        System.out.println("Report URL: "+ driver.getCapabilities().getCapability("reportUrl"));
//        System.out.println(deviceOS = client.getDeviceProperty("device.os"));
        driver.quit();
        goOverListOfCommands();

        CommandsPerRun.getInstance().clearRun(runID);
        app.closeConnection();
    }

    public void setUpClass() {}







    public void goOverListOfCommands() {
        long beforeTime = 0;
        long afterTime = 0;
        String command_xpath = null;
        Iterator i = CommandsPerRun.getInstance().getCommandsForRun(runID).iterator();
//        System.out.println("The ArrayList elements are:");
        while (i.hasNext()) {
            CommandDetails current = (CommandDetails) i.next();
//            System.out.println("********** " + current);
            if (current.when.equals("BEFORE")) {
                beforeTime = current.time;
                command_xpath = current.xpath;
            } else if (current.when.equals("AFTER") && beforeTime!=0) {
                afterTime = current.time;
                String duration = String.valueOf((double)(afterTime-beforeTime)/1000);
                rowInTable r = new rowInTable(cloudVersion, "192.168.2.100", driver.getCapabilities().getCapability("device.serialNumber").toString(), driver.getCapabilities().getCapability("device.version").toString(), testName, current.command, command_xpath, duration, driver.getCapabilities().getCapability("device.os").toString());
                app.insertCommand(r);
                beforeTime = 0;
                command_xpath = null;

            }

        }
    }


    public static String getCloudVersion() {
        String CloudVersion = "";
        HttpResponse<String> responseString;
        String url = cloudUser.getCloudFullAddress()+"/api/v2/status/capabilities";

        try {
            responseString = Unirest.get(url)
                    .basicAuth(cloudUser.getUserName(), cloudUser.getPassword())
                    .header("content-type", "application/json")
                    .asString();
            //   System.out.println(responseString.getBody());
            CloudVersion = parsingJson(responseString.getBody(), "version");
//            deviceOS = parsingJson(responseString.getBody(), "os");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return CloudVersion;
    }

    public static String parsingJson(String Json_String,String key){
        String returnValue=null;
        try {
            JSONObject Json_obj = new JSONObject(Json_String);
            returnValue = Json_obj.getString(key);
        }catch (Exception e){
            System.out.println("Cant parse JSON "+ e.getMessage());

        }

        return returnValue;
    }


}


