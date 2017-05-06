package com.lanyuan.superscan.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lanyuan.superscan.Action.GoToApp;
import com.lanyuan.superscan.Adapter.ListAdapter;
import com.lanyuan.superscan.Listener.myClickListener;
import com.lanyuan.superscan.Pojo.Rule;
import com.lanyuan.superscan.R;
import com.lanyuan.superscan.Util.FileUtil;

import java.util.ArrayList;
import java.util.List;

public class RulesActivity extends AppCompatActivity {

    private final static String TAG = "RulesActivity";

    private RecyclerView recyclerView;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("规则设置");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.mipmap.back);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        List<Rule> ruleList = FileUtil.loadRules(this);
        initData(ruleList);

        Toast.makeText(this, "点击编辑   长按删除", Toast.LENGTH_LONG).show();
    }

    private void initData(List<Rule> rules) {
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        adapter = new ListAdapter(getApplicationContext(), rules);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(adapter);
        myClickListener.addTo(recyclerView).setOnItemClickListener(new myClickListener.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                RuleDialog dialog = new RuleDialog(RulesActivity.this, adapter.getRuleList().get(position), position);
                dialog.show();
            }
        });
        myClickListener.addTo(recyclerView).setOnItemLongClickListener(new myClickListener.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, final int position, View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(RulesActivity.this);
                AlertDialog dialog = ab.setTitle("注意")
                        .setMessage("确定要删除这个规则？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delData(position);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create();
                dialog.show();
                return true;
            }
        });
    }

    public void updateData(Rule rule, int position) {
        List<Rule> ruleList = ((ListAdapter) recyclerView.getAdapter()).getRuleList();
        ruleList.set(position, rule);
        recyclerView.setAdapter(new ListAdapter(getApplicationContext(), ruleList));
    }

    public void addData(Rule rule) {
        List<Rule> ruleList = ((ListAdapter) recyclerView.getAdapter()).getRuleList();
        ruleList.add(rule);
        recyclerView.setAdapter(new ListAdapter(getApplicationContext(), ruleList));
    }

    public void delData(int position) {
        List<Rule> ruleList = ((ListAdapter) recyclerView.getAdapter()).getRuleList();
        ruleList.remove(position);
        recyclerView.setAdapter(new ListAdapter(getApplicationContext(), ruleList));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rule_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.save:
                List<Rule> ruleList = ((ListAdapter) recyclerView.getAdapter()).getRuleList();
                FileUtil.saveRules(this, ruleList);
                GoToApp.setRules(ruleList);
                onBackPressed();
                return true;
            case R.id.add:
                RuleDialog dialog = new RuleDialog(RulesActivity.this);
                dialog.show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
