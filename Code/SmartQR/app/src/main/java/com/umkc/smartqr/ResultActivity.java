package com.umkc.smartqr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    TextView txtOutput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        String result=getIntent().getStringExtra("result");
        txtOutput = findViewById(R.id.txtOutput);
        txtOutput.setText(result);
    }
}
