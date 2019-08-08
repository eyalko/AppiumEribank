package OSS;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;

import java.net.MalformedURLException;
import java.net.URL;

public class IOS_OSSnative extends baseTestOSS {


    @Before
    public void setUp() throws MalformedURLException {
        String MyDeviceUDID="00008020-001e4d5c0204002e";
//        String MyDeviceUDID="7b2ae069a67d40cb46075c7ba03c15bbee364f4b";
        dc.setCapability("appiumVersion", "1.10.0-p0");
        dc.setCapability(MobileCapabilityType.APP, "cloud:com.experitest.ExperiBank:3053");
        dc.setCapability("bundleId", "com.experitest.ExperiBank");
        dc.setCapability(MobileCapabilityType.PLATFORM_VERSION, "11.2.6");
        dc.setCapability("automationName", "XCUITest");
        dc.setCapability("deviceName", "auto");
        dc.setCapability("udid", MyDeviceUDID);
        dc.setCapability("accessKey", accessKey);
        dc.setCapability("testName", "My First Appium OSS native Test");
        driver = new IOSDriver<>(new URL(cloudURL+"/wd/hub"), dc);
    }

    @Test
    public void quickStartiOSNativeDemo() {
        driver.rotate(ScreenOrientation.PORTRAIT);
//        try {
//            Thread.sleep(30000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        String webDriverAgentURL = driver.getCapabilities().getCapability("webDriverAgentUrl").toString();
//        System.out.println(webDriverAgentURL.contains("docker"));
        System.out.println(dc.getCapability("appiumVersion"));
        System.out.println(dc.getCapability("automationName"));
        System.out.println(dc.getCapability("automationName2"));


        System.out.println(driver.getPageSource());
        System.out.println(driver.getCapabilities());
        driver.findElement(By.xpath("//*[@label='usernameTextField']")).sendKeys("company");
        driver.findElement(By.xpath("//*[@label='passwordTextField']")).sendKeys("company");
        driver.findElement(By.xpath("//*[@label='loginButton']")).click();
        driver.findElement(By.xpath("//*[@label='makePaymentButton']")).click();
        driver.findElement(By.xpath("//*[@label='phoneTextField']")).sendKeys("0541234567");
        driver.findElement(By.xpath("//*[@label='nameTextField']")).sendKeys("Jon Snow");
        driver.findElement(By.xpath("//*[@label='amountTextField']")).sendKeys("50");
        driver.findElement(By.xpath("//*[@label='countryButton']")).click();
        driver.findElement(By.xpath("//*[@label='Switzerland']")).click();
        driver.findElement(By.xpath("//*[@label='sendPaymentButton']")).click();
        driver.findElement(By.xpath("//*[@label='Yes']")).click();
    }

    @After
    public void tearDown() {
        System.out.println("Report URL: "+ driver.getCapabilities().getCapability("reportUrl"));
        driver.quit();
    }
}