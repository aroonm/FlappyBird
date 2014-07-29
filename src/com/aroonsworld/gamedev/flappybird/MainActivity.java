package com.aroonsworld.gamedev.flappybird;


import com.facebook.android.Facebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash);
		Thread logoTimer = new Thread(){
			public void run(){
				try{
					sleep(5000);
					Intent menuIntent = new Intent("com.aroonsworld.gamedev.flappybird.MENU");
					startActivity(menuIntent);
					MainActivity.this.finish();
				}catch(InterruptedException e){
					e.printStackTrace();
				}finally {
					finish();
				}
			}
		};
		logoTimer.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
