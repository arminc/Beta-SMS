/**
 * 
 */
package nl.coralic.beta.sms.utils.contact;

import nl.coralic.beta.sms.utils.constants.Const;

/**
 * @author "Armin Čoralić"
 */
public class PhoneNumber
{
	private String phoneNumberType = Const.PHONE_TYPE_MOBILE;
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
		return phoneNumber.replace("-", "");
	}
	
	public String getLabel()
	{
		return this.phoneNumberType + ": " + this.getPhoneNumber();
	}
}
