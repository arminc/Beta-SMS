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
