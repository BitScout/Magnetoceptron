package com.example.kompasstest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {

	private SensorManager mSensorManager;
	private float mAccel; // acceleration apart from gravity
	private float mAccelCurrent; // current acceleration including gravity
	private float mAccelLast; // last acceleration including gravity

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensorManager.registerListener(mSensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_NORMAL);
		mAccel = 0.00f;
		mAccelCurrent = SensorManager.GRAVITY_EARTH;
		mAccelLast = SensorManager.GRAVITY_EARTH;

	}

	private final SensorEventListener mSensorListener = new SensorEventListener() {

		private int counter = 0;
		private float avg = 0;

		public void onSensorChanged(SensorEvent event) {
			float azimuth_angle = event.values[0];
			float pitch_angle = event.values[1];
			float roll_angle = event.values[2];
			// Do something with these orientation angles.

			Context context = getApplicationContext();
			CharSequence text = "A " + azimuth_angle + " - P " + pitch_angle
					+ " - R " + roll_angle;
			int duration = Toast.LENGTH_SHORT;

			this.avg += azimuth_angle;
			
			int summierung = 200;
			
			this.counter++;
			if (this.counter >= summierung) {
				this.counter = 0;
				Log.i("Test", "Durchschnitt: " + (azimuth_angle));
				
				//Log.i("Test", "PITCH: "+p);
				// Log.i("Test", "ROLL: "+r);
				int richtzahl = Math.round(this.avg/summierung);
				
				String richtung = "S";
				if(richtzahl > 24) richtung = "W";
				if(richtzahl <= 8) richtung = "N";
				if(richtzahl > 8 && richtzahl <= 16) richtung = "O";
				
				
				//Toast toast = Toast.makeText(context, "Durchschnitt: " + Math.round(this.avg/summierung), duration);
				Toast toast = Toast.makeText(context, "Richtung: " + richtung, duration);
				toast.show();

				this.avg = 0;
			}
		}

		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float azimuth_angle = event.values[0];
		float pitch_angle = event.values[1];
		float roll_angle = event.values[2];
		// Do something with these orientation angles.

		Context context = getApplicationContext();
		CharSequence text = "Hello toast!";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(mSensorListener,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		mSensorManager.unregisterListener(mSensorListener);
		super.onPause();
	}
}
