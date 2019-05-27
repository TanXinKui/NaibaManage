package www.xinkui.com.restaurant.activity;

import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import www.xinkui.com.restaurant.R;
import www.xinkui.com.restaurant.adapter.ListViewButtonAdapter;
import www.xinkui.com.restaurant.bean.DishState;
import www.xinkui.com.restaurant.network.NetWorkManager;
import www.xinkui.com.restaurant.network.response.ResponseTransformer;
import www.xinkui.com.restaurant.network.schedulers.SchedulerProvider;
import www.xinkui.com.restaurant.service.SqlQueryService;
import www.xinkui.com.restaurant.util.Util;

import static java.lang.Thread.sleep;

public class MainActivity extends ListActivity {
    int desks[] = new int[6];
    private long firstTime = 0;
    int confirmstate[] = new int[6];
    int messageNum = 0;
    Boolean touchstate[] = new Boolean[6];
    Handler handlerrefreshList;
    Button btnmid, btnright;
    Button btn1;
    SqlQueryService sql1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initViews();
        //启动service后台刷新
        sql1 = new SqlQueryService();
        sql1.onStart(new Intent(), 1);
        sqlSearch();
        initRunnable();
    }

    private void initViews() {
        btnmid = (Button) findViewById(R.id.main_bottom_mid);
        btnright = (Button) findViewById(R.id.main_bottom_right);
        btn1 = (Button) findViewById(R.id.btnrefresh);
        btnmid.setOnClickListener(v -> {
                    Toast.makeText(MainActivity.this, "正在刷新数据", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, DishManage.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                    overridePendingTransition(0, 0);
                }
        );
        btnright.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, RechargeManage.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(i);
            overridePendingTransition(0, 0);
        });
        handlerrefreshList = new Handler();
        for (int i1 = 0; i1 < 6; i1++) {
            touchstate[i1] = true;
        }
    }

    private void initRunnable() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (int j = 0; j < 6; j++) {
                    int k = j + 1;
                    //注意判断是否接收到了数据
                    if (sql1.getDishStatesList().size() > 0) {
                        if (sql1.getDishStatesList().get(j).getConfirmState() != 2) {
                            switch (sql1.getDishStatesList().get(j).getPayState() + sql1.getDishStatesList().get(j).getState()) {
                                case 0:
                                    Log.v("yzy", k + "号桌无订单");
                                    break;
                                case 1:
                                    if (touchstate[j]) {
                                        myshow(++messageNum, k, j);
                                    }
                                    break;
                                case 2:
                                    Log.v("yzy", k + "号桌订单已经查看");
                                    break;
                                case 3:
                                    Log.v("yzy", k + "号桌订单已经查看");
                                    break;
                                default:
                                    break;
                            }
                        }
                    }

                }
            }
        }, 5000, 1000);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        l.getItemAtPosition(position);
        int realPostion = position + 1;
        if (desks[position] > 0) {
            Toast.makeText(MainActivity.this, "正在刷新数据", Toast.LENGTH_SHORT).show();
            Bundle data = new Bundle();
            data.putInt("desknum", realPostion);
            Intent intent = new Intent(this, MainOderSpecific.class);
            intent.putExtras(data);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0, 0);
        } else {
            Toast.makeText(MainActivity.this, "暂无订单", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
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

    public void refreshList(View view) {
        Button btn1 = (Button) findViewById(R.id.btnrefresh);
        btn1.setText("正在刷新！");
        sqlSearch();
    }

    public void showList() {
        // 关联Layout中的ListView
        ListView vncListView = (ListView) findViewById(android.R.id.list);
        // 生成动态数组，加入数据
        ArrayList<HashMap<String, Object>> remoteWindowItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 6; i++) {
            int j = i + 1;
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("deskNumber", j + "号桌");
            if (desks[i] > 0 && confirmstate[i] == 0) {
                map.put("deskState", "请求接单");
                // myshow();
            } else if (confirmstate[i] == 1) {
                map.put("deskState", "正在接单");
            } else if (confirmstate[i] == -1) {
                map.put("deskState", "拒绝接单");
            } else {
                map.put("deskState", "-------------");
            }
            remoteWindowItem.add(map);
        }
        // 生成适配器的Item和动态数组对应的元素
        ListViewButtonAdapter listItemAdapter = new ListViewButtonAdapter(
                this,
                //数据源
                remoteWindowItem,
                //ListItem的XML实现
                R.layout.main_list,
                new String[]{"deskNumber", "deskState"},
                new int[]{R.id.deskNumber, R.id.deskState}
        );

        vncListView.setAdapter(listItemAdapter);
    }

    public void myshow(int notifnum, final int desknum, int j) {
        touchstate[j] = false;
        //设置商家查看状态
        NetWorkManager.getRequest().setStateManage(desknum, 1,0)
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe();
        //设置noticification
        Intent i = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
        Notification notification = new Notification();
        Notification.Builder builder = new Notification.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(notifnum + "条未读信息")
                .setContentText("接单请求！")
                .setContentIntent(pi)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setWhen(System.currentTimeMillis())
                .setOngoing(true);
        notification = builder.getNotification();
        notification.defaults = Notification.DEFAULT_SOUND;
        NotificationManager noma = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        noma.notify(0, notification);
    }

    public void sqlSearch() {
        btn1.setText("正在刷新！");
        NetWorkManager.getRequest().getDishState()
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Observer<ArrayList<DishState>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<DishState> dishStates) {
                        for (int i = 0; i < dishStates.size(); i++) {
                            desks[i] = dishStates.get(i).getPayState();
                            confirmstate[i] = dishStates.get(i).getConfirmState();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v("yzy", "failed!");
                    }

                    @Override
                    public void onComplete() {
                        showList();
                        btn1.setText("点击刷新桌面信息");
                    }
                });
    }
}
