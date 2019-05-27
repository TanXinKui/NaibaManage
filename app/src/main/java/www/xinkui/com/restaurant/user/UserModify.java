package www.xinkui.com.restaurant.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import www.xinkui.com.restaurant.R;
import www.xinkui.com.restaurant.bean.Admin;
import www.xinkui.com.restaurant.network.NetWorkManager;
import www.xinkui.com.restaurant.network.exception.ApiException;
import www.xinkui.com.restaurant.network.response.ResponseTransformer;
import www.xinkui.com.restaurant.network.schedulers.SchedulerProvider;
import www.xinkui.com.restaurant.util.Util;

import static java.lang.Thread.sleep;

/**
 * Created by lenovo on 2018/3/4.
 */

public class UserModify extends Activity {
    EditText ed1, ed2, ed3;
    String newName, newPass, newphone;
    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify);
        ed1 = (EditText) findViewById(R.id.revendorName1);
        ed2 = (EditText) findViewById(R.id.revendorPass1);
        ed3 = (EditText) findViewById(R.id.revendorPhone1);
        btn1 = (Button) findViewById(R.id.modifyconfirmbtn);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newName = ed1.getText().toString();
                newPass = ed2.getText().toString();
                newphone = ed3.getText().toString();
                if (newName.equals(" ") || newPass.equals(" ") || newphone.equals(" ")) {
                    Toast.makeText(UserModify.this, "请输入完整！", Toast.LENGTH_SHORT).show();
                } else if (!newName.equals(" ") && !newPass.equals(" ") && !newphone.equals(" ")) {
                    btn1.setText("正在修改数据！");
                    modify(new Admin(newName, newPass, newphone));
                    //sqlsearch();
                   /* Handler myhandler = new Handler();
                    Runnable myrunnable = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(UserModify.this, UserLogin.class);
                            startActivity(intent);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            overridePendingTransition(0, 0);
                            Toast.makeText(UserModify.this, "修改成功！", Toast.LENGTH_SHORT).show();
                        }

                    };
                    myhandler.post(myrunnable);*/
                }
            }
        });
    }

    private void modify(Admin admin) {
        NetWorkManager.getRequest().userModify(admin)
                .compose(ResponseTransformer.handleResult())
                .compose(SchedulerProvider.getInstance().applySchedulers())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ApiException td = (ApiException) e;
                        Log.v(this.getClass().getName(), "Failed" + td.getDisplayMessage());
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(UserModify.this, "修改成功！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
    /*public void sqlsearch() {
//在android中操作数据库最好在子线程中执行，否则可能会报异常
        new Thread() {
            public void run() {
                Connection conn = null;
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    conn = DriverManager.getConnection(Util.DATABASE_URL, Util.DATABASE_USERNAME, Util.DATABASE_PASSWORD);
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate("update admin set name='" + newName + "',password='" + newPass + "',phone='" + newphone + "' where id=1");
                    stmt.close();
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ;
        }.start();
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/
}
