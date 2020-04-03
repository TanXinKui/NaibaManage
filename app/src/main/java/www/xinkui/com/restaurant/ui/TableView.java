package www.xinkui.com.restaurant.ui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import www.xinkui.com.restaurant.R;

public class TableView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    int degree;
    Camera camera = new Camera();
    MyRect myRect;
    Bitmap bitmap;
    Rect rect=new Rect(0,0,200,200);
    ObjectAnimator animator = ObjectAnimator.ofInt(this, "degree", 0, 180);
    ValueAnimator animation = ValueAnimator.ofFloat(0f, 360f);
    RotateAnimation animation2;
    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.waiting);

        animator.setDuration(2500);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);

         animation2= new RotateAnimation(0f,360f,25f,25f);
         animation2.setDuration(1000);
         animation2.setRepeatCount(ValueAnimator.INFINITE);
         animation2.setRepeatMode(ValueAnimator.RESTART);
//        animation.setDuration(3000);
//        animation.setRepeatMode(ValueAnimator.REVERSE);
//        animation.setRepeatCount(ValueAnimator.INFINITE);
//        float rxa=degree*1.0f;
//        TableView.this.animate().rotation(rxa);

    }
    public TableView(Context context) {
        super(context);
    }

    public TableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private boolean switchFlag = true;
    public void stopRotateAnimation(){
        switchFlag = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        myRect=new MyRect(getContext());
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int x = centerX - bitmapWidth / 2;
        int y = centerY - bitmapHeight / 2;
        int semiX = centerX/2;
        int semiY = centerY/2;

        canvas.save();
//        camera.save();
//        camera.rotateX(degree);
//        camera.rotateY(degree);
//        canvas.translate(semiX, semiY);
//        camera.applyToCanvas(canvas);
//        canvas.clipRect(0, 0, getWidth(), centerY);
//        canvas.drawBitmap(bitmap, x, y, paint);
//        canvas.drawText("rx",1,centerY,paint);
//        canvas.drawLine(0,0,50,50,paint);
//        canvas.d
//        ViewPropertyAnimator mAnimator= new ViewPropertyAnimator(rect);
        canvas.drawBitmap(bitmap,null,new Rect(0,0,50,50),paint);
//        canvas.drawBitmap(bitmap,0,0,paint);
//        canvas.drawRect(rect,paint);

//        animation.start();
//        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
//                // You can use the animated value in a property that uses the
//                // same type as the animation. In this case, you can use the
//                // float value in the translationX property.
//                float animatedValue = (float)updatedAnimation.getAnimatedValue();;
////                TableView.this.setAlpha(animatedValue);
//                TableView.this.animate().rotation(animatedValue);
//            }
//        });

//        float alp = degree/180;
//        TableView.this.setAlpha(alp);
        canvas.restore();
//        canvas.save();
//
//        if(degree<90){
//            canvas.clipRect(0,  centerY, getWidth(),getHeight());
//        }else {
//            canvas.clipRect(0, 0, getWidth(), centerY);
//        }


//        canvas.translate(-centerX, -centerY);
//        camera.restore();
//        canvas.drawBitmap(bitmap, x, y, paint);
//        canvas.restore();
    }

    @SuppressWarnings("unused")
    public void setDegree(int degree) {
        this.degree = degree;
        invalidate();
    }
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        animator.start();
        if(switchFlag){
            TableView.this.startAnimation(animation2);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(switchFlag){
            TableView.this.onAnimationEnd();
        }
//        animator.end();
    }
}
