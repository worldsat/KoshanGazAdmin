package ir.koshangas.pasargad.history;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import ir.koshangas.pasargad.MainActivity;
import ir.koshangas.pasargad.R;


public class HistoryActivity extends AppCompatActivity {
    private DecimalFormat formatter = new DecimalFormat("###,###,###,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        ConstraintLayout BasketLayout = findViewById(R.id.Basket_layout);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        RecyclerView recyclerViewlist = findViewById(R.id.RecyclerView);
        TextView emptyText = findViewById(R.id.emptyText);
        Button excelBtn = findViewById(R.id.excelBtn);
        emptyText.setVisibility(View.GONE);


//        //end Check Login
//        getToken token = new getToken();
//
//        if (token.Ok(getActivity())) {
        getHistoryBasket historyBasket = new getHistoryBasket();
        historyBasket.get_Items(HistoryActivity.this, progressBar, recyclerViewlist, emptyText, BasketLayout);

        getHistoryExcelBasket historyExcelBasket = new getHistoryExcelBasket();
        historyExcelBasket.get_Items(HistoryActivity.this, progressBar, recyclerViewlist, emptyText, BasketLayout, excelBtn);
//        } else {
//            emptyText.setVisibility(View.VISIBLE);
//            setHiddenLayout(getActivity());
//        }

        RunPermissionExcel();
    }

    public void setHiddenLayout(Context context) {
        ConstraintLayout BasketLayout = ((Activity) context).findViewById(R.id.Basket_layout);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        BasketLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

    }

    public void setShowLayout(Context context) {
        ConstraintLayout BasketLayout = ((Activity) context).findViewById(R.id.Basket_layout);

        BasketLayout.setVisibility(View.VISIBLE);

    }

    private void RunPermissionExcel() {

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Toast.makeText(HistoryActivity.this, "Excel OK", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(HistoryActivity.this, "اجازه دسترسی برای خروجی اکسل داده نشد", Toast.LENGTH_SHORT).show();
                        if (response.isPermanentlyDenied()) {
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }

    /**
     * From Here For Build Excel File
     */
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

    public boolean saveExcelFile(Context context, String fileName, int from, int to, List<String> DateItems, List<String> PriceItems, List<String> UserItems, List<String> NameItems, List<String> QtyItems, List<String> FactorItems, List<String> TotalPriceIdItems) {

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
        c.setCellValue("تاریخ");
        c.setCellStyle(cs);
        c = row.createCell(1);
        c.setCellValue("شماره همراه");
        c.setCellStyle(cs);
        c = row.createCell(2);
        c.setCellValue("شماره فاکتور");
        c.setCellStyle(cs);
//        c = row.createCell(3);
//        c.setCellValue("نام کالا");
//        c.setCellStyle(cs);
        c = row.createCell(3);
        c.setCellValue("قیمت واحد");
        c.setCellStyle(cs);
        c = row.createCell(4);
        c.setCellValue("تعداد خرید");
        c.setCellStyle(cs);
        c = row.createCell(5);
        c.setCellValue("جمع مبلغ خرید");
        c.setCellStyle(cs);


        CellStyle cs2 = wb.createCellStyle();
        cs2.setFillForegroundColor(HSSFColor.LIGHT_ORANGE.index);
        cs2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cs2.setAlignment(CellStyle.ALIGN_CENTER);

        int size = (to - from) + 1;
        for (int n = 1; n <= size; n++) {
            Row row2 = sheet1.createRow(n);
            for (int i = (from - 1); i < (from); i++) {
                c = row2.createCell(0);
                c.setCellValue("\u200F" + DateItems.get(i));
                c.setCellStyle(cs2);
                c = row2.createCell(1);
                c.setCellValue("\u200F" + UserItems.get(i));
                c.setCellStyle(cs2);
                c = row2.createCell(2);
                c.setCellValue("\u200F" + FactorItems.get(i));
                c.setCellStyle(cs2);
//                c = row2.createCell(3);
//                c.setCellValue("\u200F" + NameItems.get(i));
//                c.setCellStyle(cs2);
                c = row2.createCell(3);
                c.setCellValue("\u200F" + formatter.format(Long.valueOf(PriceItems.get(i))) + " تومان");
                c.setCellStyle(cs2);
                c = row2.createCell(4);
                c.setCellValue("\u200F" + QtyItems.get(i));
                c.setCellStyle(cs2);
                c = row2.createCell(5);
                c.setCellValue("\u200F" + formatter.format(Long.valueOf(TotalPriceIdItems.get(i))) + " تومان");
                c.setCellStyle(cs2);
                Log.i("mohsenjamali", "saveExcelFile: i=" + i + " from=" + from);
            }
            from++;

            if (from > to) {
                break;
            }

        }
        sheet1.setColumnWidth(0, (10 * 500));
        sheet1.setColumnWidth(1, (10 * 500));
        sheet1.setColumnWidth(2, (5 * 500));
        sheet1.setColumnWidth(3, (10 * 500));
        sheet1.setColumnWidth(4, (10 * 500));
        sheet1.setColumnWidth(5, (5 * 500));
        sheet1.setColumnWidth(6, (10 * 500));

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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
