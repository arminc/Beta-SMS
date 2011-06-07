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
