package www.xinkui.com.restaurant.activity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import www.xinkui.com.restaurant.R;
import www.xinkui.com.restaurant.bean.SellState;
import www.xinkui.com.restaurant.network.NetWorkManager;
import www.xinkui.com.restaurant.network.response.ResponseTransformer;
import www.xinkui.com.restaurant.network.schedulers.SchedulerProvider;
import www.xinkui.com.restaurant.ui.CornerView;


/**
 * Created by lenovo on 2018/3/4.
 */

/**
 * @author TONXOK
 * @description
 * @time 2019/4/27 18:10
 */
public class DishManage extends Activity {
    ListView lis1;
    private long firstTime = 0;
    int dishstate[];
    String[] dishList1;
    Button btnleft, btnright, addSpecial;
    private GridView dish_gridView;
    int[] pics = new int[]{R.drawable.dish1, R.drawable.dish2, R.drawable.dish3, R.drawable.dish4, R.drawable.dish5, R.drawable.dish6, R.drawable.dish7,
            R.drawable.dish8, R.drawable.dish9};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dishmanage);
        initViews();
        initClickEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sqlSearch();
    }
    private GridViewAdapter gridViewAdapter;
    private void initViews() {
        btnleft = (Button) findViewById(R.id.dishmanage_bottom_left);
        btnright = (Button) findViewById(R.id.dishmanage_bottom_right);
        addSpecial = (Button) findViewById(R.id.addSpecial);
        dish_gridView = findViewById(R.id.dish_gridView);
        gridViewAdapter = new GridViewAdapter(this);
        dishstate = new int[9];
        dishList1 = new String[]{"黑椒牛肉饼条饭+营养滋补炖汤", "香菇滑鸡饭+营养滋补炖汤", "NB照烧鸡腿饭+营养滋补炖汤", "奥尔良鸡排饭+营养滋补炖汤", "椒盐排骨饭+营养滋补炖汤",
                "黑椒鸡排焗饭", "黑椒牛肉焗饭", "咖喱海鲜焗饭", "咖喱鸡排焗饭"};
        dish_gridView.setAdapter(gridViewAdapter);
        dish_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle data = new Bundle();
                data.putInt("dishorder", position);
                data.putInt("dishstate", dishstate[position]);
                Intent intent = new Intent(DishManage.this, DishStateDetail.class);
                intent.putExtras(data);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }
    class  GridViewAdapter extends BaseAdapter{
        private Context mContext;
        public GridViewAdapter(Context context) {
            super();
            mContext = context;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            if(position == 2||position==5){
                return false;
            }else {
                return true;
            }
        }

        @Override
        public int getCount() {
            return dishstate.length;
        }

        @Override
        public Object getItem(int position) {
            return dishstate[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = new ViewHolder();
            convertView = View.inflate( mContext,R.layout.dish_items,null);
            viewHolder.imageView = convertView.findViewById(R.id.iv_dish_pic);
            viewHolder.textView = convertView.findViewById(R.id.tv_dish_name);
            if(dishstate[position]==1){
                viewHolder.textView.setTextColor(Color.parseColor("#00a2e9"));
            }else {
                viewHolder.textView.setTextColor(Color.GRAY);
            }
            viewHolder.imageView = convertView.findViewById(R.id.iv_dish_pic);
            viewHolder.textView.setText(dishList1[position]);
//            viewHolder.imageView.setImageResource(pics[position]);
//            viewHolder.imageView.setImageDrawable(getResources().getDrawable(pics[position]));
            viewHolder.imageView.setBitMap(getResources().getDrawable(pics[position]));
            return convertView;
        }
        private class ViewHolder{
            private CornerView imageView;
            private TextView textView;
        }
    }
    private void initClickEvents() {
        addSpecial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DishManage.this, "正在刷新数据！", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(DishManage.this, DishSpecial.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                overridePendingTransition(0, 0);
            }
        });
        btnleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DishManage.this, "正在刷新数据！", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(DishManage.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                overridePendingTransition(0, 0);
            }
        });
        btnright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DishManage.this, RechargeManage.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                overridePendingTransition(0, 0);
            }
        });
    }

//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        // TODO Auto-generated method stub
//        super.onListItemClick(l, v, position, id);
//        l.getItemAtPosition(position);
//        Bundle data = new Bundle();
//        data.putInt("dishorder", position);
//        data.putInt("dishstate", dishstate[position]);
//        Intent intent = new Intent(this, DishStateDetail.class);
//        intent.putExtras(data);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        startActivity(intent);
//        overridePendingTransition(0, 0);
//
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(DishManage.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
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

//    public void showList() {
//        // 关联Layout中的ListView
//        lis1 = (ListView) findViewById(android.R.id.list);
//        // 生成动态数组，加入数据
//        ArrayList<HashMap<String, Object>> remoteWindowItem = new ArrayList<HashMap<String, Object>>();
//        for (int i = 0; i < 9; i++) {
//            int j = i + 1;
//            HashMap<String, Object> map = new HashMap<String, Object>();
//            map.put("deskNumber", dishList1[i]);
//            if (dishstate[i] == 1) {
//                map.put("deskState", "正在出售");
//                // myshow();
//            } else if (dishstate[i] == 0) {
//                map.put("deskState", "售完");
//            }
//            remoteWindowItem.add(map);
//        }
//        // 生成适配器的Item和动态数组对应的元素
//        SimpleAdapter listItemAdapter = new SimpleAdapter(
//                this,
//                remoteWindowItem,
//                //数据源
//                R.layout.main_list,
//                //ListItem的XML实现
//                new String[]{"deskNumber", "deskState"},
//                new int[]{R.id.deskNumber, R.id.deskState}
//        );
//        lis1.setAdapter(listItemAdapter);
//    }

    public void sqlSearch() {
        NetWorkManager.getRequest().getSellState()
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Observer<ArrayList<SellState>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<SellState> sellStates) {
                        for (int i = 0; i < sellStates.size(); i++) {
                            dishstate[i] = sellStates.get(i).getState();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
//                        showList();
                        gridViewAdapter.notifyDataSetChanged();
                    }
                });
    }
}
