package ir.koshangas.pasargad.slider;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ir.koshangas.pasargad.R;


public class getSlider {

    public void get_banners(final Context context, final ImageView[] upload_image) {

        String urlJsonArray = "http://www.koshangaspasargad.ir/koshangas/getSlider.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlJsonArray,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonRootObject = new JSONObject(response);
                            JSONArray array = jsonRootObject.optJSONArray("Data");

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject person = array.getJSONObject(i);

                                String link2 = "http://www.koshangaspasargad.ir/koshangas/slider/" + person.getString("Image");

                                SharedPreferences pic_reader = context.getSharedPreferences("picture", 0);


                                if (pic_reader.getString("pic" + person.getInt("Id"), "nothing to show").equals("nothing to show")) {
                                    Glide.with(context)
                                            .load(link2)
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .skipMemoryCache(true)
                                            .error(R.mipmap.emptybanner)
                                            .placeholder(R.mipmap.emptybanner)
                                            .into(upload_image[person.getInt("Id")]);
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i("mohsenjamali", "onErrorResponseSlider: " + e.getMessage());
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("mohsenjamali", "onErrorResponseSlider: " + error.getMessage());
            }

        });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
