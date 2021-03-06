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
/**
 * 
 */
package nl.coralic.beta.sms;

import nl.coralic.beta.sms.R;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.Window;

/**
 * @author "Armin Čoralić"
 * 
 */
public class Properties extends PreferenceActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	super.onCreate(savedInstanceState);
	getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
	addPreferencesFromResource(R.layout.properties);
	Preference myPref = (Preference) findPreference("Wizard");
	OnPreferenceClickListener onPreferenceClickListener = new OnPreferenceClickListener()
	{
	    public boolean onPreferenceClick(Preference preference)
	    {
		startActivity(new Intent(Properties.this, Wizard.class));
		return false;
	    }
	};
	myPref.setOnPreferenceClickListener(onPreferenceClickListener);
    }
}
