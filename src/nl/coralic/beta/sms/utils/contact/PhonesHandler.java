/**
 * 
 */
package nl.coralic.beta.sms.utils.contact;

import nl.coralic.beta.sms.log.Log;
import nl.coralic.beta.sms.utils.constants.Const;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.Contacts.People.Phones;

/**
 * @author "Armin Čoralić"
 */
public class PhonesHandler
{
	/**
	 * It uses picked contact (uri) to search for all the phones for that contact and saves it to an object.
	 * @param contentResolver
	 * @param contactUri
	 * @return object containing contacts phone numbers
	 */
	public PhoneNumbers getPhoneNumbersForSelectedContact(ContentResolver contentResolver, Uri contactUri)
	{
		PhoneNumbers phones = new PhoneNumbers();
		Log.logit(Const.TAG_PHH, "contactUri: " + contactUri);
		Uri numbers_uri = Uri.withAppendedPath(contactUri, Contacts.People.Phones.CONTENT_DIRECTORY);
		Log.logit(Const.TAG_PHH, "contactUriWithPhones: " + numbers_uri);
		
		Cursor c = contentResolver.query(numbers_uri, null, null, null, Phones.DEFAULT_SORT_ORDER);
		Log.logit(Const.TAG_PHH, "Loop trough alle phones.");
		if (c.moveToFirst())
		{
			while (!c.isAfterLast())
			{
				phones.setContactsName(c.getString(c.getColumnIndex(Contacts.People.DISPLAY_NAME)));
				phones.addPhoneNumber(c.getString(c.getColumnIndex(Contacts.Phones.NUMBER)), getType(c));
				c.moveToNext();
			}
		}
		return phones;
	}

	/**
	 * It queries for all the phone numbers on the phone and matches typed text to the name of the contact.
	 * @param contentResolver
	 * @param context
	 * @return 
	 */
	public ContactsPhonesListAdapter getContactsPhonesListAdapter(ContentResolver contentResolver, Context context)
	{
		Log.logit(Const.TAG_CPLA, "uri test: "+ android.provider.Contacts.Phones.CONTENT_URI);
		Cursor c = contentResolver.query(Contacts.Phones.CONTENT_URI, new String[] {Contacts.People._ID, Contacts.People.NUMBER, Contacts.People.NAME}, null, null, Contacts.People.DEFAULT_SORT_ORDER);
		ContactsPhonesListAdapter adapter = new ContactsPhonesListAdapter(context, c);
		return adapter;
	}

	/**
	 * @param c
	 * @return the type of the phone
	 */
	private String getType(Cursor c)
	{
		int type = c.getInt(c.getColumnIndex(Contacts.Phones.TYPE));

		if (type == Contacts.Phones.TYPE_CUSTOM)
		{
			return c.getString(c.getColumnIndex(Contacts.Phones.LABEL));
		}
		if (type == Contacts.Phones.TYPE_HOME)
		{
			return Const.PHONE_TYPE_HOME;
		}
		if (type == Contacts.Phones.TYPE_MOBILE)
		{
			return Const.PHONE_TYPE_MOBILE;
		}
		else
		{
			return Const.PHONE_TYPE_OTHER;
		}
	}
}
