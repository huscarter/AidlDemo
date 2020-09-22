package com.whh.aidldemo.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.whh.aidldemo.R;
import com.whh.aidldemo.aidl.Book;
import com.whh.aidldemo.aidl.BookManagerImpl;
import com.whh.aidldemo.aidl.IBookManager;
import com.whh.aidldemo.service.BookManagerService;

import java.util.List;

/**
 *
 */
public class AidlActivity extends BaseActivity {

    Button btn_aidl;

    ServiceConnection connection = new InnerServiceConnection();

    @Override
    public int setLayoutId() {
        return R.layout.activity_aidl;
    }

    @Override
    public void initData() {
        TAG = AidlActivity.class.getSimpleName();
    }

    @Override
    public void initView() {
        btn_aidl = findViewById(R.id.btn_aidl);

        setListener();
    }

    private void setListener() {
        btn_aidl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, BookManagerService.class);
                bindService(intent,connection, Context.BIND_AUTO_CREATE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        unbindService(connection); // 可以不写，当activity关闭时bindService会自动关闭
        super.onDestroy();
    }

    /**
     * inner class
     */
    static class InnerServiceConnection implements ServiceConnection {
        private static final String TAG = InnerServiceConnection.class.getSimpleName();

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected");
            IBookManager bookManager = BookManagerImpl.asInterface(service);
            try {
                List<Book> list = bookManager.getBookList();
                Log.i(TAG, "query book list:" + list.getClass()+",size:"+list.size());
                bookManager.addBook(new Book(3,"test book"));
                list = bookManager.getBookList();
                Log.i(TAG, "query book list:" + list.getClass()+",size:"+list.size());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, "onServiceDisconnected");
        }

        @Override
        public void onBindingDied(ComponentName name) {
            Log.i(TAG, "onBindingDied");
        }

        @Override
        public void onNullBinding(ComponentName name) {
            Log.i(TAG, "onNullBinding");
        }
    }
}
