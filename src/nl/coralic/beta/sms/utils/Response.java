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

import java.io.StringBufferInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import nl.coralic.beta.sms.R;
import nl.coralic.beta.sms.utils.constants.Const;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import android.content.Context;
import android.util.Log;

/**
 * @author "Armin Čoralić"
 */
public class Response
{
	private String response = "";
	private String error = "";
	private boolean succeful = false;
	protected Context context;

	public Response()
	{
		
	}
	
	public Response(String data, Context con)
	{
		context = con;
		// do something
		if (!data.startsWith("Error:") && !data.startsWith("Fout:")) 
		{
			data = data.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", "");
			Document doc = getDocument(data);

			if (doc != null)
			{
				Element root = doc.getDocumentElement();
				root.normalize();

				if (root.getElementsByTagName("resultstring").item(0).getFirstChild().getNodeValue().equalsIgnoreCase("success"))
				{
					setResponse(context.getString(R.string.SMS_SEND_SUCCESS));
					setSucceful(true);
				}
				else
				{
					String tmpCause = root.getElementsByTagName("description").item(0).getFirstChild().getNodeValue();

					if (tmpCause.equalsIgnoreCase(""))
					{
						setError(context.getString(R.string.SMS_SEND_FAILED));
					}
					else
					{
						//TODO: translate the error messages to other languages
						setError(tmpCause);
					}
					setSucceful(false);
				}
			}
			else
			{
				Log.d(Const.TAG_SEND, "Error doc is null");
				setError(context.getString(R.string.SMS_FAILED_PARS_RESPONSE));
				setSucceful(false);
			}
		}
		else
		{
			setError(data);
			setSucceful(false);
		}

	}

	public String getResponse()
	{
		return response;
	}

	public void setResponse(String response)
	{
		this.response = response;
	}

	public String getError()
	{
		return error;
	}

	protected void setError(String error)
	{
		this.error = error;
	}

	public boolean isSucceful()
	{
		return succeful;
	}

	protected void setSucceful(boolean succeful)
	{
		this.succeful = succeful;
	}

	private Document getDocument(String data)
	{
		Document ret = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try
		{
			DocumentBuilder builder = factory.newDocumentBuilder();
			StringBufferInputStream sbis = new StringBufferInputStream(data);
			ret = builder.parse(sbis);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			setError(context.getString(R.string.SMS_FAILED_PARS_RESPONSE));
			setSucceful(false);
		}
		return ret;
	}

}
