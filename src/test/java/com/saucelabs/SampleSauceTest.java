package com.yourcompany;

import com.saucelabs.common.SauceOnDemandAuthentication;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.saucelabs.junit.ConcurrentParameterized;
import com.saucelabs.junit.SauceOnDemandTestWatcher;

import java.net.URL;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
 
import com.saucelabs.common.SauceOnDemandSessionIdProvider;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
 
/** 
 * Demonstrates how to write a JUnit test that runs tests against Sauce Labs using multiple browsers in parallel.
 * <p/>
 * The test also includes the {@link SauceOnDemandTestWatcher} which will invoke the Sauce REST API to mark
 * the test as passed or failed.
 *
 * @author Neil Manvar
 */
@RunWith(ConcurrentParameterized.class)
public class SampleSauceTest implements SauceOnDemandSessionIdProvider {

    /**
     * Constructs a {@link SauceOnDemandAuthentication} instance using the supplied user name/access key.  To use the authentication
     * supplied by environment variables or from an external file, use the no-arg {@link SauceOnDemandAuthentication} constructor.
     */
    public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication("kristianmeiersl", "69c9ea29-59c8-4b3a-9909-18b1b05343f6");

    /**
     * JUnit Rule which will mark the Sauce Job as passed/failed when the test succeeds or fails.
     */
    @Rule
    public SauceOnDemandTestWatcher resultReportingTestWatcher = new SauceOnDemandTestWatcher(this, authentication);

    /**
     * Represents the browser to be used as part of the test run.
     */
    private String browser;
    /**
     * Represents the operating system to be used as part of the test run.
     */
    private String os;
    /**
     * Represents the version of the browser to be used as part of the test run.
     */
    private String version;
    /**
     * Represents the deviceName of mobile device
     */
    private String deviceName;
    /**
     * Represents the device-orientation of mobile device
     */

    private String name;
    /**
     * Instance variable which contains the Sauce Job Id.
     */
    private String sessionId;

    /**
     * The {@link WebDriver} instance which is used to perform browser interactions with.
     */
    private WebDriver driver;

    /**
     * Constructs a new instance of the test.  The constructor requires three string parameters, which represent the operating
     * system, version and browser to be used when launching a Sauce VM.  The order of the parameters should be the same
     * as that of the elements within the {@link #browsersStrings()} method.
     * @param os
     * @param version
     * @param browser
     * @param deviceName
     * @param deviceOrientation
     */

    public SampleSauceTest(String os, String version, String browser, String name) {
        super();
        this.os = os;
        this.version = version;
        this.browser = browser;
        this.deviceName = deviceName;
        this.name = name;
    }

    /**
     * @return a LinkedList containing String arrays representing the browser combinations the test should be run against. The values
     * in the String array are used as part of the invocation of the test constructor
     */
    @ConcurrentParameterized.Parameters
    public static LinkedList browsersStrings() {
        LinkedList browsers = new LinkedList();
        browsers.add(new String[]{"Windows 8.1", "11", "internet explorer", "Windows 8.1 IE 11"}); 
        browsers.add(new String[]{"Windows 8.1", "38", "firefox", "Windows 8.1 Firefox 38"});
        browsers.add(new String[]{"Windows 7", "10", "internet explorer", "Windows 7 IE 10"});
        browsers.add(new String[]{"Windows 7", "9", "internet explorer", "Windows 7 IE 9"});  
        browsers.add(new String[]{"Windows XP", "36", "firefox", "Windows 8 Firefox 36"});      
        browsers.add(new String[]{"OSX 10.8", "6", "safari", "Mac 10.8 Safari 6"});
        browsers.add(new String[]{"OSX 10.10", "38", "firefox", "Mac 10.10 Firefox 38"});
        browsers.add(new String[]{"Linux", "4.4", "Android", "Android Emulator 4.4"}); 
        browsers.add(new String[]{"OSX 10.10", "8.2", "iPhone", "iPhone Emulator 8.2"});
        browsers.add(new String[]{"Windows 8.1", "11", "internet explorer", "Windows 8.1 IE 11"});   
        browsers.add(new String[]{"Windows 8", "36", "firefox", "Windows 8 Firefox 36"});
 
        return browsers;
    }

    /** 
     * Constructs a new {@link RemoteWebDriver} instance which is configured to use the capabilities defined by the {@link #browser},
     * {@link #version} and {@link #os} instance variables, and which is configured to run against ondemand.saucelabs.com, using
     * the username and access key populated by the {@link #authentication} instance.
     *
     * @throws Exception if an error occurs during the creation of the {@link RemoteWebDriver} instance.
     */
    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        if (browser != null) capabilities.setCapability(CapabilityType.BROWSER_NAME, browser);
        if (version != null) capabilities.setCapability(CapabilityType.VERSION, version);
        if (deviceName != null) capabilities.setCapability("deviceName", deviceName);

        capabilities.setCapability(CapabilityType.PLATFORM, os);
        capabilities.setCapability("name", name);

        this.driver = new RemoteWebDriver(
                new URL("http://" + authentication.getUsername() + ":" + authentication.getAccessKey() +
                        "@ondemand.saucelabs.com:80/wd/hub"),
                capabilities);
        this.sessionId = (((RemoteWebDriver) driver).getSessionId()).toString();
    } 
 
    /**
     * Runs a simple test verifying the title of the americanexpress.com home page.
     * @throws Exception
     */
    
    @Test
    public void verifyTitleTest() throws Exception {
        driver.get("http://www.myfitnesspal.com/");
        assertEquals("Free Calorie Counter, Diet & Exercise Journal | MyFitnessPal.com", driver.getTitle());
    }

    /**
     * Go to 
     * @throws Exception
     */
    @Test
    public void loginTest() throws Exception {
        driver.get("http://www.myfitnesspal.com/");
        WebDriverWait wait = new WebDriverWait(driver, 5); // wait for a maximum of 5 seconds
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("facebook-jssdk")));
        driver.findElement(By.linkText("Log In")).click();
        Thread.sleep(5000);
        driver.findElement(By.name("username")).sendKeys("nocturnegroup");
        driver.findElement(By.name("password")).sendKeys("whatwhat");
        driver.findElement(By.cssSelector("input[type='submit']")).click();
        Thread.sleep(5000);
     
     
    } 

    /**
     * Closes the {@link WebDriver} session.
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

    /**
     *
     * @return the value of the Sauce Job id.
     */
    @Override
    public String getSessionId() {
        return sessionId;
    }
}