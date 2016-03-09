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
            	intent.setData(Uri.parse("https://gist.githubusercontent.com/anonymous/af6f81fe6b3ea0ee0f9a/raw/406a92b48350ebbbcd6878e4f66bfc915a8d6459/gistfile1.txt"));
            	// intent.setData(Uri.parse("gamecenter://appdetails?packageName=com.apowo.projects.ali"));
            	mainActivity.startActivity(intent);
            }
        });


	}

}
