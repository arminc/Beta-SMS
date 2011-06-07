/*
 * Copyright 2011 Armin Čoralić
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

import nl.coralic.beta.sms.betamax.BetamaxHandler;
import nl.coralic.beta.sms.utils.objects.Response;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * @author "Armin Čoralić"
 * 
 */
public class Wizard extends Activity
{
    Button done;
    EditText txtUsername;
    EditText txtPassword;
    Spinner spnProvider;
    ProgressDialog dialog;
    ArrayAdapter<CharSequence> adapter;

    // TODO: text in text.xml
    // TODO: integration Test case
    // TODO: test what happens when you press back while waiting to verify
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.wizard);
	spnProvider = (Spinner) findViewById(R.id.spnProvider);
	adapter = ArrayAdapter.createFromResource(this, R.array.providers, android.R.layout.simple_spinner_item);
	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	spnProvider.setAdapter(adapter);

	txtUsername = (EditText) findViewById(R.id.txtUsername);
	txtPassword = (EditText) findViewById(R.id.txtPassword);

	done = (Button) findViewById(R.id.btnWizardNext);
	done.setOnClickListener(new View.OnClickListener()
	{
	    public void onClick(View v)
	    {
		checkAccount();
	    }
	});
    }

    private void checkAccount()
    {
	if (txtUsername.getText().toString().equals(""))
	{
	    Toast.makeText(Wizard.this, "Username can not be empty", Toast.LENGTH_SHORT).show();
	    return;
	}
	if (txtPassword.getText().toString().equals(""))
	{
	    Toast.makeText(Wizard.this, "Password can not be empty", Toast.LENGTH_SHORT).show();
	    return;
	}
	dialog = new ProgressDialog(Wizard.this);

	AsyncTask<Void, Void, Response> task = new AsyncTask<Void, Void, Response>()
	{
	    @Override
	    protected void onPreExecute()
	    {
		dialog.setMessage("Verifying account, please wait...");
		dialog.show();
	    }

	    @Override
	    protected Response doInBackground(Void... v)
	    {
		return BetamaxHandler
			.sendSMS(adapter.getItem((int) spnProvider.getSelectedItemId()).toString(), txtUsername.getText().toString(), txtPassword.getText().toString(), "00", "00", "fake");
	    }

	    @Override
	    protected void onPostExecute(Response response)
	    {
		dialog.dismiss();
		// If we get an error as response then it means the username/password is wrong, otherwise it can be oke
		if ("error".equals(response.getErrorMessage()))
		{
		    Toast.makeText(Wizard.this, "Could not verify the account, please check your username and password", Toast.LENGTH_LONG).show();
		}
		else
		{
		    // If one of these then an http error occurred.
		    if (R.string.ERR_CONN_ERR == response.getErrorCode() || R.string.ERR_NO_ARGUMENTS == response.getErrorCode() || R.string.ERR_PROV_NO_RESP == response.getErrorCode())
		    {
			Toast.makeText(Wizard.this, "Could not verify the account. " + response.getErrorMessage(), Toast.LENGTH_LONG).show();
		    }
		    else
		    {
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Wizard.this);
			SharedPreferences.Editor editor = prefs.edit();
			//TODO: the name of the keys as an enum?
			editor.putBoolean("verified", true);
			editor.putString("username", txtUsername.getText().toString());
			editor.putString("verified", txtPassword.getText().toString());
			editor.putFloat("provider", spnProvider.getSelectedItemId());
			editor.commit();
			//TODO: can I resume the previous activity?
			startActivity(new Intent(Wizard.this, Beta_SMS.class));
		    }
		}
	    }
	};
	task.execute();
    }
}
