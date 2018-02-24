package com.soyute.aidl;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by linet on 2018/2/24.
 */

public class MyService extends Service {

    private final static String TAG = "MyService";
    private static final String PACKAGE_SAYHI = "com.example.test";

    private NotificationManager mNotificationManager;
    private boolean mCanRun = true;

    // 步骤1：创建Binder对象 ->>分析1
    private Binder mBinder = new Stub();

    @Override
    public void onCreate() {

        Thread thr = new Thread(null, new ServiceWorker(), "BackgroundService");
        thr.start();

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        // 步骤2：创建 IInterface 接口类 的匿名类
        // 创建前，需要预先定义 继承了IInterface 接口的接口 -->分析3
        IInterface plus = new IPlus(){

            // 实现IInterface接口中唯一的方法
            @Override
            public IBinder asBinder() {
                return mBinder;
            }

            // 确定Client进程需要调用的方法
            public int add(int a, int b) {
                return a+b;
            }

        };


        // 步骤3
        mBinder.attachInterface(plus, "add two int");
        // 1. 将（add two int，plus）作为（key,value）对存入到Binder对象中的一个Map<String,IInterface>对象中
        // 2. 之后，Binder对象 可根据add two int通过queryLocalIInterface（）获得对应IInterface对象（即plus）的引用，可依靠该引用完成对请求方法的调用
        // 分析完毕，跳出

        super.onCreate();
    }


    @Override
    public void onDestroy() {
        mCanRun = false;
        super.onDestroy();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
//        return null;
        Log.d(TAG, String.format("on bind,intent = %s", intent.toString()));
//        displayNotificationMessage("服务已启动");
        return mBinder;
    }


    class ServiceWorker implements Runnable {
        long counter = 0;

        @Override
        public void run() {
            // do background processing here.....
            while (mCanRun) {
                Log.d("scott", "" + counter);
                counter++;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
