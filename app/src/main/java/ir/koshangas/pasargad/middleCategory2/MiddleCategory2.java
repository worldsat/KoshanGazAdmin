package ir.koshangas.pasargad.middleCategory2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import ir.koshangas.pasargad.DateVolley;
import ir.koshangas.pasargad.MainActivity;
import ir.koshangas.pasargad.R;

public class MiddleCategory2 extends AppCompatActivity {
    private TextView tryagain_txt;
    private Button tryagain_btn;
    private ProgressBar progressBarOne;
    private RecyclerView recyclerView;
    private DateVolley datavolly;
    private SwipeRefreshLayout refreshLayout;
    private EditText search_editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle_category2);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        //toolbar
        Toolbar my_toolbar = findViewById(R.id.app_bar_show_ads);
        setSupportActionBar(my_toolbar);
        Button back_icon = findViewById(R.id.back_icon_rtl);
        back_icon.setVisibility(View.VISIBLE);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MiddleCategory2.this, MainActivity.class);
                MiddleCategory2.this.startActivity(intent);
            }
        });
        TextView title_toolbar = findViewById(R.id.title_toolbar_rtl);
        title_toolbar.setText("دسته بندی میانی دوم");
        //toolbar end
        //refresh
        refreshLayout = findViewById(R.id.swipe_layout);
        refreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        CheckNet();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                datavolly.get_banners_middle(MiddleCategory2.this, "middle_category2", recyclerView, tryagain_txt, tryagain_btn, progressBarOne, "1");

                //Download data from net and update list
                refreshLayout.setRefreshing(false);
            }
        });

        //end refresh
        recyclerView = findViewById(R.id.View);
        tryagain_txt = findViewById(R.id.try_again_txt);
        tryagain_btn = findViewById(R.id.try_again_btn);
        progressBarOne = findViewById(R.id.progressBarOne);
        tryagain_txt.setVisibility(View.GONE);
        tryagain_btn.setVisibility(View.GONE);


        tryagain_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBarOne.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                tryagain_txt.setVisibility(View.GONE);
                tryagain_btn.setVisibility(View.GONE);

                CheckNet();
            }
        });


        //khali kardan aks activity_category dar SharedPreferences
        SharedPreferences pic_writer = getApplicationContext().getSharedPreferences("picture", 0);
        pic_writer.edit().clear().apply();
        //end

        datavolly = new DateVolley();
        // datavolly.connect(MiddleCategory.this,"activity_product","get","test");
        datavolly.get_banners_middle(MiddleCategory2.this, "middle_category2", recyclerView, tryagain_txt, tryagain_btn, progressBarOne, "1");
        TextView new_cat = findViewById(R.id.new_cat);
        new_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MiddleCategory2.this, New_MiddleCategory2.class);
                intent.putExtra("is_edit", "nothing");

                MiddleCategory2.this.startActivity(intent);
            }
        });


        search_editText = (EditText) findViewById(R.id.editText_search);
        search_editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (search_editText.getText().toString().isEmpty()) {
                        Toast.makeText(MiddleCategory2.this, "لطفا متن مورد نظر خود را وارد کنید", Toast.LENGTH_SHORT).show();

                    } else if (search_editText.getText().toString().length() < 3) {
                        Toast.makeText(MiddleCategory2.this, "لطفا طول کلمه مورد نظر  3 حرف یا بیشتر باشد", Toast.LENGTH_SHORT).show();
                    } else {

                        datavolly.search(MiddleCategory2.this, "middle_category2", search_editText.getText().toString(), recyclerView, tryagain_txt, tryagain_btn, progressBarOne);
                        // CheckNet();
                    }
                }
                return false;
            }
        });

        // baraye click roye icon search edittext
        search_editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() <= (search_editText.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                        // your action here

                        if (search_editText.getText().toString().isEmpty()) {
                            Toast.makeText(MiddleCategory2.this, "لطفا متن مورد نظر خود را وارد کنید", Toast.LENGTH_SHORT).show();

                        } else if (search_editText.getText().toString().length() < 3) {
                            Toast.makeText(MiddleCategory2.this, "لطفا طول کلمه مورد نظر  3 حرف یا بیشتر باشد", Toast.LENGTH_SHORT).show();
                        } else {
                            CheckNet();
                            // get_banners("search", search_editText.getText().toString(), "", "", "", "", "", "", "", "", "", "", "", "", "", "");
                            datavolly.search(MiddleCategory2.this, "middle_category2", search_editText.getText().toString(), recyclerView, tryagain_txt, tryagain_btn, progressBarOne);
                        }

                        return false;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MiddleCategory2.this, MainActivity.class);
        MiddleCategory2.this.startActivity(intent);
    }

    private void CheckNet() {


        boolean connect;

        ConnectivityManager connectivityManager = (ConnectivityManager) MiddleCategory2.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connect = true;
        } else {
            connect = false;
        }

        if (connect) {
            try {

                recyclerView.setVisibility(View.VISIBLE);
                tryagain_txt.setVisibility(View.GONE);
                tryagain_btn.setVisibility(View.GONE);

                datavolly.get_banners_middle(MiddleCategory2.this, "middle_category2", recyclerView, tryagain_txt, tryagain_btn, progressBarOne, "1");

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tryagain_txt.setVisibility(View.VISIBLE);
                    tryagain_btn.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    tryagain_txt.setText("عدم دسترسی به اینترنت !");
                    progressBarOne.setVisibility(View.GONE);
                }
            }, 1000);


        }
    }

}
