# Android-Fmod

必备:
inc 目录下所有文件
fmod.jar
libfmod.so
libfmodL.so
注意, 由于 libfmod 和 libfmodL 是预编译文件, 放在 jni 内部, 还需要在 Android.mk 中配置 

说明:
版本1: 
使用到 play_sound.cpp / common_platform.cpp / common.cpp
让程序运行起来, 播放一下声音

版本2:
使用到 effects.cpp / common_platform.cpp / common.cpp
试了一下fmod的特效

版本3:
使用到 effect_fix.cpp 
自定义音效, 了解了 fmod 的主要流程

大致流程如下:
1. 创建声音 system->createSound()
2. 播放声音 system->playSound()
3. 添加效果 system->createDSPByType()
           mastergroup->addDSP()