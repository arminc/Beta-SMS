package nl.coralic.beta.sms.betamax;

import static org.junit.Assert.*;

import java.util.HashMap;

import nl.coralic.beta.sms.utils.http.HttpHandler;
import nl.coralic.beta.sms.utils.objects.Response;

import org.junit.Ignore;
import org.junit.Test;


public class BetamaxProvidersTest
{
    private HashMap<String, String> arguments = new HashMap<String, String>();
    
    private String getSendSmsUrl(String provider)
    {
	return "https://"+provider+"/myaccount/sendsms.php";
    }
    
    private String getBalanceUrl(String provider)
    {
	return "https://"+provider+"/myaccount/getbalance.php";
    }
    
    @Test
    public void onetwovoipCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.12voip.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.12voip.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void actionvoipCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.actionvoip.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.actionvoip.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void calleasyCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.calleasy.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.calleasy.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void cheapvoipCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.cheapvoip.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.cheapvoip.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void dialnowCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.dialnow.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.dialnow.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Ignore
    @Test
    public void easyvoipCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.easyvoip.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.easyvoip.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Ignore
    @Test
    public void fastvoipCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.fastvoip.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.fastvoip.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void freecallCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.freecall.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.freecall.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void internetcallsCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.internetcalls.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.internetcalls.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void intervoipCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.intervoip.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.intervoip.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void jumbloCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.jumblo.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse().trim());
	response  = HttpHandler.execute(getSendSmsUrl("www.jumblo.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void justvoipCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.justvoip.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.justvoip.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void lowratevoipCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.lowratevoip.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.lowratevoip.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void netappelFR()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.netappel.fr"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.netappel.fr"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void nonohNET()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.nonoh.net"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.nonoh.net"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void poivyCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.poivy.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.poivy.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void powervoipCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.powervoip.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.powervoip.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void ryngaCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.rynga.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.rynga.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void sipdiscountCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.sipdiscount.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.sipdiscount.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void smartvoipCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.smartvoip.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.smartvoip.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void smsdiscountCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.smsdiscount.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.smsdiscount.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void smslistoCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.smslisto.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.smslisto.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void sparvoipDE()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.sparvoip.de"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.sparvoip.de"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void voipbusterCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.voipbuster.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.voipbuster.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void voipbusterproCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.voipbusterpro.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.voipbusterpro.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void voipcheapCOUK()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.voipcheap.co.uk"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.voipcheap.co.uk"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void voipcheapCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.voipcheap.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.voipcheap.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void voipdiscountCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.voipdiscount.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.voipdiscount.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void voipgainCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.voipgain.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.voipgain.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Ignore
    @Test
    public void voipianCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.voipian.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.voipian.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void voipraiderCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.voipraider.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.voipraider.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void voipstuntCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.voipstunt.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.voipstunt.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Ignore
    @Test
    public void voipwiseCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.voipwise.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.voipwise.comm"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void voipzoomCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.voipzoom.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.voipzoom.com"), arguments);
	assertTrue(response.isResponseOke());
    }
    
    @Test
    public void webcalldirectCOM()
    {
	Response response  = HttpHandler.execute(getBalanceUrl("www.webcalldirect.com"), arguments);
	assertTrue(response.isResponseOke());
	assertEquals("0", response.getResponse());
	response  = HttpHandler.execute(getSendSmsUrl("www.webcalldirect.com"), arguments);
	assertTrue(response.isResponseOke());
    }
}
