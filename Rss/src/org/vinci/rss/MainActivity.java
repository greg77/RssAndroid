package org.vinci.rss;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class MainActivity extends Activity {
	private static final int STOPSPLASH = 0;
	private static final long SPLASHTIME = 5000;
	
	private Handler splashHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case STOPSPLASH:
				Intent intentAbonnement = new Intent(MainActivity.this, AbonnementActivity.class);
				startActivityForResult(intentAbonnement,10);
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	Message msg = new Message();
	msg.what = STOPSPLASH;
	splashHandler.sendMessageDelayed(msg, SPLASHTIME);
	}


}
