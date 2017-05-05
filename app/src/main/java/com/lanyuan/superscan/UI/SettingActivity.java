package com.lanyuan.superscan.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.lanyuan.superscan.R;

public class SettingActivity extends AppCompatActivity {

    TextView rules_setting, about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setTitle("设置");
        rules_setting = (TextView) findViewById(R.id.rules_setting);
        rules_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, RulesActivity.class));
            }
        });
    }
}
