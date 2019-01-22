package ir.koshangas.pasargad.history;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
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
import ir.koshangas.pasargad.R;


public class getHistoryExcelBasket {

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
    private MaterialDialog wait;
    int from, to;
    private DecimalFormat formatter = new DecimalFormat("###,###,###,###");

    public void get_Items(final Context context, final ProgressBar ProgressBar, final RecyclerView recyclerViewlist, final TextView emptyText, final ConstraintLayout BasketLayout, final Button excelBtn) {
        excelBtn.setVisibility(View.GONE);
        final SharedPreferences sp = context.getSharedPreferences("Token", 0);
        String urlJsonArray = "http://www.koshangaspasargad.ir/koshangas/admin/" + "getHistoryExcelBasket.php";
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

                            JSONObject jsonRootObject = new JSONObject(response);
                            JSONArray array = jsonRootObject.optJSONArray("Data");

                            MiladiToShamsi miladiToShamsi = new MiladiToShamsi();

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject person = array.getJSONObject(i);
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

                            }


                            recyclerViewlist.setVisibility(View.VISIBLE);
                            ProgressBar.setVisibility(View.GONE);
                            excelBtn.setVisibility(View.VISIBLE);

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


        excelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wait = new MaterialDialog.Builder(context)
                        .cancelable(false)
                        .content("لطفا منتظر بمانید")
                        .progress(true, 0)
                        .build();


                final MaterialDialog about = new MaterialDialog.Builder(context)
                        .customView(R.layout.alert_excel, false)
                        .autoDismiss(false)
                        .build();
                about.show();

                Button BuildBtn = (Button) about.findViewById(R.id.buildExcel);

                final EditText edtExcel1 = (EditText) about.findViewById(R.id.edtExcle1);
                final EditText edtExcel2 = (EditText) about.findViewById(R.id.edtExcel2);

                BuildBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        wait.show();
                        if (edtExcel1.getText().length() > 0 && edtExcel2.getText().length() > 0) {
                            int edt1 = Integer.valueOf(edtExcel1.getText().toString());
                            int edt2 = Integer.valueOf(edtExcel2.getText().toString());

                            if (edt1 < edt2) {
                                from = edt1;
                                to = edt2;
                            } else if (edt1 > edt2) {
                                from = edt2;
                                to = edt1;
                            } else {
                                from = edt1;
                                to = edt2;
                            }
                            HistoryActivity HistoryActivity = new HistoryActivity();
                            HistoryActivity.saveExcelFile(context, "CNG Market.xls", from, to, DateItems, PriceItems, UserItems, NameItems, QtyItems, FactoreItems, TotalPriceIdItems);
                        } else {
                            Toast.makeText(context, "لطفا شماره ردیف رو وارد نمائید", Toast.LENGTH_SHORT).show();
                        }
                        wait.dismiss();
                        about.dismiss();
                    }
                });
            }
        });
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
