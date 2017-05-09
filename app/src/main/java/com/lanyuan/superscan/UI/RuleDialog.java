package com.lanyuan.superscan.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lanyuan.superscan.Pojo.Rule;
import com.lanyuan.superscan.R;

import java.util.ArrayList;
import java.util.List;

public class RuleDialog extends Dialog {

    public final static int RULES_ADD = 1;
    public final static int RULES_UPDATE = 2;

    private RulesActivity rulesActivity;
    private Button yes, no, description;
    private EditText name, package_name, activity, regexs;
    private Rule rule;
    private int position;
    private int flag;

    public RuleDialog(RulesActivity activity, Rule rule, int position) {
        super(activity);
        this.rulesActivity = activity;
        this.rule = rule;
        this.position = position;
        this.flag = RULES_UPDATE;
    }

    public RuleDialog(RulesActivity activity) {
        super(activity);
        this.rulesActivity = activity;
        this.flag = RULES_ADD;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rule);

        name = (EditText) findViewById(R.id.edit_name);
        package_name = (EditText) findViewById(R.id.edit_package_name);
        activity = (EditText) findViewById(R.id.edit_activity);
        regexs = (EditText) findViewById(R.id.edit_regexs);

        if (rule != null) {
            name.setText(rule.getName());
            package_name.setText(rule.getPackageName());
            activity.setText(rule.getActivity());
            regexs.setText(rule.getRegexs2String());
        }

        yes = (Button) findViewById(R.id.yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rule rule = new Rule();
                rule.setName(name.getText().toString());
                rule.setPackageName(package_name.getText().toString());
                rule.setActivity(activity.getText().toString());
                if (!rule.getName().equals("") && !rule.getPackageName().equals("") && !regexs.getText().toString().equals("")) {
                    List<String> list = String2List(regexs.getText().toString());
                    rule.setRegexs(list);
                    if (flag == RULES_UPDATE)
                        rulesActivity.updateData(rule, position);
                    else if (flag == RULES_ADD)
                        rulesActivity.addData(rule);
                    dismiss();
                } else {
                    Toast.makeText(rulesActivity, "必填项不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        no = (Button) findViewById(R.id.no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        description = (Button) findViewById(R.id.description);
        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(activity.getContext());
                AlertDialog dialog = ab.setTitle("说明")
                        .setMessage(R.string.description)
                        .setPositiveButton("确定", null)
                        .create();
                dialog.show();
            }
        });
    }

    private List<String> String2List(String regexs) {
        List<String> regexList = new ArrayList<>();
        if (regexs.indexOf("\n") != -1) {
            String[] temp = regexs.split("\n");
            for (String s : temp)
                regexList.add(s);
        } else {
            regexList.add(regexs);
        }
        return regexList;
    }

}
