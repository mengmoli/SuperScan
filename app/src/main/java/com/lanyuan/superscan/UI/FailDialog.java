package com.lanyuan.superscan.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lanyuan.superscan.Action.GoToApp;
import com.lanyuan.superscan.Pojo.Rule;
import com.lanyuan.superscan.R;

import java.util.List;

public class FailDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = FailDialog.class.getSimpleName();

    private String result;
    private List<Rule> ruleList;
    private MainActivity context;
    private EditText editText;
    private Button browser_open, app_open;

    public FailDialog(MainActivity context, String result, List<Rule> ruleList) {
        super(context);
        this.context = context;
        this.result = result;
        this.ruleList = ruleList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_fail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("匹配失败");

        editText = (EditText) findViewById(R.id.qrcode_content);
        editText.setText(result);

        browser_open = (Button) findViewById(R.id.browser_open);
        browser_open.setOnClickListener(this);
        app_open = (Button) findViewById(R.id.app_open);
        app_open.setOnClickListener(this);

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                context.reScan();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.browser_open:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(editText.getText().toString()));
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "这不是一个有效的网址", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.app_open:
                AlertDialog.Builder ab = new AlertDialog.Builder(context);
                ab.setItems(Rules2CharS(ruleList), new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(context,String.valueOf(which),Toast.LENGTH_SHORT).show();
                        GoToApp.go(context, ruleList.get(which));
                    }
                });
                ab.show();
                break;
        }
    }

    private String[] Rules2CharS(List<Rule> ruleList) {
        if (ruleList.size() == 0)
            return new String[]{};
        String[] strings = new String[ruleList.size()];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = ruleList.get(i).getName();
        }
        return strings;
    }

}
