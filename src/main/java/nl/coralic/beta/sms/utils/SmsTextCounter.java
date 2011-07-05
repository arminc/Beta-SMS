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
    private final int divider = 160;

    /**
     * Counts the letters that are typed in the SMS text field
     * 
     * @param txtTextCount
     */
    public SmsTextCounter(TextView txtTextCount)
    {
	this.txtTextCount = txtTextCount;
    }

    public void afterTextChanged(Editable s)
    {
	// Not needed
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
	// Not needed
    }

    // Calculates until 5 messages
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
	int smsChars = s.toString().length();
	if (smsChars >=150)
	{
	    txtTextCount.setVisibility(TextView.VISIBLE);
	    // how many sms fit in this text
	    int smsCounter = smsChars / divider;
	    int fullSmsChars = smsCounter * divider;
	    int restNumber = smsChars - fullSmsChars;
	    if (restNumber != 0)
	    {
		smsCounter++;
	    }
	    if(smsCounter == 1 && restNumber != 0)
	    {
		txtTextCount.setText(String.valueOf(divider - restNumber));
	    }
	    else if(restNumber == 0)
	    {
		txtTextCount.setText(restNumber + "/" + smsCounter);
	    }
	    else
	    {
		txtTextCount.setText(divider - restNumber + "/" + smsCounter);
	    }
	}
	else
	{
	    txtTextCount.setVisibility(TextView.INVISIBLE);
	}
    }

}
