package nl.coralic.beta.sms;

import java.net.URLDecoder;

import nl.coralic.beta.sms.log.Log;
import nl.coralic.beta.sms.utils.AndroidSMS;
import nl.coralic.beta.sms.utils.Properties;
import nl.coralic.beta.sms.utils.Response;
import nl.coralic.beta.sms.utils.SMSHelper;
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
import android.provider.Contacts.People;
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

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		// Set the view
		Log.logit(Const.TAG_MAIN, "Creating the view and the rest of the GUI.");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.betasmsmain);

		to = (AutoCompleteTextView) findViewById(R.id.txtTo);
		txtTextCount = (TextView) findViewById(R.id.txtTextCount);
		txtSmsText = (EditText) findViewById(R.id.txtSmsText);
		send = (Button) findViewById(R.id.btnSend);
		chkSendNormal = (CheckBox) findViewById(R.id.chkSendNormal);

		txtSmsText.addTextChangedListener(new SmsTextCounter(txtTextCount));

		txtBalance = (TextView) findViewById(R.id.txtBalance);

		Log.logit(Const.TAG_MAIN, "Read properties.");
		// you need to read the properties before showing balance
		properties = PreferenceManager.getDefaultSharedPreferences(Beta_SMS.this);

		// get the balance
		showBalance();

		// check if the app is started from an intent (send sms) if so strip the number and show it in the to field
		Intent recintent = getIntent();
		if (recintent.getData() != null)
		{
			Log.logit(Const.TAG_MAIN, "Got an non empty Intent.");
			checkDataIncomingIntent(recintent);
		}

		// auto complete contacts, show all phones
		phoneHandler = new PhonesHandler();
		to.setAdapter(phoneHandler.getContactsPhonesListAdapter(getContentResolver(), this));

		// Set the intent for selecting the contact
		intent = new Intent(Intent.ACTION_PICK, People.CONTENT_URI);

		// When double taped it will fire up an intent for showing Contacts,
		// when a contact is selected it will return and fire up
		// onActivityResult function
		to.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v)
			{
				Log.logit(Const.TAG_MAIN, "Double taped, show contacts");
				startActivityForResult(intent, Const.PICK_CONTACT);
			}
		});

		//when send clicked
		send.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v)
			{
				if (Utils.checkForProperties(Beta_SMS.this, properties))
				{
					onSend();
				}
			}
		});
	}

	/**
	 * It will be fired up after a user selects an contact, it will show the us
	 */
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data)
	{
		Log.logit(Const.TAG_MAIN, "User selected an contact.");
		if (reqCode == Const.PICK_CONTACT && resultCode == RESULT_OK)
		{
			AsyncTask<Uri, Void, PhoneNumbers> task = new AsyncTask<Uri, Void, PhoneNumbers>() {
				@Override
				protected PhoneNumbers doInBackground(Uri... uris)
				{
					phoneHandler = new PhonesHandler();
					Log.logit(Const.TAG_MAIN, "Get the phonenumbers from the selected contact.");
					return phoneHandler.getPhoneNumbersForSelectedContact(getContentResolver(), uris[0]);
				}

				@Override
				protected void onPostExecute(PhoneNumbers result)
				{
					Log.logit(Const.TAG_MAIN, "Present the numbers.");
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
			Log.logit(Const.TAG_MAIN, "Contact only has one number.");
			to.setText(phoneNumber.getCleanPhoneNumber(0));
		}
		else
		{
			Log.logit(Const.TAG_MAIN, "Contact has multiple numbers showing all of them.");
			builder.setTitle(phoneNumber.getContactsName());
			builder.setItems(phoneNumber.getPhoneNumbersLabelArray(), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item)
				{
					Log.logit(Const.TAG_MAIN, "User selected an number of a contact.");
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
	private void checkDataIncomingIntent(Intent recintent)
	{
		Log.logit(Const.TAG_MAIN, "The intent data.");
		String param = Utils.stripString(recintent.getDataString());
		to.setText(URLDecoder.decode(param));
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
		if (!checkIfFieldAreEmpty())
		{
			return;
		}

		if (chkSendNormal.isChecked())
		{
			Log.logit(Const.TAG_MAIN, "Send normal sms not trough betamax.");
			AndroidSMS sms = new AndroidSMS();
			sms.sendSMS(Beta_SMS.this, to.getText().toString(), txtSmsText.getText().toString());
		}
		else
		{
			sh = new SendHandler(properties.getString("PasswordKey", ""), properties.getString("UsernameKey", ""), properties.getString("PhoneKey",
					""), to.getText().toString(), txtSmsText.getText().toString(), properties.getString("ServiceKey", ""));
			showStatusAlert = new ProgressDialog(this);
			showStatusAlert.setMessage(getString(R.string.ALERT_SEND_MSG));
			showStatusAlert.setIndeterminate(true);

			AsyncTask<Void, Void, Response> task = new AsyncTask<Void, Void, Response>() {
				@Override
				protected void onPreExecute()
				{
					showStatusAlert.show();
				}

				@Override
				protected Response doInBackground(Void... v)
				{
					return sh.send(getApplicationContext());
				}

				@Override
				protected void onPostExecute(Response anwser)
				{
					showStatusAlert.dismiss();
					if (anwser.isSucceful() == true)
					{
						Toast.makeText(Beta_SMS.this, anwser.getResponse(), Toast.LENGTH_LONG).show();
						SMSHelper sms = new SMSHelper();
						if (properties.getBoolean("SaveSMSKey", false))
						{
							sms.addSMS(getContentResolver(), txtSmsText.getText().toString(), to.getText().toString());
						}
						if (properties.getBoolean("DeleteTextKey", false))
						{
							// reset everything to empty
							txtSmsText.setText("");
							to.setText("");
						}
					}
					else
					{
						Toast.makeText(Beta_SMS.this, anwser.getError(), Toast.LENGTH_LONG).show();
					}
				}
			};
			task.execute();
		}
	}

	private boolean checkIfFieldAreEmpty()
	{
		Log.logit(Const.TAG_MAIN, "Checking to see if all fields are filled.");
		if (to.getText().toString().length() < 1)
		{
			Toast.makeText(Beta_SMS.this, getText(R.string.MAIN_TO_EMPTY), Toast.LENGTH_SHORT).show();
			Log.logit(Const.TAG_MAIN, "To empty.");
			return false;
		}
		if (txtSmsText.getText().toString().length() < 1)
		{
			Toast.makeText(Beta_SMS.this, getText(R.string.MAIN_SMS_EMPTY), Toast.LENGTH_SHORT).show();
			Log.logit(Const.TAG_MAIN, "SMS text empty.");
			return false;
		}
		return true;
	}

	private void showBalance()
	{

		if (properties.getBoolean("ShowBalanceKey", false))
		{
			Log.logit(Const.TAG_MAIN, "User allowes for checking saldo so go get it.");
			AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

				@Override
				protected String doInBackground(Void... v)
				{
					SendHandler sh = new SendHandler();
					return sh.getBalance(properties.getString("ServiceKey", ""), properties.getString("UsernameKey", ""), properties.getString(
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
	}
}