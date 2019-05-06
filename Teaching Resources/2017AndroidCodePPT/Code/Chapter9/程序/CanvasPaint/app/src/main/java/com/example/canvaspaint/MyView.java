package com.example.canvaspaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class MyView extends View {

    public MyView(Context context,AttributeSet attrs)        //继承Veiw类
    {
        super(context,attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint=new Paint();	                        //采用默认设置创建一个画笔
        paint.setAntiAlias(true);	                        //使用抗锯齿功能
        paint.setColor(0xFF78C257);	                        //设置画笔的颜色为绿色
        //绘制机器人的头
        RectF rectf_head=new RectF(110, 30, 200, 120);
        canvas.drawArc(rectf_head, -10, -160, false, paint);//绘制弧
        //绘制眼睛
        paint.setColor(Color.WHITE);	                    //设置画笔的颜色为白色
        canvas.drawCircle(135, 53, 4, paint);	            //绘制圆
        canvas.drawCircle(175, 53, 4, paint);	            //绘制圆
        //绘制天线
        paint.setColor(0xFF78C257);	                        //设置画笔的颜色为绿色
        paint.setStrokeWidth(8);	                        //设置笔触的宽度
        canvas.drawLine(125, 20, 135, 35, paint);	        //绘制线
        canvas.drawLine(185, 20, 175, 35, paint);	        //绘制线
        //绘制身体
        canvas.drawRect(110, 75, 200, 150, paint);	        //绘制矩形
        RectF rectf_body=new RectF(110,140,200,160);
        canvas.drawRoundRect(rectf_body, 10, 10, paint);	//绘制圆角矩形
        //绘制胳膊
        RectF rectf_arm=new RectF(85,75,105,140);
        canvas.drawRoundRect(rectf_arm, 10, 10, paint);	   //绘制左侧的胳膊
        rectf_arm.offset(120, 0);	                       //设置在X轴上偏移120像素
        canvas.drawRoundRect(rectf_arm, 10, 10, paint);	   //绘制右侧的胳膊
        //绘制腿
        RectF rectf_leg=new RectF(125,150,145,200);
        canvas.drawRoundRect(rectf_leg, 10, 10, paint);    //绘制左侧的腿
        rectf_leg.offset(40, 0);	                       //设置在X轴上偏移40像素
        canvas.drawRoundRect(rectf_leg, 10, 10, paint);	   //绘制右侧的腿
        //写文字
        paint.setTextSize(50);                             //设置字体大小
        paint.setColor(Color.GRAY);                        //设置画笔颜色为灰色
        canvas.drawText("ANDROID",45,260,paint);           //绘制文字
        super.onDraw(canvas);
    }
}
