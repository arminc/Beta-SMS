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

import java.util.ArrayList;

/**
 * @author "Armin Čoralić"
 *
 */
public class PhoneNumbers
{
	private ArrayList<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
	private String contactsName;
	
	/**
	 * @return the mobileNumber
	 */
	public String[] getPhoneNumbersLabelArray()
	{
		String tmpStr[] = new String[this.size()];
		for(int i=0;i<this.size();i++)
		{
			tmpStr[i] = this.phoneNumbers.get(i).getLabel();
		}
		return tmpStr;
	}
	
	/**
	 * @return the mobileNumber
	 */
	public String getCleanPhoneNumber(int id)
	{
		return this.phoneNumbers.get(id).getPhoneNumberClean();
	}
	
	/**
	 * @return the mobileNumber
	 */
	public String getPhoneNumber(int id)
	{
		return this.phoneNumbers.get(id).getPhoneNumber();
	}

	/**
	 * @param mobileNumber the mobileNumber to set
	 */
	public void addPhoneNumber(String phoneNumber, String type)
	{
		PhoneNumber pn = new PhoneNumber();
		pn.setPhoneNumber(phoneNumber);
		pn.setPhoneNumberType(type);
		this.phoneNumbers.add(pn);
	}
	
	public int size()
	{
		return this.phoneNumbers.size();
	}

	public String getContactsName() {
		return contactsName;
	}

	public void setContactsName(String contactsName) {
		this.contactsName = contactsName;
	}
	
}
