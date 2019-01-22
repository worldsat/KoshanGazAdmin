package ir.koshangas.pasargad.comment;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.koshangas.pasargad.OnLoadMoreListener;


public class getComment {

    private getCommentAdapter ad;
    private int page = 0;

    private static List<String> IdItems = new ArrayList<>();
    private static List<String> VisibleItems = new ArrayList<>();
    private static List<String> TextItems = new ArrayList<>();
    private static List<String> DateItems = new ArrayList<>();
    private static List<String> NameProductItems = new ArrayList<>();
    private static List<String> MobileItems = new ArrayList<>();


    public void get_comment(final Context context, final ProgressBar ProgressBar, final RecyclerView recyclerViewlist, final Button tryagain_btn, final TextView tryagain_txt) {
        final String urlJsonArray = "http://www.koshangaspasargad.ir/koshangas/admin" + "/getComment.php";
        recyclerViewlist.setVisibility(View.INVISIBLE);
        ProgressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlJsonArray,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            recyclerViewlist.setVisibility(View.VISIBLE);
                            tryagain_txt.setVisibility(View.GONE);
                            tryagain_btn.setVisibility(View.GONE);


                            IdItems.clear();
                            TextItems.clear();
                            DateItems.clear();
                            VisibleItems.clear();
                            NameProductItems.clear();
                            MobileItems.clear();


                            JSONObject jsonRootObject = new JSONObject(response);
                            JSONArray array = jsonRootObject.optJSONArray("Data");


                            if (array.length() == 0) {
                                tryagain_txt.setVisibility(View.VISIBLE);
                                tryagain_txt.setText("هیچ پیامی موجود نیست");
                            } else {
                                tryagain_txt.setVisibility(View.GONE);
                            }


                            for (int i = 0; i < array.length(); i++) {

                                JSONObject person = array.getJSONObject(i);

                                IdItems.add(person.getString("Id"));
                                DateItems.add(person.getString("Date"));
                                TextItems.add(person.getString("Text"));
                                NameProductItems.add(person.getString("NameProduct"));
                                VisibleItems.add(person.getString("Visiblity"));
                                MobileItems.add(person.getString("Mobile"));


                            }
                            try {

                                recyclerViewlist.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

                                ad = new getCommentAdapter(context,MobileItems, NameProductItems,VisibleItems, IdItems, TextItems, DateItems, recyclerViewlist);
                                recyclerViewlist.setAdapter(ad);

                            } catch (Exception e) {
                                Toast.makeText(context, "خطایی پیش آمده است لطفا دوباره امتحان کنید", Toast.LENGTH_SHORT).show();
                                Log.i("mohsenjamali", "onResponseError: " + e);
                            }

                            recyclerViewlist.setVisibility(View.VISIBLE);
                            ProgressBar.setVisibility(View.GONE);

                            ad.setOnLoadMoreListener(new OnLoadMoreListener() {
                                @Override
                                public void onLoadMore() {
                                    IdItems.add(null);
                                    ad.notifyItemInserted(IdItems.size() - 1);

                                    //-----------------------------------------------------------------------------------------------
                                    RequestQueue MyRequestQueue = Volley.newRequestQueue(context);
                                    StringRequest MyStringRequest = new StringRequest(Request.Method.POST, urlJsonArray, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {

                                            IdItems.remove(IdItems.size() - 1);
                                            ad.notifyItemRemoved(IdItems.size());

                                            try {
                                                JSONObject jsonRootObject = new JSONObject(response);
                                                JSONArray jsonArray = jsonRootObject.optJSONArray("Data");

                                                for (int i = 0; i < jsonArray.length(); i++) {

                                                    JSONObject person = jsonArray.getJSONObject(i);


                                                    IdItems.add(person.getString("Id"));
                                                    VisibleItems.add(person.getString("Visiblity"));
                                                    DateItems.add(person.getString("Date"));
                                                    TextItems.add(person.getString("Text"));
                                                    MobileItems.add(person.getString("Mobile"));
                                                    NameProductItems.add(person.getString("NameProduct"));
                                                }
                                                ad.setLoaded();

                                            } catch (JSONException e) {
                                                ProgressBar.setVisibility(View.GONE);
                                                tryagain_txt.setVisibility(View.VISIBLE);
                                                tryagain_btn.setVisibility(View.VISIBLE);
                                                recyclerViewlist.setVisibility(View.GONE);
                                                tryagain_txt.setText("خطایی داخلی رخ داده است !");
                                                Log.i("mohsenjamali", "onErrorResponse:loadmore1 " + e.getMessage());
                                            }

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            ProgressBar.setVisibility(View.GONE);
                                            tryagain_txt.setVisibility(View.VISIBLE);
                                            tryagain_btn.setVisibility(View.VISIBLE);
                                            recyclerViewlist.setVisibility(View.GONE);
                                            tryagain_txt.setText("خطای داخلی رخ داده است !");
                                            Log.i("mohsenjamali", "onErrorResponse1::loadmore2 " + error.getMessage());
                                        }
                                    }) {
                                        protected Map<String, String> getParams() {
                                            Map<String, String> MyData = new HashMap<>();
                                            page = page + 1;
                                            MyData.put("PageNumber", String.valueOf(page));

                                            return MyData;
                                        }
                                    };


                                    MyRequestQueue.add(MyStringRequest);
                                    ad.notifyDataSetChanged();
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();

                            ProgressBar.setVisibility(View.GONE);
                            tryagain_txt.setVisibility(View.VISIBLE);
                            tryagain_btn.setVisibility(View.VISIBLE);
                            recyclerViewlist.setVisibility(View.GONE);
                            tryagain_txt.setText("ارتباط با سرور برقرار نشد !");
                            Log.i("mohsenjamali", "onErrorResponse: " + e.getMessage());
                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                ProgressBar.setVisibility(View.GONE);
                tryagain_txt.setVisibility(View.VISIBLE);
                tryagain_btn.setVisibility(View.VISIBLE);
                recyclerViewlist.setVisibility(View.GONE);
                tryagain_txt.setText("مشکل در دریافت اطلاعات !");
                Log.i("mohsenjamali", "onErrorResponse: " + error.getMessage());

            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<>();
                MyData.put("PageNumber", String.valueOf(page));

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
