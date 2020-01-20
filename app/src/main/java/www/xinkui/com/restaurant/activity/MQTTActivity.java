package www.xinkui.com.restaurant.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import www.xinkui.com.restaurant.R;
import www.xinkui.com.restaurant.mqtt.MQTTListener;
import www.xinkui.com.restaurant.mqtt.MQTTObject;
import www.xinkui.com.restaurant.mqtt.MQTTService;
import www.xinkui.com.restaurant.util.Util;

public class MQTTActivity extends AppCompatActivity implements MQTTListener {
    private Button btn1;
    private Button btn2;
    private Gson gson=new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mqtt);
        MQTTService.addMqttListener(this);
        btn1 = (Button) findViewById(R.id.connectedBtn);
        btn2 = (Button) findViewById(R.id.sendMsg);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MQTTActivity.this, MQTTService.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent);
                } else {
                    startService(intent);
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MQTTObject object=new MQTTObject("hello","tonxok");
                MQTTService.getMqttConfig().sendMessage(Util.MQTT_TOPIC,gson.toJson(object),0);
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        MQTTService.removeMqttListener(this);
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
        if(topic.equals(Util.MQTT_TOPIC)){
//            MqttMessage mqttMessage=gson.fromJson(message,MqttMessage.class);
            Util.Loge(message);
        }
    }

    @Override
    public void onSentSuccessfully() {
        Util.Loge("sentSuccessfully");
    }
}
