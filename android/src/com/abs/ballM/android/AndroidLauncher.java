package com.abs.ballM.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.abs.ballM.MyGame;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import com.google.android.gms.ads.AdListener;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


public class AndroidLauncher extends AndroidApplication implements MyGame.RequestHandler {
	InterstitialAd interstitial;
	AdRequest.Builder adRequestBuilder;
	View gameView;



	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.r = 8;
		config.g = 8;
		config.b = 8;
		config.a = 8;
		gameView = initializeForView(new MyGame(this), config);




		layout.addView(gameView);
		setContentView(layout);

	}


	@Override
	public void confirm(final MyGame.ConfirmInterface confirmInterface) {
		Log.e("bitch", "android");
		gameView.post(new Runnable() {
			public void run() {
				new AlertDialog.Builder(AndroidLauncher.this)
						.setTitle("Confirm")
						.setMessage("you really want to quit?")
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								confirmInterface.yes();
								dialog.cancel();
							}
						})
						.setNegativeButton("No", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						})
						.create().show();
			}
		});
	}

	@Override
	public void loadAds(){

		gameView.post(new Runnable() {
			public void run() {
				interstitial = new InterstitialAd(getApplicationContext());
				interstitial.setAdUnitId("ca-app-pub-6798653878803807/3584243573");

				adRequestBuilder = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR);


				interstitial.loadAd(adRequestBuilder.build());
				interstitial.setAdListener(new AdListener() {
					@Override
					public void onAdClosed() {
						AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
						interstitial.loadAd(adRequest);
					}
				});
			}
		});
	}

	@Override
	public void share(){
		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Try cool game - balls 2048 https://play.google.com/store/apps/details?id=com.abs.ballM.android"); // todo ADD LINK
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
		startActivity(Intent.createChooser(sharingIntent, "Share using"));
	}

	@Override
	public void showAds(){

		gameView.post(new Runnable() {
			public void run() {
				interstitial.show();
			}
		});

	}
}
