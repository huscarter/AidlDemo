package com.whh.aidldemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.whh.aidldemo.aidl.Book;
import com.whh.aidldemo.aidl.BookManagerImpl;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Create by huscarter@163.com on 2020/9/22
 * <p>
 */
public class BookManagerService extends Service {
    private static final String TAG = BookManagerService.class.getSimpleName();
    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();
    private Binder mBinder = new BookManagerImpl(mBookList);

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "Android"));
        mBookList.add(new Book(2, "Ios"));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

}