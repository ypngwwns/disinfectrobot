package com.hitqz.disinfectionrobot.log;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;

import com.blankj.utilcode.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Class: CrashUtil
 * Description: 捕获异常，保存在sd卡app名、crash目录下
 */
public class CrashUtil implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashUtil";
    /**
     * 类单例
     */
    private static CrashUtil mInstance = null;
    private static Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    /**
     * 存储设备信息和异常信息
     */
    private final Map<String, String> infos = new HashMap<String, String>();
    /**
     * 格式化日期，作为日志文件名的一部分
     */
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault());
    private String mCrashLogPath;
    private Context mContext;

    private CrashUtil() {

    }

    public static CrashUtil getInstance() {
        if (null == mInstance) {
            synchronized (CrashUtil.class) {
                if (null == mInstance) {
                    mInstance = new CrashUtil();
                }
            }
        }
        return mInstance;
    }

    public static String getSDPath(Context context) {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);// 判断sd卡是否存在
        if (sdCardExist) {
            if (Build.VERSION.SDK_INT >= 29) {
                //Android10之后
                sdDir = context.getExternalFilesDir(null);
            } else {
                sdDir = Environment.getExternalStorageDirectory();// 获取SD卡根目录
            }
        } else {
            sdDir = Environment.getRootDirectory();// 获取跟目录
        }
        return sdDir.toString();
    }

    public void init(Context context, String appName) {
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
        mCrashLogPath = getSDPath(context) + "/" + appName + "/crash";
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        try {
            handleException(ex);
        } catch (Exception e) {
            Log.e(TAG, "crsh 收集错误");
            e.printStackTrace();
        }

        if (mDefaultCrashHandler != null) {
            // 让系统默认的处理器处理
            SystemClock.sleep(500);
            mDefaultCrashHandler.uncaughtException(thread, ex);
        }
    }

    /**
     * 自定义错误处理，收集错误信息等
     */
    private boolean handleException(Throwable ex) {
        // 处理异常，可以提示用户崩溃了，或保存重要信息，尝试恢复现场，或者直接杀死进程
        if (ex == null) {
            return false;
        }

        // 收集设备信息
        collectDeviceInfo();
        // 保存日志文件
        saveCrashInfoToFile(ex);
        return true;
    }

    /**
     * 收集设备信息
     */
    private void collectDeviceInfo() {
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                // 应用版本信息
                infos.put("App Version", pi.versionName + '_' + pi.versionCode + "\n");
                // Android 系统版本信息
                infos.put("OS Version", Build.VERSION.RELEASE + '_' + Build.VERSION.SDK_INT + "\n");
                // 设备ID
                infos.put("Device ID", Build.ID + "\n");
                // 设备序列号
                infos.put("Device Serial", Build.SERIAL + "\n");
                // 手机制造商
                infos.put("Manufacturer", Build.MANUFACTURER + "\n");
                // 手机型号
                infos.put("Model", Build.MODEL + "\n");
                // CPU架构
                infos.put("CPU ABI", Build.CPU_ABI + "\n");
                // 手机品牌
                infos.put("Brand", Build.BRAND + "\n");
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "an error occurred when collect package info");
            e.printStackTrace();
        }

//        Field[] fields = Build.class.getDeclaredFields();
//        for (Field field : fields)
//        {
//            try
//            {
//                field.setAccessible(true);
//                infos.put(field.getName(), field.get(null).toString());
//                LogUtil.d(field.getName() + " : " + field.get(null));
//            }
//            catch (IllegalAccessException e)
//            {
//                LogUtil.e("an error occured when collect crash info");
//                e.printStackTrace();
//            }
//        }
    }

    /**
     * 保存错误信息到文件中
     */
    private String saveCrashInfoToFile(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key);
            sb.append(" : ");
            sb.append(value);
            sb.append("\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();

        String result = writer.toString();
        sb.append("\n");
        sb.append(result);

        try {
            long currentTime = System.currentTimeMillis();
            String time = formatter.format(new Date(currentTime));
            String fileName = "crash_" + time + "_" + currentTime + ".log";

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                File dir = new File(mCrashLogPath);
//                if (!dir.exists()) {
//                    boolean s = dir.mkdirs();
//                    Log.d(TAG, "创建文件夹：" + s);
//                }
//
                File file = new File(mCrashLogPath + "/" + fileName);
//                if (!file.exists()) {
//                    boolean s = file.createNewFile();
//                    Log.d(TAG, "创建文件：" + s);
//                }

                boolean created = FileUtils.createOrExistsFile(file);
                Log.d(TAG, "创建文件：" + created);

                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(sb.toString().getBytes());
                fileOutputStream.close();
            }

            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occurred while writing file...");
            e.printStackTrace();
        }

        return "";
    }
}
