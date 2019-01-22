package ir.koshangas.pasargad.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import ir.koshangas.pasargad.MainActivity;
import ir.koshangas.pasargad.R;

public class newCustomerActivity extends AppCompatActivity {
    private EditText mobile, address, tell, shahr, ostan, name, password;
    private Button saveEditProfile;
    private String RadioCustomer ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        name = findViewById(R.id.nameEdt);
        mobile = findViewById(R.id.mobileEdt);
        address = findViewById(R.id.addressEdt);
        password = findViewById(R.id.passwordEdt);
        tell = findViewById(R.id.tellEdt);
        ostan = findViewById(R.id.postalEdt);
        shahr = findViewById(R.id.emailEdt);
        saveEditProfile = findViewById(R.id.send_cat_btn);


        //toolbar
        Toolbar my_toolbar = findViewById(R.id.app_bar_show_ads);
        setSupportActionBar(my_toolbar);
        Button back_icon = findViewById(R.id.back_icon_rtl);
        back_icon.setVisibility(View.VISIBLE);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(newCustomerActivity.this, Customer.class);
                newCustomerActivity.this.startActivity(intent);
            }
        });
        TextView title_toolbar = findViewById(R.id.title_toolbar_rtl);
        title_toolbar.setText("ایجاد مشتری جدید");
        //toolbar end

        final RadioGroup payment_radio = (RadioGroup) findViewById(R.id.RadioGroup1);
        payment_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                // TODO Auto-generated method stub
                switch (payment_radio.getCheckedRadioButtonId()) {
                    case R.id.radio1:

                        RadioCustomer = "1";
                        break;
                    case R.id.radio2:

                        RadioCustomer = "2";
                        break;
                    case R.id.radio3:

                        RadioCustomer = "3";
                        break;

                }
            }
        });


        SignUp();
    }

    private void SignUp() {


        saveEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              MaterialDialog  wait = new MaterialDialog.Builder(newCustomerActivity.this)
                        .cancelable(false)
                        .content("لطفا منتظر بمانید")
                        .progress(true, 0)
                        .build();




                if (mobile.getText().toString().length() < 11) {
                    Toast.makeText(newCustomerActivity.this, "شماره همراه را بصورت صحیح وارد نمائید", Toast.LENGTH_SHORT).show();

                } else {

                    setNewCustomer newCustomer = new setNewCustomer();
                    newCustomer.edit(newCustomerActivity.this, newCustomerActivity.this, name.getText().toString(), password.getText().toString(), mobile.getText().toString(), address.getText().toString(), ostan.getText().toString(), tell.getText().toString(), shahr.getText().toString(), wait, saveEditProfile,RadioCustomer, MainActivity.class);


                }

            }
        });
    }
}
