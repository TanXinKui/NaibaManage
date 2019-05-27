package www.xinkui.com.restaurant.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import www.xinkui.com.restaurant.R;
import www.xinkui.com.restaurant.bean.Advertisement;
import www.xinkui.com.restaurant.bean.SetAdvertisement;
import www.xinkui.com.restaurant.network.NetWorkManager;
import www.xinkui.com.restaurant.network.exception.ApiException;
import www.xinkui.com.restaurant.network.response.ResponseTransformer;
import www.xinkui.com.restaurant.network.schedulers.SchedulerProvider;
import www.xinkui.com.restaurant.util.Util;

import static java.lang.Thread.sleep;

/**
 * Created by lenovo on 2018/3/20.
 */

public class DishSpecialListDetail extends Activity {
    int currentId;
    int currentState;

    TextView special_dish_specific_title;
    String sed1, sed3, dishname, c3, description;
    int price;
    int sed2;
    EditText ed1, ed2, ed3;
    Button btn1, btn2, btn3;
    SetAdvertisement advertisement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.special_dish_specific);
        ed1 = (EditText) findViewById(R.id.special_dish_specific_ed1);
        ed2 = (EditText) findViewById(R.id.special_dish_specific_ed2);
        ed3 = (EditText) findViewById(R.id.special_dish_specific_ed3);
        btn1 = (Button) findViewById(R.id.special_dish_specific_btn1);
        btn2 = (Button) findViewById(R.id.special_dish_specific_btn2);
        btn3 = (Button) findViewById(R.id.special_dish_specific_btn3);

        //获得当前的广告栏位置
        Intent intent1 = getIntent();
        Bundle data1 = intent1.getExtras();
        currentId = data1.getInt("adPosition");
        currentState = data1.getInt("adState");
        price = data1.getInt("price");
        dishname = data1.getString("dishname");
        c3 = data1.getString("c3");
        description = data1.getString("description");
        advertisement = new SetAdvertisement(currentId + 1, dishname, c3, price, currentState, description);
        //写出当前位置
        special_dish_specific_title = (TextView) findViewById(R.id.special_dish_specific_title);
        special_dish_specific_title.setText(currentId == 0 ? ("商家信息栏" + (currentState == 0 ? "已关闭" : "正在发布"))
                        : (currentId + "号特色菜栏" + (currentState == 0 ? "已关闭" : "正在发布")));
        ed2.setVisibility(View.GONE);
        btn1.setOnClickListener(v -> {
            btn1.setText("正在刷新");
            sed1 = ed1.getText().toString();
            sed2 = ed2.getInputType();
            sed3 = ed3.getText().toString();
            sqlsearch1();
            finish();
        });
        btn2.setOnClickListener(v -> {
                    btn2.setText("正在刷新");
                    sed1 = ed1.getText().toString();
                    sed2 = ed2.getInputType();
                    sed3 = ed3.getText().toString();
                    sqlsearch2();
                    finish();
                }
        );
        btn3.setOnClickListener(v -> {
            btn3.setText("正在刷新");
            sed1 = ed1.getText().toString();
            sed2 = ed2.getInputType();
            sed3 = ed3.getText().toString();
            sqlsearch3();
            finish();
        });

    }

    public void sqlsearch1() {
        advertisement.setDishname(sed1);
        advertisement.setDescription(sed3);
        setAdvertisement();
    }

    public void sqlsearch2() {
        advertisement.setState(advertisement.getState() == 0 ? 1 : 0);
        setAdvertisement();
    }

    public void sqlsearch3() {
        advertisement.setDishname(sed1);
        advertisement.setDescription(sed3);
        advertisement.setState(advertisement.getState() == 0 ? 1 : 0);
        setAdvertisement();
    }

    private void setAdvertisement() {
        NetWorkManager.getRequest().setAdvertisementInfo(advertisement)
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe();
    }
}
