package howy.com.p2pinvest.common;

import android.os.Build;
import android.os.Looper;
import android.util.Log;

import howy.com.p2pinvest.util.UIUtils;

/**
 * Created by Howy on 2017/6/16.
 */

/*
 * 程序中的未捕获的全局异常的捕获（单例）
 *
 * 解决两个问题：
 * 1.当出现未捕获的异常时，能够给用户一个相对友好的提示
 * 2.在出现异常时，能够将异常信息发送给后台，便于在后续的版本中解决bug
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    //系统默认的处理未捕获异常的处理器
    private Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler;

    private CrashHandler() {

    }

    private static CrashHandler crashHandler = null;

    public static CrashHandler getInstance() {
        if (crashHandler == null) {
            crashHandler = new CrashHandler();
        }
        return crashHandler;
    }

    public void init() {
        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        //将当前类设置为出现未捕获异常的处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }


    //一旦系统出现未捕获的异常，就会调用如下的回调方法
    @Override
    public void uncaughtException(Thread t, Throwable ex) {
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                UIUtils.toast("亲，出现了未捕获的异常了！", true);
//                Log.e("TAG", "亲，出现了未捕获的异常了！" + ex.getMessage());
                Looper.loop();
            }
        }.start();
        collectionException(ex);
        try {
            Thread.sleep(2000);
            ActivityManager.getInstance().removeCurrent();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        Log.e("TAG", "亲，出现了未捕获的异常了！" + ex.getMessage());
//        Toast.makeText(UIUtils.getContext(), "亲，出现了未捕获的异常了！", Toast.LENGTH_SHORT).show();
    }

    private void collectionException(Throwable ex) {
        final String exMessage = ex.getMessage();
        final String message = Build.DEVICE + ":" + Build.MODEL + ":" + Build.PRODUCT + ":" + Build.VERSION.SDK_INT;
        new Thread() {
            @Override
            public void run() {
                Log.e("TAG", "exception = " + exMessage);
                Log.e("TAG", "message = " + message);
            }
        }.start();
    }

}
