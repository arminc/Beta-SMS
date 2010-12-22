package nl.coralic.beta.sms.utils.contact;

import nl.coralic.beta.sms.log.Log;
import nl.coralic.beta.sms.utils.Utils;
import nl.coralic.beta.sms.utils.constants.Const;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.Contacts;
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
		Log.logit(Const.TAG_CPLA, "New");
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent)
	{
		Log.logit(Const.TAG_CPLA, "New view");
		final LayoutInflater inflater = LayoutInflater.from(context);
		final TextView view = (TextView) inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
		view.setText(cursor.getString(2) + " " + cursor.getString(1));
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor)
	{
		Log.logit(Const.TAG_CPLA, "bind to view");
		((TextView) view).setText(cursor.getString(2) + " " + cursor.getString(1));
	}

	@Override
	public String convertToString(Cursor cursor)
	{
		Log.logit(Const.TAG_CPLA, "convert to string");
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
			buffer.append(Contacts.ContactMethods.NAME);
			buffer.append(") GLOB ?");
			args = new String[] { constraint.toString().toUpperCase() + "*" };
		}
		return contentResolver.query(Contacts.Phones.CONTENT_URI, new String[] { Contacts.People._ID, Contacts.People.NUMBER, Contacts.People.NAME },
				buffer == null ? null : buffer.toString(), args, Contacts.People.DEFAULT_SORT_ORDER);
	}

}
