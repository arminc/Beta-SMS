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
import nl.coralic.beta.sms.utils.objects.BetamaxArguments;
import nl.coralic.beta.sms.utils.objects.Const;
import nl.coralic.beta.sms.utils.objects.Response;

public class BetamaxHandler
{
    public static String getBalance(BetamaxArguments arguments)
    {
	HashMap<String,String> postArguments = arguments.getBalancePostArguments();
	Response response = HttpHandler.execute(arguments.getBalanceUrl(), postArguments);
	if(response.isResponseOke())
	{
	    //Betamax only returns the amount nothing else. Remove the unwanted white spaces from the response
	    return response.getResponse().trim();
	}
	return Const.BALANCE_UNKNOWN;
    }
    
    public static Response sendSMS(BetamaxArguments arguments)
    {
	HashMap<String,String> postArguments = arguments.getSmsPostArguments();	
	Response response = HttpHandler.execute(arguments.getSendSmsUrl(), postArguments);
	//Betamax returns an xml here so we need to validate it
	response.validateBetamaxResponse();
	return response;
    }
}
