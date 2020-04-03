package www.xinkui.com.restaurant.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import www.xinkui.com.restaurant.util.Util;

public class MyRect extends View {
//    RectF df;
    Rect d=new Rect(0,0,50,300);
    ValueAnimator animation = ValueAnimator.ofFloat(0f, 360f);

    {
        animation.setDuration(5000);
        animation.start();
    }
    Paint paint=new Paint();
    public MyRect(Context context) {
        super(context);
    }

    public MyRect(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRect(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.drawRect(d,paint);
        canvas.restore();
    }
}
