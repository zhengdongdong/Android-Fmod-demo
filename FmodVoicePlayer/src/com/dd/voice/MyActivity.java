package com.dd.voice;

import java.io.File;

import org.fmod.FMOD;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

public class MyActivity extends Activity{

	// 先写好 EffectUtils.java  进入工程路径 src下, 使用"javah com.dd.voice.EffectUtils" 生成头文件
	// 配置好mk文件后, 开始写effect_fix.cpp
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FMOD.init(this);
		setContentView(R.layout.activity_main);	
		
	}
	
	public void mFix(View btn){
		String path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator + "a.wav";
		// 改变 EffectUtils 的 mode实现变声
		EffectUtils.fix(path, EffectUtils.MODE_NORMAL);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		FMOD.close();
	}
	
}
