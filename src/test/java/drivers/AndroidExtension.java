package drivers;

import DB.CommandDetails;
import DB.CommandsPerRun;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.SessionId;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AndroidExtension extends AndroidDriver {

    final static Object lock = new Object();
    private AtomicInteger runID;
    String command_xpath;

    public AndroidExtension(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities);
    }

    @Override
    protected void log(SessionId sessionId, String commandName, Object toLog, When when) {

        super.log(sessionId, commandName, toLog, when);
//        System.out.println(toLog);

        // if (commandName.equ click) {\
        // String id = from toLog;
        // elementLocater = map.get(id)
        CommandDetails specCommand = new CommandDetails(when.toString(), commandName, System.currentTimeMillis(), toLog, command_xpath);
        CommandsPerRun.getInstance().setCommandPerRun(getRunId(), specCommand);
        command_xpath = null;

    }

    @Override public WebElement findElement(By by) {
//        System.out.println("######### by: " + by);
        command_xpath = by.toString();
        return (WebElement) super.findElement(by);
    }

    //    @Override
//    public List findElements(String by, String using) {
//        List elements = super.findElements(by, using);
//        // foreach element in elements
////        RemoteWebElement elm = (RemoteWebElement) elements[0];
//        // map.put(elm.getId(), by + "=" + using)
//        return elements;
//    }

    public Integer getRunId() {
        if(this.runID == null) {
            synchronized (lock) {
                if (this.runID == null) {
                    this.runID = new AtomicInteger(CommandsPerRun.getInstance().getNextRunID());
                }
            }
        }
        return runID.get();
    }
}
