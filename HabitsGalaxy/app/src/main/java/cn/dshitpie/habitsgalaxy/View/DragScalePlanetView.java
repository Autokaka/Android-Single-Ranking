package cn.dshitpie.habitsgalaxy.View;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;


public class DragScalePlanetView extends AppCompatImageView implements View.OnTouchListener {
    protected int lastX;
    protected int lastY;
    private int oriLeft;
    private int oriRight;
    private int oriTop;
    private int oriBottom;
    private boolean flag;
    private ImageView treeView;

    //初始的旋转角度
    private float oriRotation = 0;
    private static String TAG = "HabitGalaxy";

    public DragScalePlanetView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragScalePlanetView(Context context) {
        super(context);
    }

    public boolean onTouch(View v, MotionEvent event) {
        Log.d(TAG,"OnTouch");
        int action = event.getAction()& MotionEvent.ACTION_MASK;
        if (action == MotionEvent.ACTION_DOWN) {
            oriLeft = v.getLeft();
            oriRight = v.getRight();
            oriTop = v.getTop();
            oriBottom = v.getBottom();
            lastY = (int) event.getRawY();
            lastX = (int) event.getRawX();

            oriRotation = v.getRotation();
            Log.d(TAG, "ACTION_DOWN: "+oriRotation);
        }

        delDrag(v, event, action);
        invalidate();
        return false;
    }


    protected void delDrag(View v, MotionEvent event, int action) {
        Log.d(TAG, "delDrag");

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG,"Action_MOVE");
                int dx = (int) event.getRawX() - lastX;
                int dy = (int) event.getRawY() - lastY;
                Point center = new Point(oriLeft+(oriRight-oriLeft)/2,oriTop+(oriBottom-oriTop)/2);
                Point first = new Point(lastX,lastY);
                Point second = new Point((int) event.getRawX(),(int) event.getRawY());
                oriRotation += angle(center,first,second);

                v.setRotation(oriRotation);
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                break;
        }
    }

    public float angle(Point cen, Point first, Point second)
    {
        float dx1, dx2, dy1, dy2;

        dx1 = first.x - cen.x;
        dy1 = first.y - cen.y;
        dx2 = second.x - cen.x;
        dy2 = second.y - cen.y;

        // 计算三边的平方
        float ab2 = (second.x - first.x) * (second.x - first.x) + (second.y - first.y) * (second.y - first.y);
        float oa2 = dx1*dx1 + dy1*dy1;
        float ob2 = dx2 * dx2 + dy2 *dy2;

        // 根据两向量的叉乘来判断顺逆时针
        boolean isClockwise = ((first.x - cen.x) * (second.y - cen.y) - (first.y - cen.y) * (second.x - cen.x)) > 0;

        // 根据余弦定理计算旋转角的余弦值
        double cosDegree = (oa2 + ob2 - ab2) / (2 * Math.sqrt(oa2) * Math.sqrt(ob2));

        // 异常处理，因为算出来会有误差绝对值可能会超过一，所以需要处理一下
        if (cosDegree > 1) {
            cosDegree = 1;
        } else if (cosDegree < -1) {
            cosDegree = -1;
        }

        // 计算弧度
        double radian = Math.acos(cosDegree);

        // 计算旋转过的角度，顺时针为正，逆时针为负
        return (float) (isClockwise ? Math.toDegrees(radian) : -Math.toDegrees(radian));

    }
}