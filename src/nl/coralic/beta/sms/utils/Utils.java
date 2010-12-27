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

import nl.coralic.beta.sms.R;
import nl.coralic.beta.sms.log.Log;
import nl.coralic.beta.sms.utils.constants.Const;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * @author "Armin Čoralić"
 */
public class Utils
{
	/**
	 * Strips all the unnecessary things from the string containing a phone number
	 * 
	 * @param data
	 * @return striped phone number
	 */
	public static String stripString(String data)
	{
		return data.replace("sms:", "").replace(" ", "").replace("(0)", "").replace("-", "").replace(".", "").replace("(", "").replace(")", "");
	}

	/**
	 * Checks if all properties are filled if not it shows an message
	 * 
	 * @param context
	 * @param properties
	 * @return true or false if the properties are filled or not
	 */
	public static boolean checkForProperties(Context context, SharedPreferences properties)
	{

		boolean isEveryPropertieFilled = true;
		StringBuffer sb = new StringBuffer();
		if (!properties.contains("UsernameKey") || properties.getString("UsernameKey", "").equalsIgnoreCase(""))
		{
			sb.append(context.getString(R.string.NO_USER));
			Log.logit(Const.TAG_MAIN, "No username");
			isEveryPropertieFilled = false;
		}

		if (!properties.contains("PasswordKey") || properties.getString("PasswordKey", "").equalsIgnoreCase(""))
		{
			sb.append(context.getString(R.string.NO_PASS));
			Log.logit(Const.TAG_MAIN, "No password");
			isEveryPropertieFilled = false;
		}

		if (!properties.contains("PhoneKey") || properties.getString("PhoneKey", "").equalsIgnoreCase(""))
		{
			sb.append(context.getString(R.string.NO_NUMBER));
			Log.logit(Const.TAG_MAIN, "No phone");
			isEveryPropertieFilled = false;
		}

		if (!properties.contains("ServiceKey") || properties.getString("ServiceKey", "").equalsIgnoreCase(""))
		{
			sb.append(context.getString(R.string.NO_URL));
			Log.logit(Const.TAG_MAIN, "No service url");
			isEveryPropertieFilled = false;
		}

		if (isEveryPropertieFilled)
		{
			// everything is oke return true
			return true;
		}
		else
		{
			sb.append(context.getString(R.string.NO_INFO));
			Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();
			return false;
		}
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

	public static String getBalance(String data)
	{
		int firstposition = data.indexOf("balanceid");
		if (firstposition != -1)
		{
			Log.logit(Const.TAG_UTILS, "Balance is here!");
			String subString = data.substring(firstposition, firstposition + 40);
			System.out.println(subString);
			int one = subString.lastIndexOf(";");
			int two = subString.indexOf("<");
			return subString.substring(one + 1, two);
		}
		else
		{
			Log.logit(Const.TAG_UTILS, "Balance is not here on the page!");
			return "?";
		}
	}

}