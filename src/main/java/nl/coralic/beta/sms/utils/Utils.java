/*
 * Copyright 2010 Armin Čoralić
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
package nl.coralic.beta.sms.utils;

import java.util.ArrayList;

import nl.coralic.beta.sms.Beta_SMS;
import nl.coralic.beta.sms.utils.objects.Key;

import android.content.SharedPreferences;

/**
 * @author "Armin Čoralić"
 */
public class Utils
{
    /**
     * Checks if the account is valid, by checking for the verified property
     * 
     * @param properties
     * @return true if the account is verified
     */
    public static boolean checkForValidAccount(SharedPreferences properties)
    {
	if (properties.contains(Key.VERIFIED.toString()) && properties.getBoolean(Key.VERIFIED.toString(), false) == true)
	{
	    return true;
	}
	return false;
    }
    
    /**
     * Strips all the unnecessary things from the string containing a phone number
     * 
     * @param data
     * @return striped phone number
     */
    public static String stripString(String data)
    {
	return data.replace("sms:", "").replace("smstop:", "").replace(" ", "").replace("(0)", "").replace("-", "").replace(".", "").replace("(", "").replace(")", "");
    }

    public static ArrayList<String> splitSmsTextTo160Chars(String sms)
    {
	ArrayList<String> smsSplit = new ArrayList<String>();
	if (sms.length() <= 160)
	{
	    smsSplit.add(sms);
	}
	else
	{
	    while (true)
	    {
		if (sms.length() > 160)
		{
		    smsSplit.add(sms.substring(0, 160));
		    sms = sms.substring(160);
		}
		else
		{
		    smsSplit.add(sms);
		    break;
		}
	    }
	}
	return smsSplit;
    }    
}