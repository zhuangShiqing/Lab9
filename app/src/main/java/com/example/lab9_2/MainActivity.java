package com.example.lab9_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView tv_clock;
    private Button btn_start;
    private Boolean flag =false;
    //建立BroadcastReceiver
    private BroadcastReceiver receiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //在onReceive()中加入接收廣播後要執行的動作
                Bundle b =intent.getExtras();//解析Intent取得秒數資訊
                tv_clock.setText(String.format("%02d:%02d:%02d",
                        b.getInt("H"),b.getInt("M"),b.getInt("S")));

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_clock =findViewById(R.id.tv_clock);
        btn_start =findViewById(R.id.btn_start);
        //註冊Receiver
        registerReceiver(receiver,new IntentFilter("MyMessage"));
        //取得Service狀態
        flag =MyService.flag;
        if(flag)
            btn_start.setText("暫停");
        else
            btn_start.setText("開始");
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag =!flag;
                if(flag){
                    btn_start.setText("暫停");
                    Toast.makeText(MainActivity.this,
                            "計時開始", Toast.LENGTH_SHORT).show();
                }
                else{
                    btn_start.setText("開始");
                    Toast.makeText(MainActivity.this,
                            "計時暫停", Toast.LENGTH_SHORT).show();
                }
                //啟動Service
                startService(new Intent(MainActivity.this,
                        MyService.class).putExtra("flag",flag));

            }
        });
    }
    public void onDestroy(){
        super.onDestroy();
        //註銷廣播
        unregisterReceiver(receiver);
    }
}