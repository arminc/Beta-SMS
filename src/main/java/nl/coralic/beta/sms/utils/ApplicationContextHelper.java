package nl.coralic.beta.sms.utils;

import android.content.Context;

public class ApplicationContextHelper
{
    private static Context context;
    
    private ApplicationContextHelper()
    {
	
    }
    
    public static void setContext(Context cnx)
    {
	context = cnx;
    }
    
    public static String getErrorMsgFromErrorCode(int errorCode)
    {
	return getStringUsingR_ID(errorCode);
    }
    
    public static String getStringUsingR_ID(int id)
    {
	return context.getText(id).toString();
    }
}
