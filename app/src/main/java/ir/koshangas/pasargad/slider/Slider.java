package ir.koshangas.pasargad.slider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
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

/**
 * Created by Moh3n on 29/06/2018.
 */

public class Slider {
    private SharedPreferences pic_reader;
    private MaterialDialog wait;
    private String[] picReader = new String[7];
    private String nothing = "nothing to show";

    public void connect_product(final Context context, final String link, final String Mode) {

        wait = new MaterialDialog.Builder(context)
                .cancelable(false)
                .content("لطفا منتظر بمانید")
                .progress(true, 0)
                .build();
        wait.show();
        final SharedPreferences sp = context.getSharedPreferences("Token", 0);
        pic_reader = context.getSharedPreferences("picture", 0);

        String url = "http://www.koshangaspasargad.ir/koshangas/admin/new_slider.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        wait.dismiss();
                        Toast.makeText(context, "ویرایش با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        wait.dismiss();
                        Toast.makeText(context, "خطایی پیش آمده است !! لطفا لحظاتی دیگر تلاش فرمائید", Toast.LENGTH_LONG).show();
                        Log.i("mohsenjamali", "onErrorResponse: " + error);

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                for (int i = 1; i < 7; i++) {
                    String i_str = String.valueOf(i);
                    picReader[i] = pic_reader.getString("pic" + String.valueOf(i), "nothing to show");
                    if (picReader[i].compareTo(nothing) != 0) {

                        String image = pic_reader.getString("pic" + i_str, "nothing to show");
                        params.put("image" + i_str, image);
                        params.put("image" + i_str + "_postfix", "jpg");

                    }else{
                        params.put("image" + i_str, "no_pic");
                    }
                }

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                String token = "Bearer " + sp.getString("token", "nothing");
                params.put("Authorization", token);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new

                DefaultRetryPolicy(
                300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
