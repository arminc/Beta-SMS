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
package nl.coralic.beta.sms.utils;

import nl.coralic.beta.sms.Beta_SMS;
import nl.coralic.beta.sms.betamax.BetamaxHandler;
import nl.coralic.beta.sms.utils.objects.Const;
import nl.coralic.beta.sms.utils.objects.Response;
import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;

public class BetaSMSService extends Service
{
	private SharedPreferences properties;
	
	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}
	
	@Override
	public void onCreate()
	{
		properties = PreferenceManager.getDefaultSharedPreferences(BetaSMSService.this);
	}
	
	@Override
	public void onStart(Intent intent, int startId)
	{
		
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
	    //TODO: if the sms is bigger than 160charts it will fail, fix!
		
		String to = intent.getExtras().getString("number");
		String sms = intent.getExtras().getString("sms");
		
		AsyncTask<String, Void, Response> task = new AsyncTask<String, Void, Response>() {
			
			private String to;
			private String sms;
			private int startId;

			@Override
			protected Response doInBackground(String... s)
			{
				to = s[0];
				sms = s[1];
				startId = Integer.valueOf(s[2]);
				return BetamaxHandler.sendSMS(properties.getString("ServiceKey", ""), properties.getString("UsernameKey", ""), properties.getString("PasswordKey", ""), properties.getString("PhoneKey",
				""), to, sms);
			}

			@Override
			protected void onPostExecute(Response anwser)
			{
				if (anwser.isResponseOke() == true)
				{
					SMSHelper smsHelper = new SMSHelper();
					if (properties.getBoolean("SaveSMSKey", false))
					{
						smsHelper.addSMS(getContentResolver(), sms, to);
					}
				}
				else
				{
					NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

					int icon = R.drawable.ic_dialog_alert;
					CharSequence text = "Beta-SMS failed to send SMS";
					CharSequence contentTitle = "Can't send to: "+to;
					CharSequence contentText = anwser.getErrorMessage();
					long when = System.currentTimeMillis();

					Intent i = new Intent(getApplicationContext(), Beta_SMS.class);
					i.putExtra("number", to);
					i.putExtra("sms", sms);
					PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, i, 0);
					Notification notification = new Notification(icon,text,when);
					notification.flags |= Notification.FLAG_AUTO_CANCEL;
					notification.setLatestEventInfo(getApplicationContext(), contentTitle, contentText, contentIntent);
					notificationManager.notify(startId, notification);
				}
			}
		};
		task.execute(to,sms,String.valueOf(startId));
		return 1;
	}
}
