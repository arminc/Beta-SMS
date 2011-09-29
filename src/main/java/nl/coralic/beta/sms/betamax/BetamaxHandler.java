/*
 * Copyright 2011 Armin Čoralić
 * 
 * 	http://blog.coralic.nl
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
	    //Remove the unwanted white spaces
	    return response.getResponse().trim();
	}
	return "*";
    }
    
    public static Response sendSMS(String tmpUrl, String username, String password, String from, String to, String text)
    {
	String url = getSendSmsUrl(tmpUrl);
	HashMap<String,String> arguments = new HashMap<String, String>(); 
	arguments.put(USERNAME, username);
	arguments.put(PASSWORD, password);
	arguments.put(TO, to.replace("+", "00"));
	arguments.put(FROM, from.replace("+", "00"));
	arguments.put(TEXT, text);
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
