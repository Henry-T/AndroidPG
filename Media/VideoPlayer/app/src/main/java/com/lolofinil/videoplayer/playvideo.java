package com.lolofinil.videoplayer;

import java.io.IOException;

import com.lolofinil.videoplayer.VideoView.OnFinishListener;

//import android.content.Context;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;

import android.util.Log;
//import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

//import android.widget.RelativeLayout;

public class playvideo implements OnFinishListener {
	ViewGroup viewgroup = null;
	private static playvideo pv = null;
	private static Activity instance = null;
	VideoView videoView;
	boolean isvideofinished = false;
	//跳过按钮的view
	private View layout = null;
	//跳过按钮
	private Button skipbtn = null;
	
	private int luaCallbackFunctionID;
	
	public static playvideo shareInstance() {
		if(null == pv) {
			pv = new playvideo();
		}
		
		return pv;
	}
	
	public void ShowVideo(String name) {
		System.out.println("Android ShowVideo 111111");
		if (null == instance) {
			return;
		}
		
		Log.i("", "name=" + name);
		
		videoView = new VideoView(instance);
		videoView.setOnFinishListener(this);

		Window window = instance.getWindow();
		if (window == null)
			Log.e("test", "window is null");

		viewgroup = (ViewGroup)window.getDecorView();

		if (viewgroup == null)
			Log.e("test", "videogroup is null");

		try {
			AssetFileDescriptor afd = instance.getAssets().openFd(name);
			videoView.setVideo(afd);
		} catch (IOException e) {
			e.printStackTrace();
			this.onVideoFinish();
		}
		viewgroup.addView(videoView);
		videoView.setZOrderMediaOverlay(true);
		System.out.println("Android ShowVideo 222222");
	}
	
	public void ShowVideoWithCallback(String name, int luaFunctionId) {
		this.luaCallbackFunctionID = luaFunctionId;
		System.out.println("Android ShowVideo 111111");
		if (null == instance) {
			return;
		}
		
		Log.i("", "name=" + name);
		
		videoView = new VideoView(instance);
		videoView.setOnFinishListener(this);
		
		viewgroup = (ViewGroup)instance.getWindow().getDecorView();
		try {
			AssetFileDescriptor afd = instance.getAssets().openFd(name);
			videoView.setVideo(afd);
		} catch (IOException e) {
			e.printStackTrace();
			this.onVideoFinish();
		}
		viewgroup.addView(videoView);
		videoView.setZOrderMediaOverlay(true);
		System.out.println("Android ShowVideo 222222");
		
//		//加一个新的界面
//		Context mContext = instance.getContext(); //Projects.getContext();
//		//LAYOUT_INFLATER_SERVICE表示从xml文件中加载布局 
//		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		layout = inflater.inflate(R.layout.video, null);
//		//添加一层
//		viewgroup.addView(layout);
//		
//		//找到对应的按钮
//		skipbtn = (Button)layout.findViewById(R.id.skipbutton);
//		skipbtn.setOnClickListener(listener);
//		//用相对布局定义控件的位置
//		int width = instance.getWindowManager().getDefaultDisplay().getWidth(); 
//		int height = instance.getWindowManager().getDefaultDisplay().getHeight();
//		System.out.println("width: "+width+"height: "+height);
//		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)skipbtn.getLayoutParams();
//		//left, top, right, bottom
//		params.setMargins(width*5/6, 10, 0, 0);//通过自定义坐标来放置你的控件
//		skipbtn.setLayoutParams(params);
		
		//动态创建吧，因为上面的R.java在游行运行时掉不到，避免写死R.java中用到的控件ID，所以按钮动态创建最方便
 		int width = instance.getWindowManager().getDefaultDisplay().getWidth();
 		LinearLayout linearLayout = new LinearLayout(instance);
 		viewgroup.addView(linearLayout);
		
 		skipbtn = new Button(instance);
		//skipbtn.setText("跳过动画");
 		skipbtn.setBackgroundResource(R.drawable.txt_skip);
 		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams (82,24);
 		params.setMargins(width-92, 10, 0, 0);
 		linearLayout.addView(skipbtn,params);
		
 		skipbtn.setOnClickListener(new Button.OnClickListener() {
 		     @Override
 		         public void onClick(View v) {
 		    	 	System.out.println("Android Native Button clicked 8888888888");
 		    	 	skipbtn.setVisibility(View.INVISIBLE);
 		    	 	skipVideo();
 		        }
 		      });
	}
	
	public static void playVideo(final String name) {
		if (instance != null ) {
			instance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					playvideo.shareInstance().ShowVideo(name);
				}
			});
		}
	}
	
	public static void playVideoWithCallback(final String name, final int luaFunctionId) {
		if (instance != null ) {
			instance.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					playvideo.shareInstance().ShowVideoWithCallback(name, luaFunctionId);
				}
			});
		}
	}
	
	public static boolean isVideoFinished() {
		//System.out.println("Android isVideoFinished");
		return playvideo.shareInstance().isvideofinished;
	}
	
	public static void skipVideo() {
		playvideo.shareInstance().videoView.stop();
		System.out.println("Android skipVideo 8888888888");
	}

	@Override
	public void onVideoFinish() {
		if(skipbtn != null){
			skipbtn.setVisibility(View.INVISIBLE);
		}
		viewgroup.removeView(videoView);
		viewgroup.removeView(layout);
		videoView = null;
		layout = null;
		isvideofinished = true;
		System.out.println("Android onVideoFinish");

	}
	
	public static void SetActivity(Activity ptActivity) {
		instance = ptActivity;
	}
}
