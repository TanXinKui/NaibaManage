package www.xinkui.com.restaurant.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TimeUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import www.xinkui.com.restaurant.R;
import www.xinkui.com.restaurant.bean.Recharge;
import www.xinkui.com.restaurant.bean.TranscationDetail;
import www.xinkui.com.restaurant.mqtt.MQTTObject;
import www.xinkui.com.restaurant.mqtt.MQTTService;
import www.xinkui.com.restaurant.network.NetWorkManager;
import www.xinkui.com.restaurant.network.exception.ApiException;
import www.xinkui.com.restaurant.network.response.ResponseTransformer;
import www.xinkui.com.restaurant.network.schedulers.SchedulerProvider;
import www.xinkui.com.restaurant.util.Util;

import static java.lang.Thread.sleep;

/**
 * Created by lenovo on 2018/2/27.
 */

/**
 * @author TONXOK
 * @description
 * @time 2019/4/27 18:19
 */
public class MainOderSpecific extends Activity {
    Button backbtn, comfirmbtn, refusebtn, contactClient, finishbtn, refreshBtn;
    TextView currentDesk, userName, oderTime, totalPay, clientNum;
    int deskNum, totalCost, confirmState, clientNum1, clientBalance;
    int deskOrder[];
    private long firstTime = 0;
    String currentUser, currentTime, clientPhone;
    Date currentDate;
    long timeStamp;
    String[] dishPrice = new String[]{"￥13", "￥11", "￥20", "￥14", "￥15",
            "￥13", "￥10", "￥11", "￥14"};
    int[] dishPrice1 = new int[]{13, 11, 20, 14, 15, 13, 10, 11, 14};
    String[] dishList = new String[]{"黑椒牛肉饼条饭+营养滋补炖汤", "香菇滑鸡饭+营养滋补炖汤", "NB照烧鸡腿饭+营养滋补炖汤", "奥尔良鸡排饭+营养滋补炖汤", "椒盐排骨饭+营养滋补炖汤",
            "黑椒鸡排焗饭", "黑椒牛肉焗饭", "咖喱海鲜焗饭", "咖喱鸡排焗饭"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specific_order);
        initViews();
        initClickEvent();
        sqlSearch();
    }

    private void initViews() {
        //菜单详情
        deskOrder = new int[9];
        oderTime = (TextView) findViewById(R.id.oderTime);
        userName = (TextView) findViewById(R.id.userName);
        totalPay = (TextView) findViewById(R.id.totalPay);
        clientNum = (TextView) findViewById(R.id.clientNum);
        //传入桌子参数
        Intent intent1 = getIntent();
        Bundle data1 = intent1.getExtras();
        deskNum = data1.getInt("desknum");

        //各个按钮监听
        backbtn = (Button) findViewById(R.id.colseCurrentPage);
        comfirmbtn = (Button) findViewById(R.id.comfirmAccept);
        refusebtn = (Button) findViewById(R.id.refuseAccept);
        contactClient = (Button) findViewById(R.id.contactClient);
        finishbtn = (Button) findViewById(R.id.finishAccept);
        //订单标题
        currentDesk = (TextView) findViewById(R.id.showCurrentDesk);
    }

    private void initClickEvent() {
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainOderSpecific.this, MainActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(0, 0);
            }
        });
        comfirmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comfirmbtn.setVisibility(View.INVISIBLE);
                refusebtn.setVisibility(View.INVISIBLE);
                Handler myhandler2 = new Handler();
                Runnable myrunnable2 = new Runnable() {
                    @Override
                    public void run() {
                        sqlConfirm();
                    }

                };
                myhandler2.post(myrunnable2);
            }
        });
        refusebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlRefuse();
                comfirmbtn.setVisibility(View.INVISIBLE);
                refusebtn.setVisibility(View.INVISIBLE);
            }
        });
        contactClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + clientPhone));
                dialIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialIntent);
            }
        });
        finishbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlFinish();
                Intent intent = new Intent(MainOderSpecific.this, MainActivity.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(0, 0);
            }
        });
        currentDesk.setText(deskNum + "号桌订单详情");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(MainOderSpecific.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {

                super.onDestroy();
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.addCategory(Intent.CATEGORY_HOME);
                startActivity(home);
                this.finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showList() {
        ListView list = (ListView) findViewById(R.id.specific_oderlist);
        ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 9; i++) {
            if (deskOrder[i] > 0) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("dishName", dishList[i]);
                map.put("dishNum", "X" + deskOrder[i]);
                map.put("dishPrice", dishPrice[i]);
                mylist.add(map);
            }
        }
        SimpleAdapter simpleA = new SimpleAdapter(
                this,
                mylist,
                R.layout.specific_list,
                new String[]{"dishName", "dishNum", "dishPrice"},
                new int[]{R.id.dishName, R.id.dishNum, R.id.dishPrice}
        );
        list.setAdapter(simpleA);
    }

    public void sqlSearch() {

        //在android中操作数据库最好在子线程中执行，否则可能会报异常
        NetWorkManager.getRequest().getTranscationDetail(deskNum)
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Observer<TranscationDetail>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TranscationDetail transcationDetail) {
                        deskOrder[0]=transcationDetail.getMenu().getT1();
                        deskOrder[1]=transcationDetail.getMenu().getT2();
                        deskOrder[2]=transcationDetail.getMenu().getT3();
                        deskOrder[3]=transcationDetail.getMenu().getT4();
                        deskOrder[4]=transcationDetail.getMenu().getT5();
                        deskOrder[5]=transcationDetail.getMenu().getT6();
                        deskOrder[6]=transcationDetail.getMenu().getT7();
                        deskOrder[7]=transcationDetail.getMenu().getT8();
                        deskOrder[8]=transcationDetail.getMenu().getT9();
                        totalCost=transcationDetail.getSumup();
                        timeStamp=transcationDetail.getCurrentTime();
                        currentUser=transcationDetail.getUsername();
                        clientNum1=transcationDetail.getClientNum();
                        confirmState=transcationDetail.getConfirmState();
                        clientPhone=transcationDetail.getPhone();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        showList();
                        try {
                            String time = logTimeOfDay(timeStamp);
                            oderTime.setText("下单时间：" + time);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        userName.setText("用户名称：" + currentUser);
                        totalPay.setText("小计：" + totalCost + "￥");
                        clientNum.setText("人数：" + clientNum1);
                        if (confirmState == 1) {
                            comfirmbtn.setVisibility(View.INVISIBLE);
                            refusebtn.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    public void sqlConfirm() {
        NetWorkManager.getRequest().setStateManage(deskNum,1,2)
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe();
        MQTTObject object=new MQTTObject(Util.MQTT_TOPIC,"orderResponse");
        MQTTService.getMqttConfig().sendMessage(Util.MQTT_TOPIC,new Gson().toJson(object),0);
    }

    public void sqlRefuse() {
        NetWorkManager.getRequest().setStateManage(deskNum,-1,2)
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe();
        MQTTObject object=new MQTTObject(Util.MQTT_TOPIC,"orderResponse");
        MQTTService.getMqttConfig().sendMessage(Util.MQTT_TOPIC,new Gson().toJson(object),0);
       withdrawBalance(new Recharge(currentUser,totalCost));
    }
    private void withdrawBalance(Recharge recharge) {
        NetWorkManager.getRequest().recharge(recharge)
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.v(this.getClass().getName(), d.toString());
                    }

                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(MainOderSpecific.this, "已退回订单金额！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void sqlFinish() {
        NetWorkManager.getRequest().setFinishState(deskNum)
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe();
        MQTTObject object=new MQTTObject(Util.MQTT_TOPIC,"orderResponse");
        MQTTService.getMqttConfig().sendMessage(Util.MQTT_TOPIC,new Gson().toJson(object),0);
    }

    private String logTimeOfDay(long millis) {
        Calendar c = Calendar.getInstance();
        if (millis >= 0) {
            c.setTimeInMillis(millis);
            return String.format("%ty年%tm月%td日 %tH:%tM:%tS",c, c, c, c, c, c);
        } else {
            return Long.toString(millis);
        }
    }
}
