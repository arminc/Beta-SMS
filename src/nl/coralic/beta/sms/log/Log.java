package nl.coralic.beta.sms.log;
/**
 * @author "Armin Čoralić"
 */
public class Log
{
	private static boolean logAllowed = true;
	/**
	 * With this class it's possible to turn and turn off logging
	 * @param type is the tag so you can recognize who is logging
	 * @param data the data as string that you want to log
	 */
	public static void logit(String type, String data)
	{
		if (logAllowed)
		{
			android.util.Log.d(type, data);
		}
	}
}
