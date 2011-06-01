package nl.coralic.beta.sms.utils.objects;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class BetamaxResponse extends Response
{

    public BetamaxResponse(boolean responseOke, String response)
    {
	super(responseOke, response);
    }

    public void validateBetamaxResponse()
    {
	try
	{
	    Document doc = getDocument(response.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?>", ""));
	    Element root = doc.getDocumentElement();
	    root.normalize();
	    if (!root.getElementsByTagName("resultstring").item(0).getFirstChild().getNodeValue().equalsIgnoreCase("success"))
	    {
		String tmpCause = root.getElementsByTagName("description").item(0).getFirstChild().getNodeValue();

		if (tmpCause.equalsIgnoreCase(""))
		{
		    errorMessage = "Could not read response";
		}
		else
		{
		    errorMessage = tmpCause;
		}
	    }
	}
	catch (Exception e)
	{
	    responseOke = false;
	    errorMessage = "Could not read response";
	}
    }

    private Document getDocument(String data) throws Exception
    {
	Document doc = null;
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	DocumentBuilder builder = factory.newDocumentBuilder();
	ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data.getBytes());
	doc = builder.parse(byteArrayInputStream);
	return doc;
    }
}
