package me.jarvischen.systeminformation;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SystemInfoActivity extends AppCompatActivity {

    private TextView tvBuild;
    private TextView sysTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_info);
        tvBuild = (TextView) findViewById(R.id.tvBuild);
        sysTv = (TextView) findViewById(R.id.sysTv);
        initBuild();
        initSysPro();
    }

    private void initSysPro() {
        String version = System.getProperty("os.version");
        String name = System.getProperty("os.name");
        String arch = System.getProperty("os.arch");
        String userHome = System.getProperty("user.home");
        String userName = System.getProperty("user.name");
        String userDir = System.getProperty("user.dir");
        String userTimezone = System.getProperty("user.timezone");
        String pathSeparator = System.getProperty("path.separator");
        String lineSeparator = System.getProperty("line.separator");
        String fileSeparator = System.getProperty("file.separator");
        String javaVendorUrl = System.getProperty("java.vendor.url");
        String javaClassPath = System.getProperty("java.class.path");
        String javaClassVersion = System.getProperty("java.class.version");
        String javaVendor = System.getProperty("java.vendor");
        String javaVersion = System.getProperty("java.version");
        String javaHome = System.getProperty("java.home");
        sysTv.setText("-----------------------" + "\n" +

        "OS版本:" + version + "\n" +
        "OS名称:" + name + "\n" +
        "OS架构:" + arch + "\n" +
        "Home属性:" + userHome + "\n" +
        "Name属性:" + userName + "\n" +
        "Dir属性:" + userDir + "\n" +
        "时区:" + userTimezone + "\n" +
        "路径分隔符:" + pathSeparator + "\n" +
        "行分隔符:" + lineSeparator + "\n" +
        "文件分隔符:" + fileSeparator + "\n" +
        "javaVendorUrl属性:" + javaVendorUrl + "\n" +
        "javaClassPath路径:" + javaClassPath + "\n" +
        "javaClass版本:" + javaClassVersion + "\n" +
        "javaVendor属性:" + javaVendor + "\n" +
        "Java版本:" + javaVersion + "\n" +
        "JavaHome属性:" + javaHome
        );
    }

    private void initBuild() {
        String board = Build.BOARD;
        String brand = Build.BRAND;
        String[] abis = Build.SUPPORTED_ABIS;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < abis.length; i++) {
            sb.append(abis[i] + "\n");
        }
        String device = Build.DEVICE;
        String display = Build.DISPLAY;
        String fingerprint = Build.FINGERPRINT;
        String serial = Build.SERIAL;
        String id = Build.ID;
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String hardware = Build.HARDWARE;
        String product = Build.PRODUCT;
        String tags = Build.TAGS;
        String type = Build.TYPE;
        String codename = Build.VERSION.CODENAME;
        String incremental = Build.VERSION.INCREMENTAL;
        String release = Build.VERSION.RELEASE;
        int sdkInt = Build.VERSION.SDK_INT;
        String host = Build.HOST;
        String user = Build.USER;
        long time = Build.TIME;
        tvBuild.setText("android.os.Build获取的信息如下:\n"
        +"主板:" + board + "\n"
        +"系统定制商:" + brand + "\n"
        +"CPU指令集:" + sb.toString() + "\n"
        +"设备参数:" + device + "\n"
        +"显示屏参数:" + display + "\n"
        +"唯一编号:" + fingerprint + "\n"
        +"硬件序列号:" + serial + "\n"
        +"修订版本列表:" + id + "\n"
        +"硬件制造商:" + manufacturer + "\n"
        +"版本:" + model + "\n"
        +"硬件名:" + hardware + "\n"
        +"手机产品名:" + product + "\n"
        +"描述Build的标签:" + tags + "\n"
        +"Build的类型:" + type + "\n"
        +"当前开发代号:" + codename + "\n"
        +"源码控制版本号:" + incremental + "\n"
        +"版本字符串:" + release + "\n"
        +"版本号:" + sdkInt + "\n"
        +"Host值:" + host + "\n"
        +"User名:" + user + "\n"
        +"编译时间:" + time + "\n"
        );
    }
}
