package ir.koshangas.pasargad.middleCategory2;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class getSearchCategory2 {


    private TextView tryagain_txt;
    private Button tryagain_btn;
    private int pageTotal = 0;
    private int page = 1;


    private static List<String> ImageItems = new ArrayList<>();

    private static List<String> IdItems = new ArrayList<>();

    private static List<String> NameItems = new ArrayList<>();


    public void get_banners(final Context context, final ProgressBar ProgressBar) {


        String urlJsonArray = "http://www.koshangaspasargad.ir/koshangas/admin/get_category.php";


        ProgressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlJsonArray,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            // tryagain_txt.setVisibility(View.GONE);
                            // tryagain_btn.setVisibility(View.GONE);

                            //   ImageItems.clear();
                            IdItems.clear();
                            NameItems.clear();


                            JSONObject jsonRootObject = new JSONObject(response);
                            JSONArray array = jsonRootObject.optJSONArray("data");

                            // pageTotal = jsonRootObject.getInt("TotalItemCount") / 5;

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject person = array.getJSONObject(i);

                                //  ImageItems.add(person.getString("Image"));
                                IdItems.add(person.getString("id"));
                                NameItems.add(person.getString("name"));
                                Log.i("mohsenjamali", "onResponse: " + person.getString("name"));

                            }
//                            New_MiddleCategory middleCategory = new New_MiddleCategory();
//                            middleCategory.spinner_category(context, NameItems, IdItems);
                            // advancedSearchActivity.SearchBtn(context);

                            ProgressBar.setVisibility(View.GONE);


                        } catch (JSONException e) {
                            e.printStackTrace();

                            ProgressBar.setVisibility(View.GONE);

                            //    tryagain_txt.setVisibility(View.VISIBLE);
                            //    tryagain_btn.setVisibility(View.VISIBLE);

                            //    tryagain_txt.setText("ارتباط با سرور برقرار نشد !");
                            Log.i("mohsenjamali", "onErrorResponse: " + e.getMessage());
                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ProgressBar.setVisibility(View.GONE);
                //     tryagain_txt.setVisibility(View.VISIBLE);
                //     tryagain_btn.setVisibility(View.VISIBLE);
                //     tryagain_txt.setText("مشکل در دریافت اطلاعات !");
                Log.i("mohsenjamali", "onErrorResponse: " + error.getMessage());

            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<>();
                MyData.put("PageNumber", String.valueOf(page));
                MyData.put("parent_id", "0");
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
