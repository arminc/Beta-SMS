package nl.coralic.beta.sms.utils.objects;

import java.util.HashMap;

import android.content.SharedPreferences;

public class BetamaxArguments
{
    private String provider;
    private String username;
    private String password;
    private String from;
    private String to;
    private String message;

    //used from wizard to validate account
    public BetamaxArguments(String provider, String username, String password, String from, String to, String message)
    {
	this.provider = provider;
	this.username = username;
	this.password = password;
	this.from = from;
	this.to = to;
	this.message = message;
    }
    
    //used from junit to get balance
    public BetamaxArguments(String provider, String username, String password)
    {
	this.provider = provider;
	this.username = username;
	this.password = password;
    }
    
    //used to get balance
    public BetamaxArguments(SharedPreferences properties)
    {
	this.provider = properties.getString(Const.KEY_PROVIDER, "");
	this.username = properties.getString(Const.KEY_USERNAME, "");
	this.password = properties.getString(Const.KEY_PASSWORD, "");
    }
    
    //used to send sms
    public BetamaxArguments(SharedPreferences properties, String to, String message)
    {
	this.provider = properties.getString(Const.KEY_PROVIDER, "");
	this.username = properties.getString(Const.KEY_USERNAME, "");
	this.password = properties.getString(Const.KEY_PASSWORD, "");
	this.from = properties.getString(Const.KEY_FROM, "");
	this.to = to;
	this.message = message;
    }

    public String getProvider()
    {
	return provider;
    }

    public String getUsername()
    {
	return username;
    }

    public String getPassword()
    {
	return password;
    }

    public String getFrom()
    {
	return from;
    }

    public String getTo()
    {
	return to;
    }

    public String getMessage()
    {
	return message;
    }
    
    public HashMap<String,String> getSmsPostArguments()
    {
	HashMap<String,String> arguments = new HashMap<String, String>();
	arguments.put(Const.KEY_USERNAME, username);
	arguments.put(Const.KEY_PASSWORD, password);
	arguments.put(Const.KEY_TO, to.replace("+", "00"));
	arguments.put(Const.KEY_FROM, from.replace("+", "00"));
	arguments.put(Const.KEY_TEXT, message);
	return arguments;
    }
    
    public HashMap<String,String> getBalancePostArguments()
    {
	HashMap<String,String> arguments = new HashMap<String, String>();
	arguments.put(Const.KEY_USERNAME, username);
	arguments.put(Const.KEY_PASSWORD, password);
	return arguments;
    }
    
    public String getBalanceUrl()
    {
	return "https://www."+provider+"/myaccount/getbalance.php";
    }
    
    public String getSendSmsUrl()
    {
	return "https://www."+provider+"/myaccount/sendsms.php";
    }
}
