package com.soyute.aidl;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    private static final String ACTION_BIND_SERVICE = "com.soyute.aidl.MyService";
//    private IMyService mIMyService;

    IBinder binderproxy;

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binderproxy = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            binderproxy = service;

            doPlus();

            //通过服务端onBind方法返回的binder对象得到IMyService的实例，得到实例就可以调用它的方法了

//            mIMyService = IMyService.Stub.asInterface(service);
//            try {
//                Student student = mIMyService.getStudent().get(0);
//                showDialog(student.toString());
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }

        }
    };

    private void doPlus() {

        if (binderproxy != null) {
            // 1. Client进程 将需要传送的数据写入到Parcel对象中
            // data = 数据 = 目标方法的参数（Client进程传进来的，此处就是整数a和b） + IInterface接口对象的标识符descriptor


            int a = 1;
            int b = 1;
            android.os.Parcel data = android.os.Parcel.obtain();
            data.writeInt(a);
            data.writeInt(b);

            data.writeInterfaceToken("add two int");
            // 方法对象标识符让Server进程在Binder对象中根据"add two int"通过queryLocalIInterface（）查找相应的IInterface对象（即Server创建的plus），Client进程需要调用的相加方法就在该对象中

            android.os.Parcel reply = android.os.Parcel.obtain();
            // reply：目标方法执行后的结果（此处是相加后的结果）

            // 2. 通过 调用代理对象的transact（） 将 上述数据发送到Binder驱动
            try {
                binderproxy.transact(Stub.add, data, reply, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            // 参数说明：
            // 1. Stub.add：目标方法的标识符（Client进程 和 Server进程 自身约定，可为任意）
            // 2. data ：上述的Parcel对象
            // 3. reply：返回结果
            // 0：可不管

            // 注：在发送数据后，Client进程的该线程会暂时被挂起
            // 所以，若Server进程执行的耗时操作，请不要使用主线程，以防止ANR


            // 3. Binder驱动根据 代理对象 找到对应的真身Binder对象所在的Server 进程（系统自动执行）
            // 4. Binder驱动把 数据 发送到Server 进程中，并通知Server 进程执行解包（系统自动执行）


        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startMyService();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    private void startMyService() {

        Intent intentService = new Intent(ACTION_BIND_SERVICE);
        intentService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        bindService(intentService, mServiceConnection, BIND_AUTO_CREATE);
    }


}
