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
package nl.coralic.beta.sms.utils.http;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.List;

import nl.coralic.beta.sms.R;
import nl.coralic.beta.sms.utils.constants.Const;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.util.Log;

/**
 * @author "Armin Čoralić"
 */
public class HttpHandler
{
	public String send(String URL, Context context)
	{
		HttpClient httpclient = getHttpClient();
		HttpGet httpget = new HttpGet(URL);
		Log.d(Const.TAG_HTTP, "URI: " + httpget.getURI());
		String responseBody = "";

		try
		{
			HttpResponse response = httpclient.execute(httpget);
			if (response.getStatusLine().getStatusCode() == 200)
			{
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				responseBody = responseHandler.handleResponse(response);

			}
			else
			{
				responseBody = context.getString(R.string.ERR_CODE) + response.getStatusLine().getStatusCode();
			}
		}
		catch (SocketTimeoutException se)
		{
			responseBody = context.getString(R.string.ERR_TIMEOUT);
			se.printStackTrace();
		}
		catch (ClientProtocolException e)
		{
			responseBody = context.getString(R.string.ERR_PROT);
			e.printStackTrace();
		}
		catch (IOException e)
		{
			responseBody = context.getString(R.string.ERR_IO);
			e.printStackTrace();
		}

		httpclient.getConnectionManager().shutdown();
		return responseBody;
	}

	public HttpClient getHttpClient()
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
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClientProtocolException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
