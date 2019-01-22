package ir.koshangas.pasargad.history;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class deleteRowHistoryBasket {


    public void delete_row(final Context context, final String Factor_Id, final String mobile) {

        final SharedPreferences sp = context.getSharedPreferences("Token", 0);
        String urlJsonArray = "http://www.koshangaspasargad.ir/koshangas/admin/" + "deleteRowHistoryBasket.php";
        final MaterialDialog wait = new MaterialDialog.Builder(context)
                .cancelable(false)
                .content("لطفا منتظر بمانید")
                .progress(true, 0)
                .build();
        wait.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlJsonArray,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        if (response.equals("{\"Message\":0}")) {

                            Toast.makeText(context, "حذف با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, HistoryActivity.class);
                            context.startActivity(intent);

                        } else {

                            Toast.makeText(context, "خطا در سمت سرور", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, HistoryActivity.class);
                            context.startActivity(intent);

                        }
                        wait.dismiss();
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                wait.dismiss();
                Log.i("mohsenjamali", "onErrorResponse: " + error.getMessage());

            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<>();
                MyData.put("Api_token", mobile);
                MyData.put("fatcor_id", Factor_Id);
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


}
