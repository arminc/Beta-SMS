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
package nl.coralic.beta.sms.widget;

import nl.coralic.beta.sms.R;
import nl.coralic.beta.sms.utils.SendHandler;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

public class WidgetUpdateService extends Service
{
	@Override
	public void onStart(Intent intent, int startId)
	{
		// first build
		RemoteViews updateViews = buildUpdate(this);
		// add to screen
		ComponentName thisWidget = new ComponentName(this, BetaSmsWidget.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(this);
		manager.updateAppWidget(thisWidget, updateViews);
	}

	public RemoteViews buildUpdate(Context context)
	{
		RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.widgetmain);
		SendHandler sh = new SendHandler();
		SharedPreferences properties = PreferenceManager.getDefaultSharedPreferences(WidgetUpdateService.this);
		updateViews.setTextViewText(R.id.widget_textview, sh.getBalance(properties.getString("ServiceKey", ""), properties.getString("UsernameKey", ""),properties.getString("PasswordKey", "")));
		return updateViews;
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
}
