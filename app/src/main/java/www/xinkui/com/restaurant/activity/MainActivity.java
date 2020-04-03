package www.xinkui.com.restaurant.activity;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import www.xinkui.com.restaurant.R;
import www.xinkui.com.restaurant.adapter.ListViewButtonAdapter;
import www.xinkui.com.restaurant.bean.DishState;
import www.xinkui.com.restaurant.mqtt.MQTTListener;
import www.xinkui.com.restaurant.mqtt.MQTTObject;
import www.xinkui.com.restaurant.mqtt.MQTTService;
import www.xinkui.com.restaurant.network.NetWorkManager;
import www.xinkui.com.restaurant.network.response.ResponseTransformer;
import www.xinkui.com.restaurant.network.schedulers.SchedulerProvider;
import www.xinkui.com.restaurant.ui.MyRect;
import www.xinkui.com.restaurant.ui.TableView;
import www.xinkui.com.restaurant.util.DPTools;
import www.xinkui.com.restaurant.util.Util;


public class MainActivity extends Activity implements MQTTListener {
    int desks[] = new int[6];
    private long firstTime = 0;
    int confirmstate[] = new int[6];
    int messageNum = 0;
    Boolean touchstate[] = new Boolean[6];
    Handler handlerrefreshList;
    Button btnmid, btnright;
    Button btn1;
    SwipeRefreshLayout sw_ly;
    //    SqlQueryService sql1;
    GridView gv_table_gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initViews();
        //启动service后台刷新
//        sql1 = new SqlQueryService();
//        sql1.onStart(new Intent(), 1);
        sqlSearch();
//        initRunnable();
        MQTTService.addMqttListener(this);
        //connectMQTT service
        connectMQTT();

//       repeatanimation();

    }

    MyRect myRect;

    private void repeatanimation() {
        ValueAnimator animation = ValueAnimator.ofFloat(0f, 360f);

        animation.setDuration(5000);
        animation.start();
//        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
//                float animatedValue = (float)updatedAnimation.getAnimatedValue();
//                myRect.animate().rotation(animatedValue);
//                Util.Loge(animatedValue+":tonxok 36.66");
////                if(animatedValue==360f){
////                    repeatanimation();
////                }
//            }
//        });
    }

    private void connectMQTT() {
        Intent intent = new Intent(MainActivity.this, MQTTService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    class GridAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<DishState> dishStates;


        public GridAdapter(Context mContext, ArrayList<DishState> dishStates) {
            this.mContext = mContext;
            this.dishStates = dishStates;
        }


        @Override
        public int getCount() {
            return dishStates.size();
        }

        @Override
        public final boolean hasStableIds() {
            return true;
        }

        @Override
        public Object getItem(int position) {
            return dishStates.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
//            if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.grid_items, null);
            viewHolder = new ViewHolder();
            viewHolder.tableView = convertView.findViewById(R.id.tableview);
            viewHolder.tv_grid_num = convertView.findViewById(R.id.tv_grid_num);
            viewHolder.iv_customers = convertView.findViewById(R.id.iv_customers);
            viewHolder.iv_eating = convertView.findViewById(R.id.iv_eating);
            viewHolder.iv_look_up_menu = convertView.findViewById(R.id.iv_look_up_menu);
            viewHolder.iv_raisehand = convertView.findViewById(R.id.iv_raisehand);
            viewHolder.iv_chair = convertView.findViewById(R.id.iv_chair);
            viewHolder.rl = convertView.findViewById(R.id.rl_whole);
            int deskNum = position + 1;

                if (desks[position] > 0 && confirmstate[position] == 0) {
                    //请求接单
                    viewHolder.iv_eating.setVisibility(View.INVISIBLE);
                    viewHolder.iv_raisehand.setVisibility(View.VISIBLE);
                    viewHolder.iv_customers.setImageResource(R.drawable.customers);
                    viewHolder.iv_look_up_menu.setVisibility(View.INVISIBLE);
                    viewHolder.rl.setBackgroundResource(R.drawable.pswback1);
                    viewHolder.tableView.stopRotateAnimation();
                    viewHolder.iv_chair.setVisibility(View.INVISIBLE);
                    // myshow();
                } else if (confirmstate[position] == 1) {
                    //正在接单
                    viewHolder.iv_eating.setVisibility(View.VISIBLE);
                    viewHolder.iv_customers.setImageResource(R.drawable.customers);
                    viewHolder.iv_raisehand.setVisibility(View.INVISIBLE);
                    viewHolder.iv_look_up_menu.setVisibility(View.INVISIBLE);
                    viewHolder.rl.setBackgroundResource(R.drawable.pswback);
                    viewHolder.iv_chair.setVisibility(View.INVISIBLE);
                    viewHolder.tableView.stopRotateAnimation();
                } else if (confirmstate[position] == -1) {
                    //拒绝接单 -- 相当于空闲了
                    viewHolder.iv_eating.setVisibility(View.GONE);
                    viewHolder.iv_raisehand.setVisibility(View.GONE);
                    viewHolder.iv_look_up_menu.setVisibility(View.GONE);
                    viewHolder.rl.setBackgroundResource(R.drawable.pswback2);
                    viewHolder.iv_chair.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.iv_eating.setVisibility(View.GONE);
                    viewHolder.iv_raisehand.setVisibility(View.GONE);
                    viewHolder.iv_look_up_menu.setVisibility(View.GONE);
                    viewHolder.rl.setBackgroundResource(R.drawable.pswback2);
                    viewHolder.iv_chair.setVisibility(View.VISIBLE);
                }

//                convertView.setTag(viewHolder);
            viewHolder.tv_grid_num.setText(deskNum + "号桌");
//            } else {
//                viewHolder = (ViewHolder) convertView.getTag();
//            }
            return convertView;
        }

        private class ViewHolder {
            private RelativeLayout rl;
            private TableView tableView;
            private TextView tv_grid_num;
            private ImageView iv_look_up_menu;
            private ImageView iv_eating;
            private ImageView iv_raisehand;
            private ImageView iv_customers;
            private ImageView iv_chair;

        }
    }

    private void initViews() {
        btnmid = (Button) findViewById(R.id.main_bottom_mid);
        sw_ly = findViewById(R.id.sw_ly);
//        float cx3 = DPTools.dp2px(this, 40) / 2;
//        float cy3 = DPTools.dp2px(this, 50) / 2;
//        Util.Loge(cx3 + "******:******" + cy3);
//
//        RotateAnimation animation3 = new RotateAnimation(0f, 360f, cx3, cy3);
//        animation3.setRepeatMode(ValueAnimator.RESTART);
//        animation3.setRepeatCount(ValueAnimator.INFINITE);
//        animation3.setDuration(2000);
//        btnmid.setAnimation(animation3);

        gv_table_gridView = findViewById(R.id.gv_table_gridView);

        btnright = (Button) findViewById(R.id.main_bottom_right);
        btn1 = (Button) findViewById(R.id.btnrefresh);

        sw_ly.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Button btn1 = (Button) findViewById(R.id.btnrefresh);
                btn1.setText("正在刷新！");
                sqlSearch();
            }
        });


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

//    private void initRunnable() {
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//
//            }
//        }, 5000, 1000);
//    }


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

//    public void showList() {
//        // 关联Layout中的ListView
//        ListView vncListView = (ListView) findViewById(android.R.id.list);
//        // 生成动态数组，加入数据
//        ArrayList<HashMap<String, Object>> remoteWindowItem = new ArrayList<HashMap<String, Object>>();
//        for (int i = 0; i < 6; i++) {
//            int j = i + 1;
//            HashMap<String, Object> map = new HashMap<String, Object>();
//            map.put("deskNumber", j + "号桌");
//            if (desks[i] > 0 && confirmstate[i] == 0) {
//                map.put("deskState", "请求接单");
//                // myshow();
//            } else if (confirmstate[i] == 1) {
//                map.put("deskState", "正在接单");
//            } else if (confirmstate[i] == -1) {
//                map.put("deskState", "拒绝接单");
//            } else {
//                map.put("deskState", "-------------");
//            }
//            remoteWindowItem.add(map);
//        }
//        // 生成适配器的Item和动态数组对应的元素
//        ListViewButtonAdapter listItemAdapter = new ListViewButtonAdapter(
//                this,
//                //数据源
//                remoteWindowItem,
//                //ListItem的XML实现
//                R.layout.main_list,
//                new String[]{"deskNumber", "deskState"},
//                new int[]{R.id.deskNumber, R.id.deskState}
//        );
//
//        vncListView.setAdapter(listItemAdapter);
//
//    }

    public void myshow(int notifnum, final int desknum, int j) {
        touchstate[j] = false;
        //设置商家查看状态
        NetWorkManager.getRequest().setStateManage(desknum, 1, 0)
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
                        showMsg(dishStates);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v("yzy", "failed!");
                    }

                    @Override
                    public void onComplete() {
//                        showList();
                        Toast.makeText(MainActivity.this,"已完成加载",Toast.LENGTH_SHORT).show();
                        btn1.setText("点击或下拉刷新桌面信息");
                        sw_ly.setRefreshing(false);
                    }
                });
    }
    GridAdapter gridAdapter;
    boolean tagFirstFlag = true;
    ArrayList<DishState> mDishStates;
    private void showMsg(ArrayList<DishState> dishStates) {
        mDishStates = dishStates;
        if(tagFirstFlag){
            gridAdapter = new GridAdapter(this, mDishStates);
            gv_table_gridView.setAdapter(gridAdapter);
            gv_table_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int realPostion = position + 1;
                    if (desks[position] > 0) {
                        Toast.makeText(MainActivity.this, "正在刷新数据", Toast.LENGTH_SHORT).show();
                        Bundle data = new Bundle();
                        data.putInt("desknum", realPostion);
                        Intent intent = new Intent(MainActivity.this, MainOderSpecific.class);
                        intent.putExtras(data);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    } else {
                        Toast.makeText(MainActivity.this, "暂无订单", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            tagFirstFlag = false;
        }else {
            gridAdapter.notifyDataSetChanged();
        }
        for (int j = 0; j < 6; j++) {
            int k = j + 1;
            //注意判断是否接收到了数据
            if (dishStates.size() > 0) {
                if (dishStates.get(j).getConfirmState() != 2) {
                    switch (dishStates.get(j).getPayState() + dishStates.get(j).getState()) {
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

    @Override
    public void onConnected() {

    }

    @Override
    public void onLost() {

    }

    @Override
    public void onFailed() {

    }

    @Override
    public void onReceived(String topic, String message) {
        if (topic.equals(Util.MQTT_TOPIC)) {
            Util.Loge(message);
//            Type type = new TypeToken<ArrayList<DishState>>(){}.getType();
//            ArrayList<DishState> list = new Gson().fromJson(message,type);

            MQTTObject mqttObject = new Gson().fromJson(message, MQTTObject.class);
            if (mqttObject.getMessage().equals("orderRequest")) {
                sqlSearch();
            }
        }
    }

    @Override
    public void onSentSuccessfully() {
        Util.Loge("sentSuccessfully");
    }
}
