package me.jarvischen.customview_02;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by chenfuduo on 2016/3/1.
 */
public class TopBar extends RelativeLayout {

    int leftTextColor;
    Drawable leftBackground;
    String leftText;
    int rightTextColor;
    Drawable rightBackground;
    String rightText;
    float titleTextSize;
    int titleTextColor;
    String titleText;

    Button leftButton;
    Button rightButton;
    TextView titleView;

    LayoutParams leftParams;
    LayoutParams rightParams;
    LayoutParams titleParams;

    TopBarClickListener topBarClickListener;

    public void setTopBarClickListener(TopBarClickListener topBarClickListener) {
        this.topBarClickListener = topBarClickListener;
    }

    public TopBar(Context context) {
        this(context, null);
    }

    public TopBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TopBar);

        leftTextColor = array.getColor(R.styleable.TopBar_leftTextColor, 0);
        leftBackground = array.getDrawable(R.styleable.TopBar_leftBackground);
        leftText = array.getString(R.styleable.TopBar_leftText);

        rightTextColor = array.getColor(R.styleable.TopBar_rightTextColor, 0);
        rightBackground = array.getDrawable(R.styleable.TopBar_rightBackground);
        rightText = array.getString(R.styleable.TopBar_rightText);

        titleTextSize = array.getDimension(R.styleable.TopBar_titleTextSize, 10);
        titleTextColor = array.getColor(R.styleable.TopBar_titleTextColorCustom, 0);
        titleText = array.getString(R.styleable.TopBar_titleCustom);

        array.recycle();

        leftButton = new Button(context);
        rightButton = new Button(context);
        titleView = new TextView(context);

        leftButton.setTextColor(leftTextColor);
        leftButton.setBackground(leftBackground);
        leftButton.setText(leftText);

        rightButton.setTextColor(rightTextColor);
        rightButton.setBackground(rightBackground);
        rightButton.setText(rightText);

        titleView.setText(titleText);
        titleView.setTextColor(titleTextColor);
        titleView.setTextSize(titleTextSize);
        titleView.setGravity(Gravity.CENTER);

        leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,TRUE);
        addView(leftButton, leftParams);

        rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);
        addView(rightButton, rightParams);

        titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT,TRUE);
        addView(titleView, titleParams);

        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topBarClickListener.leftClick();
            }
        });

        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                topBarClickListener.rightClick();
            }
        });

    }

    public void setButtonVisible(int id,boolean flag){
        if (flag){
            if (id == 0){
                leftButton.setVisibility(View.VISIBLE);
            }else{
                rightButton.setVisibility(View.VISIBLE);
            }
        }else{
            if (id == 0){
                leftButton.setVisibility(View.GONE);
            }else{
                rightButton.setVisibility(View.GONE);
            }
        }
    }


}
