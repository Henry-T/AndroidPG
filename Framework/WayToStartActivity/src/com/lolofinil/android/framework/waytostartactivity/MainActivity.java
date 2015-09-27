package com.lolofinil.android.framework.waytostartactivity;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	
	protected MainActivity mainActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mainActivity = this;
		
		
		final Button button = (Button) findViewById(R.id.btnYunOSHYSG);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent intent = new Intent(Intent.ACTION_VIEW);
            	intent.setData(Uri.parse("gamecenter://appdetails?packageName=com.apowo.projects.ali"));
            	mainActivity.startActivity(intent);
            }
        });
	}

}
