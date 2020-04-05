package services.empirix.tests;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class MultiplePlatformTestCases {
	
	static WebDriver driver = null;
	static WebDriverWait wait = null;
	static String naviUrl= "https://services.empirix.com/";
	static String userName = "QA_traininguser13";
	static String passKey = "Empirix!";
	String rPath = System.getProperty("user.dir")+"/src/test/resources/";
	String cPath = System.getProperty("user.dir")+"/src/test/java/";
	Properties prop = new Properties();
	FileInputStream fis;
	
	@BeforeSuite
	public void setup() throws IOException {
		fis = new FileInputStream("./config.properties");
		prop.load(fis);
	}
	
	@AfterSuite
	public void tearoff() throws FileNotFoundException {
		driver.quit();
	}
	
	@Test
	public void RunChrome() throws InterruptedException {
		
		System.out.println("Configure driver..");
		System.setProperty("webdriver.chrome.driver", rPath+"driver/"+prop.getProperty("chromepath"));
		ChromeOptions options = new ChromeOptions();
		options.setPageLoadStrategy(PageLoadStrategy.NONE);
		System.out.println("initialization of chrome driver");
		driver = new ChromeDriver(options);
		ExecuteTest();
	}
	
	
	@Test
	public void RunFirefox() throws InterruptedException {
		
		System.out.println("Configure driver..");
		System.setProperty("webdriver.gecko.driver", rPath+"driver/"+prop.getProperty("firefoxpath"));
		
		System.out.println("initialization of chrome driver");
		driver = new FirefoxDriver();
		ExecuteTest();
		
	}
	
	
	
	@Test
	public void RunEdge() throws InterruptedException {
		
		System.out.println("Configure driver..");
		System.setProperty("webdriver.edge.driver",rPath+"driver/"+prop.getProperty("edgepath"));
		
		System.out.println("initialization of chrome driver");
		driver = new EdgeDriver();
		ExecuteTest();
	}
	
	
	
	public void ExecuteTest() throws InterruptedException {
		
		driver.manage().deleteAllCookies();
	
		System.out.println("open browser");
		Tests.OpenBrowser();
		System.out.println("Enter user and pass and login");
		Tests.doLogin();
		Thread.sleep(5000);
		System.out.println("verification tab in english language");
		Tests.verifyTabs();
		Thread.sleep(3000);
		System.out.println("verification of client data");
		Tests.verifyClientData();
		Thread.sleep(3000);
		System.out.println("change language to japanese");
		Tests.changeLanguage();
		Thread.sleep(3000);
		System.out.println("verification tab in japanase language");
		Tests.verifyTabs();
		
		
	}
	
	
	public static class Tests {
		
		public static void OpenBrowser() {
			
			driver.get(naviUrl);
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			wait = new WebDriverWait(driver,200);
			
			
		}
		
		public static void doLogin() throws InterruptedException {
			
			By user = By.xpath("//input[@type='text']");
			By pass = By.xpath("//input[@type='password']");
			By login = By.xpath("//input[@type='submit']");
			By variables = By.xpath("//*[@class='nav navbar-nav']/child::li/following::li/child::a[@href='/variables']");
			
			
						
			wait.until(ExpectedConditions.visibilityOfElementLocated(user));
			
			driver.findElement(user).sendKeys(userName);
			driver.findElement(pass).sendKeys(passKey);
			driver.findElement(login).click();
			
			Thread.sleep(3000);
			String var = driver.findElement(variables).getText();
			if(!var.contains("Variables")) {
				
				driver.findElement(By.xpath("//a[@href='#']")).click();
			}else {
				System.out.println("English language appears");
			}
			  
			  
			/*
			 * String stlang = "???????";
			 * wait.until(ExpectedConditions.visibilityOfElementLocated(contact));
			 * if(driver.findElement(contact).getText().contains(stlang)) {
			 * changeLanguage();
			 * driver.manage().timeouts().pageLoadTimeout(10,TimeUnit.SECONDS); }
			 */
			 
		}
		
		public static void verifyTabs() {
			
			By dashboard = By.xpath("//*[@class='nav navbar-nav']/child::li/child::a[@href='/dashboard']");
			By alerts = By.xpath("//*[@class='nav navbar-nav']/child::li/following::li/child::a[@href='/alerts']");
			By tests = By.xpath("//*[@class='nav navbar-nav']/child::li/following::li/child::a[@href='/tests']");
			By variables = By.xpath("//*[@class='nav navbar-nav']/child::li/following::li/child::a[@href='/variables']");
			By notifications = By.xpath("//*[@class='nav navbar-nav']/child::li/following::li/child::a[@href='/notifi']");
			
			wait.until(ExpectedConditions.visibilityOfElementLocated(notifications));
			Assert.assertTrue(driver.findElement(dashboard).isDisplayed());
			Assert.assertTrue(driver.findElement(alerts).isDisplayed());
			Assert.assertTrue(driver.findElement(tests).isDisplayed());
			Assert.assertTrue(driver.findElement(variables).isDisplayed());
			Assert.assertTrue(driver.findElement(notifications).isDisplayed());
		}
		
		public static void changeLanguage() {
			
			By qatraining = By.xpath("//*[@class='dropdown-toggle ng-binding']/self::*");
			
			By changeLang = By.xpath("//*[@class='ng-scope']/child::a");
			
			driver.findElement(qatraining).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(changeLang));
			driver.findElement(changeLang).click();
			wait.until(ExpectedConditions.alertIsPresent());
			driver.switchTo().alert().accept();
			
		}
		
		
		public static void verifyClientData() throws InterruptedException {
			
			By qatraining = By.xpath("//*[@class='dropdown-toggle ng-binding']/self::*");
			By client = By.xpath("//*[@class='dropdown-toggle ng-binding']/following::li[3]/child::a");
			
			String tdata = "//*[@class='form-horizontal ng-pristine ng-valid']/child::div[";
			String tcol1 = "]/label[1]";
			String tcol2 = "]/label[2]";
									
			LinkedList<String>key = new LinkedList<String>();
			key.add("Client Name");
			key.add("Description");
			key.add("Subscription Start Date");
			key.add("Subscription End Date");
			key.add("Maximum Load Tests");
			key.add("Maximum VoiceWatch Tests");
			key.add("Maximum Scenarios");
			key.add("Minimum Test Schedule Period (mins)");
			
			LinkedList<String>value = new LinkedList<String>();
			value.add("Empirix_QA_Training");
			value.add("This client is for QA Test purposes");
			value.add("Jan 14, 2020");
			value.add("Aug 11, 2022");
			value.add("0");
			value.add("0");
			value.add("0");
			value.add("15");
			
					
			driver.findElement(qatraining).click();
			Thread.sleep(2000);
			((JavascriptExecutor)driver).executeScript("arguments[0].click();", driver.findElement(client));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(tdata+1+tcol1)));
			
			for(int col1=0;col1<key.size();col1++) {
				
				String actual = driver.findElement(By.xpath(tdata+(col1+1)+tcol1)).getText();
				System.out.println("Actual key data is : "+actual);
				Assert.assertEquals(actual, key.get(col1));

			}
			
			for(int col2=0;col2<value.size();col2++) {
							
							String actual = driver.findElement(By.xpath(tdata+(col2+1)+tcol2)).getText();
							System.out.println("Actual value data is : "+actual);
							Assert.assertEquals(actual, value.get(col2));
			
			}
			
		}
						
	}
		
	}


