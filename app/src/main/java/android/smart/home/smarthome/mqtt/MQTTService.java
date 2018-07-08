package android.smart.home.smarthome.mqtt;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


/**
 * @Author TangZiWen
 * @Time 2018/5/10 0010 19:22
 * @Description ${MQTT Service}
 */

public class MQTTService extends Service {

    public static final String TAG = MQTTService.class.getSimpleName();

    private static MqttAndroidClient client;
    private MqttConnectOptions conOpt;

    private String host = "tcp://183.230.40.39:6002";//onenet mqtt service domain
    private String userName = "132334";//onenet product ID
    private String passWord = "smartHome";//auth info
    private static String tempTopic = "/29670406/dht11_temp";
    private static String humiTopic = "/29670406/dht11_humi";
    private static String triggerTopic = "/dht11/trigger";
    private String clientId = "30985754";//device ID

    public class MqttBinder extends Binder {
        public MQTTService getService(){
            return MQTTService.this;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MqttBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init();
        return super.onStartCommand(intent, flags, startId);
    }

    public static void publish(String topic,String msg){
        Integer qos = 0;
        Boolean retained = false;
        try {
            client.publish(topic, msg.getBytes(), qos, retained);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        // 服务器地址（协议+地址+端口号）
        String uri = host;
        client = new MqttAndroidClient(this, uri, clientId);
        // 设置MQTT监听并且接受消息
        client.setCallback(mqttCallback);

        conOpt = new MqttConnectOptions();
        // 断开连接不清除缓存
        conOpt.setCleanSession(false);
        // 设置超时时间，单位：秒
        conOpt.setConnectionTimeout(10);
        // 心跳包发送间隔，单位：秒
        conOpt.setKeepAliveInterval(120);
        // 用户名
        conOpt.setUserName(userName);
        // 密码
        conOpt.setPassword(passWord.toCharArray());
        //版本
        conOpt.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
        // last will message
        boolean doConnect = true;
        //String message = "{\"terminal_uid\":\"" + clientId + "\"}";
        //String topic = myTopic;
        //Integer qos = 0;
        //Boolean retained = false;
        /*if ((!message.equals("")) || (!topic.equals(""))) {
            // 最后的遗嘱
            try {
                conOpt.setWill(topic, message.getBytes(), qos, retained);
            } catch (Exception e) {
                Log.i(TAG, "Exception Occured", e);
                doConnect = false;
                iMqttActionListener.onFailure(null, e);
            }
        }*/

        if (doConnect) {
            doClientConnection();
        }

    }

    @Override
    public void onDestroy() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    /** 连接MQTT服务器 */
    private void doClientConnection() {
        if (!client.isConnected() && isConnectIsNomarl()) {
            try {
                client.connect(conOpt, null, iMqttActionListener);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

    }

    // MQTT是否连接成功
    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken arg0) {
            Log.i(TAG, "连接成功 ");
            try {
                // 订阅myTopic话题
                //client.unsubscribe(tempTopic);
                //client.unsubscribe(humiTopic);
                client.unsubscribe(triggerTopic);
                client.subscribe(triggerTopic,1);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            arg1.printStackTrace();
            // 连接失败，重连
        }
    };

    // MQTT监听并且接受消息
    private MqttCallback mqttCallback = new MqttCallback() {


        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {

        }

        @Override
        public void connectionLost(Throwable arg0) {
            // 失去连接，重连
        }

        @Override
        public void messageArrived(String s, MqttMessage message) throws Exception {
            String payload = new String(message.getPayload());
            Log.d(TAG, "messageArrived:" + payload);
            Intent intent = new Intent("ACTION_TRIGGER_ANNOTATION");
            intent.putExtra("trigger",payload);
            sendBroadcast(intent);
        }
    };

    /** 判断网络是否连接 */
    private boolean isConnectIsNomarl() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            String name = info.getTypeName();
            Log.i(TAG, "MQTT当前网络名称：" + name);
            return true;
        } else {
            Log.i(TAG, "MQTT 没有可用网络");
            return false;
        }
    }
}
