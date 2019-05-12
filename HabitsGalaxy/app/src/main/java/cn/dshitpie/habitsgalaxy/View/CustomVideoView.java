package cn.dshitpie.habitsgalaxy.View;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.VideoView;

/**
 * Auther: Crazy.Mo on 2018/8/24 14:35
 * Summary:继承VideoView实现自适应全屏幕
 */
public class CustomVideoView extends VideoView {
    public CustomVideoView(Context context) {
        this(context,null);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //重新计算高度
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    /**
     * @param pPreparedListener 设置一个在媒体文件加载并准备就绪时调用的回调。
     */
    @Override
    public void setOnPreparedListener(MediaPlayer.OnPreparedListener pPreparedListener) {
        super.setOnPreparedListener(pPreparedListener);
    }

    /**
     * @param pCompletionListener 设置一个在媒体文件播放完毕，到达终点时调用的回调。
     */
    @Override
    public void setOnCompletionListener(MediaPlayer.OnCompletionListener pCompletionListener) {
        super.setOnCompletionListener(pCompletionListener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
