package com.lanyuan.superscan.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanyuan.superscan.Pojo.Rule;
import com.lanyuan.superscan.R;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.myHolder> {

    private List<Rule> ruleList;
    private Context mContext;
    private LayoutInflater inflater;

    public ListAdapter(Context context, List<Rule> ruleList) {
        this.ruleList = ruleList;
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public myHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        myHolder holder = new myHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(myHolder holder, int position) {
        Rule rule = ruleList.get(position);
        holder.rule_title.setText(rule.getName());
        holder.rule_package_name.setText(rule.getPackageName());
        holder.rule_activity.setText(rule.getActivity());
        holder.rule_regexs.setText(rule.getRegexs2String());
    }

    @Override
    public int getItemCount() {
        return ruleList.size();
    }

    class myHolder extends RecyclerView.ViewHolder {

        TextView rule_title, rule_package_name,rule_activity, rule_regexs;

        public myHolder(View itemView) {
            super(itemView);
            rule_title = (TextView) itemView.findViewById(R.id.rule_title);
            rule_package_name = (TextView) itemView.findViewById(R.id.rule_package_name);
            rule_activity = (TextView) itemView.findViewById(R.id.rule_activity);
            rule_regexs = (TextView) itemView.findViewById(R.id.rule_regexs);
        }
    }

    public List<Rule> getRuleList(){
        return ruleList;
    }

}
