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
import nl.coralic.beta.sms.log.Log;
import nl.coralic.beta.sms.utils.constants.Const;
import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Log.logit(Const.TAG_SERV, "Trying to send SMS.");
		String to = intent.getExtras().getString("number");
		String sms = intent.getExtras().getString("sms");
		SendHandler sh = new SendHandler(properties.getString("PasswordKey", ""), properties.getString("UsernameKey", ""), properties.getString("PhoneKey",
		""), to, sms, properties.getString("ServiceKey", ""));
		Response  anwser = sh.send(getApplicationContext());
		if (anwser.isSucceful() == true)
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

			int icon = R.drawable.ic_menu_send;
			CharSequence text = "Beta-SMS faild to send";
			CharSequence contentTitle = "Beta-SMS faild to send SMS";
			CharSequence contentText = "Faild for "+to+ " . Tap to resend.";
			long when = System.currentTimeMillis();

			Intent i = new Intent(this, Beta_SMS.class);
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, i, flags);

			Notification notification = new Notification(icon,text,when);

			notification.setLatestEventInfo(this, contentTitle, contentText, contentIntent);

			notificationManager.notify(startId, notification);
		}
		return 1;
	}
}
