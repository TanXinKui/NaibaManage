package www.xinkui.com.restaurant.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import www.xinkui.com.restaurant.R;
import www.xinkui.com.restaurant.bean.Advertisement;
import www.xinkui.com.restaurant.bean.Menu;
import www.xinkui.com.restaurant.network.NetWorkManager;
import www.xinkui.com.restaurant.network.response.ResponseTransformer;
import www.xinkui.com.restaurant.network.schedulers.SchedulerProvider;
import www.xinkui.com.restaurant.util.Util;

import static java.lang.Thread.sleep;

/**
 * Created by lenovo on 2018/3/20.
 */

/**
 * @author TONXOK
 * @description
 * @time 2019/4/27 18:17
 */
public class DishSpecial extends ListActivity {
    ListView listvie1;
    int state1[] = new int[5];
    Button backbtn;
    private long firstTime = 0;
    private ArrayList<String> dishnameList = new ArrayList<>();
    private ArrayList<String> c3List = new ArrayList<>();
    private ArrayList<Integer> priceList = new ArrayList<>();
    private ArrayList<String> descriptionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.special_dish);
        initViews();
    }

    private void initViews() {
        backbtn = (Button) findViewById(R.id.special_dish_specific_btnback);
        backbtn.setOnClickListener(v -> {
            Intent i = new Intent(DishSpecial.this, DishManage.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
            overridePendingTransition(0, 0);
            Toast.makeText(DishSpecial.this, "正在刷新数据！", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        l.getItemAtPosition(position);
        Bundle data = new Bundle();
        data.putInt("adPosition", position);
        data.putInt("adState", state1[position]);
        data.putString("dishname", dishnameList.get(position));
        data.putString("c3", c3List.get(position));
        data.putInt("price", priceList.get(position));
        data.putString("description", descriptionList.get(position));
        Intent intent = new Intent(this, DishSpecialListDetail.class);
        intent.putExtras(data);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sqlsearch();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(DishSpecial.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
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
        // 关联Layout中的ListView
        listvie1 = (ListView) findViewById(android.R.id.list);
        // 生成动态数组，加入数据
        ArrayList<HashMap<String, Object>> remoteWindowItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 5; i++) {
            int j = i + 1;
            HashMap<String, Object> map = new HashMap<String, Object>();
            if (i == 0) {
                map.put("columNum", "商家信息栏");
            } else {
                map.put("columNum", i + "号特色菜栏");
            }
            if (state1[i] == 1) {
                map.put("columState", "正在发布");
                // myshow();
            } else if (state1[i] == 0) {
                map.put("columState", "暂无公告");
            }
            remoteWindowItem.add(map);
        }
        // 生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(
                this,
                //数据源
                remoteWindowItem,
                //ListItem的XML实现
                R.layout.main_list,
                new String[]{"columNum", "columState"},
                new int[]{R.id.deskNumber, R.id.deskState}
        );

        listvie1.setAdapter(listItemAdapter);
    }

    public void sqlsearch() {
        NetWorkManager.getRequest().getAdvertisementInfo()
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Observer<ArrayList<Advertisement>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<Advertisement> advertisements) {
                        for (int i = 0; i < advertisements.size(); i++) {
                            state1[i] = advertisements.get(i).getState();
                            descriptionList.add(advertisements.get(i).getDescription());
                            c3List.add(advertisements.get(i).getC3());
                            priceList.add(advertisements.get(i).getPrice());
                            dishnameList.add(advertisements.get(i).getDishname());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        showList();
                    }
                });
    }
}
