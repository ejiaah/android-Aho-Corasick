package com.ejiaah.aho_corasick;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class KeywordRecyclerAdapter extends RecyclerView.Adapter<KeywordRecyclerAdapter.ViewHolder> {

    private List<String> keywordList = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView keywordTextView;

        public ViewHolder(View v) {
            super(v);
            keywordTextView = (TextView) v.findViewById(R.id.keyword_textView);
        }
    }

    public KeywordRecyclerAdapter(List<String> keywordList) {
        this.keywordList = keywordList;
    }

    @NonNull
    @Override
    public KeywordRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_keyword, parent, false);
        final ViewHolder viewHolder = new KeywordRecyclerAdapter.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String keyword = keywordList.get(position);
        if (TextUtils.isEmpty(keyword)) {
            return;
        }

        holder.keywordTextView.setText(keyword);
    }

    @Override
    public int getItemCount() {
        return keywordList.size();
    }

    public void insertItem(String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            return;
        }
        keywordList.add(keyword);
        notifyItemInserted(keywordList.size());
    }

    public void deleteItem(int position) {
        keywordList.remove(position);
        notifyItemRemoved(position);
    }

}