package com.dd.voice;

public class EffectUtils {
	
	//音效的类型
	public static final int MODE_NORMAL = 0;
	public static final int MODE_LUOLI = 1;
	public static final int MODE_DASHU = 2;
	public static final int MODE_JINGSONG = 3;
	public static final int MODE_GAOGUAI = 4;
	public static final int MODE_KONGLING = 5;
	
	/**
	 * 音效处理
	 * @param path 声音文件路径
	 * @param type 处理类型
	 */
	public native static void fix(String path, int type);
	
	static{
		System.loadLibrary("fmodL");
		System.loadLibrary("fmod");
		System.loadLibrary("qq_voicer");
	}
}
