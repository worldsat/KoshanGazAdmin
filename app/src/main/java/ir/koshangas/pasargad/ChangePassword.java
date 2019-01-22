package ir.koshangas.pasargad;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePassword extends AppCompatActivity {
    private EditText username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_passwod);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        //toolbar
        Toolbar my_toolbar = (Toolbar) findViewById(R.id.app_bar_show_ads);
        setSupportActionBar(my_toolbar);
        Button back_icon = (Button) findViewById(R.id.back_icon_rtl);
        back_icon.setVisibility(View.VISIBLE);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePassword.this, MainActivity.class);
                ChangePassword.this.startActivity(intent);
            }
        });
        TextView title_toolbar = (TextView) findViewById(R.id.title_toolbar_rtl);
        title_toolbar.setText("تغییر رمز عبور");
        //toolbar end


        username = findViewById(R.id.username_edt);
        password = findViewById(R.id.pass_edt);
        Button login = findViewById(R.id.login_btn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Toast.makeText(ChangePassword.this, "تمامی گزینه ها را پر نمائید", Toast.LENGTH_SHORT).show();
                } else {
                    CheckNet();
                }
            }
        });
    }
    private void CheckNet() {


        boolean connect;

        ConnectivityManager connectivityManager = (ConnectivityManager) ChangePassword.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connect = true;
        } else {
            connect = false;
        }

        if (connect) {
            try {

               DateVolley change=new DateVolley();
                change.changePassword(ChangePassword.this, username.getText().toString(), password.getText().toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(ChangePassword.this, "اتصال اینترنت را بررسی نمائید", Toast.LENGTH_SHORT).show();

                }
            }, 1000);


        }
    }
}

