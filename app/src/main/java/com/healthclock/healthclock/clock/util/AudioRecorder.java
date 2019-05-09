/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.healthclock.healthclock.clock.util;

import android.content.Context;
import android.media.MediaRecorder;

/**
 * 录音器
 *
 * @author 咖枯
 * @version 1.0 2015/05
 */
public class AudioRecorder {

    /**
     * 录音
     */
    public MediaRecorder mRecorder;

    /**
     * 共通录音器实例
     */
    private static AudioRecorder sAudioRecorder;

    private static Context sContext;

    /**
     * 录音开启成功
     */
    public boolean mIsStarted = false;

    private AudioRecorder(Context appContext) {
        sContext = appContext;
    }

    /**
     * 取得录音器实例
     *
     * @return 录音器实例
     */
    public static AudioRecorder getInstance(Context context) {
        if (sAudioRecorder == null) {
            sAudioRecorder = new AudioRecorder(context.getApplicationContext());
        }
        return sAudioRecorder;
    }

    /**
     * 停止录音
     */
    public void stop() {
        if (mRecorder != null) {
            try {
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }

    }

//    /**
//     * 开始录音
//     *
//     * @param fileName 录音文件名
//     */
//    public void record(String fileName) {
//        stop();
//        mRecorder = new MediaRecorder();
//        // 设置音频来源（MIC表示麦克风）
//        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        // 设置音频输出格式（.amr格式推荐3gp）
//        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        // 设置音频编码方式
//        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//        // 指定音频输出文件
//        mRecorder.setOutputFile(fileName);
//        // 录音最大时长为10分钟
//        mRecorder.setMaxDuration(WeacConstants.MAX_RECORD_LENGTH);
//        try {
//            mRecorder.prepare();
//            mRecorder.start();
//        } catch (IllegalStateException | IOException e) {
//            T.showShort(sContext,
//                    sContext.getString(R.string.record_fail_confirm));
//            mIsStarted = false;
//            mRecorder = null;
//            return;
//        }
//        mIsStarted = true;
//        mRecorder.setOnErrorListener(new OnErrorListener() {
//
//            @Override
//            public void onError(MediaRecorder mr, int what, int extra) {
//                T.showShort(sContext,
//                        sContext.getString(R.string.record_fail));
//                mIsStarted = false;
//            }
//        });
//    }

}
