package nl.coralic.beta.sms.betamax;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.util.Properties;

import nl.coralic.beta.sms.betamax.BetamaxHandler;
import nl.coralic.beta.sms.utils.objects.Response;

import org.junit.BeforeClass;
import org.junit.Test;


public class BetamaxHandlerTest
{
    private static String username;
    private static String password;
    private static String provider;
    
    
    @BeforeClass
    public static void setUp()
    {
	Properties prop = new Properties();
	try
	{
	    prop.load(new FileInputStream("testprovider.properties"));
	}
	catch (Exception e)
	{
	   fail("Please provide a file testprovider.properties in the root of the Beta-SMS project. The file should contain an Betamax provider test account: \n" +
	   		"username=test \n" +
	   		"password=test \n" +
	   		"provider=test.com");
	}
	username = prop.getProperty("username");
	password = prop.getProperty("password");
	provider = prop.getProperty("provider");
    }
    
    @Test
    public void sendSmsWrongUsernamePassword()
    {
	Response response = BetamaxHandler.sendSMS(provider, "WRONG", "WRONG", "00", "00", "fake");
	assertFalse(response.isResponseOke());
	assertEquals("error", response.getErrorMessage());
    }   
    
    @Test
    public void sendSmsWrongToNumber()
    {
	Response response = BetamaxHandler.sendSMS(provider, username, password, "00", "00", "fake");
	assertFalse(response.isResponseOke());
	assertEquals("Invalid Number", response.getErrorMessage());
    }
    
    @Test
    public void sendSmsLowBalance()
    {
	Response response = BetamaxHandler.sendSMS(provider, username, password, "00", "0031612345678", "fake");
	assertFalse(response.isResponseOke());
	assertEquals("Sorry, you do not have enough credit to send this sms. Go to your accountpage to buy credit!", response.getErrorMessage());
    }
}
