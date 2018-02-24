package com.soyute.aidl;

import android.os.IInterface;

/**
 * Created by linet on 2018/2/24.
 */

public interface IPlus extends IInterface {


    // 继承自IInterface接口->>分析4
    // 定义需要实现的接口方法，即Client进程需要调用的方法
    int add(int a,int b);


}
