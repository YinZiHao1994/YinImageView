package com.source.yin.yinimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

/**
 * 圆形 ImageView
 * Created by yin on 2017/8/29.
 */

public class CircleImageView extends android.support.v7.widget.AppCompatImageView {

    private Paint circlePaint;      //画圆形图像的笔
    private Paint circleBorderPaint;          //画圆形边界的笔
    private Matrix mMatrix;          //图片变换处理器-用来缩放图片以适应view控件的大小
    private int circleBorderWidth = 0;        //边界宽度
    private int circleBorderColor = Color.WHITE;             //边界边框颜色

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.CircleImageView_circle_border_width) {
                circleBorderWidth = (int) typedArray.getDimension(attr, 0);
            } else if (attr == R.styleable.CircleImageView_circle_border_color) {
                circleBorderColor = typedArray.getColor(attr, Color.WHITE);
            }
        }
        typedArray.recycle();
        init();
    }

    private void init() {
        //初始化图片变换处理器
        mMatrix = new Matrix();

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);//抗锯齿
        circlePaint.setStrokeWidth(12);//设置圆边界宽度
        //附加效果设置阴影
        this.setLayerType(LAYER_TYPE_SOFTWARE, circlePaint);
        circlePaint.setShadowLayer(13.0f, 5.0f, 5.0f, Color.GRAY);

        //给图形加边框
        circleBorderPaint = new Paint();
        circleBorderPaint.setAntiAlias(true);
        circleBorderPaint.setStyle(Paint.Style.STROKE);
        circleBorderPaint.setStrokeWidth(circleBorderWidth);
        circleBorderPaint.setColor(circleBorderColor);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    /**
     * 如果来源 drawable 不是{@link BitmapDrawable} 的子类，例如利用 Glide 框架加载图片，
     * 使用的是 {com.bumptech.glide.load.resource.drawable.GlideDrawable} ，则使用默认的 onDraw 方法，
     * 开发者自行利用 glide 的 {com.bumptech.glide.load.resource.bitmap.BitmapTransformation } 进行形状转换
     */
    @Override
    protected void onDraw(Canvas canvas) {
//        使用裁剪画布的方式也是一种办法，但是存在一定问题
       /* Path path = new Path();
        int width = getWidth();
        int radius = width / 2;
        path.addCircle(radius, radius, radius, Path.Direction.CCW);
        //先将canvas保存
        canvas.save();
        //设置为在圆形区域内绘制
        canvas.clipPath(path);
        super.onDraw(canvas);*/


        //这里注释掉onDraw是为了不绘制原来的画布,否则就意味着又是渲染一层
        //        super.onDraw(canvas);
        Drawable drawable = getDrawable();
        if (drawable != null) {
            if (!(drawable instanceof BitmapDrawable)) {
                Log.e(getClass().getName(), "drawable : " + drawable + " not instanceof BitmapDrawable");
                super.onDraw(canvas);
            } else {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                if (bitmap == null) {
                    Log.e(getClass().getName(), "bitmap is null");
                    return;
                }

                int viewWidth = getWidth();
                int viewHeight = getHeight();
                //圆的直径
                int diameter = Math.min(viewWidth, viewHeight);
                //圆的半径
                int radius = diameter / 2 - circleBorderWidth;
                setBitmapShader(bitmap, viewWidth, viewHeight);

                canvas.drawCircle(viewHeight / 2, viewWidth / 2, radius, circlePaint);
                //画边框
                canvas.drawCircle(viewHeight / 2, viewWidth / 2, radius + circleBorderWidth / 2, circleBorderPaint);
            }
        } else {
            //如果在xml中这个继承ImageView的类没有被set图片，就用默认的ImageView方案
            super.onDraw(canvas);
        }
    }


    //使用BitmapShader画圆图形
    private void setBitmapShader(Bitmap bitmap, int viewWidth, int viewHeight) {
        //将bitmap放进图像着色器，后面两个模式是x，y轴的缩放模式，CLAMP表示拉伸
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //计算缩放比例，保证图片充满视图
        float widthScale = (float) viewWidth / bitmap.getWidth();
        float heightScale = (float) viewHeight / bitmap.getHeight();
        float scale = Math.max(widthScale, heightScale);
        //利用这个图像变换处理器，设置伸缩比例，长宽以相同比例伸缩
        mMatrix.setScale(scale, scale);
        //给那个图像着色器设置变换矩阵，绘制时就根据view的size，设置图片的size
        bitmapShader.setLocalMatrix(mMatrix);
        //为画笔套上一个Shader的笔套
        circlePaint.setShader(bitmapShader);
    }
}
