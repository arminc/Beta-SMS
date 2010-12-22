package nl.coralic.beta.sms.utils;

import java.util.ArrayList;
import java.util.Iterator;

import nl.coralic.beta.sms.Beta_SMS;
import nl.coralic.beta.sms.R;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.gsm.SmsManager;
import android.widget.Toast;

public class AndroidSMS
{
	Beta_SMS context;
	SMSHelper sms;
	String phoneNumber;
	String message;

	public void sendSMS(Beta_SMS cont, String phone, String data)
	{
		sms = new SMSHelper();
		context = cont;
		phoneNumber = phone;
		message = data;
		
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(SENT), 0);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, new Intent(DELIVERED), 0);

		context.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1)
			{
				switch (getResultCode())
				{
					case Activity.RESULT_OK:
						Toast.makeText(context, context.getString(R.string.SMS_SEND_SUCCESS), Toast.LENGTH_SHORT).show();
						sms.addSMS(context.getContentResolver(), message, phoneNumber);
						if (context.properties.getBoolean("DeleteTextKey", false))
						{
							// reset everything to empty
							context.txtSmsText.setText("");
							context.to.setText("");
						}
						break;
					default:
						Toast.makeText(context, context.getString(R.string.SMS_SEND_FAILED), Toast.LENGTH_SHORT).show();
						break;
				}
			}
		}, new IntentFilter(SENT));

		context.registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1)
			{
				switch (getResultCode())
				{
					case Activity.RESULT_OK:
						Toast.makeText(context, context.getString(R.string.SMS_DELIVERED_SUCCESS), Toast.LENGTH_SHORT).show();
						break;
					case Activity.RESULT_CANCELED:
						Toast.makeText(context, context.getString(R.string.SMS_DELIVERED_FAILED), Toast.LENGTH_SHORT).show();
						break;
				}
			}
		}, new IntentFilter(DELIVERED));

		SmsManager sms = SmsManager.getDefault();
		ArrayList<String> text = Utils.splitSmsTextTo160Chars(message);
		if (text.size() > 1)
		{
			Iterator<String> i = text.iterator();
			while (i.hasNext())
			{
				sms.sendTextMessage(phoneNumber, null, i.next(), sentPI, deliveredPI);
			}
		}
		else
		{
			sms.sendTextMessage(phoneNumber, null, text.get(0), sentPI, deliveredPI);
		}
	}
}