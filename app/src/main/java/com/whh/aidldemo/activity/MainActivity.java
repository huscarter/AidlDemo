package com.whh.aidldemo.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.whh.aidldemo.R;

public class MainActivity extends BaseActivity {

    Button btn_aidl;

    @Override
    public int setLayoutId() {
        return R.layout.activity_main;
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
                startActivity(new Intent(context, AidlActivity.class));
            }
        });
    }
}
