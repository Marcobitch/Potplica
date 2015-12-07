package com.example.r912.new1;



import android.support.v7.app.AppCompatActivity;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {


    private boolean isConnected;

    private TextView mStatusTv;
    private Button mActivateBtn;
    private Button mPairedBtn;
    private Button mScanBtn;
    private Button mControlBtn;

    private ProgressDialog mProgressDlg;

    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket socket_nxt1;

    int tem_val=0;
    int wat_val=0;
    int sun_val=0;

    /*
     * alarm status
     * 0 : 부족
     * 1 : 적당
     * 2 : 과잉
     * 물도 부족하고 빛도 부족하면 어떤식으로 출력? 생각해 보자.
     *
     */
    //int alarmStat = 0;
    int watStat = 0;
    int sunStat = 0;

    TextView sun_text;
    TextView wat_text;
    TextView tem_text;
    TextView alarm_text;

    String low="부족";
    String mid="적당";
    String high="과잉";

    //water button, light button 버튼이 눌린걸 확인하기 위한 flag
    boolean watSupply = false;
    boolean sunSupply = false;

    //수분, 광량에 따른 한계점
    int wat_low=3,wat_mid=6,sun_thd=500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






        //온도값을 출력
        //wat_text=(TextView) findViewById(R.id.wat_view);
        //sun_text=(TextView) findViewById(R.id.sun_view);
        //tem_text=(TextView) findViewById(R.id.tem_view);
        //alarm_text=(TextView) findViewById(R.id.alarm_view);
/*

        //back Thread
        BackThread backThread = new BackThread();
        backThread.setDaemon(true); // shut down this thread when main thread is killed
        backThread.start();

        // btn handler
        Button watBtn = (Button)findViewById(R.id.wat_button);
        watBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                System.out.println("water supply");
                watSupply = true;
            }
        });

        Button sunBtn = (Button)findViewById(R.id.sun_button);
        sunBtn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                System.out.println("light supply");
                sunSupply = true;
            }
        });*/





        //습도 값에 따라 상태출력
        /*
        if(wat_val<wat_low){
            wat_text=(TextView) findViewById(R.id.wat_view);
            wat_text.setText(""+low);
            alram_text=(TextView) findViewById(R.id.alram_view);
            alram_text.setText("목말라!!");

        }
        if(wat_low<=wat_val&&wat_val<wat_mid){
            wat_text=(TextView) findViewById(R.id.wat_view);
            wat_text.setText(""+mid);
        }
        if(wat_mid<=wat_val){
            wat_text=(TextView) findViewById(R.id.wat_view);
            wat_text.setText(""+high);
            alram_text=(TextView) findViewById(R.id.alram_view);
            alram_text.setText("퉁퉁 불겠군.");
        }

        //빛 값에 따라 상태출력
        if(sun_val<sun_thd){
            sun_text=(TextView) findViewById(R.id.sun_view);
            sun_text.setText(""+low);
            alram_text=(TextView) findViewById(R.id.alram_view);
            alram_text.setText("어디로 가야하오.");
        }
        else{
            sun_text=(TextView) findViewById(R.id.sun_view);
            sun_text.setText(""+mid);
            alram_text=(TextView) findViewById(R.id.alram_view);
            alram_text.setText("일광욕 하기 딱 좋은 날씨군.");
        }
        */

        /* 블루투스 코드
        *
        * */


        mStatusTv = (TextView) findViewById(R.id.tv_status);
        mActivateBtn = (Button) findViewById(R.id.btn_enable);
       // mPairedBtn = (Button) findViewById(R.id.btn_view_paired);
        mScanBtn = (Button) findViewById(R.id.btn_scan);
        mControlBtn = (Button) findViewById(R.id.btn_control);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        isConnected = false;

        mProgressDlg = new ProgressDialog(this);

        mProgressDlg.setMessage("Scanning...");
        mProgressDlg.setCancelable(false);
        mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        mBluetoothAdapter.cancelDiscovery();
                    }
                });

        if (mBluetoothAdapter == null) {
            showUnsupported();
        } else {
            /*mPairedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
                            .getBondedDevices();

                    if (pairedDevices == null || pairedDevices.size() == 0) {
                        showToast("No Paired Devices Found");
                    } else {
                        ArrayList<BluetoothDevice> list = new ArrayList<BluetoothDevice>();

                        list.addAll(pairedDevices);

                        Intent intent = new Intent(MainActivity.this,
                                DeviceListActivity.class);

                        intent.putParcelableArrayListExtra("device.list", list);

                        startActivity(intent);
                    }
                }
            });
*/
            mScanBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    mBluetoothAdapter.startDiscovery();
                }
            });

            mActivateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.disable();

                        showDisabled();
                    } else {
                        Intent intent = new Intent(
                                BluetoothAdapter.ACTION_REQUEST_ENABLE);

                        startActivityForResult(intent, 1000);
                    }
                }
            });
			/*
			 * Blutooth Paring
			 */

           mControlBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

					 // Intent intent = new Intent(MainActivity.this,
                    //ControlActivity.class); startActivity(intent);
                    if (!connectToNXTs() && !isConnected)
                        Toast.makeText(MainActivity.this, "FAIL",
                                Toast.LENGTH_SHORT).show();
                    try {
                        Constant.out = new OutputStreamWriter(socket_nxt1
                                .getOutputStream());
                        Constant.in = new InputStreamReader(socket_nxt1
                                .getInputStream());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


					 // If board is paring with arduino, start Potplica

                    if (Constant.out == null) {
                        Toast.makeText(MainActivity.this, "You should be Pairing",
                                Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(MainActivity.this, "Pairing Success",
                                Toast.LENGTH_SHORT).show();

                    }
                }
            });
			/*
			 * Select Mode Activity ( Freeriding , Control )
			 */

            if (mBluetoothAdapter.isEnabled()) {
                showEnabled();
            } else {
                showDisabled();
            }
        }

        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);



    }


    /*
     * back Thread / 1126
     * daemon thread로 설정 해놨으므로 메인 쓰레드가 종료될 때 같이 죽음
     *
     */

    class BackThread extends Thread{
        public void run(){
            while(true){

                /*
                 * 쓰레드 돌아가는거 확인하기 위해 임시로 값을 변경하게 해놓음
                 * 여기서 아두이노에서 받아오는 값으로 wat, tem, sun val을 바꿔준다.
                 */
                wat_val++;
                tem_val++;
                sun_val++;

                //입력 받은 값에 따라 water, light 에 대한 status 를 변경
                if(wat_val<wat_low){
                   watStat = 0;
                }
                if(wat_low<=wat_val&&wat_val<wat_mid){
                   watStat = 1;
                }
                if(wat_mid<=wat_val){
                  watStat=2;
                }

                //빛 값에 따라 상태출력
                if(sun_val<sun_thd){
                    sunStat=0;
                }
                else{
                    sunStat=1;
                }



                /*
                 * 버튼 누르면 sunsupply, watsupply 값이 true로 바뀜,
                 * 이 경우 블루투스롤 통해 아두이노에 데이터 전송하면 됨.
                 */
                if(sunSupply){
                    sun_val=0;
                    sunSupply = false;
                }

                if(watSupply){
                    wat_val=0;
                    watSupply=false;
                }


                /*
                 * 핸들러를 통해 변한 값이 지속적으로 출력
                 */
                mHandler.sendEmptyMessage(0);

                try{Thread.sleep(1000);} catch (InterruptedException e){;}
            }
        }
    }


    /*
     * 값이 변경 되었을 때 바로 화면에 출력하기 위해 핸들러 이용
     */

    Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            if(msg.what == 0){
                /*
                 * 나중에 값을 출력하는게 아니라 특정 값에 따라 충분 / 보통 / 부족 출력하게 수정
                 */
                sun_text.setText("" + sun_val);
                tem_text.setText("" + tem_val);
                wat_text.setText("" + wat_val);

            switch(watStat){
                case 0:
                    alarm_text.setText("low water");
                    break;
                case 1:
                    alarm_text.setText("mid water");
                    break;
                case 2:
                    alarm_text.setText("high water");
                    break;
            }
          }
        }
    };

    @Override
    public void onPause() {
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
        }

        super.onPause();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);

        super.onDestroy();
    }

    private void showEnabled() {
        mStatusTv.setText("Bluetooth is On");
        mStatusTv.setTextColor(Color.BLUE);

        mActivateBtn.setText("Disable");
        mActivateBtn.setEnabled(true);

//        mPairedBtn.setEnabled(true);
       mScanBtn.setEnabled(true);
    }

    private void showDisabled() {
        mStatusTv.setText("Bluetooth is Off");
        mStatusTv.setTextColor(Color.RED);

        mActivateBtn.setText("Enable");
        mActivateBtn.setEnabled(true);

    //    mPairedBtn.setEnabled(false);
        mScanBtn.setEnabled(false);
    }

    private void showUnsupported() {
        mStatusTv.setText("Bluetooth is unsupported by this device");

        mActivateBtn.setText("Enable");
        mActivateBtn.setEnabled(false);

//        mPairedBtn.setEnabled(false);
     mScanBtn.setEnabled(false);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
                .show();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(
                        BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                if (state == BluetoothAdapter.STATE_ON) {
                    showToast("Enabled");

                    showEnabled();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                mDeviceList = new ArrayList<BluetoothDevice>();

                mProgressDlg.show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                    .equals(action)) {
                mProgressDlg.dismiss();

                Intent newIntent = new Intent(MainActivity.this,
                        DeviceListActivity.class);

                newIntent.putParcelableArrayListExtra("device.list",
                        mDeviceList);

                startActivity(newIntent);
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                mDeviceList.add(device);

                showToast("Found device " + device.getName());
            }
        }
    };

    private BluetoothDevice getPairedDevice() {

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
                .getBondedDevices();

        // If there are paired devices

        if (pairedDevices != null && pairedDevices.size() > 0) {
            // Loop through paired devices

            for (BluetoothDevice device : pairedDevices) {
				/*
				 * String deviceName = device.getName(); String deviceAddress =
				 * device.getAddress();
				 */
                if(device.getAddress().equals("98:D3:31:20:3D:A7")) // arduino 98:D3:31:20:3D:A7 , Speaker A0:E9:DB:12:OE:47
                     return device;
            }

			/*
			 * boolean isDuplicate = false; for (DeviceData deviceData :
			 * mInfoList) { if (deviceData.getAddress().equals(deviceAddress)) {
			 * isDuplicate = true; } } if (!isDuplicate) { DeviceData data = new
			 * DeviceData(); data.setName(deviceName);
			 * data.setAddress(deviceAddress);
			 *
			 * mInfoList.add(data); mAdapter.notifyDataSetChanged(); }
			 */
        }
        return null;
    }

    // connect to both NXTs
    private boolean connectToNXTs() {

        // get the BluetoothDevice of the NXT
        // try to connect to the nxt
        boolean success;

        try {
            socket_nxt1 = getPairedDevice().createRfcommSocketToServiceRecord(
                    UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            socket_nxt1.connect();
            success = true;
            isConnected = true;
        } catch (IOException e) {
            Log.d("Bluetooth", "Err: Device not found or cannot connect");
            success = false;
        }
        return success;
    }

}

