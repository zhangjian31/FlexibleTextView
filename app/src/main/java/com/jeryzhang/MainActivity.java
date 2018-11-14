package com.jeryzhang;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.jery.flexibletextview.FlexibleTextView;
import com.jeryzhang.demo.R;

public class MainActivity extends AppCompatActivity {
    private FlexibleTextView textView;
    private String pre = "The WatchKit framework contains the classes that a WatchKit extension uses to manipulate the interface of a watchOS app. A watchOS app contains one or more interface controllers, each of which can have tables, buttons, sliders, and other types of visual elements. The WatchKit extension uses the classes of this framework to configure those visual elements and to respond to user interactions.";
    private String last = " #后缀";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);
        textView.setFlexibleText(pre, last);
        textView.setOnTextClickListener(new FlexibleTextView.OnTextClickListener() {
            @Override
            public void onPreTextClick(String text) {
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMoreTextClick(String text) {
                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
