package com.lanyuan.superscan.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lanyuan.superscan.Adapter.ListAdapter;
import com.lanyuan.superscan.Listener.myClickListener;
import com.lanyuan.superscan.Pojo.Rule;
import com.lanyuan.superscan.R;

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
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ArrayList<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setName("微信");
        rule.setPackageName("com.mm.wechat");
        rule.setActivity("com.mm.wechat.scan");
        List<String> list = new ArrayList<>();
        list.add("http://weixin.qq.com\\S*");
        rule.setRegexs(list);
        rules.add(rule);
        rules.add(rule);
        rules.add(rule);
        rules.add(rule);
        rules.add(rule);
        rules.add(rule);
        rules.add(rule);
        rules.add(rule);
        initData(rules);
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
                //Log.e(TAG, "onclick" + position);
                //ListAdapter adapter = (ListAdapter) recyclerView.getAdapter();
                //Log.e(TAG, String.valueOf(adapter.getRuleList().size()));
                RuleDialog dialog = new RuleDialog(RulesActivity.this, adapter.getRuleList().get(position), position);
                dialog.show();
            }
        });
    }

    public void updateData(Rule rule, int position) {
        List<Rule> ruleList = adapter.getRuleList();
        ruleList.set(position, rule);
        recyclerView.setAdapter(new ListAdapter(getApplicationContext(), ruleList));
    }

    public void addData(Rule rule) {
        List<Rule> ruleList = adapter.getRuleList();
        ruleList.add(rule);
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
