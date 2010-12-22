/**
 * 
 */
package nl.coralic.beta.sms.utils;

import nl.coralic.beta.sms.R;
import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * @author "Armin Čoralić"
 *
 */
public class Properties extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.properties);
	}
}
