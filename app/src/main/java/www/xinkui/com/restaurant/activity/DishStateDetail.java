package www.xinkui.com.restaurant.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import www.xinkui.com.restaurant.R;
import www.xinkui.com.restaurant.bean.SellState;
import www.xinkui.com.restaurant.network.NetWorkManager;
import www.xinkui.com.restaurant.network.response.ResponseTransformer;
import www.xinkui.com.restaurant.network.schedulers.SchedulerProvider;
import www.xinkui.com.restaurant.util.Util;

import static java.lang.Thread.sleep;

/**
 * Created by lenovo on 2018/3/4.
 */

/**
 * @author TONXOK
 * @description
 * @time 2019/4/27 18:17
 */
public class DishStateDetail extends Activity {
    Button confbtn;
    int deskNum1, deskState1;
    TextView tv1, tv2;
    String dishList2[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dishstate_specific);
        dishList2 = new String[]{"黑椒牛肉饼条饭+营养滋补炖汤", "香菇滑鸡饭+营养滋补炖汤", "NB照烧鸡腿饭+营养滋补炖汤", "奥尔良鸡排饭+营养滋补炖汤", "椒盐排骨饭+营养滋补炖汤",
                "黑椒鸡排焗饭", "黑椒牛肉焗饭", "咖喱海鲜焗饭", "咖喱鸡排焗饭"};
        tv1 = (TextView) findViewById(R.id.dishName);
        tv2 = (TextView) findViewById(R.id.dishstate);
        Intent intent1 = getIntent();
        Bundle data1 = intent1.getExtras();
        deskNum1 = data1.getInt("dishorder");
        deskState1 = data1.getInt("dishstate");
        tv1.setText(dishList2[deskNum1]);
        if (deskState1 == 1) {
            tv2.setText("更改为：售完");
        } else if (deskState1 == 0) {
            tv2.setText("更改为：正在出售");
        }
        confbtn = (Button) findViewById(R.id.confirmChange);
        confbtn.setOnClickListener(v -> {
                    confbtn.setText("正在更新数据！");
                    int realposition = deskNum1 + 1;
                    sqlSearch(realposition, deskState1);
                }
        );
    }

    public void sqlSearch(final int currentorder, final int deskState1) {
        SellState ss = new SellState(currentorder, deskState1 == 0 ? 1 : 0);
        NetWorkManager.getRequest().setSellState(ss)
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        finish();
                    }
                });

    }
}
