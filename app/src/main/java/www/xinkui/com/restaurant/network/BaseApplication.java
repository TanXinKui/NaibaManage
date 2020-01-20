package www.xinkui.com.restaurant.network;

import android.app.Application;
import android.content.res.Configuration;

import www.xinkui.com.restaurant.mqtt.MQTTState;

/**
*@description
*@author TONXOK
*@time 2019/5/3 15:41
*/
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NetWorkManager.getInstance().init();
        MQTTState.getInstance();
        MQTTState.setApplication(this);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        MQTTState.getInstance().onTerminate();
    }
}
