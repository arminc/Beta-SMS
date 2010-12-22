package nl.coralic.beta.sms.utils;
import android.content.Context;
/**
 * @author "Armin Čoralić"
 */
public class FeatureRequestResponse extends Response
{

	public FeatureRequestResponse(String data, Context con)
	{
		context = con;
		// do something
		if(data != null && !data.startsWith("Error:") && !data.startsWith("Fout:"))
		{
			if(data.equalsIgnoreCase("DONE"))
			{
				setResponse(data);
				setSucceful(true);
			}
			else
			{
				setError(data);
				setSucceful(false);
			}
		}
		else
		{
			setError(data);
			setSucceful(false);
		}
	}
}
