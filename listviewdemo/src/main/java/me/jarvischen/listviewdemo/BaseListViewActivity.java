package me.jarvischen.listviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BaseListViewActivity extends AppCompatActivity {

    private List<String> mData;
    private ListView mListView;
    private MyBaseListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_list_view);
        mData = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            mData.add("" + i);
        }
        mListView = (ListView) findViewById(R.id.listView);
        mAdapter = new MyBaseListViewAdapter(this, mData);
        mListView.setAdapter(mAdapter);
        mListView.setSelection(44);
        for (int i = 0; i < mListView.getChildCount(); i++) {
            View view = mListView.getChildAt(i);
        }
    }

    public void btnAdd(View view) {
        mData.add("new");
        mAdapter.notifyDataSetChanged();
        mListView.setSelection(mData.size() - 1);
    }
}
