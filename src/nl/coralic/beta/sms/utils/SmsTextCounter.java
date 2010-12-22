package nl.coralic.beta.sms.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
/**
 * @author "Armin Čoralić"
 */
public class SmsTextCounter implements TextWatcher
{
	private TextView txtTextCount;

	/**
	 * Counts the letters that are typed in the SMS text field
	 * @param txtTextCount
	 */
	public SmsTextCounter(TextView txtTextCount)
	{
		this.txtTextCount = txtTextCount;
	}

	@Override
	public void afterTextChanged(Editable s)
	{
		// Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{
		// Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		txtTextCount.setText("" + s.toString().length());
	}

}
