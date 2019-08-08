package OSS;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;

public class IOS_OSSweb extends baseTestOSS {

    protected IOSDriver<IOSElement> driver = null;

    @Before
    public void setUp() throws MalformedURLException {
        String MyDeviceUDID="b386670b67fa917c2a65a9f2d70470347457678b";
//        String MyDeviceUDID="30353b7e19d6d08b254e38267908aa09c714f55a";

        dc.setCapability("appiumVersion", "1.12.1-p0");
        dc.setCapability(MobileCapabilityType.BROWSER_NAME, "Safari");
        dc.setCapability(MobileCapabilityType.PLATFORM_VERSION, "11.2.6");
        dc.setCapability("automationName", "XCUITest");
        dc.setCapability("deviceName", "auto");
        dc.setCapability("udid", MyDeviceUDID);
        dc.setCapability("accessKey", accessKey);
        dc.setCapability("testName", "My First Appium OSS web Test");
        driver = new IOSDriver<>(new URL(cloudURL+"/wd/hub"), dc);
    }

    @Test
    public void quickStartiOSNativeDemo() {
        driver.rotate(ScreenOrientation.PORTRAIT);
        driver.get("https://www.google.com");
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(driver.getPageSource());
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.name("q")));
        WebElement searchBar = driver.findElement(By.name("q"));
        searchBar.sendKeys("Experitest");
        driver.findElement(By.xpath("//*[@css='BUTTON.Tg7LZd']")).click();
    }

    @After
    public void tearDown() {
//        System.out.println("Report URL: "+ driver.getCapabilities().getCapability("reportUrl"));
        driver.quit();
    }
}