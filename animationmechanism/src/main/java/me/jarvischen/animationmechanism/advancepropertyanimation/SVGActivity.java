package me.jarvischen.animationmechanism.advancepropertyanimation;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import me.jarvischen.animationmechanism.R;

public class SVGActivity extends AppCompatActivity {

    private ImageView iv;
    private ImageView ivsvg1;
    private ImageView ivsvg2;
    private ImageView ivsvg3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg);
        iv = (ImageView) findViewById(R.id.ivsvg);
        ivsvg1 = (ImageView) findViewById(R.id.ivsvg1);
        ivsvg2 = (ImageView) findViewById(R.id.ivsvg2);
        ivsvg3 = (ImageView) findViewById(R.id.ivsvg3);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate0();
            }
        });
        ivsvg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate();
            }
        });

        ivsvg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate2();
            }
        });

        ivsvg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate3();
            }
        });

    }

    private void animate0() {
        Drawable drawable = iv.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

    private void animate() {
        Drawable drawable = ivsvg1.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

    private void animate2() {
        Drawable drawable = ivsvg2.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

    private void animate3() {
        Drawable drawable = ivsvg3.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }
}
