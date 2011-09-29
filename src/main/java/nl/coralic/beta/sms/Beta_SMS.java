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
package nl.coralic.beta.sms;

import java.net.URLDecoder;
import java.util.Iterator;

import nl.coralic.beta.sms.betamax.BetamaxHandler;
import nl.coralic.beta.sms.utils.BetaSMSService;
import nl.coralic.beta.sms.utils.SmsTextCounter;
import nl.coralic.beta.sms.utils.Utils;
import nl.coralic.beta.sms.utils.contact.PhoneNumbers;
import nl.coralic.beta.sms.utils.contact.PhonesHandler;
import nl.coralic.beta.sms.utils.objects.Const;
import nl.coralic.beta.sms.utils.objects.Key;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author "Armin Čoralić"
 */
public class Beta_SMS extends Activity
{
    SharedPreferences properties;
    private static Context context;

    Intent intent;

    AutoCompleteTextView to;
    EditText txtSmsText;
    Button send;
    CheckBox chkSendNormal;
    Button contact;
    TextView txtTextCount;
    TextView txtTitleSaldoValue;
    TextView txtTitleSaldo;
    String[] providers;

    PhonesHandler phoneHandler;
    PhoneNumbers phoneNumber;

    AlertDialog chooseNumberAlert;
    ProgressDialog showStatusAlert;

    String intentValue;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
	context = getApplicationContext();

	// TODO: check for incoming intent?
	intentValue = null;
	checkForIntent(getIntent());
	super.onCreate(savedInstanceState);
	properties = PreferenceManager.getDefaultSharedPreferences(Beta_SMS.this);
	// Check if the account is valid, if not open the wizard (should happen only the first time you open the app
	if (!Utils.checkForValidAccount(properties))
	{
	    startActivity(new Intent(this, Wizard.class));
	}

	// Set the view
	Log.d(Const.TAG_MAIN, "Creating the view and the rest of the GUI.");

	//allow custom title
	requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

	setContentView(R.layout.betasms);

	//set custom title
	getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
	txtTitleSaldoValue = (TextView) findViewById(R.id.txtTitleSaldoValue);
	txtTitleSaldo = (TextView) findViewById(R.id.txtTitleSaldo);	
	
	//show providers
	providers = getResources().getStringArray(R.array.providers);

	//get the rest of the ui components
	to = (AutoCompleteTextView) findViewById(R.id.txtTo);
	txtTextCount = (TextView) findViewById(R.id.txtTextCount);
	txtSmsText = (EditText) findViewById(R.id.txtSmsText);
	send = (Button) findViewById(R.id.btnSend);
	contact = (Button) findViewById(R.id.btnContact);

	// get the balance
	showBalance();

	txtSmsText.addTextChangedListener(new SmsTextCounter(txtTextCount));

	if (intentValue != null)
	{
	    to.setText(intentValue);
	}

	// auto complete contacts, show all phones
	phoneHandler = new PhonesHandler();
	to.setAdapter(phoneHandler.getContactsPhonesListAdapter(getContentResolver(), this));

	// Set the intent for selecting the contact
	intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

	// When taped it will fire up an intent for showing Contacts,
	// when a contact is selected it will return and fire up
	// onActivityResult function
	contact.setOnClickListener(new View.OnClickListener()
	{
	    public void onClick(View v)
	    {
		Log.d(Const.TAG_MAIN, "Double taped, show contacts");
		startActivityForResult(intent, Const.PICK_CONTACT);
	    }
	});

	// when send clicked
	send.setOnClickListener(new View.OnClickListener()
	{
	    public void onClick(View v)
	    {
		onSend();
	    }
	});

	// set focus on the sms text field
	txtSmsText.requestFocus();
    }

    @Override
    protected void onRestart()
    {
	super.onRestart();
	// If the users presses back on the Wizard it means no username/pass is filled and he wants to quit, there is no reason showing Beta-SMS app if you can't use it.
	properties = PreferenceManager.getDefaultSharedPreferences(Beta_SMS.this);
	if (!Utils.checkForValidAccount(properties))
	{
	    finish();
	}
    }

    public static Context getAppContext()
    {
	return context;
    }

    /**
     * It will be fired up after a user selects an contact
     */
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data)
    {
	Log.d(Const.TAG_MAIN, "User selected an contact.");
	if (reqCode == Const.PICK_CONTACT && resultCode == RESULT_OK)
	{
	    AsyncTask<Uri, Void, PhoneNumbers> task = new AsyncTask<Uri, Void, PhoneNumbers>()
	    {
		@Override
		protected PhoneNumbers doInBackground(Uri... uris)
		{
		    phoneHandler = new PhonesHandler();
		    Log.d(Const.TAG_MAIN, "Get the phonenumbers from the selected contact.");
		    return phoneHandler.getPhoneNumbersForSelectedContact(getContentResolver(), uris[0]);
		}

		@Override
		protected void onPostExecute(PhoneNumbers result)
		{
		    Log.d(Const.TAG_MAIN, "Present the numbers.");
		    chooseNumber(result);
		}
	    };
	    task.execute(data.getData());
	}
    }

    /**
     * let's you choose witch number you want from the selected contact. If the contact has only one number it auto selects that
     */
    private void chooseNumber(PhoneNumbers contact)
    {
	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	this.phoneNumber = contact;
	if (phoneNumber.getPhoneNumbersLabelArray().length == 1)
	{
	    Log.d(Const.TAG_MAIN, "Contact only has one number.");
	    to.setText(phoneNumber.getCleanPhoneNumber(0));
	}
	else
	{
	    Log.d(Const.TAG_MAIN, "Contact has multiple numbers showing all of them.");
	    builder.setTitle(phoneNumber.getContactsName());
	    builder.setItems(phoneNumber.getPhoneNumbersLabelArray(), new DialogInterface.OnClickListener()
	    {
		public void onClick(DialogInterface dialog, int item)
		{
		    Log.d(Const.TAG_MAIN, "User selected an number of a contact.");
		    to.setText(phoneNumber.getCleanPhoneNumber(item));
		    phoneNumber = null;
		    chooseNumberAlert.dismiss();
		}
	    });
	    chooseNumberAlert = builder.create();
	    chooseNumberAlert.show();
	}
    }

    /**
     * Checks if the data of the intent can be used
     * 
     * @param recintent
     *            Intent that is received
     */
    @SuppressWarnings("deprecation")
    private void checkDataIncomingIntent(Intent recintent)
    {
	Log.d(Const.TAG_MAIN, "The intent data.");
	String param = Utils.stripString(recintent.getDataString());
	intentValue = URLDecoder.decode(param);
    }

    /**
     * Creates the menu from betasmsmenu.xml
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
	getMenuInflater().inflate(R.menu.betasmsmenu, menu);
	return true;
    }

    /**
     * Does different things depending on the menu item that is selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
	switch (item.getItemId())
	{
	    case R.id.info:
		Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.info);
		dialog.setTitle(R.string.INFO_TITLE);
		dialog.show();
		return true;
	    case R.id.settings:
		startActivity(new Intent(this, Properties.class));
		return true;
	}
	return false;
    }

    /**
     * Sends the SMS
     */
    private void onSend()
    {
	if (!checkIfFieldAreEmpty())
	{
	    return;
	}
	Intent in = new Intent(this, BetaSMSService.class);
	in.putExtra("number", to.getText().toString());
	in.putExtra("sms", txtSmsText.getText().toString());
	startService(in);
	Toast.makeText(Beta_SMS.this, getText(R.string.SMS_SENDING), Toast.LENGTH_SHORT).show();
	if (properties.getBoolean("DeleteTextKey", false))
	{
	    // reset everything to empty
	    txtSmsText.setText("");
	    to.setText("");
	}
    }

    private boolean checkIfFieldAreEmpty()
    {
	Log.d(Const.TAG_MAIN, "Checking to see if all fields are filled.");
	if (to.getText().toString().length() < 1)
	{
	    Toast.makeText(Beta_SMS.this, getText(R.string.MAIN_TO_EMPTY), Toast.LENGTH_SHORT).show();
	    Log.d(Const.TAG_MAIN, "To empty.");
	    return false;
	}
	if (txtSmsText.getText().toString().length() < 1)
	{
	    Toast.makeText(Beta_SMS.this, getText(R.string.MAIN_SMS_EMPTY), Toast.LENGTH_SHORT).show();
	    Log.d(Const.TAG_MAIN, "SMS text empty.");
	    return false;
	}
	return true;
    }

    private void showBalance()
    {
	Log.d(Const.TAG_MAIN, "User allowes for checking saldo so go get it.");
	AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>()
	{
	    @Override
	    protected String doInBackground(Void... v)
	    {
		return BetamaxHandler.getBalance(providers[(int) properties.getFloat(Key.PROVIDERID.toString(), 0)], properties.getString(Key.USERNAME.toString(), ""),
			properties.getString(Key.PASSWORD.toString(), ""));
	    }

	    @Override
	    protected void onPostExecute(String anwser)
	    {
		// if we receive * don't show that
		if (!"*".equals(anwser))
		{
		    txtTitleSaldo.setText(getString(R.string.txtTitleSaldo));
		    txtTitleSaldoValue.setText(anwser);
		}
	    }
	};
	task.execute();
    }

    @Override
    protected void onDestroy()
    {
	stopService(new Intent(this, BetaSMSService.class));
	super.onDestroy();
    }

    private void checkForIntent(Intent receivedIntent)
    {
	Bundle extras = receivedIntent.getExtras();
	//if it contains extras then it's our own bundle
	if (extras != null)
	{
	    Iterator<String> i = extras.keySet().iterator();
	    while (i.hasNext())
	    {
		String tmp = i.next();
		Log.d(Const.TAG_MAIN, "Bundle key: " + tmp + " value: " + extras.getString(tmp));
	    }
	    Intent in = new Intent(this, BetaSMSService.class);
	    in.putExtra("number", extras.getString("number"));
	    in.putExtra("sms", extras.getString("sms"));
	    startService(in);
	    this.finish();
	}
	//if it only contains data then it has the number to sms to
	else if (receivedIntent.getData() != null)
	{
	    Log.d(Const.TAG_MAIN, "Got an non empty Intent.");
	    checkDataIncomingIntent(receivedIntent);
	}
    }
}