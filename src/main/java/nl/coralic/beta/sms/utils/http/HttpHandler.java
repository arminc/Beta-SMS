package nl.coralic.beta.sms.utils.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public static Response execute(String url, HashMap<String, String> data)
    {
	List<NameValuePair> postdata = new ArrayList<NameValuePair>();
	if (data != null)
	{
	    for (String key : data.keySet())
	    {
		postdata.add(new BasicNameValuePair(key, data.get(key)));
	    }
	}
	else
	{
	    return new Response("No arguments");
	}
	return doPost(url, postdata);
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
		return new Response(true,responseHandler.handleResponse(response));
	    }
	    else
	    {
		return new Response("Provider did not respond correctly");
	    }
	}
	catch (Exception e)
	{
	    return new Response("Could not connect to the provider");
	}
	finally
	{
	    // Release the resources
	    httpclient.getConnectionManager().shutdown();
	}
    }
}
