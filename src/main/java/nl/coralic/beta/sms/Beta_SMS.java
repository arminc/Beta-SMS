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

import nl.coralic.beta.sms.connector.operator.OperatorSMS;
import nl.coralic.beta.sms.services.BetaSMSService;
import nl.coralic.beta.sms.utils.BalanceHandler;
import nl.coralic.beta.sms.utils.Properties;
import nl.coralic.beta.sms.utils.SendHandler;
import nl.coralic.beta.sms.utils.SmsTextCounter;
import nl.coralic.beta.sms.utils.Utils;
import nl.coralic.beta.sms.utils.constants.Const;
import nl.coralic.beta.sms.utils.contact.PhoneNumbers;
import nl.coralic.beta.sms.utils.contact.PhonesHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

	Intent intent;

	public AutoCompleteTextView to;
	public EditText txtSmsText;
	TextView txtBalance;
	Button send;
	CheckBox chkSendNormal;
	public TextView txtTextCount;

	PhonesHandler phoneHandler;
	PhoneNumbers phoneNumber;
	SendHandler sh;

	AlertDialog chooseNumberAlert;
	ProgressDialog showStatusAlert;
	public SharedPreferences properties;
	String intentToNumber;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		intentToNumber = null;
		//check if we have received any intent
		processIncomingIntent(getIntent());
		
		// Set the view
		super.onCreate(savedInstanceState);
		setContentView(R.layout.betasmsmain);

		to = (AutoCompleteTextView) findViewById(R.id.txtTo);
		txtTextCount = (TextView) findViewById(R.id.txtTextCount);
		txtSmsText = (EditText) findViewById(R.id.txtSmsText);
		send = (Button) findViewById(R.id.btnSend);
		chkSendNormal = (CheckBox) findViewById(R.id.chkSendNormal);

		txtSmsText.addTextChangedListener(new SmsTextCounter(txtTextCount));

		txtBalance = (TextView) findViewById(R.id.txtBalance);

		// you need to read the properties before showing balance
		properties = PreferenceManager.getDefaultSharedPreferences(Beta_SMS.this);
		
		if (Utils.arePropertiesEmpty(Beta_SMS.this, properties))
		{
			startActivity(new Intent(Beta_SMS.this, Properties.class));
		}

		if(intentToNumber != null)
		{
			to.setText(intentToNumber);
		}
		// get the balance
		showBalance();
		
		// auto complete contacts, show all phones
		phoneHandler = new PhonesHandler();
		to.setAdapter(phoneHandler.getContactsPhonesListAdapter(getContentResolver(), this));

		// Set the intent for selecting the contact
		intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI); 

		// When double taped it will fire up an intent for showing Contacts,
		// when a contact is selected it will return and fire up
		// onActivityResult function
		to.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v)
			{
				startActivityForResult(intent, Const.PICK_CONTACT);
			}
		});

		//when send clicked
		send.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v)
			{
				if (!Utils.arePropertiesEmpty(Beta_SMS.this, properties))
				{
					onSend();
				}
			}
		});
		txtSmsText.requestFocus();
	}

	/**
	 * It will be fired up after a user selects an contact, it will show the us
	 */
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data)
	{
		if (reqCode == Const.PICK_CONTACT && resultCode == RESULT_OK)
		{
			AsyncTask<Uri, Void, PhoneNumbers> task = new AsyncTask<Uri, Void, PhoneNumbers>() {
				@Override
				protected PhoneNumbers doInBackground(Uri... uris)
				{
					phoneHandler = new PhonesHandler();
					return phoneHandler.getPhoneNumbersForSelectedContact(getContentResolver(), uris[0]);
				}

				@Override
				protected void onPostExecute(PhoneNumbers result)
				{
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
			to.setText(phoneNumber.getCleanPhoneNumber(0));
		}
		else
		{
			builder.setTitle(phoneNumber.getContactsName());
			builder.setItems(phoneNumber.getPhoneNumbersLabelArray(), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item)
				{
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
	 * Creates the menu from betasmsmenu.xml
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.betasmsmenu, menu);
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
			case R.id.request:
				String url = "http://beta-sms.coralic.nl";
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
				return true;
			case R.id.quit:
				this.finish();
				return true;
		}
		return false;
	}

	/**
	 * Sends the SMS
	 */
	private void onSend()
	{
		if (!Utils.areFieldsEmpty(Beta_SMS.this, to.getText().toString(), txtSmsText.getText().toString()))
		{
			return;
		}

		if (chkSendNormal.isChecked())
		{
			OperatorSMS sms = new OperatorSMS();
			sms.sendSMS(Beta_SMS.this, to.getText().toString(), txtSmsText.getText().toString());
		}
		else
		{
			sendSmsTroughService(to.getText().toString(), txtSmsText.getText().toString());
			Toast.makeText(Beta_SMS.this, getText(R.string.SMS_SENDING), Toast.LENGTH_SHORT).show();
			if (properties.getBoolean("DeleteTextKey", false))
			{
				// reset everything to empty
				txtSmsText.setText("");
				to.setText("");
			}
		}
	}


	private void showBalance()
	{
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... v)
			{
				BalanceHandler balance = new BalanceHandler();
				return balance.getBalance(properties.getString("ServiceKey", ""), properties.getString("UsernameKey", ""), properties.getString(
						"PasswordKey", ""));
			}

			@Override
			protected void onPostExecute(String anwser)
			{
				txtBalance.setText(anwser);
			}
		};
		task.execute();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
	
	private void processIncomingIntent(Intent recintent)
	{
		Bundle extras = recintent.getExtras();
		if(extras != null)
		{
			sendSmsTroughService(extras.getString(Const.NUMBER), extras.getString(Const.SMS));
			this.finish();					
		}
		else if (recintent.getData() != null)
		{
			String number = Utils.stripString(recintent.getDataString());
			intentToNumber = URLDecoder.decode(number);
		}
	}
	
	private void sendSmsTroughService(String number, String sms)
	{
		if(number == null || sms == null || "".equals(number) || "".equals(sms))
		{
			Toast.makeText(this, R.string.SMSTO_INTENT_WRONG, Toast.LENGTH_LONG);
			return;
		}
		
		Intent newIntent = new Intent(this, BetaSMSService.class);
		newIntent.putExtra(Const.NUMBER, number);
		newIntent.putExtra(Const.SMS, sms);
		startService(newIntent);		
	}
}