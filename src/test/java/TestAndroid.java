import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

public class TestAndroid {

    private String accessKey = "eyJ4cC51IjoxNTg0ODA0LCJ4cC5wIjoyLCJ4cC5tIjoiTVRVMU5ERXdORFF6TmprMU13IiwiYWxnIjoiSFMyNTYifQ.eyJleHAiOjE4Njk0NjQ0MzYsImlzcyI6ImNvbS5leHBlcml0ZXN0In0.cuilvVOQwSXcXr2uV9pOzuw3yqhjtSrdUsq-Ilj57xQ";
    protected AndroidDriver<AndroidElement> driver = null;
    DesiredCapabilities dc = new DesiredCapabilities();

    @Before
    public void setUp() throws MalformedURLException {
        dc.setCapability("testName", "Quick Start Android Native Demo");
//        dc.setCapability("newSessionWaitTimeout", 30);
        dc.setCapability("accessKey", accessKey);
//        dc.setCapability("deviceQuery", "@os='android'");
        dc.setCapability("deviceQuery", "@serialNumber='03157df3ca7b8a28'");
        dc.setCapability(MobileCapabilityType.APP, "cloud:com.example.shaharyannay.dotgame/.Activity.LoginActivity");
        dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.example.shaharyannay.dotgame");
        dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".Activity.LoginActivity");
        driver = new AndroidDriver<>(new URL("https://qa-win2016.experitest.com/wd/hub"), dc);
    }

    @Test
    public void quickStartAndroidNativeDemo() {
        driver.rotate(ScreenOrientation.PORTRAIT);
        System.out.println(driver.getContext());
//        driver.findElement(By.xpath("//*[@id='usernameTextField']")).sendKeys("company");
//        driver.hideKeyboard();
//        driver.findElement(By.xpath("//*[@id='passwordTextField']")).sendKeys("company");
//        driver.findElement(By.xpath("//*[@id='loginButton']")).click();
//        driver.findElement(By.xpath("//*[@id='makePaymentButton']")).click();
//        driver.findElement(By.xpath("//*[@id='phoneTextField']")).sendKeys("0541234567");
//        driver.findElement(By.xpath("//*[@id='nameTextField']")).sendKeys("Jon Snow");
//        driver.findElement(By.xpath("//*[@id='amountTextField']")).sendKeys("50");
//        driver.findElement(By.xpath("//*[@id='countryButton']")).click();
//        driver.findElement(By.xpath("//*[@text='Switzerland']")).click();
//        driver.findElement(By.xpath("//*[@id='sendPaymentButton']")).click();
//        driver.findElement(By.xpath("//*[@text='Yes']")).click();
    }

    @After
    public void tearDown() {
        System.out.println("Report URL: "+ driver.getCapabilities().getCapability("reportUrl"));
        driver.quit();
    }
}