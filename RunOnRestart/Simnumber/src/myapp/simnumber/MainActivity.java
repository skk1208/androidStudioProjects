package myapp.simnumber;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MainActivity extends BroadcastReceiver {
	@Override
	public void onReceive(Context c, Intent i)
		{
		
			Intent a=new Intent(c,Sendmsg.class);
			a.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			c.startActivity(a);			
		}
	}
	



