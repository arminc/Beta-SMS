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
		//Not needed
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{
		//Not needed
	}

	//Calculates until 5 messages
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		int sms = 0;
		if(s.toString().length() > 640)
		{
			sms = 5;
		}
		else if(s.toString().length() > 480)
		{
			sms = 4;
		}
		else if(s.toString().length() > 320)
		{
			sms = 3;
		}
		else if(s.toString().length() > 160)
		{
			sms = 2;
		}
		else
		{
			sms = 1;
		}
		txtTextCount.setText(sms + "/" + s.toString().length());
	}

}
