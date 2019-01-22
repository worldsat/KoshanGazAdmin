package ir.koshangas.pasargad.customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
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

import java.util.HashMap;
import java.util.Map;


public class setNewCustomer {


    public void edit(final Context context, final Activity activity, final String name, final String password, final String mobile, final String address, final String ostan, final String tell, final String shahr, final MaterialDialog ProgressBar, final TextView send,final String RadioCustomer, final Class className) {


        String urlJsonArray = "http://www.koshangaspasargad.ir/koshangas/admin/new_customer.php";


        ProgressBar.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlJsonArray,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        ProgressBar.dismiss();


                        switch (response) {
                            case "{\"Message\":0}":

                                activity.finish();

                                Toast.makeText(context, "مشتری ایجاد شد", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, Customer.class);
                                context.startActivity(intent);


                                break;
                            case "{\"Message\":1}":

                                Toast.makeText(context, "این شماره قبلا ثبت شده است", Toast.LENGTH_SHORT).show();


                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ProgressBar.dismiss();
                // send.setText(context.getString(R.string.signupButton));
                Log.i("mohsenjamali", "onErrorResponse: " + error.toString());
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<>();
                SharedPreferences sp = context.getSharedPreferences("Token", 0);

                MyData.put("Fullname", name);
                MyData.put("Address", address);
                MyData.put("PhoneNumber", tell);
                MyData.put("Mobile", mobile);
                MyData.put("Ostan", ostan);
                MyData.put("Shahr", shahr);
                MyData.put("Cat", RadioCustomer);

                //  MyData.put("Api_Token", sp.getString("token", "nothing"));

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
