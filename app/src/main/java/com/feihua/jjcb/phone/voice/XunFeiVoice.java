package com.feihua.jjcb.phone.voice;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.feihua.jjcb.phone.R;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wcj on 2016-09-02.
 */
public class XunFeiVoice
{
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private Toast mToast;
    private OnDialogListener listener;

    public XunFeiVoice(Context ctx){

        mToast = Toast.makeText(ctx, "", Toast.LENGTH_SHORT);
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(ctx, mInitListener);
        setParam();
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(ctx, mInitListener);
    }

    public void setOnDialogListener(OnDialogListener listener){
        this.listener = listener;
    }

    public void showDialog(){
        // 显示听写对话框
        mIatDialog.setListener(mRecognizerDialogListener);
        mIatDialog.show();
        showTip("请开始说话…");
    }

    public void onFinish(){
        // 退出时释放连接
        mIat.cancel();
        mIat.destroy();
    }

    /**
     * 参数设置
     *
     * @param
     * @return
     */
    private void setParam()
    {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        // 设置语言en_us英语zh_cn中文
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域,mandarin普通话cantonese粤语henanese河南话en_us英语
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理,0-10000ms
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音,0-10000ms
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener()
    {
        public void onResult(RecognizerResult results, boolean isLast)
        {
            printResult(results);
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error)
        {
            showTip(error.getPlainDescription(true));
        }

    };

    /**
     * 解析服务器返回内容
     * @param results
     */
    private void printResult(RecognizerResult results)
    {

        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try
        {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(text)){
            return;
        }
//        et.setText(text);
        if (listener != null){
            listener.onResult(text);
        }
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener()
    {

        @Override
        public void onInit(int code)
        {
            Log.d("MainActivity", "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS)
            {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };

    private void showTip(final String str)
    {
        mToast.setText(str);
        mToast.show();
    }
}
