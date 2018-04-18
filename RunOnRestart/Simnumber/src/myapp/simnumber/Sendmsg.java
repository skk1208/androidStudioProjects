package myapp.simnumber;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

public class Sendmsg extends Activity {

	SharedPreferences sp=null;
	SharedPreferences.Editor edit;
	TelephonyManager telemananger=null;
	TelephonyManager telemanager2=null;
	String anothervar=null;
	String var=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendms);
		telemananger = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		 anothervar = telemananger.getSimSerialNumber();
		 
		sp=getSharedPreferences("share1",Activity.MODE_APPEND);
		edit=sp.edit();
		
		var=sp.getString("sim_no",null);
		if(var==null)
		{
		
		sp.edit().putString("sim_no",anothervar).commit();
		var=sp.getString("sim_no",null);
		TextView tv=(TextView)findViewById(R.id.textView2);
		tv.setText(String.valueOf(anothervar));
		}
		else
		Toast.makeText(this, "hi..", Toast.LENGTH_LONG).show();
		TextView tv=(TextView)findViewById(R.id.textView4);
		tv.setText(String.valueOf("this is the value of shared prefernces....... ="+var));
		
		}	
		
	}
