package www.xinkui.com.restaurant.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import www.xinkui.com.restaurant.R;

public class CornerView extends android.support.v7.widget.AppCompatImageView {
    //圆角类型
    private int type = 0;
    private static final int TYPE_CIRCLE = 0;
    private static final int TYPE_CORNER = 1;
    //圆角大小 默认 10
    private static final int CORNER_DEFAULT_VALUES = 10;
    //圆角的大小
    private int corner;
    //画笔 paint
    private Paint shaderPaint;
    //圆角的半径
    private int mRadius;
    //3*3 矩阵 用于缩放图片
    private Matrix matrix;
    //使用图像为绘制的图形着色
    private BitmapShader bitmapShader;
    //view的宽度
    private int width;
    private RectF mRect;

    public CornerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        matrix = new Matrix();
        shaderPaint = new Paint();
        shaderPaint.setAntiAlias(true);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CornerView);//
        corner = array.getDimensionPixelSize(R.styleable.CornerView_boarderRadius,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, CORNER_DEFAULT_VALUES, getResources().getDisplayMetrics()));
        type = array.getInt(R.styleable.CornerView_type, TYPE_CORNER);
        array.recycle();
    }
  Drawable myd;
   public void setBitMap(Drawable mdy){
        myd = mdy;
   }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (type == TYPE_CIRCLE) {
            width = Math.min(getMeasuredHeight(), getMeasuredWidth());
            mRadius = width / 2;
            setMeasuredDimension(width, width);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        if(getDrawable()==null){
//            return;
//        }
        setupShader();
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        if(type == TYPE_CIRCLE){
            canvas.drawCircle(mRadius,mRadius,mRadius,shaderPaint);
        }else if(type == TYPE_CORNER){
            canvas.drawRoundRect(mRect,corner,corner,shaderPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(type == TYPE_CORNER){
            mRect = new RectF(0,0,getWidth(),getHeight());
        }
    }

    private void setupShader() {
//        Drawable drawable = getDrawable();
//        if (drawable == null) {
//            return;
//        }
//        Bitmap bitmap = drawable2Bitmap(drawable);
//        Bitmap bitmap = drawable2Bitmap(getResources().getDrawable(R.drawable.dish1));
        Bitmap bitmap = drawable2Bitmap(myd);
        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        if (type == TYPE_CIRCLE) {
            int bSize = Math.max(bitmap.getWidth(),bitmap.getHeight());
            scale = width*1.0f/bSize;
        } else if(type == TYPE_CORNER){
            scale = Math.max(getWidth()*1.0f/bitmap.getWidth(),getHeight()*1.0f/bitmap.getHeight());
        }
        matrix.setScale(scale,scale);
        bitmapShader.setLocalMatrix(matrix);
        shaderPaint.setShader(bitmapShader);
    }

    private Bitmap drawable2Bitmap(Drawable drawable) {
//        if (drawable instanceof BitmapDrawable) {
//            BitmapDrawable bd = (BitmapDrawable) drawable;
//            return bd.getBitmap();
//        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }
}
