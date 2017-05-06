package com.lanyuan.superscan.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lanyuan.superscan.R;
import com.lanyuan.superscan.Util.CommandUtil;
import com.lanyuan.superscan.Util.UpdateUtil;

public class SettingActivity extends AppCompatActivity {

    TextView rules_setting, about, update;
    Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("设置");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.mipmap.back);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        rules_setting = (TextView) findViewById(R.id.rules_setting);
        rules_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, RulesActivity.class));
            }
        });

        update = (TextView) findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUtil.goToAppMarket(getApplicationContext());
            }
        });

        about = (TextView) findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, Aboutctivity.class));
            }
        });

        aSwitch = (Switch) findViewById(R.id.switch_root);
        aSwitch.setChecked(CommandUtil.isSwitch_on());
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(SettingActivity.this);
                    AlertDialog dialog = ab.setTitle("ROOT模式说明")
                            .setMessage(R.string.root_description)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (CommandUtil.isRoot()) {
                                        Toast.makeText(SettingActivity.this, "ROOT模式已打开", Toast.LENGTH_SHORT).show();
                                        CommandUtil.setSwitch_on(SettingActivity.this, true);
                                        dialog.dismiss();
                                    } else {
                                        Toast.makeText(SettingActivity.this, "ROOT请求被拒绝或设备没有获得ROOT权限，请确认后重试!", Toast.LENGTH_LONG).show();
                                        aSwitch.setChecked(false);
                                        dialog.dismiss();
                                    }
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    aSwitch.setChecked(false);
                                    Toast.makeText(SettingActivity.this, "ROOT模式已关闭", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            })
                            .setCancelable(false)
                            .create();
                    dialog.show();
                } else if (isChecked == false) {
                    CommandUtil.setSwitch_on(SettingActivity.this, false);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
