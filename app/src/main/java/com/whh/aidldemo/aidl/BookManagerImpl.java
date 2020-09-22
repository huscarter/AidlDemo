package com.whh.aidldemo.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Create by huscarter@163.com on 2020/9/22
 * <p>
 */
public class BookManagerImpl extends Binder implements IBookManager {
    private static final String TAG = BookManagerImpl.class.getSimpleName();

    private CopyOnWriteArrayList<Book> mBookList;

    /**
     * Construct the stub at attach it to the interface.
     */
    public BookManagerImpl() {
        this.attachInterface(this, DESCRIPTOR);
    }

    public BookManagerImpl(CopyOnWriteArrayList<Book> mBookList) {
        this();
        this.mBookList = mBookList;
    }

    /**
     * 用于将服务端的Binder对象转换成客户端所需的AIDL接口类型的对象，这种转换过程是区分进程的，如果客户端和服务端位于同一进程，
     * 那么此方法返回的就是服务端的Stub对象本身，否则返回的是系统封装后的Stub.proxy对象。
     */
    public static IBookManager asInterface(IBinder obj) {
        if ((obj == null)) {
            return null;
        }
        android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
        if (((iin != null) && (iin instanceof IBookManager))) {
            return ((IBookManager) iin);
        }
        return new BookManagerImpl.Proxy(obj);
    }

    /**
     * 返回当前Binder对象
     *
     * @return IBinder
     */
    @Override
    public IBinder asBinder() {
        return this;
    }

    /**
     * 这个方法运行在服务端中的Binder线程池中，当客户端发起跨进程请求时，远程请求会通过系统底层封装后交由此方法来处理
     *
     * @param code  服务端通过code可以确定客户端所请求的目标方法是什么
     * @param data  从中取出参数
     * @param reply 返回值
     * @param flags
     * @return false：客户端请求失败，true：客户端请求成功
     * @throws RemoteException
     */
    @Override
    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        Log.i(TAG, "onTransact code:" + code + ",flags:" + flags);
        switch (code) {
            case INTERFACE_TRANSACTION: {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            case TRANSACTION_getBookList: {
                data.enforceInterface(DESCRIPTOR);
                List<Book> result = this.getBookList();
                reply.writeNoException();
                reply.writeTypedList(result);
                return true;
            }
            case TRANSACTION_addBook: {
                data.enforceInterface(DESCRIPTOR);
                Book arg0;
                if ((0 != data.readInt())) {
                    arg0 = Book.CREATOR.createFromParcel(data);
                } else {
                    arg0 = null;
                }
                this.addBook(arg0);
                reply.writeNoException();
                return true;
            }
        }
        return super.onTransact(code, data, reply, flags);
    }

    /**
     * 这个方法运行在服务器端。
     * <p>
     * 当客户端远程调用此方法时，它的内部实现过程：
     * <li>首先创建该方法所需要的输入型Parcel对象_data、输出型Parcel对象_reply和返回值对象List；
     * <li>然后把该方法的参数信息写入_data中（如果有参数的话）；
     * <li>接着调用transact方法来发起RPC（远程过程调用）请求，同时当前线程挂起；
     * <li>然后服务端的onTransact方法会被调用，直到RPC过程返回后，当前线程继续执行，并从_reply中取出RPC过程的返回结果；
     * <li>最后返回_reply中的数据。
     *
     * @return
     * @throws RemoteException
     */
    @Override
    public List<Book> getBookList() throws RemoteException {
        Log.i(TAG, "getBookList");
        return mBookList;
    }

    @Override
    public void addBook(Book book) throws RemoteException {
        Log.i(TAG, "addBook book:"+book);
        mBookList.add(book);
    }

    /**
     * IBookManager 代理类
     */
    private static class Proxy implements IBookManager {
        private static final String TAG = Proxy.class.getSimpleName();

        private IBinder mRemote;

        Proxy(IBinder remote) {
            mRemote = remote;
        }

        @Override
        public IBinder asBinder() {
            return mRemote;
        }

        public String getInterfaceDescriptor() {
            return DESCRIPTOR;
        }

        /**
         * 运行在客户端
         *
         * @return
         * @throws RemoteException
         */
        @Override
        public List<Book> getBookList() throws RemoteException {
            Log.i(TAG, "getBookList");
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            List<Book> result;
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                mRemote.transact(TRANSACTION_getBookList, data, reply, 0);
                reply.readException();
                result = reply.createTypedArrayList(Book.CREATOR);
            } finally {
                reply.recycle();
                data.recycle();
            }
            return result;
        }

        /**
         * 运行在客户端
         *
         * @param book
         * @throws RemoteException
         */
        @Override
        public void addBook(Book book) throws RemoteException {
            Log.i(TAG, "addBook book:" + book);
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                if ((book != null)) {
                    data.writeInt(1);
                    book.writeToParcel(data, 0);
                } else {
                    data.writeInt(0);
                }
                mRemote.transact(TRANSACTION_addBook, data, reply, 0);
                reply.readException();
            } finally {
                reply.recycle();
                data.recycle();
            }
        }
    }
}