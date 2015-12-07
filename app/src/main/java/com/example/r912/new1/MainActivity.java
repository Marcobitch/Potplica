package com.example.r912.new1;



import android.support.v7.app.AppCompatActivity;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;



public class MainActivity extends AppCompatActivity {

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

}

