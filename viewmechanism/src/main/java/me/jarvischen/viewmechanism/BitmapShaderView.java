package me.jarvischen.viewmechanism;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chenfuduo on 2016/3/9.
 */
public class BitmapShaderView extends View {

    private Bitmap bitmap;

    private BitmapShader bitmapShader;

    private Paint paint;

    public BitmapShaderView(Context context) {
        this(context, null);
    }

    public BitmapShaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BitmapShaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    //第一套演示,如果想看这个效果,注释第二个演示的代码,同理,对于,第二个
    /*private void init() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint = new Paint();
        paint.setShader(bitmapShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth()/2 ,bitmap.getHeight()/2 + 100,200,paint);
    }*/

    //第二套演示
    /*private void init() {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        paint = new Paint();
        paint.setShader(bitmapShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth()/2 ,bitmap.getHeight()/2 + 100,200,paint);
    }*/

    private void init() {
        paint = new Paint();
        paint.setShader(new LinearGradient(0,0,400,400, Color.BLUE,Color.YELLOW,
                Shader.TileMode.REPEAT));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0,0,400,400,paint);
    }
}
