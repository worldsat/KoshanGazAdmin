package ir.koshangas.pasargad.history;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.koshangas.pasargad.MiladiToShamsi;


public class getHistoryBasket {

    private getHistoryBasketAdapter ad;


    private static List<String> UserItems = new ArrayList<>();
    private static List<String> IdItems = new ArrayList<>();
    private static List<String> NameItems = new ArrayList<>();
    private static List<String> DateItems = new ArrayList<>();
    private static List<String> PriceItems = new ArrayList<>();
    private static List<String> ProductIdItems = new ArrayList<>();
    private static List<String> QtyItems = new ArrayList<>();
    private static List<String> FactoreItems = new ArrayList<>();
    private static List<String> TotalPriceIdItems = new ArrayList<>();
    private static List<String> PaymentMethodItems = new ArrayList<>();
    private static List<String> SendMethodItems = new ArrayList<>();
    private static List<String> StatusItems = new ArrayList<>();
    private static List<String> NameCustomerItems = new ArrayList<>();

    private static List<String> NumberItems = new ArrayList<>();

    private DecimalFormat formatter = new DecimalFormat("###,###,###,###");

    public void get_Items(final Context context, final ProgressBar ProgressBar, final RecyclerView recyclerViewlist, final TextView emptyText, final ConstraintLayout BasketLayout) {

        final SharedPreferences sp = context.getSharedPreferences("Token", 0);
        String urlJsonArray = "http://www.koshangaspasargad.ir/koshangas/admin/" + "getHistoryBasket.php";
        recyclerViewlist.setVisibility(View.INVISIBLE);
        ProgressBar.setVisibility(View.VISIBLE);
        BasketLayout.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlJsonArray,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            recyclerViewlist.setVisibility(View.VISIBLE);
                            BasketLayout.setVisibility(View.VISIBLE);
                            // tryagain_txt.setVisibility(View.GONE);
                            // tryagain_btn.setVisibility(View.GONE);

                            DateItems.clear();
                            PriceItems.clear();
                            IdItems.clear();
                            UserItems.clear();
                            NameItems.clear();
                            ProductIdItems.clear();
                            QtyItems.clear();
                            FactoreItems.clear();
                            TotalPriceIdItems.clear();
                            PaymentMethodItems.clear();
                            SendMethodItems.clear();
                            StatusItems.clear();
                            NumberItems.clear();
                            NameCustomerItems.clear();

                            JSONObject jsonRootObject = new JSONObject(response);
                            JSONArray array = jsonRootObject.optJSONArray("Data");

                            MiladiToShamsi miladiToShamsi = new MiladiToShamsi();
                            int n = array.length();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject person = array.getJSONObject(i);

                                NumberItems.add(String.valueOf(n));
                                n = n - 1;
                                DateItems.add(miladiToShamsi.convert(person.getString("Date")));
                                PriceItems.add(person.getString("Price"));
                                IdItems.add(person.getString("Id"));
                                UserItems.add(person.getString("User"));
                                NameItems.add(person.getString("Name"));
                                ProductIdItems.add(person.getString("Id"));
                                QtyItems.add(person.getString("Qty"));
                                FactoreItems.add(person.getString("Factor"));
                                TotalPriceIdItems.add(person.getString("TotalPrice"));
                                PaymentMethodItems.add(person.getString("Payment_method"));
                                SendMethodItems.add(person.getString("Send_method"));
                                StatusItems.add(person.getString("Status"));
                                NameCustomerItems.add(person.getString("NameCustomer"));

                            }


                            //set empty message in basket
                            HistoryActivity Basket = new HistoryActivity();
                            if (array.length() == 0) {
                                emptyText.setVisibility(View.VISIBLE);
                                Basket.setHiddenLayout(context);
                            } else {
                                emptyText.setVisibility(View.GONE);
                                Basket.setShowLayout(context);
                            }


                            try {

                                recyclerViewlist.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

                                ad = new getHistoryBasketAdapter(context, IdItems, DateItems, PriceItems, UserItems, NameItems, ProductIdItems, QtyItems, FactoreItems, TotalPriceIdItems, PaymentMethodItems, SendMethodItems, StatusItems, NumberItems, NameCustomerItems, emptyText, recyclerViewlist);
                                recyclerViewlist.setAdapter(ad);

                            } catch (Exception e) {
                                Toast.makeText(context, "خطایی پیش آمده است لطفا دوباره امتحان کنید", Toast.LENGTH_SHORT).show();
                                Log.i("mohsenjamali", "onResponseError: " + e);
                            }

                            recyclerViewlist.setVisibility(View.VISIBLE);
                            ProgressBar.setVisibility(View.GONE);


                        } catch (JSONException e) {
                            e.printStackTrace();

                            ProgressBar.setVisibility(View.GONE);
                            BasketLayout.setVisibility(View.GONE);

                            //    tryagain_txt.setVisibility(View.VISIBLE);
                            //    tryagain_btn.setVisibility(View.VISIBLE);
                            recyclerViewlist.setVisibility(View.GONE);
                            //    tryagain_txt.setText("ارتباط با سرور برقرار نشد !");
                            Log.i("mohsenjamali", "onErrorResponse: " + e.getMessage());
                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ProgressBar.setVisibility(View.GONE);
                BasketLayout.setVisibility(View.GONE);

                //     tryagain_txt.setVisibility(View.VISIBLE);
                //     tryagain_btn.setVisibility(View.VISIBLE);
                recyclerViewlist.setVisibility(View.GONE);
                //     tryagain_txt.setText("مشکل در دریافت اطلاعات !");
                Log.i("mohsenjamali", "onErrorResponse: " + error.getMessage());

            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<>();
                MyData.put("Api_token", sp.getString("token", "nothing"));

                return MyData;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);


    }

    private String changeNumber(String num) {
        num = num.replaceAll("۰", "0");
        num = num.replaceAll("۱", "1");
        num = num.replaceAll("۲", "2");
        num = num.replaceAll("۳", "3");
        num = num.replaceAll("۴", "4");
        num = num.replaceAll("۵", "5");
        num = num.replaceAll("۶", "6");
        num = num.replaceAll("۷", "7");
        num = num.replaceAll("۸", "8");
        num = num.replaceAll("۹", "9");
        return num;
    }


}
