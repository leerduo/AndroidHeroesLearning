package me.jarvischen.listviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BaseListViewActivity extends AppCompatActivity {

    private static final String TAG = "BaseListViewActivity";
    private List<String> mData;
    private MyListView mListView;
    private MyBaseListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_list_view);
        mData = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            mData.add("" + i);
        }
        mListView = (MyListView) findViewById(R.id.listView);
        mAdapter = new MyBaseListViewAdapter(this, mData);
        mListView.setAdapter(mAdapter);
        mListView.setSelection(44);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState){
                    case SCROLL_STATE_IDLE:
                        //滑动停止时
                        Log.e(TAG, "onScrollStateChanged: SCROLL_STATE_IDLE");
                        break;

                    case SCROLL_STATE_TOUCH_SCROLL:
                        //正在滚动
                        Log.e(TAG, "onScrollStateChanged: SCROLL_STATE_TOUCH_SCROLL");
                        break;

                    case SCROLL_STATE_FLING:
                        //
                        Log.e(TAG, "onScrollStateChanged: SCROLL_STATE_FLING");
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.e(TAG, "onScroll: firstVisibleItem=" + firstVisibleItem +
                        "    visibleItemCount=" + visibleItemCount + "   totalItemCount=" + totalItemCount);
            }
        });
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
