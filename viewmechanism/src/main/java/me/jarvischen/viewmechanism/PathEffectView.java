package me.jarvischen.viewmechanism;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by chenfuduo on 2016/3/10.
 */
public class PathEffectView extends View {

    private float phase = 0;

    private Path path;

    private PathEffect[] effects;

    private Paint paint;

    public PathEffectView(Context context) {
        this(context, null);
    }

    public PathEffectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathEffectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.colorAccent));
        paint.setAntiAlias(true);
        path = new Path();
        path.moveTo(0,0);
        for (int i = 1; i <= 30; i++) {
            path.lineTo(i*35, (float) (Math.random()*100));
        }
        effects = new PathEffect[6];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        effects[0] = null;
        effects[1] = new CornerPathEffect(10);
        effects[2] = new DiscretePathEffect(3.0f,5.0f);
        effects[3] = new DashPathEffect(new float[] { 20, 10, 5, 10 }, phase);
        Path mPath = new Path();
        //count-clock-wise
        mPath.addRect(0,0,8,8, Path.Direction.CCW);
        effects[4] = new PathDashPathEffect(mPath,12,phase, PathDashPathEffect.Style.ROTATE);
        effects[5] = new ComposePathEffect(effects[2],effects[4]);
        for (int i = 0; i < effects.length; i++) {
            paint.setPathEffect(effects[i]);
            canvas.drawPath(path,paint);
            canvas.translate(0,200);
        }
        // 改变phase值,形成动画效果
        phase += 1;
        invalidate();
    }
}
