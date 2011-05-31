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
package nl.coralic.beta.sms.connector.operator;

import java.util.ArrayList;

import nl.coralic.beta.sms.Beta_SMS;
import nl.coralic.beta.sms.R;
import nl.coralic.beta.sms.utils.SMSHelper;
import nl.coralic.beta.sms.utils.Utils;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.widget.Toast;

public class OperatorSMS
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

		context.registerReceiver(new BroadcastReceiver() 
		{
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

		context.registerReceiver(new BroadcastReceiver() 
		{
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
		for(String smsText : text)
		{
			sms.sendTextMessage(phoneNumber, null, smsText, sentPI, deliveredPI);
		}
	}
}