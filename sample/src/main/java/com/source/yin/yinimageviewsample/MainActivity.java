package com.source.yin.yinimageviewsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnCircleImage;
    private Button btnSquareImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCircleImage = (Button) findViewById(R.id.btn_circle_image);
        btnSquareImage = (Button) findViewById(R.id.btn_square_image);

        btnCircleImage.setOnClickListener(this);
        btnSquareImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_circle_image:
                intent = new Intent(getApplicationContext(), CircleImageActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_square_image:
                intent = new Intent(getApplicationContext(), SquareImageActivity.class);
                startActivity(intent);
                break;
        }
    }
}
