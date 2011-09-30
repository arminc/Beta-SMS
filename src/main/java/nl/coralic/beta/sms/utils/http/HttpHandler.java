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
package nl.coralic.beta.sms.utils.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.coralic.beta.sms.R;
import nl.coralic.beta.sms.utils.objects.Response;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

public class HttpHandler
{
    public static Response execute(String url, HashMap<String, String> arguments)
    {
	try
	{
	    List<NameValuePair> postdata = createPostData(arguments);
	    return doPost(url, postdata);
	}
	catch(Exception e)
	{
	    return new Response(R.string.ERR_NO_ARGUMENTS);
	}	
    }
    
    private static List<NameValuePair> createPostData(HashMap<String, String> arguments) throws Exception
    {
	List<NameValuePair> postdata = new ArrayList<NameValuePair>();
	if (arguments != null)
	{
	    for (String key : arguments.keySet())
	    {
		postdata.add(new BasicNameValuePair(key, arguments.get(key)));
	    }
	    return postdata;
	}
	throw new Exception("No arguments");
    }

    private static Response doPost(String url, List<NameValuePair> postdata)
    {
	HttpClient httpclient = getHttpClient();
	try
	{
	    HttpPost httpost = new HttpPost(url);
	    httpost.setEntity(new UrlEncodedFormEntity(postdata, HTTP.UTF_8));
	    HttpResponse response = httpclient.execute(httpost);
	    if (response.getStatusLine().getStatusCode() == 200)
	    {
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		return new Response(responseHandler.handleResponse(response));
	    }
	    else
	    {
		return new Response(R.string.ERR_PROV_NO_RESP);
	    }
	}
	catch (Exception e)
	{
	    return new Response(R.string.ERR_CONN_ERR);
	}
	finally
	{
	    // Release the resources
	    httpclient.getConnectionManager().shutdown();
	}
    }
    
    private static HttpClient getHttpClient()
    {
	HttpParams httpParameters = new BasicHttpParams();
	int timeoutConnection = 28000;
	HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
	int timeoutSocket = 28000;
	HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
	return new DefaultHttpClient(httpParameters);
    }
}
