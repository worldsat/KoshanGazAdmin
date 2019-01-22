package ir.koshangas.pasargad.login;

import android.content.Context;
import android.content.Intent;
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

import ir.koshangas.pasargad.MainActivity;

public class GetToken {

    public void connect(final Context context, final String username, final String password) {

     final   MaterialDialog   wait = new MaterialDialog.Builder(context)
                .cancelable(false)
                .content("لطفا منتظر بمانید")
                .progress(true, 0)
                .build();
        wait.show();
        String url = "http://www.koshangaspasargad.ir/koshangas/admin/login.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       wait.dismiss();
                        try {
                          if (response.equals("{\"Message\":0}")){
                            //Log.i("mohsenjamali", "arrayLenght: " + jsonRootObject.getString("token"));
//                            SharedPreferences sp = context.getSharedPreferences("Token", 0);
//                            SharedPreferences.Editor edit = sp.edit();
//                            edit.putString("token", jsonRootObject.getString("token"));
//                            edit.apply();
//                            Log.i("mohsenjamali", "arrayerror: " +jsonRootObject.getString("token"));
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);}
                            else{
                              Toast.makeText(context, "رمز عبور یا نام کاربری اشتباه است", Toast.LENGTH_LONG).show();
                          }
                        } catch (Exception e) {
                            wait.dismiss();
                            Log.i("mohsenjamali", "arrayerror: " + e.toString());
                            Toast.makeText(context, "خطا در اتصال به سامانه", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        wait.dismiss();
                        Toast.makeText(context, "خطا در اتصال به سامانه", Toast.LENGTH_LONG).show();
                        Log.i("mohsenjamali", "onErrorResponse: " + error);

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put("mobile", username);
                params.put("password", password);

                return params;
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
