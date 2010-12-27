/**
 * 
 */
package nl.coralic.beta.sms.utils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import nl.coralic.beta.sms.log.Log;
import nl.coralic.beta.sms.utils.constants.Const;
import nl.coralic.beta.sms.utils.http.HttpHandler;

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
		this.password = URLEncoder.encode(password);
		this.username = URLEncoder.encode(username);
		this.from = URLEncoder.encode(from);
		this.to = URLEncoder.encode(to);
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
				String uri = "https://" + url + "/myaccount/sendsms.php?username=" + username + "&password=" + password + "&to=" + to + "&text="
						+ URLEncoder.encode(i.next()) + "&from=" + from;
				HttpHandler hh = new HttpHandler();
				response = new Response(hh.send(uri, context), context); 
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
			String uri = "https://" + url + "/myaccount/sendsms.php?username=" + username + "&password=" + password + "&to=" + to + "&text="
					+ URLEncoder.encode(text.get(0)) + "&from=" + from;
			HttpHandler hh = new HttpHandler();
			return new Response(hh.send(uri, context), context);
		}
	}
	
	public String getBalance(String url, String user, String pass)
	{
		Log.logit(Const.TAG_SEND, "Try to get the balance.");
		String returnValue = "*";
		
		List<NameValuePair> postdata = new ArrayList<NameValuePair>();
		postdata.add(new BasicNameValuePair("user", user));
		postdata.add(new BasicNameValuePair("pass", pass));
		
		HttpHandler hh = new HttpHandler();
		HttpClient httpclient = hh.getHttpClient();
		Log.logit(Const.TAG_SEND, "First call for balance.");
		if(hh.doPost(httpclient,"https://"+url+"/myaccount/index.php?part=tplogin",postdata) != null)
		{
			Log.logit(Const.TAG_SEND, "Second call for balance.");
			if(hh.doGet(httpclient, "https://"+url+"/myaccount/index.php?part=menu&justloggedin=true") != null)
			{
				Log.logit(Const.TAG_SEND, "Last call for balance.");
				String data = hh.doGet(httpclient, "https://"+url+"/myaccount/contacts.php");
				if(data != null)
				{
					Log.logit(Const.TAG_SEND, "Got anwser from contacts.php.");
					returnValue = Utils.getBalance(data);
				}
			}
		}
		httpclient.getConnectionManager().shutdown();	
		return returnValue;
	}
}
