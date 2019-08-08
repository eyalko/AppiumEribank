
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;


public class Android extends baseTest {


    @Override
    public void setUpClass() {
        testName = "Test Android";
        String udid = runInParallel.useDeviceFromFilterDevices("Android");
        if (udid != null) {
            deviceQuery = "@serialNumber='"+udid+"'";
        } else {

        }
        dc.setCapability(MobileCapabilityType.APP, "cloud:com.experitest.ExperiBank/.LoginActivity");
        dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.experitest.ExperiBank");
        dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".LoginActivity");


    }

    @Test
    public void quickStartAndroidNativeDemo() throws InterruptedException {
        System.out.println(client.getDevicesInformation());
        driver.rotate(ScreenOrientation.PORTRAIT);
        driver.findElement(By.xpath("//*[@id='usernameTextField']")).sendKeys("company");
        driver.hideKeyboard();
        driver.findElement(By.xpath("//*[@id='passwordTextField']")).sendKeys("company");
        driver.findElement(By.xpath("//*[@id='loginButton']")).click();
//        driver.findElement(By.xpath("//*[@id='makePaymentButton']")).click();
//        driver.findElement(By.xpath("//*[@id='phoneTextField']")).sendKeys("0541234567");
//        driver.findElement(By.xpath("//*[@id='nameTextField']")).sendKeys("Jon Snow");
//        driver.findElement(By.xpath("//*[@id='amountTextField']")).sendKeys("50");
//        driver.findElement(By.xpath("//*[@id='countryButton']")).click();
//        driver.findElement(By.xpath("//*[@text='Switzerland']")).click();
//        driver.findElement(By.xpath("//*[@id='sendPaymentButton']")).click();
//        driver.findElement(By.xpath("//*[@text='Yes']")).click();

    }


    public void setWifi() {
        try {
            client.run("am instrument -w -r com.experitest.device.devicecontrol/com.experitest.device.devicecontrol.instrumentations.WifiForgetAll");
            client.run("am instrument -w -r -e ssid lab -e password 0528544681 com.experitest.device.devicecontrol/com.experitest.device.devicecontrol.instrumentations.WifiConfig");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}