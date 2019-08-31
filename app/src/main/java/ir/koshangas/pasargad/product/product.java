package ir.koshangas.pasargad.product;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import ir.koshangas.pasargad.DateVolley;
import ir.koshangas.pasargad.MainActivity;
import ir.koshangas.pasargad.R;
import ir.koshangas.pasargad.history.HistoryActivity;

public class product extends AppCompatActivity {
    private TextView tryagain_txt;
    private Button tryagain_btn;
    private TextView excelBTn;
    private ProgressBar progressBarOne;
    private RecyclerView recyclerView;
    private DateVolley datavolly;
    private SwipeRefreshLayout refreshLayout;
    private EditText search_editText;
    private DecimalFormat formatter = new DecimalFormat("###,###,###,###");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        //toolbar
        Toolbar my_toolbar = findViewById(R.id.app_bar_show_ads);
        setSupportActionBar(my_toolbar);
        excelBTn=findViewById(R.id.excel);
        Button back_icon = findViewById(R.id.back_icon_rtl);
        back_icon.setVisibility(View.VISIBLE);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(product.this, MainActivity.class);
                product.this.startActivity(intent);
            }
        });
        TextView title_toolbar = findViewById(R.id.title_toolbar_rtl);
        title_toolbar.setText("زیر شاخه ها");
        //toolbar end

        recyclerView = findViewById(R.id.View);
        tryagain_txt = findViewById(R.id.try_again_txt);
        tryagain_btn = findViewById(R.id.try_again_btn);
        progressBarOne = findViewById(R.id.progressBarOne);
        tryagain_txt.setVisibility(View.GONE);
        tryagain_btn.setVisibility(View.GONE);

        RunPermissionExcel();

        tryagain_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBarOne.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                tryagain_txt.setVisibility(View.GONE);
                tryagain_btn.setVisibility(View.GONE);

                CheckNet();
            }
        });

        refreshLayout = findViewById(R.id.swipe_layout);
        refreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        CheckNet();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                datavolly.get_banners(product.this, "activity_product","", recyclerView, tryagain_txt, tryagain_btn, progressBarOne,excelBTn);

                //Download data from net and update list
                refreshLayout.setRefreshing(false);
            }
        });

        //end refresh
        //khali kardan aks activity_category dar SharedPreferences
        SharedPreferences pic_writer = getApplicationContext().getSharedPreferences("picture", 0);
        pic_writer.edit().clear().apply();
        //end


        datavolly = new DateVolley();

        datavolly.get_banners(product.this, "activity_product","", recyclerView, tryagain_txt, tryagain_btn, progressBarOne,excelBTn);
        TextView new_cat = findViewById(R.id.new_cat);
        new_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(product.this, New_Product.class);
                intent.putExtra("is_edit", "nothing");
                product.this.startActivity(intent);
            }
        });


        search_editText = (EditText) findViewById(R.id.editText_search);
        search_editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (search_editText.getText().toString().isEmpty()) {
                        Toast.makeText(product.this, "لطفا متن مورد نظر خود را وارد کنید", Toast.LENGTH_SHORT).show();

                    } else if (search_editText.getText().toString().length() < 3) {
                        Toast.makeText(product.this, "لطفا طول کلمه مورد نظر  3 حرف یا بیشتر باشد", Toast.LENGTH_SHORT).show();
                    } else {

                        datavolly.get_banners(product.this, "search_product", search_editText.getText().toString(),recyclerView, tryagain_txt, tryagain_btn, progressBarOne,excelBTn);
                        // CheckNet();
                    }
                }
                return false;
            }
        });

        // baraye click roye icon search edittext
        search_editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() <= (search_editText.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                        // your action here

                        if (search_editText.getText().toString().isEmpty()) {
                            Toast.makeText(product.this, "لطفا متن مورد نظر خود را وارد کنید", Toast.LENGTH_SHORT).show();

                        } else if (search_editText.getText().toString().length() < 3) {
                            Toast.makeText(product.this, "لطفا طول کلمه مورد نظر  3 حرف یا بیشتر باشد", Toast.LENGTH_SHORT).show();
                        } else {
//                            CheckNet();
                            // get_banners("search", search_editText.getText().toString(), "", "", "", "", "", "", "", "", "", "", "", "", "", "");
//                            datavolly.search(product.this, "activity_product", search_editText.getText().toString(), recyclerView, tryagain_txt, tryagain_btn, progressBarOne);
                            datavolly.get_banners(product.this, "search_product", search_editText.getText().toString(),recyclerView, tryagain_txt, tryagain_btn, progressBarOne,excelBTn);

                        }

                        return false;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(product.this, MainActivity.class);
        product.this.startActivity(intent);
    }

    private void CheckNet() {


        boolean connect;

        ConnectivityManager connectivityManager = (ConnectivityManager) product.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connect = true;
        } else {
            connect = false;
        }

        if (connect) {
            try {

                recyclerView.setVisibility(View.VISIBLE);
                tryagain_txt.setVisibility(View.GONE);
                tryagain_btn.setVisibility(View.GONE);

                datavolly.get_banners(product.this, "activity_product","", recyclerView, tryagain_txt, tryagain_btn, progressBarOne,excelBTn);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tryagain_txt.setVisibility(View.VISIBLE);
                    tryagain_btn.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    tryagain_txt.setText("عدم دسترسی به اینترنت !");
                    progressBarOne.setVisibility(View.GONE);
                }
            }, 1000);


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
    private void checkRunTimePermission() {
        String[] permissionArrays = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 1);
        } else {

        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    checkRunTimePermission();
                }
                return;
            }
        }
    }
    public boolean saveExcelFile(Context context, List<String>TitleItems, List<String> ExplainItems, List<String> FeeItems, List<String> Fee2Items) {

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return false;
        }

        boolean success = false;
        Workbook wb = new HSSFWorkbook();


        Cell c = null;
        CellStyle cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cs.setAlignment(CellStyle.ALIGN_CENTER);

        Sheet sheet1 = null;
        sheet1 = wb.createSheet("\u200F" + "Export");

        Row row = sheet1.createRow(0);
        c = row.createCell(0);
        c.setCellValue("عنوان");
        c.setCellStyle(cs);
        c = row.createCell(1);
        c.setCellValue("توضیحات محصول");
        c.setCellStyle(cs);
        c = row.createCell(2);
        c.setCellValue("قیمت بدون تخفیف");
        c.setCellStyle(cs);
        c = row.createCell(3);
        c.setCellValue("قیمت نهایی");
        c.setCellStyle(cs);



        CellStyle cs2 = wb.createCellStyle();
        cs2.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
        cs2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cs2.setAlignment(CellStyle.ALIGN_CENTER);

        for (int n = 0; n < TitleItems.size(); n++) {
            Row row2 = sheet1.createRow(n+1);
//            for (int i = (from - 1); i < (from); i++) {
                c = row2.createCell(0);
                c.setCellValue("\u200F" + TitleItems.get(n));
                c.setCellStyle(cs2);
                c = row2.createCell(1);
                c.setCellValue("\u200F" + ExplainItems.get(n));
                c.setCellStyle(cs2);
                c = row2.createCell(2);
                c.setCellValue("\u200F" + FeeItems.get(n));
                c.setCellStyle(cs2);
                c = row2.createCell(3);
                c.setCellValue("\u200F" +Fee2Items.get(n));
                c.setCellStyle(cs2);

//                Log.i("mohsenjamali", "saveExcelFile: i=" + i + " from=" + from);
//            }
//            from++;
//
//            if (from > to) {
//                break;
//            }

        }
        sheet1.setColumnWidth(0, (15 * 500));
        sheet1.setColumnWidth(1, (10 * 500));
        sheet1.setColumnWidth(2, (10 * 500));
        sheet1.setColumnWidth(3, (10 * 500));
//        sheet1.setColumnWidth(4, (10 * 500));
//        sheet1.setColumnWidth(5, (5 * 500));
//        sheet1.setColumnWidth(6, (10 * 500));

        File root = new File(Environment.getExternalStorageDirectory() + "/CNG Market", "");
        if (!root.exists()) {
            root.setReadable(true);
            root.setWritable(true);
            root.mkdirs();
        }

        File file = new File(root, "mahsoolat.xls");
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            wb.write(os);
            success = true;
        } catch (IOException e) {
            e.toString();
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
    private void RunPermissionExcel() {

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Toast.makeText(product.this, "Excel OK", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(product.this, "اجازه دسترسی برای خروجی اکسل داده نشد", Toast.LENGTH_SHORT).show();
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
