package com.whh.aidldemo.aidl;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import java.util.List;

/**
 * Create by huscarter@163.com on 2020/9/20
 * <p>
 */
public interface IBookManager extends IInterface {
    //
    static final String DESCRIPTOR = "com.whh.algorithm.aidl.IBookManager";
    //
    static final int TRANSACTION_getBookList = IBinder.FIRST_CALL_TRANSACTION + 0;
    //
    static final int TRANSACTION_addBook = IBinder.FIRST_CALL_TRANSACTION+ 1;

    /**
     *
     * @return
     * @throws RemoteException
     */
    public List<Book> getBookList() throws RemoteException;

    /**
     *
     * @param book
     * @throws RemoteException
     */
    public void addBook(Book book) throws RemoteException;

}
