package ir.koshangas.pasargad;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
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

import ir.koshangas.pasargad.category.Category;
import ir.koshangas.pasargad.customer.Customer;
import ir.koshangas.pasargad.login.EnterPasswordActivity;
import ir.koshangas.pasargad.middleCategory.MiddleCategory;
import ir.koshangas.pasargad.middleCategory2.MiddleCategory2;
import ir.koshangas.pasargad.product.getProductsAdapter;
import ir.koshangas.pasargad.product.product;

public class DateVolley {
    private static List<String> NameItems = new ArrayList<>();
    private static List<String> TextItems = new ArrayList<>();
    private static List<String> IDItem = new ArrayList<>();
    private static List<String> ImageItems = new ArrayList<>();
    private static List<String> IdItems = new ArrayList<>();
    private static List<String> DescriptionItems = new ArrayList<>();
    private static List<String> CategoryItems = new ArrayList<>();
    private static List<String> AddressItems = new ArrayList<>();
    private static List<String> MobileItems = new ArrayList<>();
    private static List<String> PasswordItems = new ArrayList<>();
    private static List<String> AvailableItems = new ArrayList<>();
    private static List<String> ParentItems = new ArrayList<>();
    private static List<String> DiscountItems = new ArrayList<>();
    private static List<String> SpecialItems = new ArrayList<>();
    private static List<String> CityItems = new ArrayList<>();
    private static List<String> StateItems = new ArrayList<>();
    private static List<String> TellItems = new ArrayList<>();
    private static List<String> PercentDiscountItems = new ArrayList<>();


    private static List<String> priceItems = new ArrayList<>();

    private static List<String> Votes = new ArrayList<>();
    private static List<String> MainImageItems = new ArrayList<>();
    private static List<String> Image1Items = new ArrayList<>();
    private static List<String> Image2Items = new ArrayList<>();
    private static List<String> Image3Items = new ArrayList<>();
    private static List<String> Image4Items = new ArrayList<>();
    private static List<String> Image5Items = new ArrayList<>();
    private static List<String> Image6Items = new ArrayList<>();
    private static List<String> OtherImageItems = new ArrayList<>();
    private static List<String> ShowPrice = new ArrayList<>();
    private static List<String> olaviat = new ArrayList<>();

    private String url;
    private int rq;
    private RVAdapter ad;
    private getProductsAdapter ad2;

    private RVAdapterListName adListname;
    private int page = 2;
    private int paging = 2;
    private Intent intent_delete;
    private MaterialDialog wait;
    private String jsonResponseName, jsonResponseParentID, jsonResponseIDID, jsonResponseId, jsonResponseImage, jsonResponseDescription, jsonResponseCategory, jsonResponseText, jsonResponseAddress, jsonResponseMobile, jsonResponsePassword, jsonResponseAvailable;
    private String nothing = "nothing to show";
    private SharedPreferences pic_reader;
    private String[] picReader = new String[7];
    private String urlJsonArray = "";

    public void connect(final Context context, final String link, final String Mode, final Boolean Middle, final String cat_id, final String picture, final String name_category, final String id, final String band) {
        wait = new MaterialDialog.Builder(context)
                .cancelable(false)
                .content("لطفا منتظر بمانید")
                .progress(true, 0)
                .build();
        wait.show();
        Log.i("mohsenjamali", "band: " + band);
        final SharedPreferences sp = context.getSharedPreferences("Token", 0);
        switch (link) {
            case "register":
                url = "http://ghomashe.com/api/register";
                break;
            case "activity_product":
                url = "http://ghomashe.com/api/activity_product";
                break;
            case "activity_category":
                url = "http://koshangaspasargad.ir/koshangas/admin/new_category.php";
                break;
        }
        switch (Mode) {
            case "get":
                rq = Request.Method.GET;
                break;
            case "send":
                rq = Request.Method.POST;
                break;
            case "update":
                rq = Request.Method.POST;
                break;
            case "delete":
                // rq = Request.Method.DELETE;
                rq = Request.Method.POST;

                break;
        }
        if (Mode.equals("delete")) {
            if (link.equals("activity_category")) {
                url = "http://www.koshangaspasargad.ir/koshangas/admin/delete_category.php";
            } else if (link.equals("activity_product")) {
                url = "http://www.koshangaspasargad.ir/koshangas/admin/delete_products.php";
            }
        }
        if (Mode.equals("update")) {
            if (link.equals("activity_category")) {
                url = "http://www.koshangaspasargad.ir/koshangas/admin/update_category.php";
            } else if (link.equals("activity_product")) {
                url = "http://ghomashe.com/api/activity_product/update";
            }
        }

        StringRequest stringRequest = new StringRequest(rq, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        wait.dismiss();
                        // Log.i("mohsenjamali", "Done " + response);

                        if (Mode.equals("delete")) {
                            Toast.makeText(context, "حذف با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                            if (link.equals("activity_category")) {
                                if (Middle && band.equals("0")) {
                                    intent_delete = new Intent(context, MiddleCategory.class);
                                } else if (band.equals("1")) {
                                    intent_delete = new Intent(context, MiddleCategory2.class);
                                } else {
                                    intent_delete = new Intent(context, Category.class);
                                }

                            } else if (link.equals("activity_product")) {
                                intent_delete = new Intent(context, product.class);
                            }
                            context.startActivity(intent_delete);
                        }
                        if (Mode.equals("update")) {
                            Toast.makeText(context, "ویرایش با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                            if (Middle && band.equals("0")) {
                                intent_delete = new Intent(context, MiddleCategory.class);
                            } else if (band.equals("1")) {
                                intent_delete = new Intent(context, MiddleCategory2.class);
                            } else {
                                intent_delete = new Intent(context, Category.class);
                            }
                            Log.i("mohsenjamali", "onResponseMiddleBand: " + band);
                            context.startActivity(intent_delete);
                        }

                        if (Mode.equals("send")) {
                            Toast.makeText(context, "عملیات با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                            if (link.equals("activity_category")) {
                                if (Middle && band.equals("0")) {
                                    intent_delete = new Intent(context, MiddleCategory.class);
                                } else if (band.equals("1")) {
                                    intent_delete = new Intent(context, MiddleCategory2.class);
                                } else {
                                    intent_delete = new Intent(context, Category.class);
                                }
                            } else if (link.equals("activity_product")) {
                                intent_delete = new Intent(context, product.class);
                            }
                            context.startActivity(intent_delete);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        wait.dismiss();
                        Toast.makeText(context, "خطایی پیش آمده است !! لطفا لحظاتی دیگر تلاش فرمائید", Toast.LENGTH_LONG).show();
                        Log.i("mohsenjamali", "onErrorResponse: " + e);

                        // onErrorResponse2(error, context);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                switch (Mode) {
                    case "delete":
                        params.put("id", id);
                        break;
                    case "send":
                        if (link.equals("activity_category")) {
                            params.put("name", name_category);
                            params.put("band", band);

                            if (!picture.equals("")) {
                                params.put("image", picture);
                            } else {
                                params.put("image", "no_pic");
                            }
                            if (Middle) {
                                params.put("parent_id", cat_id);
                                Log.i("mohsenjamali", "middle_send: " + cat_id + " " + picture);
                            } else {
                                params.put("parent_id", "0");
                            }
                            Log.i("mohsenjamali", "CAT: " + " " + picture);
                        }
                        break;
                    case "update":
                        if (link.equals("activity_category")) {
                            params.put("name", name_category);
                            params.put("band", band);
                            if (!picture.equals("")) {
                                params.put("image", picture);
                            } else {
                                params.put("image", "no_pic");
                            }

                            params.put("id", id);
                            if (Middle) {
                                params.put("parent_id", cat_id);
                                Log.i("mohsenjamali", "middle_send: " + cat_id);
                            }
                        }
                        // Log.i("mohsenjamali", "update: " + id + " " + name_category);
                        break;

                }
                //  Log.i("mohsenjamali", "update2: " + " " + id + " " + Mode + " " + url);
                //Log.i("mohsenjamali", "update3: " + " " + picture + " " + rq);
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
        CustomHurlStack customHurlStack = new CustomHurlStack();
/*
        HttpStack httpStack;
        if (Build.VERSION.SDK_INT > 19) {
            httpStack = new CustomHurlStack();
        } else if ( Build.VERSION.SDK_INT <= 19) {
         //   httpStack = new OkHttpHurlStack();
        } else {
            httpStack = new HttpClientStack(AndroidHttpClient.newInstance("Android"));
        }

*/
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void connect_product(final Context context, final String link, final String Mode, final String description, final String name_product, final String id, final String product_id, final String available, final String special, final String ShowPrice,final String showNewProduct,final String khadamat, final String price, final String discount, final String votes, final String olaviat, final String percentDiscount) {

        wait = new MaterialDialog.Builder(context)
                .cancelable(false)
                .content("لطفا منتظر بمانید")
                .progress(true, 0)
                .build();
        wait.show();
        final SharedPreferences sp = context.getSharedPreferences("Token", 0);
        pic_reader = context.getSharedPreferences("picture", 0);

        switch (link) {

            case "activity_product":
                url = "http://www.koshangaspasargad.ir/koshangas/admin/new_product.php";
                break;

        }
        switch (Mode) {
            case "get":
                rq = Request.Method.GET;
                break;
            case "send":
                rq = Request.Method.POST;
                break;
            case "update":
                rq = Request.Method.POST;
                break;
            case "delete":
                // rq = Request.Method.DELETE;
                rq = Request.Method.POST;

                break;
        }
        if (Mode.equals("delete")) {
            if (link.equals("activity_product")) {
                url = "http://www.koshangaspasargad.ir/koshangas/admin/delete_products.php";
            }
        }
        if (Mode.equals("update")) {
            if (link.equals("activity_product")) {
                url = "http://www.koshangaspasargad.ir/koshangas/admin/update_products.php";
            }
        }

        StringRequest stringRequest = new StringRequest(rq, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        wait.dismiss();
                        if (Mode.equals("delete")) {
                            Toast.makeText(context, "حذف با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, product.class);
                            context.startActivity(intent);
                        }
                        if (Mode.equals("update")) {
                            Toast.makeText(context, "ویرایش با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, product.class);
                            context.startActivity(intent);
                        }
                        if (Mode.equals("send")) {
                            Toast.makeText(context, "محصول با موفقیت ایجاد شد", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, product.class);
                            context.startActivity(intent);
                        }
                        Log.i("mohsenjamali", "onResponse: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        wait.dismiss();
                        Toast.makeText(context, "خطایی پیش آمده است !! لطفا لحظاتی دیگر تلاش فرمائید", Toast.LENGTH_LONG).show();
                        Log.i("mohsenjamali", "onErrorResponse: " + error);

                        //onErrorResponse2(error, context);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                SharedPreferences pic_link = context.getSharedPreferences("pic_link", 0);

                switch (Mode) {
                    case "delete":
                        params.put("id", id);
                        break;
                    case "send":
                        params.put("name", name_product);
                        params.put("description", description);
                        params.put("price", price);
                        params.put("votes", votes);
                        params.put("percentDiscount", percentDiscount);
                        params.put("discount", discount);
                        params.put("olaviat", olaviat);


                        if (pic_reader.getString("pic0", "nothing to show").compareTo(nothing) != 0
                                || pic_reader.getString("pic0", "nothing to show").equals("null")) {
                            String name = "pic_" + product_id + "_0";
                            params.put("main_image", pic_reader.getString("pic0", "nothing to show"));
                            params.put("image0_delete", pic_link.getString(name, "nothing to show"));
                            Log.i("mohsenjamali", "getParamsMainImage: " + pic_reader.getString("pic0", "nothing to show"));
                        } else {
                            params.put("main_image", "no_pic");
                        }

                        params.put("main_image_postfix", "jpg");
                        params.put("thumbnail_postfix", "jpg");
                        params.put("category_id", id);
                        params.put("isAvailable", available);
                        params.put("special", special);
                        params.put("ShowPrice", ShowPrice);
                        params.put("showNewProduct", showNewProduct);
                        params.put("khadamat", khadamat);

                        //   Log.i("mohsenjamali", "getParams: " + id + " " + pic_reader.getString("pic0", "nothing to show"));

                        //بررسی عکس ها برای ارسال
                        for (int i = 1; i < 7; i++) {
                            String i_str = String.valueOf(i);
                            picReader[i] = pic_reader.getString("pic" + String.valueOf(i), "nothing to show");
                            if (picReader[i].compareTo(nothing) != 0) {

                                String image = pic_reader.getString("pic" + i_str, "nothing to show");
                                params.put("image" + i_str, image);
                                params.put("image" + i_str + "_postfix", "jpg");


                            } else {
                                params.put("image" + i_str, "no_pic");
                            }
                        }
                        break;
                    case "update":
                        params.put("id", product_id);
                        params.put("name", name_product);
                        params.put("description", description);
                        params.put("price", price);
                        params.put("votes", votes);
                        params.put("discount", discount);
                        params.put("olaviat", olaviat);
                        params.put("percentDiscount", percentDiscount);

                        if (pic_reader.getString("pic0", "nothing to show").compareTo(nothing) != 0
                                || pic_reader.getString("pic0", "nothing to show").equals("null")) {
                            String name = "pic_" + product_id + "_0";
                            params.put("main_image", pic_reader.getString("pic0", "nothing to show"));
                            params.put("image0_delete", pic_link.getString(name, "nothing to show"));
                            Log.i("mohsenjamali", "getParamsMainImage: " + pic_reader.getString("pic0", "nothing to show"));
                        } else {
                            params.put("main_image", "no_pic");
                            params.put("image0_delete", "no_pic");
                        }
                        params.put("main_image_postfix", "jpg");
                        params.put("thumbnail_postfix", "jpg");
                        params.put("category_id", id);
                        params.put("isAvailable", available);
                        params.put("special", special);
                        params.put("ShowPrice", ShowPrice);
                        params.put("showNewProduct", showNewProduct);
                        params.put("khadamat", khadamat);
                        //  Log.i("mohsenjamali", "update: " + pic_reader.getString("pic2", "nothing to show"));
                        //  SharedPreferences pic_database = context.getSharedPreferences("pic_database", 0);
                        //بررسی عکس ها برای ارسال

                        for (int i = 1; i < 7; i++) {
                            String i_str = String.valueOf(i);

                            picReader[i] = pic_reader.getString("pic" + String.valueOf(i), "nothing to show");
                            if (picReader[i].compareTo(nothing) != 0) {

                                String image = pic_reader.getString("pic" + i_str, "nothing to show");
                                params.put("image" + i_str, image);
                                String name = "pic_" + product_id + "_" + (i);
                                params.put("image" + i_str + "_delete", pic_link.getString(name, "nothing to show"));
                                Log.i("mohsenjamali", "getParams: " + i + " " + pic_reader.getString("pic" + i_str, "nothing to show"));

                            } else {
                                params.put("image" + i_str, "no_pic");
                                params.put("image" + i_str + "_delete", "no_pic");
                            }
                        }
                        break;
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

    public void del_image(final Context context, final String index, final String id) {
        wait = new MaterialDialog.Builder(context)
                .cancelable(false)
                .content("لطفا منتظر بمانید")
                .progress(true, 0)
                .build();
        wait.show();
        final SharedPreferences sp = context.getSharedPreferences("Token", 0);
        url = "http://ghomashe.com/api/activity_product/image/delete";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        wait.dismiss();
                        //   Toast.makeText(context, "حذف با موفقیت انجام شد", Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        wait.dismiss();
                        Toast.makeText(context, "خطایی پیش آمده است !! لطفا لحظاتی دیگر تلاش فرمائید", Toast.LENGTH_LONG).show();
                        Log.i("mohsenjamali", "onErrorResponse: " + error);

                        // onErrorResponse2(error, context);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("index", index);

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
        CustomHurlStack customHurlStack = new CustomHurlStack();
/*
        HttpStack httpStack;
        if (Build.VERSION.SDK_INT > 19) {
            httpStack = new CustomHurlStack();
        } else if ( Build.VERSION.SDK_INT <= 19) {
         //   httpStack = new OkHttpHurlStack();
        } else {
            httpStack = new HttpClientStack(AndroidHttpClient.newInstance("Android"));
        }

*/
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void onErrorResponse2(VolleyError error, Context context) {
        String json = null;

        NetworkResponse response = error.networkResponse;
        if (response != null && response.data != null) {
            switch (response.statusCode) {
                case 400:
                    json = new String(response.data);
                    //  json = trimMessage(json, "message");
                    if (json != null) displayMessage(json, context);
                    break;
            }
        }
    }

    public void displayMessage(String toastString, Context context) {
        Toast.makeText(context, toastString, Toast.LENGTH_LONG).show();
    }

    public void get_banners(final Context context, final String Mode, final String search, final RecyclerView recyclerViewlist, final TextView tryagain_txt, final Button tryagain_btn, final ProgressBar progressBarOne, final TextView ExcelBtn) {
        String urlJsonArray = "";

        if (Mode.equals("activity_category")) {
            urlJsonArray = "http://www.koshangaspasargad.ir/koshangas/admin/get_category.php";

        } else if (Mode.equals("activity_product")) {
            urlJsonArray = "http://www.koshangaspasargad.ir/koshangas/admin/get_products.php";

        }
        if (Mode.equals("middle_category")) {
            urlJsonArray = "http://www.koshangaspasargad.ir/koshangas/admin/get_category.php";
        }
        if (Mode.equals("search_product")) {
            urlJsonArray = "http://www.koshangaspasargad.ir/koshangas/admin/search_product.php";
        }
        recyclerViewlist.setVisibility(View.GONE);
        progressBarOne.setVisibility(View.VISIBLE);
        StringRequest req = new StringRequest(Request.Method.POST, urlJsonArray,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            recyclerViewlist.setVisibility(View.VISIBLE);
                            tryagain_txt.setVisibility(View.GONE);
                            tryagain_btn.setVisibility(View.GONE);

                            NameItems.clear();
                            ImageItems.clear();
                            IdItems.clear();
                            CategoryItems.clear();
                            DescriptionItems.clear();
                            AvailableItems.clear();
                            SpecialItems.clear();
                            PercentDiscountItems.clear();


                            DiscountItems.clear();
                            IdItems.clear();
                            DescriptionItems.clear();
                            NameItems.clear();
                            priceItems.clear();
                            Votes.clear();
                            MainImageItems.clear();
                            Image1Items.clear();
                            Image2Items.clear();
                            Image3Items.clear();
                            Image4Items.clear();
                            Image5Items.clear();
                            Image6Items.clear();
                            OtherImageItems.clear();
                            ShowPrice.clear();
                            olaviat.clear();

                            JSONObject jsonRootObject = new JSONObject(response);
                            JSONArray array2 = jsonRootObject.optJSONArray("data");
                            //  Log.i("mohsenjamali", "onResponseTEst: " + array2.length());
                            for (int i = 0; i < array2.length(); i++) {

                                JSONObject person = array2.getJSONObject(i);
                                if (Mode.equals("activity_product") || Mode.equals("search_product")) {

                                    String images[] = new String[7];
                                    SharedPreferences pic_database = context.getSharedPreferences("pic_database", 0);
                                    SharedPreferences pic_link = context.getSharedPreferences("pic_link", 0);
                                    for (int n = 1; n < 7; n++) {

                                        images[n] = person.getString("Image" + n);
                                        String id = person.getString("Id");
                                        String name = "pic_" + id + "_" + (n);
                                        pic_database.edit().putString(name, images[n]).apply();
                                        pic_link.edit().putString(name, images[n]).apply();
                                        // Log.i("mohsenjamali", "onResponseID1: " + name);

                                    }

                                    String main_image_name = "main_image_" + person.getString("Id");
                                    pic_database.edit().putString(main_image_name, person.getString("Main_image")).apply();

                                    String id = person.getString("Id");
                                    String name = "pic_" + id + "_0";
                                    pic_link.edit().putString(name, person.getString("Main_image")).apply();

                                }
                                jsonResponseName = "";
                                jsonResponseImage = "";
                                jsonResponseId = "";
                                jsonResponseDescription = "";
                                jsonResponseCategory = "";
                                jsonResponseAvailable = "";


                                if (Mode.equals("activity_product") || Mode.equals("search_product")) {


                                    DiscountItems.add(person.getString("Discount"));
                                    IdItems.add(person.getString("Id"));
                                    NameItems.add(person.getString("Name"));
                                    DescriptionItems.add(person.getString("Desc"));
                                    priceItems.add(person.getString("Price"));
                                    Votes.add(person.getString("Votes"));
                                    MainImageItems.add(person.getString("Main_image"));
                                    CategoryItems.add(person.getString("Category_id"));
                                    AvailableItems.add(person.getString("Available"));
                                    SpecialItems.add(person.getString("Special"));
                                    ShowPrice.add(person.getString("ShowPrice"));
                                    olaviat.add(person.getString("olaviat"));
                                    PercentDiscountItems.add(person.getString("PercentDiscount"));
                                } else {
                                    String image = "";
                                    String description = "";
                                    String category_id = "";
                                    String available = "";
                                    image = person.getString("image");
                                    category_id = person.getString("parent");
                                    String name = person.getString("name");

                                    String id = person.getString("id");
                                    jsonResponseName = name;
                                    jsonResponseImage = image;
                                    jsonResponseId = id;
                                    jsonResponseDescription = description;
                                    jsonResponseCategory = category_id;
                                    jsonResponseAvailable = available;


                                    AvailableItems.add(jsonResponseAvailable);
                                    NameItems.add(jsonResponseName);
                                    ImageItems.add(jsonResponseImage);
                                    IdItems.add(jsonResponseId);


                                    DescriptionItems.add(jsonResponseDescription);
                                    CategoryItems.add(jsonResponseCategory);
                                    ParentItems.add("");
                                    ParentItems.add("");
                                }
                                //  Log.i("mohsenjamali", "ItemAdd: " + jsonResponseName + " " + person.getString("name"));

                            }
                            if (Mode.equals("activity_product") || Mode.equals("search_product")) {
                                ExcelBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        product product = new product();
                                        product.saveExcelFile(context, NameItems, DescriptionItems, priceItems, ShowPrice);
                                    }
                                });
                            }
                            try {
                                recyclerViewlist.setLayoutManager(new LinearLayoutManager(context));
                                if (Mode.equals("activity_product") || Mode.equals("search_product")) {
                                    ad2 = new getProductsAdapter(context, ShowPrice, olaviat, DiscountItems, IdItems, DescriptionItems, NameItems, priceItems, MainImageItems, Image1Items, Image2Items, Votes, Image3Items, Image4Items, Image5Items, Image6Items, OtherImageItems, CategoryItems, AvailableItems, SpecialItems, PercentDiscountItems, recyclerViewlist);
                                    recyclerViewlist.setAdapter(ad2);
                                } else {
                                    ad = new RVAdapter(context, Mode, NameItems, IdItems, ParentItems, ImageItems, DescriptionItems, CategoryItems, AvailableItems, recyclerViewlist);
                                    recyclerViewlist.setAdapter(ad);
                                }

                            } catch (Exception e) {
                                Toast.makeText(context, "خطایی پیش آمده است لطفا دوباره امتحان کنید", Toast.LENGTH_SHORT).show();
                            }
                            recyclerViewlist.setVisibility(View.VISIBLE);
                            progressBarOne.setVisibility(View.GONE);
//                            if (Mode.equals("activity_product")) {
//                                ad.setOnLoadMoreListener(new OnLoadMoreListener() {
//                                    @Override
//                                    public void onLoadMore() {
//                                        NameItems.add(null);
//                                        ad.notifyItemInserted(NameItems.size() - 1);
//
//                                        //-----------------------------------------------------------------------------------------------
//                                        RequestQueue MyRequestQueue = Volley.newRequestQueue(context);
//                                        url = "http://ghomashe.com/api/product?page=" + page;
//
//                                        StringRequest MyStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//                                            @Override
//                                            public void onResponse(String response) {
//
//
//                                                //hazfe perogresbar pas az load
//                                                NameItems.remove(NameItems.size() - 1);
//                                                ad.notifyItemRemoved(NameItems.size());
//                                                try {
//
//                                                    JSONObject jsonRootObject = new JSONObject(response);
//                                                    //   JSONArray array = jsonRootObject.optJSONArray("data");
//                                                    //   JSONObject person = array.getJSONObject(0);
//                                                    //Log.i("mohsenjamali", "mohsenchi: "+jsonRootObject.getString("total"));
//                                                    if (Mode.equals("activity_product")) {
//                                                        jsonRootObject = new JSONObject(response).getJSONObject("data");
//                                                    }
//
//                                                    paging = Integer.valueOf(jsonRootObject.getString("total")) / 10;
//                                                    //   Log.i("mohsenjamali", "mohsenchi: " + paging);
//                                                    page = page + 1;
//                                                    JSONArray array3 = jsonRootObject.optJSONArray("data");
//                                                    JSONObject forecast2[] = new JSONObject[array3.length()];
//
//                                                    for (int i = 0; i < array3.length(); i++) {
//                                                        forecast2[i] = array3.getJSONObject(i);
//                                                        if (Mode.equals("activity_product")) {
//                                                            int image_array_n2 = forecast2[i].getJSONArray("images").length();
//                                                            JSONArray image_array2 = forecast2[i].getJSONArray("images");
//                                                            String images2[] = new String[image_array_n2];
//                                                            SharedPreferences pic_database = context.getSharedPreferences("pic_database", 0);
//
//
//                                                            for (int n = 0; n < image_array_n2; n++) {
//                                                                images2[n] = image_array2.getString(n);
//                                                                String id = forecast2[i].getString("id");
//                                                                String name = "pic_" + id + "_" + (n + 1);
//                                                                pic_database.edit().putString(name, images2[n]).apply();
//
//                                                                // Log.i("mohsenjamali", "picture: " + name + "   " + forecast2[i].getString("main_image"));
//                                                            }
//                                                            String main_image_name = "main_image_" + forecast2[i].getString("id");
//                                                            pic_database.edit().putString(main_image_name, forecast2[i].getString("main_image")).apply();
//                                                        }
//
//
//                                                        jsonResponseName = "";
//                                                        jsonResponseImage = "";
//                                                        jsonResponseId = "";
//                                                        jsonResponseDescription = "";
//                                                        jsonResponseCategory = "";
//                                                        jsonResponseAvailable = "";
//
//                                                        String name2 = forecast2[i].getString("name");
//                                                        String image2 = "";
//                                                        String description2 = "";
//                                                        String category_id2 = "";
//                                                        String available2 = "";
//
//                                                        if (Mode.equals("activity_product")) {
//                                                            image2 = forecast2[i].getString("main_image");
//                                                            description2 = forecast2[i].getString("description");
//                                                            category_id2 = forecast2[i].getString("category_id");
//                                                            available2 = forecast2[i].getString("isAvailable");
//                                                        } else {
//                                                            image2 = forecast2[i].getString("image");
//                                                        }
//
//                                                        String id2 = forecast2[i].getString("id");
//
//                                                        jsonResponseName = name2;
//                                                        jsonResponseImage = image2;
//                                                        jsonResponseId = id2;
//                                                        jsonResponseDescription = description2;
//                                                        jsonResponseCategory = category_id2;
//                                                        jsonResponseAvailable = available2;
//
//                                                        AvailableItems.add(jsonResponseAvailable);
//                                                        NameItems.add(jsonResponseName);
//                                                        ImageItems.add(jsonResponseImage);
//                                                        IdItems.add(jsonResponseId);
//                                                        DescriptionItems.add(jsonResponseDescription);
//                                                        CategoryItems.add(jsonResponseCategory);
//                                                        ParentItems.add("");
//
//                                                    }
//                                                    ad.setLoaded();
//
//
//                                                } catch (JSONException e) {
//
//                                                    tryagain_txt.setVisibility(View.VISIBLE);
//                                                    tryagain_btn.setVisibility(View.VISIBLE);
//                                                    recyclerViewlist.setVisibility(View.GONE);
//                                                    tryagain_txt.setText("خطایی داخلی رخ داده است !");
//
//                                                    Log.i("mohsenjamali", "onErrorResponse:loadmore1 " + e.getMessage());
//                                                }
//
//                                            }
//                                        }, new Response.ErrorListener() {
//                                            @Override
//                                            public void onErrorResponse(VolleyError error) {
//
//                                                tryagain_txt.setVisibility(View.VISIBLE);
//                                                tryagain_btn.setVisibility(View.VISIBLE);
//                                                recyclerViewlist.setVisibility(View.GONE);
//                                                tryagain_txt.setText("خطای داخلی رخ داده است !");
//
//                                                Log.i("mohsenjamali", "onErrorResponse1::loadmore2 " + error.getMessage());
//                                            }
//                                        }) {
//                                            protected Map<String, String> getParams() {
//                                                Map<String, String> MyData = new HashMap<>();
//                                                //    MyData.put("Cate", cate);
//                                                //    MyData.put("Search", search);
//
//                                                return MyData;
//                                            }
//                                        };
//                                        if (page < (paging + 3)) {
//                                            MyRequestQueue.add(MyStringRequest);
//                                            //----------------------------------------------------------------------------------------------
//
//                                            ad.notifyDataSetChanged();
//                                        }
//                                    }
//                                });
//                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBarOne.setVisibility(View.GONE);
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

                progressBarOne.setVisibility(View.GONE);
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

                MyData.put("page", "2");
                MyData.put("parent_id", "0");
                MyData.put("search", search);

                return MyData;
            }
        };

        AppController.getInstance().addToRequestQueue(req);

    }

    public void get_banners_middle(final Context context, final String Mode, final RecyclerView recyclerViewlist, final TextView tryagain_txt, final Button tryagain_btn, final ProgressBar progressBarOne, final String parent2) {
        String urlJsonArray = "";

        urlJsonArray = "http://www.koshangaspasargad.ir/koshangas/admin/get_middle_category.php";
        recyclerViewlist.setVisibility(View.GONE);
        progressBarOne.setVisibility(View.VISIBLE);
        StringRequest req = new StringRequest(Request.Method.POST, urlJsonArray,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            recyclerViewlist.setVisibility(View.VISIBLE);
                            tryagain_txt.setVisibility(View.GONE);
                            tryagain_btn.setVisibility(View.GONE);

                            NameItems.clear();
                            ImageItems.clear();
                            IdItems.clear();
                            CategoryItems.clear();
                            DescriptionItems.clear();
                            AvailableItems.clear();
                            ParentItems.clear();
                            JSONObject jsonRootObject = new JSONObject(response);
                            JSONArray array2 = jsonRootObject.optJSONArray("data");
                            JSONObject forecast[] = new JSONObject[array2.length()];

                            for (int i = 0; i < array2.length(); i++) {

                                forecast[i] = array2.getJSONObject(i);

                                jsonResponseName = "";
                                jsonResponseImage = "";
                                jsonResponseId = "";
                                jsonResponseDescription = "";
                                jsonResponseCategory = "";
                                jsonResponseAvailable = "";
                                jsonResponseParentID = "";

                                String name = forecast[i].getString("name");
                                String image = "";
                                String description = "";
                                String category_id = "";
                                String available = "";
                                String parent_id = forecast[i].getString("parent");
                                String id = forecast[i].getString("id");

                                image = forecast[i].getString("image");

                                jsonResponseName = name;
                                jsonResponseImage = image;
                                jsonResponseId = id;
                                jsonResponseDescription = description;
                                jsonResponseCategory = category_id;
                                jsonResponseParentID = parent_id;
                                jsonResponseAvailable = available;

                                AvailableItems.add(jsonResponseAvailable);
                                NameItems.add(jsonResponseName);
                                ImageItems.add(forecast[i].getString("image"));
                                IdItems.add(jsonResponseId);
                                DescriptionItems.add(jsonResponseDescription);
                                CategoryItems.add(jsonResponseCategory);
                                ParentItems.add(jsonResponseParentID);

                            }
                            try {

                                recyclerViewlist.setLayoutManager(new LinearLayoutManager(context));
                                ad = new RVAdapter(context, Mode, NameItems, IdItems, ParentItems, ImageItems, DescriptionItems, CategoryItems, AvailableItems, recyclerViewlist);
                                recyclerViewlist.setAdapter(ad);
                            } catch (Exception e) {
                                Toast.makeText(context, "خطایی پیش آمده است لطفا دوباره امتحان کنید", Toast.LENGTH_SHORT).show();
                            }
                            recyclerViewlist.setVisibility(View.VISIBLE);
                            progressBarOne.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();

                            progressBarOne.setVisibility(View.GONE);

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

                progressBarOne.setVisibility(View.GONE);
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

                MyData.put("parent_id2", parent2);
                Log.i("mohsenjamali", "getParamsparent: " + parent2);

                return MyData;
            }
        };

        AppController.getInstance().addToRequestQueue(req);
        //end recycle
    }


    public void changePassword(final Context context, final String username, final String password) {

        final MaterialDialog wait = new MaterialDialog.Builder(context)
                .cancelable(false)
                .content("لطفا منتظر بمانید")
                .progress(true, 0)
                .build();
        wait.show();
        String url = "http://www.koshangaspasargad.ir/koshangas/admin/update_user.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        wait.dismiss();
                        if (response.equals("ok")) {
                            Toast.makeText(context, "تغییر با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                            context.startActivity(new Intent(context, EnterPasswordActivity.class));
                        } else {
                            Toast.makeText(context, "خطا در انجام عملیات.لطفا با سرپرست سیستم تماس بگیرید", Toast.LENGTH_SHORT).show();
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
                // params.put("mobile", "0913");
                // params.put("password", "123");
                params.put("user", username);
                params.put("pass", password);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                final SharedPreferences sp = context.getSharedPreferences("Token", 0);
                String token = "Bearer " + sp.getString("token", "nothing");
                params.put("Authorization", token);

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

    public void listName(final Context context, final String link, final String Mode, final String title, final String picture, final String text, final String id, final RecyclerView recyclerViewlist, final TextView tryagain_txt, final Button tryagain_btn, final ProgressBar progressBarOne) {

        wait = new MaterialDialog.Builder(context)
                .cancelable(false)
                .content("لطفا منتظر بمانید")
                .progress(true, 0)
                .build();
        wait.show();
        final SharedPreferences sp = context.getSharedPreferences("Token", 0);
        switch (link) {
            case "full":
                url = "http://www.koshangaspasargad.ir/koshangas/admin/get_customer.php";
                break;
            case "update":
                url = "http://www.koshangaspasargad.ir/koshangas/admin/update_customer.php";
                break;
            case "delete":
                url = "http://www.koshangaspasargad.ir/koshangas/admin/delete_customer.php";
                break;
            case "search":
                url = "http://www.koshangaspasargad.ir/koshangas/admin/search_customer.php";
                break;
        }
        rq = Request.Method.POST;

        StringRequest stringRequest = new StringRequest(rq, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        wait.dismiss();
                        if (!Mode.equals("get") & !Mode.equals("search")) {

                            if (Mode.equals("delete")) {
                                Toast.makeText(context, "حذف با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                            }
                            if (Mode.equals("update")) {
                                Toast.makeText(context, "ویرایش با موفقیت انجام شد", Toast.LENGTH_SHORT).show();
                            }
                            intent_delete = new Intent(context, Customer.class);
                            context.startActivity(intent_delete);
                        } else {
                            try {
                                recyclerViewlist.setVisibility(View.VISIBLE);
                                tryagain_txt.setVisibility(View.GONE);
                                tryagain_btn.setVisibility(View.GONE);

                                NameItems.clear();
                                CityItems.clear();
                                StateItems.clear();
                                TellItems.clear();
                                AddressItems.clear();
                                MobileItems.clear();
                                PasswordItems.clear();
                                IDItem.clear();


                                JSONObject jsonRootObject = new JSONObject(response);
                                JSONArray array2 = jsonRootObject.optJSONArray("data");
                                JSONObject forecast[] = new JSONObject[array2.length()];
                                MiladiToShamsi miladiToShamsi = new MiladiToShamsi();
                                for (int i = 0; i < array2.length(); i++) {

                                    forecast[i] = array2.getJSONObject(i);

                                    NameItems.add(forecast[i].getString("name"));
                                    CityItems.add(forecast[i].getString("city"));
                                    StateItems.add(forecast[i].getString("state"));
                                    TellItems.add(forecast[i].getString("tell"));
                                    AddressItems.add(forecast[i].getString("address"));
                                    MobileItems.add(forecast[i].getString("mobile"));
                                    IDItem.add(forecast[i].getString("id"));


                                    Log.i("mohsenjamali", "onResponseID: " + IDItem);

                                }
                                try {
                                    recyclerViewlist.setLayoutManager(new LinearLayoutManager(context));
                                    adListname = new RVAdapterListName(context, NameItems, MobileItems, AddressItems, IDItem, CityItems, StateItems, TellItems, recyclerViewlist);
                                    recyclerViewlist.setAdapter(adListname);
                                } catch (Exception e) {
                                    Toast.makeText(context, "خطایی پیش آمده است لطفا دوباره امتحان کنید", Toast.LENGTH_SHORT).show();
                                }
                                recyclerViewlist.setVisibility(View.VISIBLE);
                                progressBarOne.setVisibility(View.GONE);

//                                adListname.setOnLoadMoreListener(new OnLoadMoreListener() {
//                                    @Override
//                                    public void onLoadMore() {
//
//                                        NameItems.add(null);
//                                        adListname.notifyItemInserted(NameItems.size() - 1);
//
//                                        //-----------------------------------------------------------------------------------------------
//                                        RequestQueue MyRequestQueue = Volley.newRequestQueue(context);
//                                        url = "http://ghomashe.com/api/members?page=" + page;
//
//                                        // Log.i("mohsenjamali", "onLoadMore: " + url);
//                                        StringRequest MyStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//                                            @Override
//                                            public void onResponse(String response) {
//
//                                                new Handler().postDelayed(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                    }
//                                                }, 80000);
//
//                                                //hazfe perogresbar pas az load
//
//                                                NameItems.remove(NameItems.size() - 1);
//                                                adListname.notifyItemRemoved(NameItems.size());
//
//                                                try {
//
//                                                    JSONObject jsonRootObject = new JSONObject(response);
//                                                    paging = Integer.valueOf(jsonRootObject.getString("total")) / 10;
//                                                    page = page + 1;
//                                                    JSONArray array3 = jsonRootObject.optJSONArray("data");
//                                                    JSONObject forecast2[] = new JSONObject[array3.length()];
//
//                                                    for (int i = 0; i < array3.length(); i++) {
//
//                                                        forecast2[i] = array3.getJSONObject(i);
//
//                                                        jsonResponseName = "";
//                                                        //   jsonResponseImage = "";
//                                                        jsonResponseAddress = "";
//                                                        jsonResponseMobile = "";
//                                                        // jsonResponsePassword = "";
//                                                        jsonResponseIDID = "";
//
//                                                        String name2 = forecast2[i].getString("full_name");
//                                                        String address2 = forecast2[i].getString("address");
//                                                        String password2 = forecast2[i].getString("password");
//                                                        String mobile2 = forecast2[i].getString("mobile");
//                                                        String ID2 = forecast2[i].getString("id");
//
//                                                        jsonResponseName = name2;
//                                                        // jsonResponseImage = image;
//                                                        jsonResponseAddress = address2;
//                                                        jsonResponseMobile = mobile2;
//                                                        //jsonResponsePassword = password2;
//                                                        jsonResponseIDID = ID2;
//
//
//                                                        NameItems.add(jsonResponseName);
//                                                        //ImageItems.add(jsonResponseImage);
//                                                        IDItem.add(jsonResponseIDID);
//                                                        AddressItems.add(jsonResponseAddress);
//                                                        MobileItems.add(jsonResponseMobile);
//                                                        // PasswordItems.add(jsonResponsePassword);
//                                                    }
//                                                    adListname.setLoaded();
//
//
//                                                } catch (JSONException e) {
//
//                                                    tryagain_txt.setVisibility(View.VISIBLE);
//                                                    tryagain_btn.setVisibility(View.VISIBLE);
//                                                    recyclerViewlist.setVisibility(View.GONE);
//                                                    tryagain_txt.setText("خطایی داخلی رخ داده است !");
//
//                                                    Log.i("mohsenjamali", "onErrorResponse:loadmore1 " + e.getMessage());
//                                                }
//
//                                            }
//                                        }, new Response.ErrorListener() {
//                                            @Override
//                                            public void onErrorResponse(VolleyError error) {
//
//                                                tryagain_txt.setVisibility(View.VISIBLE);
//                                                tryagain_btn.setVisibility(View.VISIBLE);
//                                                recyclerViewlist.setVisibility(View.GONE);
//                                                tryagain_txt.setText("خطای داخلی رخ داده است !");
//
//                                                Log.i("mohsenjamali", "onErrorResponse1::loadmore2 " + error.getMessage());
//                                                onErrorResponse2(error, context);
//                                            }
//                                        }) {
//                                            protected Map<String, String> getParams() {
//                                                Map<String, String> MyData = new HashMap<>();
//                                                //    MyData.put("Cate", cate);
//                                                //    MyData.put("Search", search);
//                                                return MyData;
//                                            }
//
//                                            @Override
//                                            public Map<String, String> getHeaders() throws AuthFailureError {
//                                                Map<String, String> params = new HashMap<String, String>();
//                                                String token = "Bearer " + sp.getString("token", "nothing");
//                                                params.put("Authorization", token);
//                                                return params;
//                                            }
//                                        };
//                                        if (page < (paging + 3)) {
//                                            MyRequestQueue.add(MyStringRequest);
//                                            //----------------------------------------------------------------------------------------------
//
//                                            adListname.notifyDataSetChanged();
//                                        }
//                                    }
//                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressBarOne.setVisibility(View.GONE);
                                tryagain_txt.setVisibility(View.VISIBLE);
                                tryagain_btn.setVisibility(View.VISIBLE);
                                recyclerViewlist.setVisibility(View.GONE);
                                tryagain_txt.setText("ارتباط با سرور برقرار نشد !");
                                Log.i("mohsenjamali", "onErrorResponse: " + e.getMessage());
                            }
                        }
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


                switch (Mode) {
                    case "delete":
                        params.put("id", id);
                        break;
                    case "send":
                        params.put("title", title);
                        params.put("text", text);
                        break;
                    case "update":

                        params.put("cat", text);
                        params.put("id", id);
                        break;
                    case "search":
                        params.put("text", text);
                        break;
                }
                //   Log.i("mohsenjamali", "listname: " + id + " " + Mode);
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public void search(final Context context, final String Mode, final String search, final RecyclerView recyclerViewlist, final TextView tryagain_txt, final Button tryagain_btn, final ProgressBar progressBarOne) {


        if (Mode.equals("activity_category")) {
            urlJsonArray = "http://ghomashe.com/api/activity_category?name=" + Uri.encode(search, "UTF-8");
        } else if (Mode.equals("activity_product")) {
            urlJsonArray = "http://ghomashe.com/api/activity_product?name=" + Uri.encode(search, "UTF-8");

        }
        if (Mode.equals("middle_category")) {
            urlJsonArray = "http://ghomashe.com/api/activity_category/middle?name=" + Uri.encode(search, "UTF-8");
        }

        recyclerViewlist.setVisibility(View.GONE);
        progressBarOne.setVisibility(View.VISIBLE);
        StringRequest req = new StringRequest(Request.Method.GET, urlJsonArray,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            recyclerViewlist.setVisibility(View.VISIBLE);
                            tryagain_txt.setVisibility(View.GONE);
                            tryagain_btn.setVisibility(View.GONE);

                            NameItems.clear();
                            ImageItems.clear();
                            IdItems.clear();
                            CategoryItems.clear();
                            DescriptionItems.clear();
                            AvailableItems.clear();
                            JSONObject jsonRootObject = new JSONObject(response);
                            JSONObject jsonRootObject_images = new JSONObject(response);
                            if (Mode.equals("activity_product")) {
                                jsonRootObject = new JSONObject(response).getJSONObject("data");
                            }

                            JSONArray array2 = jsonRootObject.optJSONArray("data");
                            JSONObject forecast[] = new JSONObject[array2.length()];

                            for (int i = 0; i < array2.length(); i++) {

                                forecast[i] = array2.getJSONObject(i);
                                if (Mode.equals("activity_product")) {
                                    int image_array_n = forecast[i].getJSONArray("images").length();
                                    JSONArray image_array = forecast[i].getJSONArray("images");
                                    String images[] = new String[image_array_n];
                                    SharedPreferences pic_database = context.getSharedPreferences("pic_database", 0);

                                    for (int n = 0; n < image_array_n; n++) {
                                        images[n] = image_array.getString(n);
                                        String id = forecast[i].getString("id");
                                        String name = "pic_" + id + "_" + (n + 1);

                                        pic_database.edit().putString(name, images[n]).apply();

                                    }
                                    String main_image_name = "main_image_" + forecast[i].getString("id");
                                    pic_database.edit().putString(main_image_name, forecast[i].getString("main_image")).apply();
                                }
                                jsonResponseName = "";
                                jsonResponseImage = "";
                                jsonResponseId = "";
                                jsonResponseDescription = "";
                                jsonResponseCategory = "";
                                jsonResponseAvailable = "";

                                String name = forecast[i].getString("name");
                                String image = "";
                                String description = "";
                                String category_id = "";
                                String available = "";
                                if (Mode.equals("activity_product")) {
                                    image = forecast[i].getString("main_image");
                                    description = forecast[i].getString("description");
                                    category_id = forecast[i].getString("category_id");
                                    available = forecast[i].getString("isAvailable");

                                } else {
                                    image = forecast[i].getString("image");
                                }

                                String id = forecast[i].getString("id");
                                jsonResponseName = name;
                                jsonResponseImage = image;
                                jsonResponseId = id;
                                jsonResponseDescription = description;
                                jsonResponseCategory = category_id;
                                jsonResponseAvailable = available;

                                AvailableItems.add(jsonResponseAvailable);
                                NameItems.add(jsonResponseName);
                                ImageItems.add(jsonResponseImage);
                                IdItems.add(jsonResponseId);
                                DescriptionItems.add(jsonResponseDescription);
                                CategoryItems.add(jsonResponseCategory);
                                ParentItems.add("");

                            }
                            try {
                                recyclerViewlist.setLayoutManager(new LinearLayoutManager(context));
                                ad = new RVAdapter(context, Mode, NameItems, IdItems, ParentItems, ImageItems, DescriptionItems, CategoryItems, AvailableItems, recyclerViewlist);
                                recyclerViewlist.setAdapter(ad);
                            } catch (Exception e) {
                                Toast.makeText(context, "خطایی پیش آمده است لطفا دوباره امتحان کنید", Toast.LENGTH_SHORT).show();
                            }
                            recyclerViewlist.setVisibility(View.VISIBLE);
                            progressBarOne.setVisibility(View.GONE);
                            if (Mode.equals("activity_product")) {
                                ad.setOnLoadMoreListener(new OnLoadMoreListener() {
                                    @Override
                                    public void onLoadMore() {
                                        IdItems.add(null);
                                        ad.notifyItemInserted(IdItems.size() - 1);

                                        //-----------------------------------------------------------------------------------------------
                                        RequestQueue MyRequestQueue = Volley.newRequestQueue(context);
                                        // url = "http://ghomashe.com/api/product?page=" + page;
                                        url = urlJsonArray + "&page=" + page;
                                        StringRequest MyStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                    }
                                                }, 80000);

                                                //hazfe perogresbar pas az load
                                                IdItems.remove(IdItems.size() - 1);
                                                ad.notifyItemRemoved(IdItems.size());
                                                try {

                                                    JSONObject jsonRootObject = new JSONObject(response);
                                                    //   JSONArray array = jsonRootObject.optJSONArray("data");
                                                    //   JSONObject person = array.getJSONObject(0);
                                                    //Log.i("mohsenjamali", "mohsenchi: "+jsonRootObject.getString("total"));
                                                    if (Mode.equals("activity_product")) {
                                                        jsonRootObject = new JSONObject(response).getJSONObject("data");
                                                    }

                                                    paging = Integer.valueOf(jsonRootObject.getString("total")) / 10;
                                                    //   Log.i("mohsenjamali", "mohsenchi: " + paging);
                                                    page = page + 1;
                                                    JSONArray array3 = jsonRootObject.optJSONArray("data");
                                                    JSONObject forecast2[] = new JSONObject[array3.length()];

                                                    for (int i = 0; i < array3.length(); i++) {
                                                        forecast2[i] = array3.getJSONObject(i);
                                                        if (Mode.equals("activity_product")) {
                                                            int image_array_n2 = forecast2[i].getJSONArray("images").length();
                                                            JSONArray image_array2 = forecast2[i].getJSONArray("images");
                                                            String images2[] = new String[image_array_n2];
                                                            SharedPreferences pic_database = context.getSharedPreferences("pic_database", 0);


                                                            for (int n = 0; n < image_array_n2; n++) {
                                                                images2[n] = image_array2.getString(n);
                                                                String id = forecast2[i].getString("id");
                                                                String name = "pic_" + id + "_" + (n + 1);
                                                                pic_database.edit().putString(name, images2[n]).apply();

                                                                // Log.i("mohsenjamali", "picture: " + name + "   " + forecast2[i].getString("main_image"));
                                                            }
                                                            String main_image_name = "main_image_" + forecast2[i].getString("id");
                                                            pic_database.edit().putString(main_image_name, forecast2[i].getString("main_image")).apply();
                                                        }


                                                        jsonResponseName = "";
                                                        jsonResponseImage = "";
                                                        jsonResponseId = "";
                                                        jsonResponseDescription = "";
                                                        jsonResponseCategory = "";
                                                        jsonResponseAvailable = "";

                                                        String name2 = forecast2[i].getString("name");
                                                        String image2 = "";
                                                        String description2 = "";
                                                        String category_id2 = "";
                                                        String available2 = "";

                                                        if (Mode.equals("activity_product")) {
                                                            image2 = forecast2[i].getString("main_image");
                                                            description2 = forecast2[i].getString("description");
                                                            category_id2 = forecast2[i].getString("category_id");
                                                            available2 = forecast2[i].getString("isAvailable");
                                                        } else {
                                                            image2 = forecast2[i].getString("image");
                                                        }

                                                        String id2 = forecast2[i].getString("id");

                                                        jsonResponseName = name2;
                                                        jsonResponseImage = image2;
                                                        jsonResponseId = id2;
                                                        jsonResponseDescription = description2;
                                                        jsonResponseCategory = category_id2;
                                                        jsonResponseAvailable = available2;

                                                        AvailableItems.add(jsonResponseAvailable);
                                                        NameItems.add(jsonResponseName);
                                                        ImageItems.add(jsonResponseImage);
                                                        IdItems.add(jsonResponseId);
                                                        DescriptionItems.add(jsonResponseDescription);
                                                        CategoryItems.add(jsonResponseCategory);
                                                        ParentItems.add("");

                                                    }
                                                    ad.setLoaded();


                                                } catch (JSONException e) {

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

                                                tryagain_txt.setVisibility(View.VISIBLE);
                                                tryagain_btn.setVisibility(View.VISIBLE);
                                                recyclerViewlist.setVisibility(View.GONE);
                                                tryagain_txt.setText("خطای داخلی رخ داده است !");

                                                Log.i("mohsenjamali", "onErrorResponse1::loadmore2 " + error.getMessage());
                                            }
                                        }) {
                                            protected Map<String, String> getParams() {
                                                Map<String, String> MyData = new HashMap<>();
                                                //    MyData.put("Cate", cate);
                                                //    MyData.put("Search", search);

                                                return MyData;
                                            }
                                        };
                                        if (page < (paging + 3)) {
                                            MyRequestQueue.add(MyStringRequest);
                                            //----------------------------------------------------------------------------------------------

                                            ad.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBarOne.setVisibility(View.GONE);
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

                progressBarOne.setVisibility(View.GONE);
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
                MyData.put("page", "2");
                return MyData;
            }
        };

        AppController.getInstance().addToRequestQueue(req);

    }

}
