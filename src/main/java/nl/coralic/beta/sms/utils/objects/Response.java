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
package nl.coralic.beta.sms.utils.objects;

import nl.coralic.beta.sms.Beta_SMS;

public class Response
{
    protected boolean responseOke = false;
    protected int errorCode;
    protected String errorMessage;
    protected String response;
    
    public Response(int errorCode)
    {
	this.errorCode = errorCode;
    }
    
    public Response(boolean responseOke, String response)
    {
	this.responseOke = true;
	this.response = response;
    }

    public boolean isResponseOke()
    {
        return responseOke;
    }
    
    public int getErrorCode()
    {
        return errorCode;
    }
    
    public String getErrorMessage()
    {
	//error 9999 means the error message is coming from Betamax provider, show that one instead of our own
        if(errorCode == 9999)
        {
            return errorMessage;
        }
        else
        {
            return Beta_SMS.getAppContext().getText(errorCode).toString();
        }
    }
    
    public String getResponse()
    {
        return response;
    }  
}
