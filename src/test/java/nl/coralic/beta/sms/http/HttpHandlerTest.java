package nl.coralic.beta.sms.http;

import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import nl.coralic.beta.sms.utils.http.HttpHandler;
import nl.coralic.beta.sms.utils.objects.Response;

public class HttpHandlerTest
{
 
    private static HashMap<String, String> arguments;
    
    @BeforeClass
    public static void setUp()
    {
	arguments = new HashMap<String, String>();
	arguments.put("user", "fake");
    }
    
    @Test
    public void emptyArguments()
    {
	Response response  = HttpHandler.execute("", null);
	assertFalse(response.isResponseOke());
    }
    
    @Test
    public void visitWebsite()
    {	
	Response response  = HttpHandler.execute("https://www.webcalldirect.com/myaccount/getbalance.php", arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void wrongUrl()
    {
	Response response  = HttpHandler.execute("http://wrong", arguments);
	assertFalse(response.isResponseOke());
	System.err.println("Response error: " + response.getErrorMessage());
    }
}
