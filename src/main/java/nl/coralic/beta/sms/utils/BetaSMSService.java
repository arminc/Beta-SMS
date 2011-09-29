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
import nl.coralic.beta.sms.Beta_SMS.ResponseReceiver;
import nl.coralic.beta.sms.betamax.BetamaxHandler;
import nl.coralic.beta.sms.utils.objects.Response;
import android.R;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class BetaSMSService extends IntentService
{
    public static final String TO = "to";
    public static final String SMS = "sms";

    private SharedPreferences properties;

    public BetaSMSService()
    {
	super("BetaSMSService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
	properties = PreferenceManager.getDefaultSharedPreferences(BetaSMSService.this);
	// TODO: if the sms is bigger than 160charts it will fail, fix!

	String to = intent.getExtras().getString(TO);
	String sms = intent.getExtras().getString(SMS);
	Response response = BetamaxHandler.sendSMS(properties.getString("ServiceKey", ""), properties.getString("UsernameKey", ""), properties.getString("PasswordKey", ""),
		properties.getString("PhoneKey", ""), to, sms);
	if (response.isResponseOke() == true)
	{
	    //send broadcast
	    Intent broadcastIntent = new Intent();
	    broadcastIntent.setAction(ResponseReceiver.ACTION_RESP);
	    broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
	    sendBroadcast(broadcastIntent);
	    
	    
	    SMSHelper smsHelper = new SMSHelper();
	    if (properties.getBoolean("SaveSMSKey", false))
	    {
		smsHelper.addSMS(getContentResolver(), sms, to);
	    }
	    
	}
	else
	{
	    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

	    int icon = R.drawable.ic_dialog_alert;
	    CharSequence text = "Beta-SMS failed to send SMS";
	    CharSequence contentTitle = "Can't send to: " + to;
	    CharSequence contentText = response.getErrorMessage();
	    long when = System.currentTimeMillis();

	    Intent i = new Intent(getApplicationContext(), Beta_SMS.class);
	    i.putExtra(TO, to);
	    i.putExtra(SMS, sms);
	    PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, i, 0);
	    Notification notification = new Notification(icon, text, when);
	    notification.flags |= Notification.FLAG_AUTO_CANCEL;
	    notification.setLatestEventInfo(getApplicationContext(), contentTitle, contentText, contentIntent);
	    notificationManager.notify(1, notification);
	}
    }
}
