package nl.coralic.beta.sms.utils;

import static org.junit.Assert.*;
import nl.coralic.beta.sms.betamax.BetamaxHandler;

import org.junit.Test;


public class BetamaxHandlerTest
{
   
    @Test
    public void getBalance()
    {
	String balance = BetamaxHandler.getBalance("www.webcalldirect.com", "testingwbc", "wbcwbc123");
	System.out.println(balance);
	assertEquals("0", balance);
    }
    
    @Test
    public void getBalanceWrongPassword()
    {
	String balance = BetamaxHandler.getBalance("www.webcalldirect.com", "WRONG", "WRONG");
	System.out.println(balance);
    }
    
    @Test
    public void sendSmsLowWrongUsername()
    {
	
    }
    
    @Test
    public void sendSmsLowWrongPassword()
    {
	
    }
    
    @Test
    public void sendSmsLowBalance()
    {
	
    }
    
    //TODO: I need more tests but those require a account with money
}
