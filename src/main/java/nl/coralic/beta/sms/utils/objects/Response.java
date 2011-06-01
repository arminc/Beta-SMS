package nl.coralic.beta.sms.utils.objects;

public class Response
{
    protected boolean responseOke = false;
    protected String errorMessage;
    protected String response;
    
    public Response(String errorMessage)
    {
	this.errorMessage = errorMessage;
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

    public String getErrorMessage()
    {
        return errorMessage;
    }
    
    public String getResponse()
    {
        return response;
    }  
}
