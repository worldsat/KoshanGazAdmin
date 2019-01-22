package ir.koshangas.pasargad.comment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import ir.koshangas.pasargad.MainActivity;
import ir.koshangas.pasargad.R;


public class CommentActivity extends AppCompatActivity {
    private ProgressBar progressBarOne;
    private RecyclerView recyclerView;
    private Button tryagain_btn;
    private TextView tryagain_txt;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        //toolbar
        Toolbar my_toolbar = (Toolbar) findViewById(R.id.app_bar_show_ads);
        setSupportActionBar(my_toolbar);
        Button back_icon = (Button) findViewById(R.id.back_icon_rtl);
        back_icon.setVisibility(View.VISIBLE);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommentActivity.this, MainActivity.class);
                CommentActivity.this.startActivity(intent);
            }
        });
        TextView title_toolbar = (TextView) findViewById(R.id.title_toolbar_rtl);
        title_toolbar.setText("نظرات");
        //toolbar end


        progressBarOne = findViewById(R.id.progressBarOne);
        recyclerView = findViewById(R.id.View);
        tryagain_btn = findViewById(R.id.try_again_btn);
        tryagain_txt = findViewById(R.id.try_again_txt);

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

        refreshLayout = findViewById(R.id.swipe_layout);
        refreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        CheckNet();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getComment getComment = new getComment();
                getComment.get_comment(CommentActivity.this, progressBarOne, recyclerView, tryagain_btn, tryagain_txt);

                //Download data from net and update list
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void CheckNet() {


        boolean connect;

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
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

                getComment getComment = new getComment();
                getComment.get_comment(CommentActivity.this, progressBarOne, recyclerView, tryagain_btn, tryagain_txt);

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
