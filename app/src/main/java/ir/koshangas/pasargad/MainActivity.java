package ir.koshangas.pasargad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import ir.koshangas.pasargad.category.Category;
import ir.koshangas.pasargad.comment.CommentActivity;
import ir.koshangas.pasargad.customer.Customer;
import ir.koshangas.pasargad.history.HistoryActivity;
import ir.koshangas.pasargad.login.GetToken;
import ir.koshangas.pasargad.middleCategory.MiddleCategory;
import ir.koshangas.pasargad.middleCategory2.MiddleCategory2;
import ir.koshangas.pasargad.product.product;
import ir.koshangas.pasargad.slider.New_Slider;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        //toolbar
        Toolbar my_toolbar = (Toolbar) findViewById(R.id.app_bar_show_ads);
        setSupportActionBar(my_toolbar);

        TextView title_toolbar = (TextView) findViewById(R.id.title_toolbar_rtl);
        title_toolbar.setText("مدیریت");
        //toolbar end

        SharedPreferences pic_reader = getApplicationContext().getSharedPreferences("picture", 0);
        SharedPreferences pic_database = getApplicationContext().getSharedPreferences("pic_database", 0);
        pic_reader.edit().clear().apply();
        pic_database.edit().clear().apply();

        GetToken Token = new GetToken();
        // Token.connect(MainActivity.this,"0913","123");
    }

    public void onBtn1(View v) {
        Intent intent = new Intent(MainActivity.this, Category.class);
        MainActivity.this.startActivity(intent);
    }

    public void onBtn2(View v) {
        Intent intent = new Intent(MainActivity.this, product.class);
        MainActivity.this.startActivity(intent);
    }

    public void onBtn3(View v) {
        Intent intent = new Intent(MainActivity.this, MiddleCategory2.class);
        MainActivity.this.startActivity(intent);
    }

    public void onBtn4(View v) {
        Intent intent = new Intent(MainActivity.this, Customer.class);
        MainActivity.this.startActivity(intent);
    }

    public void onBtn5(View v) {
        final MaterialDialog exit_alert = new MaterialDialog.Builder(MainActivity.this)
                .customView(R.layout.alert_exit, false)
                .autoDismiss(false)
                .show();
        TextView delete_alert_no = (TextView) exit_alert.findViewById(R.id.alert_exit_no);
        TextView delete_alert_yes = (TextView) exit_alert.findViewById(R.id.alert_exit_yes);
        delete_alert_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit_alert.dismiss();

            }
        });
        delete_alert_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   if (android.os.Build.VERSION.SDK_INT<21) {
                finishAffinity();
                // }else if (android.os.Build.VERSION.SDK_INT>=21) {
                //     finishAndRemoveTask();
                //  }
            }
        });

    }

    public void onBtn6(View v) {
        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
        MainActivity.this.startActivity(intent);

    }

    public void onBtn7(View v) {
        Intent intent = new Intent(MainActivity.this, MiddleCategory.class);
        MainActivity.this.startActivity(intent);
    }

    public void onBtn8(View v) {
        Intent intent = new Intent(MainActivity.this, New_Slider.class);
        MainActivity.this.startActivity(intent);
    }

    public void onBtn9(View v) {
        Intent intent = new Intent(MainActivity.this, CommentActivity.class);
        MainActivity.this.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        final MaterialDialog exit_alert = new MaterialDialog.Builder(MainActivity.this)
                .customView(R.layout.alert_exit, false)
                .autoDismiss(false)
                .show();
        TextView delete_alert_no = (TextView) exit_alert.findViewById(R.id.alert_exit_no);
        TextView delete_alert_yes = (TextView) exit_alert.findViewById(R.id.alert_exit_yes);
        delete_alert_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit_alert.dismiss();

            }
        });
        delete_alert_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   if (android.os.Build.VERSION.SDK_INT<21) {
                finishAffinity();
                // }else if (android.os.Build.VERSION.SDK_INT>=21) {
                //     finishAndRemoveTask();
                //  }
            }
        });
    }
}
