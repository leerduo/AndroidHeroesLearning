package me.jarvischen.viewmechanism;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;

public class PrimaryColorActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{

    private static int MAX_VALUE = 255;
    private static int MID_VALUE = 127;
    private ImageView mImageView;
    //分别为色调,饱和度,亮度
    private SeekBar mSeekbarhue, mSeekbarSaturation, mSeekbarLum;
    //分别为色调,饱和度,亮度
    private float mHue, mStauration, mLum;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary_color);
        bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon);
        mImageView = (ImageView) findViewById(R.id.imageview);
        mSeekbarhue = (SeekBar) findViewById(R.id.seekbarHue);
        mSeekbarSaturation = (SeekBar) findViewById(R.id.seekbarSaturation);
        mSeekbarLum = (SeekBar) findViewById(R.id.seekbatLum);
        mSeekbarhue.setOnSeekBarChangeListener(this);
        mSeekbarSaturation.setOnSeekBarChangeListener(this);
        mSeekbarLum.setOnSeekBarChangeListener(this);
        mSeekbarhue.setMax(MAX_VALUE);
        mSeekbarSaturation.setMax(MAX_VALUE);
        mSeekbarLum.setMax(MAX_VALUE);
        mSeekbarhue.setProgress(MID_VALUE);
        mSeekbarSaturation.setProgress(MID_VALUE);
        mSeekbarLum.setProgress(MID_VALUE);
        mImageView.setImageBitmap(bitmap);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seekbarHue:
                mHue = (progress - MID_VALUE) * 1.0F / MID_VALUE * 180;
                break;
            case R.id.seekbarSaturation:
                mStauration = progress * 1.0F / MID_VALUE;
                break;
            case R.id.seekbatLum:
                mLum = progress * 1.0F / MID_VALUE;
                break;
        }
        mImageView.setImageBitmap(ImageHelper.handleImageEffect(
                bitmap, mHue, mStauration, mLum));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
