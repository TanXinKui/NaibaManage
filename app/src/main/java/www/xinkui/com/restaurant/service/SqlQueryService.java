package www.xinkui.com.restaurant.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import www.xinkui.com.restaurant.activity.MainActivity;
import www.xinkui.com.restaurant.bean.DishState;
import www.xinkui.com.restaurant.network.NetWorkManager;
import www.xinkui.com.restaurant.network.response.ResponseTransformer;
import www.xinkui.com.restaurant.network.schedulers.SchedulerProvider;
import www.xinkui.com.restaurant.util.Util;

import static java.lang.Thread.sleep;

/**
 * Created by lenovo on 2018/2/6.
 */

/**
 * @author TONXOK
 * @description
 * @time 2019/4/27 18:25
 */
public class SqlQueryService extends Service {
    MainActivity jj;
    Boolean finishState = true;
    private static final String TAG = "MyService";
    private ArrayList<DishState> dishStatesList = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        jj = new MainActivity();
        Log.e(TAG, "--------->onCreate: ");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (finishState) {
                    finishState = false;
                    myTimer();
                }
            }
        }, 5000, 5000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "--------->onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "--------->onDestroy: ");
        super.onDestroy();
    }

    public void myTimer() {
        sqlSearch();
    }

    public ArrayList<DishState> getDishStatesList() {
        return dishStatesList;
    }

    public void setDishStatesList(ArrayList<DishState> dishStatesList) {
        this.dishStatesList.clear();
        this.dishStatesList = dishStatesList;
    }

    public void sqlSearch() {
        NetWorkManager.getRequest().getDishState()
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Observer<ArrayList<DishState>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<DishState> dishStates) {
                        setDishStatesList(dishStates);
                        Log.v("yzy", "success to connect!");
                        finishState = true;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v("yzy", "failed!");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
