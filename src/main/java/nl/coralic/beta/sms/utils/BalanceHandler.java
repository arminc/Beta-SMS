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

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import nl.coralic.beta.sms.log.Log;
import nl.coralic.beta.sms.utils.constants.Const;
import nl.coralic.beta.sms.utils.http.HttpHandler;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

public class BalanceHandler
{
	public String getBalance(String url, String user, String pass)
	{
		Log.logit(Const.TAG_BLC, "Try to get the balance.");
		return getBalanceForAll(url, user, pass);
	}
	
	private String getBalanceForAll(String url, String user, String pass)
	{
		String returnValue = "*";
		
		List<NameValuePair> postdata = new ArrayList<NameValuePair>();
		postdata.add(new BasicNameValuePair("user", user));
		postdata.add(new BasicNameValuePair("pass", pass));
		
		HttpClient httpclient = getHttpClient();
		Log.logit(Const.TAG_BLC, "First call for balance.");
		String data = doGet(httpclient, "https://"+url+"/myaccount/getbalance.php?username="+URLEncoder.encode(user)+"&password="+URLEncoder.encode(pass));
		httpclient.getConnectionManager().shutdown();
		if(data == null)
		{		
			return "*";
		}
		return data;
	}
	
	private HttpClient getHttpClient()
	{
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 28000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		int timeoutSocket = 28000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		return new DefaultHttpClient(httpParameters);
	}
	
	public String doPost(HttpClient httpclient, String uri, List<NameValuePair> postdata)
	{
		try
		{
			HttpPost httpost = new HttpPost(uri);
			httpost.setEntity(new UrlEncodedFormEntity(postdata, HTTP.UTF_8));
			HttpResponse response = httpclient.execute(httpost);
			if (response.getStatusLine().getStatusCode() == 200)
			{
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				return responseHandler.handleResponse(response);

			}
			else
			{
				return null;
			}
		}
		catch(Exception e)
		{
			Log.logit(Const.TAG_BLC, e.getMessage());
		}
		return null;
	}

	public String doGet(HttpClient httpclient, String uri)
	{

		try
		{
			HttpGet ht = new HttpGet(uri);
			HttpResponse response = httpclient.execute(ht);
			if (response.getStatusLine().getStatusCode() == 200)
			{
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				return responseHandler.handleResponse(response);
			}
			else
			{
				return null;
			}
		}
		catch(Exception e)
		{
			Log.logit(Const.TAG_BLC, e.getMessage());
		}
		return null;
	}
}
