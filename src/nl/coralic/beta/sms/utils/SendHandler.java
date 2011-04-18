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
/**
 * 
 */
package nl.coralic.beta.sms.utils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import nl.coralic.beta.sms.utils.http.HttpHandler;
import nl.coralic.beta.sms.utils.http.RequestMethod;

/**
 * @author "Armin Čoralić"
 */
public class SendHandler
{

	private String password = "";
	private String username = "";
	private String from = "";
	private String to = "";
	private ArrayList<String> text;
	private String url = "www.webcalldirect.com";

	public SendHandler()
	{

	}

	/**
	 * 
	 */
	public SendHandler(String password, String username, String from, String to, String text, String url)
	{
		this.password = password;
		this.username = username;
		this.from = from.replace("+", "00");
		this.to = to.replace("+", "00");;
		this.text = Utils.splitSmsTextTo160Chars(text);
		if (url != null && !url.equalsIgnoreCase(""))
		{
			this.url = url;
		}

	}

	public Response send(Context context)
	{
		Response response = null;
		if (text.size() > 1)
		{
			Iterator<String> i = text.iterator();
			while (i.hasNext())
			{
				HttpHandler http = new HttpHandler("https://" + url + "/myaccount/sendsms.php", context);
				http.AddParam("username", username);
				http.AddParam("password", password);
				http.AddParam("to", to);
				http.AddParam("text", i.next());
				http.AddParam("from", from);
				http.Execute(RequestMethod.GET);
				response = new Response(http, context); 
				//if there is an error don't send others
				if(!response.isSucceful())
				{
					return response;
				}
			}
			return response;
		}
		else
		{
			HttpHandler http = new HttpHandler("https://" + url + "/myaccount/sendsms.php", context);
			http.AddParam("username", username);
			http.AddParam("password", password);
			http.AddParam("to", to);
			http.AddParam("text", text.get(0));
			http.AddParam("from", from);
			http.Execute(RequestMethod.GET);
			return new Response(http, context);
		}
	}
}
