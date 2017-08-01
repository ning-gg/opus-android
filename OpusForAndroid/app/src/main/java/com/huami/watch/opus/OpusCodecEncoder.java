package com.huami.watch.opus;

/**
 * Created by gcn on 26/7/17.
 */

public class OpusCodecEncoder {

    private final static int OPUS_OK = 0;

    static {
        System.loadLibrary("libopus-jni");
    }

    public enum OPUS_APPLICATION{
        OPUS_APPLICATION_VOIP(0),OPUS_APPLICATION_AUDIO(1),OPUS_APPLICATION_RESTRICTED_LOWDELAY(2);
        private int mApplication =0;
        OPUS_APPLICATION(int application){
            this.mApplication = application;
        }
        public int getApplication(){
            return mApplication;
        }
    }

    public enum OPUS_FRAME_DURATION{
        TIME_2_HALF(2.5f),TIME_5(5),TIME_10(10),TIME_20(20),TIME_40(40),TIME_60(60);

        private float duration;
        OPUS_FRAME_DURATION(float duration){
            this.duration = duration;
        }

        public float getDuration(){
            return duration;
        }
    }

    public boolean createOpusEncoder(int simpleRate,int channel,OPUS_APPLICATION application){
        int result = opusEncodeCreate(simpleRate,channel,application.getApplication());
        if(result != OPUS_OK){
            return false;
        }
        return true;
    }

    public int getOpusEncoderSize(int channel){
        return opusEncoderGetSize(channel);
    }

    public boolean initOpusEncoder(int simpleRate,int channel,OPUS_APPLICATION application){
        int error = opusEncoderInit(simpleRate,channel,application.getApplication());
        if(error != OPUS_OK){
            return false;
        }
        return true;
    }

    public byte[] opusEncode(byte[]inputData,OPUS_FRAME_DURATION duration,int maxDataBytes){
        byte[] output = opusIntOpusEncode(inputData,duration.getDuration(),maxDataBytes);
        return output;
    }

    public void destroyOpusEncoder(){
        opusEncoderDestroy();
    }

    public void setOpusEncodeCtrl(int request){
        opusEncodeCtl(request);
    }

    public int getFrameCount(int simpleRate,OPUS_FRAME_DURATION duration){
        int frameCount = (int) (duration.getDuration()*2*simpleRate/2000);
        return frameCount;
    }

    public int getInputByteBufferSize(int simpleRate, OPUS_FRAME_DURATION duration, int channels){
        return getFrameCount(simpleRate,duration)*channels*4;
    }



    private native int opusEncodeCreate(int simpleRate, int channel, int opusApplication);
    private native int opusEncoderGetSize(int channel);
    private native int opusEncoderInit(int simpleRate, int channel, int opusApplication);
    private native byte[] opusIntOpusEncode(byte[]inputData,float frameDuration ,int maxDataBytes);
    private native void opusEncoderDestroy();
    private native int opusEncodeCtl(int request);
}
