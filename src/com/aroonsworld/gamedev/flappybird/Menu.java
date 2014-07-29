package com.aroonsworld.gamedev.flappybird;

import com.facebook.android.Facebook;
import com.facebook.widget.FacebookDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class Menu extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		//int value = getIntent().getExtras().getInt("highScore");
		//System.out.println("aaaaaaaaaaaaaa: "+value);
		Button play = (Button)findViewById(R.id.play);
		Button score = (Button)findViewById(R.id.score);
		//Button fb =(Button)findViewById(R.id.fb);
		Button share =(Button)findViewById(R.id.share);
		
		play.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent playIntent = new Intent("com.aroonsworld.gamedev.flappybird.SURFACEVIEWEXAMPLE");
				startActivity(playIntent);
			}
		});
		
		share.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				
				Intent shareIntent=new Intent(Intent.ACTION_SEND);
			    shareIntent.setType("text/plain");
			    shareIntent.putExtra(Intent.EXTRA_TEXT, "Play 'Super Flappy Chicken' on Android"+
			    		"\nwww.google.com");
			    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "");
			    startActivity(Intent.createChooser(shareIntent, "Share Score"));
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
}
