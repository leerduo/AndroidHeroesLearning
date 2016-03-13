package me.jarvischen.systeminformation;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ActivityManagerActivity extends AppCompatActivity {

    private ListView mListView;
    private List<AMInfo> mAmProcessInfoList;
    private ActivityManager mActivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_manager);
        mListView = (ListView) findViewById(R.id.listView_am_process);
        mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        mListView.setAdapter(new AMAdapter(this, getRunningProcessInfo()));
    }

    private List<AMInfo> getRunningProcessInfo() {
        mAmProcessInfoList = new ArrayList<AMInfo>();

        List<ActivityManager.RunningAppProcessInfo> appProcessList =
                mActivityManager.getRunningAppProcesses();

        for (int i = 0; i < appProcessList.size(); i++) {
            ActivityManager.RunningAppProcessInfo info =
                    appProcessList.get(i);
            int pid = info.pid;
            int uid = info.uid;
            String processName = info.processName;
            int[] memoryPid = new int[]{pid};
            Debug.MemoryInfo[] memoryInfo = mActivityManager
                    .getProcessMemoryInfo(memoryPid);
            int memorySize = memoryInfo[0].getTotalPss();

            AMInfo processInfo = new AMInfo();
            processInfo.setPid("" + pid);
            processInfo.setUid("" + uid);
            processInfo.setMemorySize("" + memorySize);
            processInfo.setProcessName(processName);
            mAmProcessInfoList.add(processInfo);
        }
        return mAmProcessInfoList;
    }
}