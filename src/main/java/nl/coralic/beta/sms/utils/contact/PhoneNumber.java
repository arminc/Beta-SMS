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

/**
 * @author "Armin Čoralić"
 */
public class PhoneNumber
{
	private String phoneNumberType = ApplicationContextHelper.getStringUsingR_ID(R.string.PHONE_TYPE_MOBILE);
	private String phoneNumber = "";

	public String getPhoneNumberType()
	{
		return phoneNumberType;
	}

	public void setPhoneNumberType(String phoneNumberType)
	{
		this.phoneNumberType = phoneNumberType;
	}

	public String getPhoneNumber()
	{
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneNumberClean()
	{
		return phoneNumber.replace("-", "").replace("+", "00");
	}
	
	public String getLabel()
	{
		return this.phoneNumberType + ": " + this.getPhoneNumber();
	}
}
