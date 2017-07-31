package com.leo618.utils;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;


/**
 * function: 截获（记录）崩溃. : 当程序产生未捕获异常则有此类接管并将异常记录在SD卡应用根目录或应用缓存目录的.crashLog文件夹下面.
 * <p></p>
 * Created by lzj on 2015/12/31.
 */
@SuppressWarnings("ALL")
public class CrashHandler implements UncaughtExceptionHandler {
    /** 记录标志. */
    private static final String TAG = CrashHandler.class.getSimpleName();
    /** CrashHandler实例. */
    private static final AtomicReference<CrashHandler> instance = new AtomicReference<>();

    /** 初始化. */
    public static void init(Context context) {
        for (; ; ) {
            CrashHandler netManager = instance.get();
            if (netManager != null) return;
            netManager = new CrashHandler(context);
            if (instance.compareAndSet(null, netManager)) return;
        }
    }

    /** 程序的Context对象. */
    private final Context mContext;
    /** 用于格式化日期,作为日志文件名的一部分. */
    private final DateFormat formatter = new SimpleDateFormat("MMdd-HH:mm:ss", Locale.getDefault());

    /** 进程名字. 默认主进程名是包名 */
    private final String mProcessName;
    /** 系统默认的UncaughtException处理类. */
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private final UncaughtExceptionHandler mDefaultHandler;

    /** 保证只有一个CrashHandler实例. */
    private CrashHandler(Context context) {
        mContext = context.getApplicationContext();
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        mProcessName = PackageManagerUtil.getProcessNameByPid(mContext, Process.myPid());
    }

    /** 当UncaughtException发生时会转入该函数来处理. */
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        UIUtil.getHandler().removeCallbacksAndMessages(null);
        Log.e(TAG, "---------------uncaughtException start---------------\r\n");
        Log.e(TAG, "process [" + mProcessName + "],is abnormal!\r\n");
        throwable.printStackTrace();
        if (PackageManagerUtil.isMainProcess(mContext, mProcessName)) {
            Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
            if (intent != null) {
                AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                PendingIntent restartIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, restartIntent); // 100毫秒钟后重启应用activit
            }
        }
        try {
            handleException(thread, throwable);
        } catch (Exception ex) {
            Log.e(TAG, "uncaughtException,ex:" + ex.getMessage());
            ex.printStackTrace();
        }
        Log.e(TAG, "---------------uncaughtException end---------------\r\n");
        Process.killProcess(Process.myPid());
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     */
    @SuppressLint("DefaultLocale")
    private void handleException(Thread thread, Throwable rhrowable) throws IOException {
        //记录数量达到10个就清理数据
        File logDir = FileStorageUtil.getLogDir();
        if (logDir.exists()) {
            clearLogexMax(logDir);
        } else {
            logDir.mkdirs();
        }

        //写入错误信息到文件

        // 本次记录文件名
        Date date = new Date(); // 当前时间
        String logFileName = formatter.format(date) + String.format("[%s-%d]", thread.getName(), thread.getId()) + ".txt";
        File logex = new File(logDir, logFileName);
        logex.createNewFile();
        // 写入异常到文件中
        FileWriter fw = new FileWriter(logex, true);
        fw.write("\r\nProcess[" + mProcessName + "," + Process.myPid() + "]"); // 进程信息，线程信息
        fw.write("\r\n" + thread + "(" + thread.getId() + ")"); // 进程信息，线程信息
        fw.write("\r\nTime stamp：" + date); // 日期
        // 打印调用栈
        PrintWriter printWriter = new PrintWriter(fw);
        rhrowable.printStackTrace(printWriter);
        Throwable cause = rhrowable.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        fw.write("\r\n");
        fw.flush();
        printWriter.close();
        fw.close();
    }

    /**
     * 清理日志,限制日志数量.
     *
     * @param logdir 日志目录
     */
    private void clearLogexMax(File logdir) {
        File[] logList = logdir.listFiles();
        if (logList == null || logList.length == 0) {
            return;
        }
        int length = logList.length;
        if (length >= 10) {
            for (File aLogList : logList) {
                try {
                    if (aLogList.delete()) {
                        Log.d(TAG, "clearLogexMax delete:" + aLogList.getName());
                    }
                } catch (Exception ex) {
                    Log.e(TAG, "clearLogexMax,ex:" + ex);
                }
            }
        }
    }

}
