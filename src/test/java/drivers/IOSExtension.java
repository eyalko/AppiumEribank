package drivers;

import DB.CommandDetails;
import DB.CommandsPerRun;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.SessionId;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

public class IOSExtension extends IOSDriver {

    final static Object lock = new Object();
    private AtomicInteger runID;
    String command_xpath;

    public IOSExtension(URL remoteAddress, Capabilities desiredCapabilities) {
        super(remoteAddress, desiredCapabilities);
    }

    @Override
    protected void log(SessionId sessionId, String commandName, Object toLog, When when) {

        super.log(sessionId, commandName, toLog, when);

        CommandDetails specCommand = new CommandDetails(when.toString(), commandName, System.currentTimeMillis(), toLog, command_xpath);
        CommandsPerRun.getInstance().setCommandPerRun(getRunId(), specCommand);
        command_xpath = null;

    }

    @Override public WebElement findElement(By by) {
//        System.out.println("######### by: " + by);
        command_xpath = by.toString();
        return (WebElement) super.findElement(by);
    }

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
