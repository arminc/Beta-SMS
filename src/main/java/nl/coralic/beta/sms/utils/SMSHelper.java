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
package nl.coralic.beta.sms.utils;

import nl.coralic.beta.sms.utils.objects.Const;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;

/**
 * @author "Armin Čoralić"
 */
public class SMSHelper
{ 
	/**
	 * Adds SMS to the phones SMS database
	 * @param cr
	 * @param body
	 * @param phonenumber
	 */
	public void addSMS(ContentResolver cr, String body, String phonenumber)
	{
	 ContentValues values = new ContentValues();
     values.put(Const.ADDRESS, phonenumber);
     values.put(Const.DATE, getDate());
     values.put(Const.READ, 1);
     values.put(Const.STATUS, -1);
     values.put(Const.TYPE, 2);
     values.put(Const.BODY, body);
     cr.insert(Uri.parse("content://sms"), values);
	}
    
	private String getDate()
	{
		return String.valueOf(System.currentTimeMillis());
	}
}
