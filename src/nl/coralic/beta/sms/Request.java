package nl.coralic.beta.sms;

import nl.coralic.beta.sms.utils.LogCollector;
import nl.coralic.beta.sms.utils.SendHandler;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * @author "Armin Čoralić"
 */
public class Request extends Activity
{
	Button send;
	EditText txtEmailText;
	EditText txtRequestText;
	CheckBox chkAttachLog;
	String debug = "";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.betasmsrequest);

		txtEmailText = (EditText) findViewById(R.id.txtEmailText);
		txtRequestText = (EditText) findViewById(R.id.txtRequestText);
		chkAttachLog = (CheckBox) findViewById(R.id.chkAttachLog);

		send = (Button) findViewById(R.id.btnSend);

		send.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v)
			{
				if (chkAttachLog.isChecked())
				{
					LogCollector logc = new LogCollector(Request.this);
					logc.collect();
					String LINE_SEPARATOR = System.getProperty("line.separator");
					StringBuilder sb = new StringBuilder("TESTING").append(LINE_SEPARATOR);
					String phoneInfo = logc.collectPhoneInfo();
					sb.append(LINE_SEPARATOR).append(phoneInfo);
					for (String line : logc.mLastLogs)
						sb.append(LINE_SEPARATOR).append(line);
					debug = sb.toString();
				}
				SendHandler sh = new SendHandler();
				sh.sendFeatureRequest(Request.this, txtRequestText.getText().toString(), debug, txtEmailText.getText().toString());
			}
		});
	}
}
