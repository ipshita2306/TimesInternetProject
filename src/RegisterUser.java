import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RegisterUser {

	public static WebDriver driver;
	
	@BeforeMethod
	public void setup() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-notifications");
		System.setProperty("webdriver.chrome.driver", "testdata/chromedriver.exe");
		driver= new ChromeDriver(options);
		driver.manage().window().maximize();
		driver.navigate().to("https://www.timesjobs.com/");
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(5));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	}

	@DataProvider(name="getData")
	public String[][] excelData() throws IOException{
		String path=".\\testdata\\TestData.xlsx";
		XLUtility xlutil=new XLUtility(path);
		xlutil.getRowCount("TestData");
		
		int totalrows=xlutil.getRowCount("TestData");
		int totalcolmns=xlutil.getCellCount("TestData",1);
		
		String data[][]=new String[totalrows][totalcolmns];
		
		//fetching data from second row and first column
		for(int i=1;i<=totalrows;i++){
			for(int j=0;j<totalcolmns;j++) {
				data[i-1][j]=xlutil.getCellData("TestData",i,j);
			}
		}
		return data;
	}
	
	
	@Test(dataProvider="getData")
	public void registerUser(String email, String password, String mobile, String years, String months, String name) throws InterruptedException, AWTException {
		//click on register button
		driver.findElement(By.xpath("(//a[contains(text(),'Register')])[2]")).click();
		Thread.sleep(2000);
		
		//enter email
		driver.findElement(By.xpath("//input[@id='emailAdd']")).sendKeys(email);
		driver.findElement(By.xpath("//input[@id='emailAdd']")).sendKeys(Keys.ARROW_DOWN);
		driver.findElement(By.xpath("//input[@id='emailAdd']")).sendKeys(Keys.ENTER);
		Thread.sleep(2000);
		
		//check if email is already registered
		Boolean isPresent = false;
		try {
		isPresent = driver.findElements(By.xpath("//a[@href='https://www.timesjobs.com/candidate/login.html']")).size()>0;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		if(isPresent) {
				System.out.println("Email already registered, testing next email");
		}
		 else{
			 //enter password and confirm password
			 driver.findElement(By.xpath("//input[@id='passwordField']")).sendKeys(password);
			 driver.findElement(By.xpath("//input[@id='retypePassword']")).sendKeys(password);
			 Thread.sleep(2000);
			 
			 //enter mobile number
			 driver.findElement(By.xpath("//input[@id='mobNumber']")).sendKeys(mobile);
			 Thread.sleep(2000);
		
			 //select job function
			 driver.findElement(By.xpath("//b[@id='defLabelSpnId']")).click();
			 WebElement ul = driver.findElement(By.xpath("//ul[@class='ui-multiselect-checkboxes ui-helper-reset']"));
			 String valueToSelect= "IT Software - Security/ Operating Systems";
			 List<WebElement> jobList=ul.findElements(By.tagName("li"));
			 for (WebElement li: jobList) {
				 if(li.getText().equals(valueToSelect)) {
					 li.click();
				 }
			 }
			 Thread.sleep(2000);
			 
			 //enter years
			 driver.findElement(By.xpath("//input[@id='cboWorkExpYear']")).sendKeys(years);
			 Thread.sleep(2000);
		
			 //enter months
			 driver.findElement(By.xpath("//input[@id='cboWorkExpMonth']")).sendKeys(months);
			 Thread.sleep(2000);
			 
			 //select current work location
			 String location="Gurgaon";
			 Select currlocation= new Select(driver.findElement(By.xpath("//select[@id='curLocation']")));
			 currlocation.selectByVisibleText(location);
			 Thread.sleep(2000);
			 
			 //enter key skills
			 driver.findElement(By.xpath("//input[@id='token-input-keySkills']")).sendKeys("Selenium");
			 driver.findElement(By.xpath("//li[@class='token-input-dropdown-item-facebook']")).click();
			 Robot r = new Robot();
			 Thread.sleep(3000);
			 r.keyPress(KeyEvent.VK_TAB);
			 r.keyRelease(KeyEvent.VK_TAB);
			 Thread.sleep(3000);
			 
			 //i dont have a resume option
			 driver.findElement(By.xpath("//input[@id='dontHaveResume']")).click();
			 String degreeselect="Bachelor of Fashion Technology";
			 Select degree= new Select(driver.findElement(By.xpath("//select[@id='categorisedDegree']")));
			 degree.selectByVisibleText(degreeselect);
			 Thread.sleep(2000);
			 
			 driver.findElement(By.xpath("//input[@id='curEmpDesignation']")).sendKeys("Employee1");
			 driver.findElement(By.xpath("//input[@id='curEmpName']")).sendKeys("Airtel");
			 
			 r.keyPress(KeyEvent.VK_TAB);
			 r.keyRelease(KeyEvent.VK_TAB);
			 Thread.sleep(2000);
			 
			 driver.findElement(By.xpath("//input[@id='lacSalary']")).sendKeys("6");
			 driver.findElement(By.xpath("//input[@id='thousandSalary']")).sendKeys("25");
			 Thread.sleep(2000);
			 
			 //click continue
			 driver.findElement(By.xpath("//button[@id='basicSubmit']")).click();
			 Thread.sleep(4000);
			 
			 //close notification popup
			 Boolean ispopup= false;
			try {
				ispopup = driver.findElements(By.xpath("//div[@id='nvpush_cross']")).size()>0;
				if(ispopup) {
				driver.findElement(By.xpath("//div[@id='nvpush_cross']")).click();
				Thread.sleep(2000);
				}
				else {
					System.out.println("No notification popup on this test");
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			} 
			finally{
			 //Enter name
			 driver.findElement(By.xpath("//input[@id='name']")).sendKeys(name);
			 Thread.sleep(2000);
			 
			 //select specialization
			 String specselect="Systems Programming";
			 Select spec= new Select(driver.findElement(By.xpath("//select[@id='areaOfSpec']")));
			 spec.selectByVisibleText(specselect);
			 Thread.sleep(2000);
			 
			 //resume title
			 driver.findElement(By.xpath("//input[@id='resumeTitle']")).sendKeys("Automation for 3 years");
			 Thread.sleep(2000);
			 
			//upload resume
			 String filepath=System.getProperty("user.dir")+"\\testdata\\TestResume.pdf";
			 WebElement uploadbutton=driver.findElement(By.xpath("//input[@id='resumeFile']"));
			 uploadbutton.sendKeys(filepath);
			 Thread.sleep(2000);
			 r.keyPress(KeyEvent.VK_TAB);
			 r.keyRelease(KeyEvent.VK_TAB);
			 Thread.sleep(2000);
			 
			 //click submit button- skipping the captcha option
			 driver.findElement(By.xpath("//input[@id='submit_button']")).click();
			 
			 System.out.println("Test passed for:"+ email);
			 }
		}
	}
	
	
	@AfterMethod
	public void tearDown() {
		if (driver!=null) {
			driver.quit();
		}
	}
}
