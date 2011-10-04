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
package nl.coralic.beta.sms.utils.contact;

import nl.coralic.beta.sms.R;
import nl.coralic.beta.sms.utils.ApplicationContextHelper;
import nl.coralic.beta.sms.utils.objects.Const;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

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
		Log.d(Const.TAG_PHH, "contactUri: " + contactUri);
		Cursor c =  contentResolver.query(contactUri, null, null, null, null);
		Log.d(Const.TAG_PHH, "Loop trough alle phones.");
		if (c.moveToFirst())
		{
			String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
		    String   name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
		    phones.setContactsName(name);
		    String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
	       if (hasPhone.equalsIgnoreCase("1")) 
	       {
	    	   Cursor ph = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
	    	   while (ph.moveToNext()) 
	    	   {
	    		   phones.addPhoneNumber(ph.getString(ph.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)),getType(ph));
	    	   }
	    	   ph.close();
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
		Log.d(Const.TAG_CPLA, "uri test: "+ ContactsContract.Contacts.CONTENT_URI);
		Cursor c = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[] {ContactsContract.CommonDataKinds.Phone._ID, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}, null, null,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");		
		ContactsPhonesListAdapter adapter = new ContactsPhonesListAdapter(context, c);
		return adapter;
	}

	/**
	 * @param c
	 * @return the type of the phone
	 */
	private String getType(Cursor c)
	{
		int type = c.getInt(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));

		if (type == ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM)
		{
			return c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));
		}
		if (type == ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
		{
			return ApplicationContextHelper.getStringUsingR_ID(R.string.PHONE_TYPE_HOME);
		}
		if (type == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
		{
			return ApplicationContextHelper.getStringUsingR_ID(R.string.PHONE_TYPE_MOBILE);
		}
		else
		{
			return ApplicationContextHelper.getStringUsingR_ID(R.string.PHONE_TYPE_OTHER);
		}
	}
}
