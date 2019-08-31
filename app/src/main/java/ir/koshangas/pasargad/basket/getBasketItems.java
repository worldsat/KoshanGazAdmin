package ir.koshangas.pasargad.basket;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.koshangas.pasargad.MiladiToShamsi;
import ir.koshangas.pasargad.basket.domain.basketDomain;


public class getBasketItems {

    private getBasketItemAdapter ad;


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

    private DecimalFormat formatter = new DecimalFormat("###,###,###,###");

    public void get_Items(final Context context, final Button excleBtn, final basketDomain basketDomain, final ProgressBar ProgressBar, final RecyclerView recyclerViewlist, final TextView emptyText, final ConstraintLayout BasketLayout, final String factor, final String mobile) {

        final SharedPreferences sp = context.getSharedPreferences("Token", 0);
        String urlJsonArray = "http://www.koshangaspasargad.ir/koshangas/admin/" + "get_basket_items.php";
        recyclerViewlist.setVisibility(View.INVISIBLE);
        ProgressBar.setVisibility(View.VISIBLE);
        BasketLayout.setVisibility(View.GONE);

        RunPermissionExcel(context);

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

                                Log.i("mohsenjamali", "onResponseId: " + person.getString("Id"));
                            }


                            //set empty message in basket
//                            HistoryActivity Basket = new HistoryActivity();
//                            if (array.length() == 0) {
//                                emptyText.setVisibility(View.VISIBLE);
//                                Basket.setHiddenLayout(context);
//                            } else {
//                                emptyText.setVisibility(View.GONE);
//                                Basket.setShowLayout(context);
//                            }


                            try {
                                excleBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        saveExcelFile(context, basketDomain,basketDomain.getNameShow()+".xls", NameItems,PriceItems,QtyItems);
                                    }
                                });

                                recyclerViewlist.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

                                ad = new getBasketItemAdapter(context, IdItems, DateItems, PriceItems, UserItems, NameItems, ProductIdItems, QtyItems, FactoreItems, TotalPriceIdItems, PaymentMethodItems, SendMethodItems, StatusItems, emptyText, recyclerViewlist);
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
                //  MyData.put("Api_token", sp.getString("token", "nothing"));
                MyData.put("factor", factor);
                MyData.put("mobile", mobile);

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

    public boolean saveExcelFile(Context context, basketDomain basketDomain, String fileName, List<String> NameItems, List<String> PriceItems, List<String> TotalItems) {

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return false;
        }

        boolean success = false;
        Workbook wb = new HSSFWorkbook();


        Cell c ;
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cs.setAlignment(CellStyle.ALIGN_CENTER);

        Sheet sheet1 ;

        sheet1 = wb.createSheet("\u200F" + basketDomain.getDateShow());

        Row row = sheet1.createRow(0);
        c = row.createCell(0);
        c.setCellValue("تاریخ");
        c.setCellStyle(cs);
        c = row.createCell(1);
        c.setCellValue("نام ");
        c.setCellStyle(cs);
        c = row.createCell(2);
        c.setCellValue("شماره همراه");
        c.setCellStyle(cs);
        c = row.createCell(3);
        c.setCellValue("شماره فاکتور");
        c.setCellStyle(cs);
        c = row.createCell(4);
        c.setCellValue("جمع فاکتور");
        c.setCellStyle(cs);
        c = row.createCell(5);
        c.setCellValue("نوع پرداخت");
        c.setCellStyle(cs);


        CellStyle cs3 = wb.createCellStyle();
        cs3.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
        cs3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cs3.setAlignment(CellStyle.ALIGN_CENTER);

        row = sheet1.createRow(1);
        c = row.createCell(0);
        c.setCellValue(basketDomain.getDateShow());
        c.setCellStyle(cs3);
        c = row.createCell(1);
        c.setCellValue(basketDomain.getNameShow());
        c.setCellStyle(cs3);
        c = row.createCell(2);
        c.setCellValue(basketDomain.getMobileShow());
        c.setCellStyle(cs3);
        c = row.createCell(3);
        c.setCellValue(basketDomain.getFactorShow());
        c.setCellStyle(cs3);
        c = row.createCell(4);
        c.setCellValue(basketDomain.getTotalShow());
        c.setCellStyle(cs3);
        c = row.createCell(5);
        c.setCellValue(basketDomain.getPaymentShow());
        c.setCellStyle(cs3);

        row = sheet1.createRow(2);

        cs.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        row = sheet1.createRow(3);
        c = row.createCell(0);
        c.setCellValue("نام کالا");
        c.setCellStyle(cs);
        c = row.createCell(1);
        c.setCellValue("تعداد");
        c.setCellStyle(cs);
        c = row.createCell(2);
        c.setCellValue("قیمت واحد");
        c.setCellStyle(cs);



        Collections.reverse(NameItems);
        Collections.reverse(PriceItems);
        Collections.reverse(TotalItems);

        CellStyle cs2 = wb.createCellStyle();
        cs2.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
        cs2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cs2.setAlignment(CellStyle.ALIGN_CENTER);


        for (int n = 4; n < NameItems.size()+4; n++) {
            Row row2 = sheet1.createRow(n);

                c = row2.createCell(0);
                c.setCellValue("\u200F" + NameItems.get(n-4));
                c.setCellStyle(cs2);
                c = row2.createCell(1);
                c.setCellValue("\u200F" + TotalItems.get(n-4));
                c.setCellStyle(cs2);
                c = row2.createCell(2);
                c.setCellValue("\u200F" + PriceItems.get(n-4));
                c.setCellStyle(cs2);




        }
        sheet1.setColumnWidth(0, (20 * 500));
        sheet1.setColumnWidth(1, (10 * 500));
        sheet1.setColumnWidth(2, (10 * 500));
        sheet1.setColumnWidth(3, (10 * 500));
        sheet1.setColumnWidth(4, (10 * 500));
        sheet1.setColumnWidth(5, (10 * 500));



        File root = new File(Environment.getExternalStorageDirectory() + "/CNG Market", "");
        if (!root.exists()) {
            root.setReadable(true);
            root.setWritable(true);
            root.mkdirs();
        }

        File file = new File(root, fileName);
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            success = true;
        } catch (IOException e) {
        } catch (Exception e) {
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }
        Toast.makeText(context, "خروجی اکسل با موفقیت ساخته شد", Toast.LENGTH_SHORT).show();
        return success;
    }

    private void checkRunTimePermission(Context context) {
        String[] permissionArrays = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ((Activity) context).requestPermissions(permissionArrays, 1);
        } else {

        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
//                    checkRunTimePermission();
                }
                return;
            }
        }
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private void RunPermissionExcel(final Context context) {

        Dexter.withActivity(((Activity) context))
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Toast.makeText(context, "Excel OK", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(context, "اجازه دسترسی برای خروجی اکسل داده نشد", Toast.LENGTH_SHORT).show();
                        if (response.isPermanentlyDenied()) {
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }
}
