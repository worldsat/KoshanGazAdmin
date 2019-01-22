package ir.koshangas.pasargad.basket;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;

import ir.koshangas.pasargad.R;

public class BasketActivity extends AppCompatActivity {
    private DecimalFormat formatter = new DecimalFormat("###,###,###,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        Bundle address = getIntent().getExtras();

        ConstraintLayout BasketLayout = findViewById(R.id.Basket_layout);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        RecyclerView recyclerViewlist = findViewById(R.id.RecyclerView);
        TextView emptyText = findViewById(R.id.emptyText);
        emptyText.setVisibility(View.GONE);

        TextView dateShow = findViewById(R.id.date_show);
        TextView mobileShow = findViewById(R.id.mobile_show);
        TextView nameShow = findViewById(R.id.name_show);
        TextView factorShow = findViewById(R.id.factor_show);
        TextView totalShow = findViewById(R.id.total_price_show);
        TextView paymentShow = findViewById(R.id.payment_show);
        TextView postShow = findViewById(R.id.post_show);


        if (address != null) {
            dateShow.setText(address.getString("DateItems", "0"));
            mobileShow.setText(address.getString("UserItems", "0"));
            nameShow.setText(address.getString("NameCustomerItems", "0"));

            factorShow.setText(address.getString("FactorItems", "0"));
            String str3 = formatter.format(Long.valueOf(address.getString("TotalPriceIdItems", "0"))) + " تومان";
            totalShow.setText(str3);
            paymentShow.setText(Payment(address.getString("PaymentMethodItems", "0")));
            postShow.setText(Post(address.getString("SendMethodItems", "0")));


            String factor = address.getString("FactorItems", "0");
            String mobile = address.getString("UserItems", "0");

            getBasketItems historyBasket = new getBasketItems();
            historyBasket.get_Items(BasketActivity.this, progressBar, recyclerViewlist, emptyText, BasketLayout, factor, mobile);
        }
    }

    private String Payment(String method) {
        String str = "";
        switch (method) {
            case "1":
                str = " نقدی ";
                break;
            case "2":
                str = " اعتباری ";
                break;
        }
        return str;
    }

    private String Post(String method) {
        String str = "";

        switch (method) {
            case "1":
                str = " پست ";
                break;
            case "2":
                str = " باربری ";
                break;
            case "3":
                str = " نمایندگی ";
                break;
        }
        return str;
    }


}
