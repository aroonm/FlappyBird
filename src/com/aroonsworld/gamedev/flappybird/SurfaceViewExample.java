package com.aroonsworld.gamedev.flappybird;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class SurfaceViewExample extends Activity implements OnTouchListener{

	OurView v;
	int score  = 0;
	int highScore=0;
	boolean gameOver = false;
	int xpipe = 75;
	public static Rect up = new Rect(0, 0, 0, 0);
	public static Rect down = new Rect(0, 0, 0, 0);
	public static Rect bird = new Rect(0, 0, 0, 0);
	public static Rect gnd = new Rect(0,0,0,0);
	public static Bitmap background, ball, ground, pipem, result;
	float x,y, bkdx=1;
	Pipe p;
	int bkx;
	Player player;
	ArrayList<Pipe> pipe = new ArrayList<Pipe>();
	Pipe pObj;
	Typeface font1, font2;
	Paint paint, paint1, paint2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		v = new OurView(this);
		v.setOnTouchListener(this);
		font1 = Typeface.createFromAsset(getAssets(), "VILLA.TTF");
		ball = BitmapFactory.decodeResource(getResources(), R.drawable.rubato);
		background = BitmapFactory.decodeResource(getResources(), R.drawable.flappy_bg);
		ground = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
		pipem = BitmapFactory.decodeResource(getResources(), R.drawable.tube_mid);
		result = BitmapFactory.decodeResource(getResources(), R.drawable.scor);
		x = 50; y = 100;
		player = new Player();
		Random r=new Random();
		for(int i=2;i<=500;i++)
		{
			pObj = new Pipe(i*(250), r.nextInt(200)+120);
			pipe.add(pObj);
		}
		paint = new Paint();
		paint1 = new Paint();
		paint2 = new Paint();
		setContentView(v);
		loadPrefs();
	}
	private void loadPrefs() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		String loadedscore = sp.getString("HIGHSCORE", highScore+"");
		highScore = Integer.parseInt(loadedscore);
	}
	private void savePrefs(String key, String value) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		v.pause();
	}
	@Override
	protected void onResume() {
		super.onResume();
		v.resume();
	}
	@Override
	public void onBackPressed() {
		v.pause();
		AlertDialog.Builder builder = new AlertDialog.Builder(SurfaceViewExample.this);
		builder.setMessage("Head back to Menu Screen?")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								v.resume();
								SurfaceViewExample.this.finish();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						v.resume();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
	public class OurView extends SurfaceView implements Runnable{

		Thread t=null;
		SurfaceHolder holder;
		boolean isItOK = false;
		
		public OurView(Context context) {
			super(context);
			holder = getHolder();
		}

		@Override
		public void run() {
			while(isItOK == true) {
				if(!holder.getSurface().isValid()){
					continue;
				}
				Canvas c = holder.lockCanvas();
				starting();
				doPaint(c);
				holder.unlockCanvasAndPost(c);
			}
		}
		private void starting() {
			player.update();
			for(int i=0;i<pipe.size();i++){
				p = pipe.get(i);
				p.update();
				if(p.getX() < -(pipem.getWidth())){
					//pipe.remove(p);
				}
				
				int top = p.getY()-pipem.getHeight()/3;
				up.set(p.getX()+17, top, p.getX()+pipem.getWidth()-5, (top+(pipem.getHeight()/2-69))); // l,t,r,b
				down.set(p.getX()+17, (top+(pipem.getHeight()/2+69)), p.getX()+pipem.getWidth()-5, getHeight()-ground.getHeight());
				bird.set(player.getCenterX()+25, player.getCenterY()+25, player.getCenterX()+75, player.getCenterY()+75);
				gnd.set(0, getHeight()-ground.getHeight(), getWidth(), getHeight());
				
				if((p.getX()+pipem.getWidth())==(player.getCenterX()+40)){
					if(gameOver == false)
						score+=1;
				}
				if(up.intersect(bird) || down.intersect(bird) || gnd.intersect(bird)){
					gameOver = true;
					
				}
			}
			if(score > highScore) {
				highScore = score;
				savePrefs("HIGHSCORE", highScore+"");
			}
		}

		private void doPaint(Canvas c) {
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Align.CENTER);
			paint.setTextSize(60);
			paint.setTypeface(font1);
			c.drawARGB(255, 34, 136, 220);
			c.drawBitmap(background, 0, 100, null);
			if (bkx > c.getWidth() * -1) {
				bkx -= bkdx;
			} else {
				bkx = 0;
			}
			for(int j=0;j<pipe.size();j++){
				p = pipe.get(j);
				c.drawBitmap(pipem, p.getX(), p.getY()- pipem.getHeight()/3, null);
			}
			c.drawBitmap(ground, bkx, (getHeight()-ground.getHeight()), null);
			c.drawBitmap(ground, bkx+ground.getWidth() , (getHeight()-ground.getHeight()), null);
			c.drawBitmap(ball, player.getCenterX(), player.getCenterY(), null);
			if(gameOver==false)
				c.drawText(score+"", background.getWidth()/2, 170, paint);
			paint2.setTextSize(36);
			paint2.setTypeface(font1);
			paint1.setARGB(220, 241, 214, 85);
			if(gameOver == true) {
				c.drawRect(background.getWidth()/2-200,background.getHeight()/2-30,background.getWidth()/2+200,background.getHeight()/2+200, paint1);
				c.drawText("GAME OVER", background.getWidth()/2, 350, paint);
				
				c.drawText("SCORE", background.getWidth()/2-180, 450, paint2);
				c.drawText(score+"", background.getWidth()/2+100, 450, paint2);
				
				c.drawText("HIGH SCORE", background.getWidth()/2-180, 550, paint2);
				c.drawText(highScore+"", background.getWidth()/2+100, 550, paint2);
				
				
				
				
				
				
				
				/*
				Intent shareIntent=new Intent(Intent.ACTION_SEND);
			    shareIntent.setType("text/plain");
			    shareIntent.putExtra(Intent.EXTRA_TEXT, "www.google.com?extraText=\n\naaaaa");
			    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "");
			    startActivity(Intent.createChooser(shareIntent, "Share Score"));*/
				
				
				
			}
			
		}

		public void pause() {
			isItOK=false;
			while(true){
				try{
					t.join();
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				break;
			}
			t=null;
		}
		public void resume() {
			isItOK=true;
			t = new Thread(this);
			t.start();
		}
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(gameOver == false)
				player.jump();
			break;
			
		}
		
		return false;
	}
	
	
	
}
