package br.exemplogcm;

import java.io.IOException;

import br.exemplogcm.util.AndroidSystemUtil;
import br.exemplogcm.util.HttpConnectionUtil;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public static final String TAG = "Script";
	
	private String SENDER_ID = "";
	private String regId;
	private GoogleCloudMessaging gcm;
	private TextView tvRegistrationId;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tvRegistrationId = (TextView) findViewById(R.id.tvRegistrationId);
		
		if(checkPlayServices()){
			gcm = GoogleCloudMessaging.getInstance(MainActivity.this);
			regId = AndroidSystemUtil.getRegistrationId(MainActivity.this);
			
			if(regId.trim().length() == 0){
				registerIdInBackground();
			}
		}
	}
	
	
	@Override
	public void onResume(){
		super.onResume();
		checkPlayServices();
	}
	
	
	
	// UTIL
		public boolean checkPlayServices(){
			int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(MainActivity.this);
			
			if(resultCode != ConnectionResult.SUCCESS){
				if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
					GooglePlayServicesUtil.getErrorDialog(resultCode, MainActivity.this, PLAY_SERVICES_RESOLUTION_REQUEST);
				}
				else{
					Toast.makeText(MainActivity.this, "PlayServices sem suporte", Toast.LENGTH_SHORT).show();
					finish();
				}
				return(false);
			}
			return(true);
		}
		
		
		public void registerIdInBackground(){
			new AsyncTask(){
				@Override
				protected Object doInBackground(Object... params) {
					String msg = "";
					
					try{
						if(gcm == null){
							gcm = GoogleCloudMessaging.getInstance(MainActivity.this);
						}
						
						regId = gcm.register(SENDER_ID);
						
						msg = "Register Id: "+regId;
						
						String feedback = HttpConnectionUtil.sendRegistrationIdToBackend(regId);
						Log.i(TAG, feedback);
						
						AndroidSystemUtil.storeRegistrationId(MainActivity.this, regId);
					}
					catch(IOException e){
						Log.i(TAG, e.getMessage());
					}
					
					return msg;
				}
				
				@Override
				public void onPostExecute(Object msg){
					tvRegistrationId.setText((String)msg);
				}
				
			}.execute(null, null, null);
		}
}
