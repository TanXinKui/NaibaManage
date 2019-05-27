package www.xinkui.com.restaurant.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import www.xinkui.com.restaurant.activity.MainActivity;
import www.xinkui.com.restaurant.R;
import www.xinkui.com.restaurant.bean.Admin;
import www.xinkui.com.restaurant.network.NetWorkManager;
import www.xinkui.com.restaurant.network.exception.ApiException;
import www.xinkui.com.restaurant.network.response.ResponseTransformer;
import www.xinkui.com.restaurant.network.schedulers.SchedulerProvider;

/**
 * Created by lenovo on 2018/3/4.
 */

/**
 * @author TONXOK
 * @description
 * @time 2019/4/27 18:12
 */
public class UserLogin extends Activity {
    private long firstTime = 0;
    EditText vnameed, vpassed;
    Button btn1, btn2;
    private final static int LOGINMODE = 10;
    private final static int MODIFYMODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        vnameed = (EditText) findViewById(R.id.vendorName1);
        vpassed = (EditText) findViewById(R.id.vendorPass1);
        btn1 = (Button) findViewById(R.id.loginbtn);
        btn2 = (Button) findViewById(R.id.modifybtn);
        btn1.setOnClickListener(v -> {
                    btn1.setText("正在验证身份");
                    checkLogin(LOGINMODE);
                }
        );
        btn2.setOnClickListener(v -> {
            btn2.setText("正在验证身份");
            checkLogin(MODIFYMODE);
        });
    }

    private void checkLogin(int mode) {
        NetWorkManager.getRequest().userLogin((new Admin(vnameed.getText().toString(), vpassed.getText().toString())))
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.v(this.getClass().getName(), d.toString());
                    }

                    @Override
                    public void onNext(String s) {
                        Log.v(this.getClass().getName(), s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        switch (mode) {
                            case 10:
                                fail();
                                break;
                            case 11:
                                noPriviledge();
                                break;
                            default:
                                break;
                        }
                        ApiException td = (ApiException) e;
                        Log.v(this.getClass().getName(), "Failed" + td.getDisplayMessage());
                    }

                    @Override
                    public void onComplete() {
                        switch (mode) {
                            case 10:
                                succeed();
                                break;
                            case 11:
                                toModify();
                                break;
                            default:
                                break;
                        }
                        Log.v(this.getClass().getName(), "finish");
                    }
                });
    }

    private void succeed() {
        Intent intent = new Intent(UserLogin.this, MainActivity.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        overridePendingTransition(0, 0);
        Toast.makeText(UserLogin.this, "登陆成功！", Toast.LENGTH_SHORT).show();
    }

    private void toModify() {
        Intent intent = new Intent(UserLogin.this, UserModify.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        overridePendingTransition(0, 0);
        Toast.makeText(UserLogin.this, "登陆成功！", Toast.LENGTH_SHORT).show();
    }

    private void fail() {
        Toast.makeText(UserLogin.this, "密码或账号错误！", Toast.LENGTH_SHORT).show();
        btn1.setText("登陆");
    }

    private void noPriviledge() {
        Toast.makeText(UserLogin.this, "密码或账号错误！", Toast.LENGTH_SHORT).show();
        btn2.setText("修改商家信息");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(UserLogin.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
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
}
