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
package nl.coralic.beta.sms.utils.contact;

import nl.coralic.beta.sms.utils.Utils;
import nl.coralic.beta.sms.utils.objects.Const;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filterable;
import android.widget.TextView;
/**
 * @author "Armin Čoralić"
 */
public class ContactsPhonesListAdapter extends CursorAdapter implements Filterable
{
	private ContentResolver contentResolver;

	public ContactsPhonesListAdapter(Context context, Cursor c)
	{
		super(context, c);
		contentResolver = context.getContentResolver();
		Log.d(Const.TAG_CPLA, "New");
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent)
	{
		Log.d(Const.TAG_CPLA, "New view");
		final LayoutInflater inflater = LayoutInflater.from(context);
		final TextView view = (TextView) inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
		view.setText(cursor.getString(2) + " " + cursor.getString(1));
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor)
	{
		Log.d(Const.TAG_CPLA, "bind to view");
		((TextView) view).setText(cursor.getString(2) + " " + cursor.getString(1));
	}

	@Override
	public String convertToString(Cursor cursor)
	{
		Log.d(Const.TAG_CPLA, "convert to string");
		return Utils.stripString(cursor.getString(1));
	}

	@Override
	public Cursor runQueryOnBackgroundThread(CharSequence constraint)
	{
		if (getFilterQueryProvider() != null)
		{
			return getFilterQueryProvider().runQuery(constraint);
		}
		StringBuilder buffer = null;
		String[] args = null;
		if (constraint != null)
		{
			buffer = new StringBuilder();
			buffer.append("UPPER(");
			buffer.append(ContactsContract.Contacts.DISPLAY_NAME);
			buffer.append(") GLOB ?");
			args = new String[] { constraint.toString().toUpperCase() + "*" };
		}
		return contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[] {ContactsContract.CommonDataKinds.Phone._ID, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},buffer == null ? null : buffer.toString(), args, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC");
	}

}
