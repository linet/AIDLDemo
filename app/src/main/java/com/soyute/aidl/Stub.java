package com.soyute.aidl;

import android.os.Binder;
import android.os.Parcel;
import android.os.RemoteException;

/**
 * Created by linet on 2018/2/24.
 */

public class Stub extends Binder {


    public final static int add = 0x00001;









    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {

        switch (code) {
            case Stub.add: {

                data.enforceInterface("add two int");

                int  arg0  = data.readInt();
                int  arg1  = data.readInt();

                IPlus plus = (IPlus) queryLocalInterface("add two int");
                if (plus != null) {
                    int  result = plus.add( arg0,  arg1);
                    reply.writeInt(result);
                    return true;
                }

            }
        }


        return super.onTransact(code, data, reply, flags);
    }
}
