LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := fmod
LOCAL_SRC_FILES := libfmod.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := fmodL
LOCAL_SRC_FILES := libfmodL.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE    := qq_voicer
LOCAL_SRC_FILES := play_sound.cpp common_platform.cpp common.cpp
LOCAL_SHARED_LIBRARIES := fmod fmodL
LOCAL_LDLIBS := -llog
include $(BUILD_SHARED_LIBRARY)
