package nl.coralic.beta.sms.betamax;

import java.util.HashMap;

import nl.coralic.beta.sms.utils.http.HttpHandler;
import nl.coralic.beta.sms.utils.objects.BetamaxResponse;
import nl.coralic.beta.sms.utils.objects.Response;

public class BetamaxHandler
{
    private static String USERNAME = "username";
    private static String PASSWORD = "password";
    private static String TO = "to";
    private static String TEXT = "text";
    private static String FROM = "from";
    
    public static String getBalance(String tmpUrl, String username, String password)
    {
	String url = getBalanceUrl(tmpUrl);
	HashMap<String,String> arguments = new HashMap<String, String>();
	arguments.put(USERNAME, username);
	arguments.put(PASSWORD, password);
	Response response = HttpHandler.execute(url, arguments);
	if(response.isResponseOke())
	{
	    //Betamax only returns balance here so we can pass it directly
	    return response.getResponse();
	}
	return "*";
    }
    
    public static Response sendSMS(String tmpUrl, String username, String password, String from, String to, String text)
    {
	String url = getSendSmsUrl(tmpUrl);
	HashMap<String,String> arguments = new HashMap<String, String>(); 
	arguments.put(USERNAME, username);
	arguments.put(PASSWORD, password);
	//TODO: IMPORTENT replacements done on the TO en FROM, do we need to do them here or else?
	arguments.put(TO, to);
	arguments.put(TEXT, text);
	arguments.put(FROM, from);
	Response response = HttpHandler.execute(url, arguments);
	if(response.isResponseOke())
	{
	    BetamaxResponse betamaxResponse = new BetamaxResponse(response.isResponseOke(), response.getResponse());
	    betamaxResponse.validateBetamaxResponse();
	    return betamaxResponse;
	}
	return response;
    }
    
    private static String getBalanceUrl(String tmpUrl)
    {
	return "https://www."+tmpUrl+"/myaccount/getbalance.php";
    }
    
    private static String getSendSmsUrl(String tmpUrl)
    {
	return "https://www."+tmpUrl+"/myaccount/sendsms.php";
    }
}
