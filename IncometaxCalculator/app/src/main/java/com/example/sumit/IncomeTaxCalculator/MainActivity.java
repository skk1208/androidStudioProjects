package com.example.sumit.IncomeTaxCalculator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button button_sbm;
    private Button button_sbm1;
    private Button button_sbm2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListnerOnButton();

        Toast ImageToast = new Toast(getBaseContext());
        LinearLayout toastLayout = new LinearLayout(getBaseContext());
        toastLayout.setOrientation(LinearLayout.HORIZONTAL);
        ImageView image = new ImageView(getBaseContext());
        TextView text = new TextView(getBaseContext());
        image.setImageResource(R.drawable.calcy_icon);
        text.setText("Hello!");
        toastLayout.addView(image);
        toastLayout.addView(text);
        ImageToast.setView(toastLayout);
        ImageToast.setDuration(Toast.LENGTH_SHORT);
        ImageToast.show();
    }

    public void addListnerOnButton() {

        button_sbm = (Button) findViewById(R.id.button);

        button_sbm1 = (Button) findViewById(R.id.button2);
        button_sbm2 = (Button) findViewById(R.id.button4);
        button_sbm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent("com.example.sumit.pswrdproject1.Main2Activity");
                startActivity(intent);

            }

        });
        button_sbm1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent("com.example.sumit.pswrdproject1.Main3Activity");
                startActivity(intent1);

            }

        });
        button_sbm2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent("com.example.sumit.pswrdproject1.Main4Activity");
                startActivity(intent1);
            }

        });
    }
    public void browser1(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/Income_tax_in_India"));
        startActivity(browserIntent);

        }

    }
