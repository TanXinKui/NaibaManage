package www.xinkui.com.restaurant.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import www.xinkui.com.restaurant.R;
import www.xinkui.com.restaurant.bean.Admin;
import www.xinkui.com.restaurant.bean.Recharge;
import www.xinkui.com.restaurant.network.NetWorkManager;
import www.xinkui.com.restaurant.network.response.ResponseTransformer;
import www.xinkui.com.restaurant.network.schedulers.SchedulerProvider;
import www.xinkui.com.restaurant.user.UserLogin;
import www.xinkui.com.restaurant.util.Util;

import static java.lang.Thread.sleep;

/**
 * Created by lenovo on 2018/3/4.
 */

/**
 * @author TONXOK
 * @description
 * @time 2019/4/27 22:02
 */
public class RechargeManage extends Activity {
    Button confirmRecharge, recharge_leftbtn, recharge_midbtn, exitbtn;
    String userName;
    int addToBlance = 0;
    EditText ed1;
    RadioGroup rg1;
    RadioButton rb1, rb2, rb3, rb4, rb5;
    private long firstTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rechargemanage);
        rg1 = (RadioGroup) findViewById(R.id.rBtnGrp);
        rb1 = (RadioButton) findViewById(R.id.RBtn0);
        rb2 = (RadioButton) findViewById(R.id.RBtn1);
        rb3 = (RadioButton) findViewById(R.id.RBtn2);
        rb4 = (RadioButton) findViewById(R.id.RBtn3);
        rb5 = (RadioButton) findViewById(R.id.RBtn4);
        exitbtn = (Button) findViewById(R.id.exitbtn);
        confirmRecharge = (Button) findViewById(R.id.confirmRecharge);
        ed1 = (EditText) findViewById(R.id.userName1);
        recharge_leftbtn = (Button) findViewById(R.id.recharge_bottom_left);
        recharge_midbtn = (Button) findViewById(R.id.recharge_bottom_mid);
        initViews();
    }

    private void initViews() {
        exitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RechargeManage.this, UserLogin.class);
                startActivity(intent);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(0, 0);
            }
        });
        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == rb1.getId()) {
                    addToBlance = 20;
                } else if (i == rb2.getId()) {
                    addToBlance = 50;
                } else if (i == rb3.getId()) {
                    addToBlance = 100;
                } else if (i == rb4.getId()) {
                    addToBlance = 200;
                } else if (i == rb5.getId()) {
                    addToBlance = 1000;
                }
            }
        });
        recharge_leftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RechargeManage.this, "正在刷新数据！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RechargeManage.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        recharge_midbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RechargeManage.this, "正在刷新数据！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RechargeManage.this, DishManage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        confirmRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmRecharge.setText("正在充值！");
                userName = ed1.getText().toString();
                addBalance(new Recharge(userName, addToBlance));
            }
        });
    }

    private void addBalance(Recharge recharge) {
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
                        Log.v(this.getClass().getName(), "finish");
                        Toast.makeText(RechargeManage.this, "充值完成！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(RechargeManage.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
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
}
