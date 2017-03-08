#include "inc/fmod.hpp"
#include <stdlib.h>
#include <unistd.h>
#include "com_dd_voice_EffectUtils.h"

#define MODE_NORMAL 0
#define MODE_LUOLI 1
#define MODE_DASHU 2
#define MODE_JINGSONG 3
#define MODE_GAOGUAI 4
#define	MODE_KONGLING 5

using namespace FMOD;

// 以下只是调用Fmod 内置dsp简单的改变声音, 还可以自定义, 参考示例dsp_custom.cpp
JNIEXPORT void JNICALL Java_com_dd_voice_EffectUtils_fix
  (JNIEnv * env, jclass jcls, jstring jstr_path, jint type){
	//FMOD::System *system;
	System *system;
	Sound *sound;
	Channel *channel;
	DSP *dsp;
	bool playing = true;
	float frequency = 0;

	System_Create(&system);
	// 初始化  (最大通道数, 一个常量, ..)
	system->init(32, FMOD_INIT_NORMAL, NULL);
	// 创建声音
	const char* path_cstr = env->GetStringUTFChars(jstr_path, NULL);
	system->createSound(path_cstr, FMOD_DEFAULT, NULL, &sound);

	switch(type){
	case MODE_NORMAL:
		// 原声播放
		system->playSound(sound, 0, false, &channel);
		break;
	case MODE_LUOLI:
		// 萝莉
		// 提升/降低音效
		system->createDSPByType(FMOD_DSP_TYPE_PITCHSHIFT, &dsp);
		// 升高一个八度
		dsp->setParameterFloat(FMOD_DSP_TYPE_PITCHSHIFT, 2.0f);
		// 播放
		system->playSound(sound, 0, false, &channel);
		// 第一个参数 表示添加音效的顺序, 可以同时添加多个
		channel->addDSP(0, dsp);
		break;
	case MODE_DASHU:
		// 大叔
		// 提升/降低音效
		system->createDSPByType(FMOD_DSP_TYPE_PITCHSHIFT, &dsp);
		// 降低八度
		dsp->setParameterFloat(FMOD_DSP_TYPE_PITCHSHIFT, 0.8f);
		// 播放
		system->playSound(sound, 0, false, &channel);
		// 第一个参数 表示添加音效的顺序, 可以同时添加多个
		channel->addDSP(0, dsp);
		break;
	case MODE_JINGSONG:
		// 惊悚
		system->createDSPByType(FMOD_DSP_TYPE_TREMOLO, &dsp);
		dsp->setParameterFloat(FMOD_DSP_TREMOLO_SKEW, 0.5);
		system->playSound(sound, 0, false, &channel);
		channel->addDSP(0, dsp);
		break;
	case MODE_GAOGUAI:
		// 搞怪
		system->playSound(sound, 0, false, &channel);
		// 改变速度
		channel->getFrequency(&frequency);
		frequency = frequency * 1.6;
		channel->setFrequency(frequency);
		break;
	case MODE_KONGLING:
		// 空灵
		system->createDSPByType(FMOD_DSP_TYPE_ECHO, &dsp);
		dsp->setParameterFloat(FMOD_DSP_ECHO_DELAY, 300);
		dsp->setParameterFloat(FMOD_DSP_ECHO_FEEDBACK, 200);
		system->playSound(sound, 0, false, &channel);
		channel->addDSP(0, dsp);

		break;
	default:
		break;
	}

	system->update();
	// 释放资源   判断是否播放完毕, 播放完毕 释放资源
	while(playing){
		channel->isPlaying(&playing);
		// 每秒判断下是否在播放
		// 单位 微秒
		usleep(1*1000*1000);
	}
	env->ReleaseStringUTFChars(jstr_path, path_cstr);

	sound->release();
	system->close();
	system->release();
}
