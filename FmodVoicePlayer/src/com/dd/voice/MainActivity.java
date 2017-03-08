package com.dd.voice;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnTouchListener, Runnable {

	// 使用fmod实现变声
	
	// fmod api 有两套, 一套全的, 一套部分功能(low), 这里实现使用简版
	
	// 1. 引入 fmod.jar, so文件(这里的so文件是预编译的, 放在jni内, mk文件加入配置), 头文件(FMod 没有提供源代码, 使用的话通过头文件函数操作)等
	// 2. 根据 common_platform.cpp 中的 JNI 方法写 java 方法
	// 3. 添加 Native支持. 配置Android.mk
	// 4. bulid, 哪里错误改哪里(HPP 代表里面既有函数声明, 又有函数实现)
	//    这里还用到了C++标准模板库(STL), 需要额外配置, 添加Application.mk
	//    common_platform.cpp中包名也需要修改
	
	
	// 大致查看了一下音效代码, 流程如下:
	// 1. 创建声音 system->createSound()
	// 2. 播放声音 system->playSound()
	// 3. 添加效果 system->createDSPByType()
	//            mastergroup->addDSP()
	
	private TextView mTxtScreen;
	private Thread mThread;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);

    	// Create the text area
    	mTxtScreen = new TextView(this);
    	mTxtScreen.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10.0f);
    	mTxtScreen.setTypeface(Typeface.MONOSPACE);

        // Create the buttons
        Button[] buttons = new Button[9];
        for (int i = 0; i < buttons.length; i++)
        {
        	buttons[i] = new Button(this);
        	buttons[i].setText(getButtonLabel(i));
        	buttons[i].setOnTouchListener(this);
        	buttons[i].setId(i);
        }
        
        // Create the button row layouts
        LinearLayout llTopRowButtons = new LinearLayout(this);
        llTopRowButtons.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout llMiddleRowButtons = new LinearLayout(this);
        llMiddleRowButtons.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout llBottomRowButtons = new LinearLayout(this);
        llBottomRowButtons.setOrientation(LinearLayout.HORIZONTAL);
        
        // Create the main view layout
        LinearLayout llView = new LinearLayout(this);
        llView.setOrientation(LinearLayout.VERTICAL);       

        // Create layout parameters
        LinearLayout.LayoutParams lpLayout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
        
        // Set up the view hierarchy
        llTopRowButtons.addView(buttons[0], lpLayout);
        llTopRowButtons.addView(buttons[6], lpLayout);
        llTopRowButtons.addView(buttons[1], lpLayout);
        llMiddleRowButtons.addView(buttons[4], lpLayout);
        llMiddleRowButtons.addView(buttons[8], lpLayout);
        llMiddleRowButtons.addView(buttons[5], lpLayout);
        llBottomRowButtons.addView(buttons[2], lpLayout);
        llBottomRowButtons.addView(buttons[7], lpLayout);
        llBottomRowButtons.addView(buttons[3], lpLayout);
        llView.addView(mTxtScreen, lpLayout);
        llView.addView(llTopRowButtons);
        llView.addView(llMiddleRowButtons);
        llView.addView(llBottomRowButtons);
        
        setContentView(llView);

        // Request necessary permissions
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//        {
//            String[] perms = { "android.permission.RECORD_AUDIO", "android.permission.WRITE_EXTERNAL_STORAGE" };
//            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED ||
//                checkSelfPermission(perms[1]) == PackageManager.PERMISSION_DENIED)
//            {
//                requestPermissions(perms, 200);
//            }
//        }

        org.fmod.FMOD.init(this);
        
        mThread = new Thread(this, "Example Main");
        mThread.start();
        
        setStateCreate();
    }
	
    @Override
    protected void onStart()
    {
    	super.onStart();
    	setStateStart();
    }
    
    @Override
    protected void onStop()
    {
    	setStateStop();
    	super.onStop();
    }
    
    @Override
    protected void onDestroy()
    {
    	setStateDestroy();
    	
    	try
    	{
    		mThread.join();
    	}
    	catch (InterruptedException e) { }
    	
    	org.fmod.FMOD.close();
    	
    	super.onDestroy();
    }
    
	@Override
	public boolean onTouch(View view, MotionEvent motionEvent)
	{
		if (motionEvent.getAction() == MotionEvent.ACTION_DOWN)
		{
			buttonDown(view.getId());	
		}
		else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
		{
			buttonUp(view.getId());	
		}			
	    
		return true;
	}

	@Override
	public void run()
	{
        main();
	}
	
	public void updateScreen(final String text)
	{
		runOnUiThread(new Runnable()
		{
	        @Override
	        public void run()
	        {
	            mTxtScreen.setText(text);
	        }
		});
	}
	
	private native String getButtonLabel(int index);
	private native void buttonDown(int index);
	private native void buttonUp(int index);
	private native void setStateCreate();
	private native void setStateStart();
	private native void setStateStop();
	private native void setStateDestroy();
	private native void main();
	
    static 
    {
    	/*
    	 * To simplify our examples we try to load all possible FMOD
    	 * libraries, the Android.mk will copy in the correct ones
    	 * for each example. For real products you would just load
    	 * 'fmod' and if you use the FMOD Studio tool you would also
    	 * load 'fmodstudio'.
    	 */

    	// Try debug libraries...
    	// 调试用的
//    	try { System.loadLibrary("fmodD");
//    		  System.loadLibrary("fmodstudioD"); }
//    	catch (UnsatisfiedLinkError e) { }
    	// Try logging libraries...
    	try { 
    		// 日志输出
    		System.loadLibrary("fmodL");
//    		   System.loadLibrary("fmodstudioL");  // 界面级别的
    		}
    	catch (UnsatisfiedLinkError e) { }
		// Try release libraries...
		try { 
			System.loadLibrary("fmod");
//		      System.loadLibrary("fmodstudio");  // 界面级别的
		}
		catch (UnsatisfiedLinkError e) { }
    	
		// 这里注释掉了
    	// System.loadLibrary("stlport_shared");
        System.loadLibrary("qq_voicer");
    }
}