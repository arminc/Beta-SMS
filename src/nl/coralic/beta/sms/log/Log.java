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
package nl.coralic.beta.sms.log;
/**
 * @author "Armin Čoralić"
 */
public class Log
{
	private static boolean logAllowed = true;
	/**
	 * With this class it's possible to turn and turn off logging
	 * @param type is the tag so you can recognize who is logging
	 * @param data the data as string that you want to log
	 */
	public static void logit(String type, String data)
	{
		if (logAllowed)
		{
			android.util.Log.d(type, data);
		}
	}
}
