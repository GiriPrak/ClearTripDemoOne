package org.qa.tests;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.qa.util.UtilClass;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

public class TestCases extends UtilClass {

	@Test(enabled=true)
	public void FlightSearch() throws Exception
	{

		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE,dd MM, yyyy");
		Date date = new Date();
		String FromDate= dateFormat.format(date);
		Calendar c = Calendar.getInstance();
		try{
			c.setTime(dateFormat.parse(FromDate));
		}catch(ParseException e){
			e.printStackTrace();
		}
		//Incrementing the date by 10 days
		c.add(Calendar.DAY_OF_MONTH, 10);  
		String ReturnDate = dateFormat.format(c.getTime());  

		driver.get("https://www.cleartrip.com/");
		String CurrTest = new Throwable() .getStackTrace()[0]  .getMethodName(); 
		testReport = extent.createTest("verify TC "+CurrTest);
		driver.manage().window().maximize();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("RoundTrip"))).click();
		testReport.log(Status.PASS,"Round trip Selected ");
		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("FromTag"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("FromTag"))).sendKeys("New Delhi");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ui-id-1"))).click();
		testReport.log(Status.PASS,"From city selected");
		//wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ToTag"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ToTag"))).sendKeys("Mumbai");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ui-id-2"))).click();
		testReport.log(Status.PASS,"To city selected");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("DepartDate"))).sendKeys(FromDate);
		testReport.log(Status.PASS,"Departure date given");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ReturnDate"))).sendKeys(ReturnDate);
		testReport.log(Status.PASS,"Return travel date given");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("SearchBtn"))).click();
		testReport.log(Status.PASS,"Searched for the Flights");
		testReport.log(Status.PASS,"verify TC "+CurrTest);
	}

	@Test(enabled=true, dependsOnMethods={"FlightSearch"})
	public void OnwardFlightsCount() 
	{
		String CurrTest = new Throwable() .getStackTrace()[0]  .getMethodName(); 
		testReport = extent.createTest("verify TC "+CurrTest);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ta-center"))).getText().contentEquals("Almost done...");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".ta-center"))).getText().contentEquals("Almost done...");
		//List<WebElement> upward = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@data-test-attrib='onward-view']/div/div")));
		List<WebElement> upwardallflights = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@data-test-attrib='onward-view']/div/div/div/div/div[3]/div[2]/p")));

		int under5k=0;
		for(int i=0;i<upwardallflights.size();i++)
		{
			String Onwardprice=upwardallflights.get(i).getText().replace("₹", "").replace(",", "");
			int OnwardRupee= Integer.parseInt(Onwardprice); 
			if(OnwardRupee <= 5000)
			{
				under5k++;
			}
		}
		testReport.log(Status.PASS,"Number of flights found under 5k :: "+under5k);
		testReport.log(Status.PASS,"verify TC "+CurrTest);
	}

	@Test(enabled=true, dependsOnMethods={"OnwardFlightsCount"})
	public void ReturnFlightsCount() 
	{
		String CurrTest = new Throwable() .getStackTrace()[0]  .getMethodName(); 
		testReport = extent.createTest("verify TC "+CurrTest);
//		List<WebElement> returnFligts = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@data-test-attrib='return-view']/div/div")));
		List<WebElement> returnallflights = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@data-test-attrib='return-view']/div/div/div/div/div[3]/div[2]/p")));
		int under7k=0;
		for(int i=0;i<returnallflights.size();i++)
		{
			String Returnprice=returnallflights.get(i).getText().replace("₹", "").replace(",", "");
			int ReturnRupee= Integer.parseInt(Returnprice); 
			if(ReturnRupee <= 7000)
			{
				under7k++;
			}
		}
		testReport.log(Status.PASS,"Number of flights found under 7k :: "+under7k);
		testReport.log(Status.PASS,"verify TC "+CurrTest);
	}
	@Test(enabled=true, dependsOnMethods={"ReturnFlightsCount"})
	public void UptoOneStop() 
	{
		String CurrTest = new Throwable() .getStackTrace()[0]  .getMethodName(); 
		testReport = extent.createTest("verify TC "+CurrTest);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".checkbox:nth-child(1) .checkbox__mark"))).click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".checkbox:nth-child(2) .checkbox__mark"))).click();
		testReport.log(Status.PASS,"Non Stop and one stop Selected ");
		List<WebElement> allflights = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".m-0.fw-600.c-neutral-900")));
		int under7k=0;
		for(int i=0;i<allflights.size();i++)
		{

			String price=allflights.get(i).getText().replace("₹", "").replace(",", "");
			int Rupees= Integer.parseInt(price); 
			if(Rupees <= 7000)
			{
				under7k++;
			}
		}

		testReport.log(Status.PASS,"Number of flights found under 7k ::  "+under7k);

		testReport.log(Status.PASS,"verify TC "+CurrTest);
	}
	@Test(enabled=true, dependsOnMethods={"UptoOneStop"})
	public void FlightSelection() throws Exception 
	{
		String CurrTest = new Throwable() .getStackTrace()[0]  .getMethodName(); 
		testReport = extent.createTest("verify TC "+CurrTest);
		String OnwardPrice= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-test-attrib='onward-view']/div/div[2]/div/div/div[3]/div[2]/p"))).getText();
		String ReturnPrice= wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-test-attrib='return-view']/div/div[5]/div/div/div[3]/div[2]/p"))).getText();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-test-attrib='onward-view']/div/div[2]/div/div/div[3]/div[2]/p"))).click();																		  
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-test-attrib='return-view']/div/div[5]/div/div/div[3]/div[2]/p"))).click();																		  
		Thread.sleep(2000);
		OnwardPrice=OnwardPrice.replace("₹", "").replace(",", "");
		int OnwardCharges= Integer.parseInt(OnwardPrice); 
		ReturnPrice=ReturnPrice.replace("₹", "").replace(",", "");
		int ReturnCharges= Integer.parseInt(ReturnPrice); 
		int ChargesOne=OnwardCharges+ReturnCharges;
		Assert.assertEquals(ChargesOne, Integer.parseInt(wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".flex .flex-bottom .flex-right"))).getText().replace("₹", "").replace(",", "")));
		testReport.log(Status.PASS,"Sum of onward and return charges are "+ ChargesOne);
		testReport.log(Status.PASS,"Assertion  on sum of two flights charges vs system generated charges passed");
		

		testReport.log(Status.PASS,"verify TC "+CurrTest);
	}
	

}  

