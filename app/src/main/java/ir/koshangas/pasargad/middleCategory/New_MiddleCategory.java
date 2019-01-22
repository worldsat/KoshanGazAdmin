package ir.koshangas.pasargad.middleCategory;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.system.ErrnoException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ir.koshangas.pasargad.DateVolley;
import ir.koshangas.pasargad.R;
import ir.koshangas.pasargad.croping_image;

public class New_MiddleCategory extends AppCompatActivity {
    private Uri mCropImageUri;
    private EditText title_edt, cat_id_edt;
    private ImageView image_category, del_cat;
    private Bundle address;
    String id = "";
    private SharedPreferences pic_reader;
    DateVolley volley;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_middle_category);

        //toolbar
        Toolbar my_toolbar = findViewById(R.id.app_bar_show_ads);
        setSupportActionBar(my_toolbar);
        Button back_icon = findViewById(R.id.back_icon_rtl);
        back_icon.setVisibility(View.VISIBLE);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(New_MiddleCategory.this, MiddleCategory.class);
                New_MiddleCategory.this.startActivity(intent);
            }
        });
        TextView title_toolbar = findViewById(R.id.title_toolbar_rtl);
        title_toolbar.setText("ایجاد دسته بندی میانی جدید");
        //toolbar end
        address = getIntent().getExtras();
        title_edt = findViewById(R.id.title_edt);
        cat_id_edt = findViewById(R.id.category_id_edt);
        image_category = findViewById(R.id.image_category);
        del_cat = findViewById(R.id.del_cat);
        del_cat.setVisibility(View.GONE);
        Button send_cat_btn = findViewById(R.id.send_cat_btn);
        pic_reader = getApplicationContext().getSharedPreferences("picture", 0);
        volley = new DateVolley();
        if (pic_reader.getString("Editable?", "nothing to show").equals("yes")) {
            if (address.getString("is_edit", "nothing").equals("true")) {
                edit_ads();
                title_toolbar.setText("ویرایش دسته بندی میانی");
                id = address.getString("id", "");
            } else {
                id = "";
                if (pic_reader.getString("pic_category_edit", " ").equals("true")) {
                    title_toolbar.setText("ویرایش دسته بندی میانی");
                    id = pic_reader.getString("id", " ");
                }
            }
        }

        final SharedPreferences pic_reader = getApplicationContext().getSharedPreferences("picture", 0);
        final String pic_category = pic_reader.getString("pic_category", "");

        del_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_category.setImageResource(R.mipmap.camera);
            }
        });
        send_cat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (address.getString("is_edit", "nothing").equals("true")) {
                    //  Log.i("mohsenjamali", "onClick: 1");
                    volley.connect(New_MiddleCategory.this, "activity_category", "update", true, cat_id_edt.getText().toString(), pic_category, title_edt.getText().toString(), id,"0");

                } else if (pic_reader.getString("Editable?", "nothing to show").equals("yes")) {
                    volley.connect(New_MiddleCategory.this, "activity_category", "update", true, cat_id_edt.getText().toString(), pic_category, title_edt.getText().toString(), id,"0");
                    //    Log.i("mohsenjamali", "onClick: 2");
                } else {
                    volley.connect(New_MiddleCategory.this, "activity_category", "send", true, cat_id_edt.getText().toString(), pic_category, title_edt.getText().toString(), id,"0");
                    //  Log.i("mohsenjamali", "onClick: 3");
                    // }
                }

            }
        });


        image_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(getPickImageChooserIntent(), 200);
            }
        });

        if (getIntent().getByteArrayExtra("byteArray") != null) {
            Bitmap bit = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("byteArray"), 0, getIntent().getByteArrayExtra("byteArray").length);
            image_category.setImageBitmap(bit);
            //  del_cat.setVisibility(View.VISIBLE);
        } else {
            image_category.setImageResource(R.mipmap.camera);
        }

        read_cash();
    }

    private void edit_ads() {

        title_edt.setText(address.getString("name", ""));
        cat_id_edt.setText(address.getString("parent_id", ""));

        Glide.with(New_MiddleCategory.this)
                .load(address.getString("image", ""))
                .override(300, 300)
                .error(R.mipmap.no_picture)
                .centerCrop()
                .into(image_category);
        //  del_cat.setVisibility(View.VISIBLE);
    }

    private void save_cash() {

        SharedPreferences.Editor edit = pic_reader.edit();

        if (!title_edt.getText().toString().isEmpty()) {
            edit.putString("title", title_edt.getText().toString());
        }
        if (!cat_id_edt.getText().toString().isEmpty()) {
            edit.putString("parent_id", cat_id_edt.getText().toString());
        }
        edit.putString("id", id);
        edit.apply();
    }

    private void read_cash() {
        if (!pic_reader.getString("title", " ").equals(" ")) {
            title_edt.setText(pic_reader.getString("title", " "));
        }
        if (!pic_reader.getString("parent_id", " ").equals(" ")) {
            cat_id_edt.setText(pic_reader.getString("parent_id", " "));
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(New_MiddleCategory.this, MiddleCategory.class);
        New_MiddleCategory.this.startActivity(intent);
    }

    public Intent getPickImageChooserIntent() {

// Determine Uri of camera image to  save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

// collect all camera intents
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

// collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

// the main intent is the last in the  list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

// Create a chooser from the main  intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "انتخاب کنید:");

// Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
        //   pic_reader.edit().putString("number_pick_image", number).apply();

        return chooserIntent;
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }


    /**
     * Get the URI of the selected image from  {}.<br/>
     * Will return the correct URI for camera  and gallery image.
     *
     * @param data the returned data of the  activity result
     */
    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    /**
     * Test if we can open the given Android URI to background_topBorder_Green if permission required error is thrown.<br>
     */
    public boolean isUriRequiresPermissions(Uri uri) {
        try {
            ContentResolver resolver = getContentResolver();
            InputStream stream = resolver.openInputStream(uri);
            stream.close();
            return false;
        } catch (FileNotFoundException e) {
            if (e.getCause() instanceof ErrnoException) {
                return true;
            }
        } catch (Exception e) {
            //nothing
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // mCropImageView.setImageUriAsync(mCropImageUri);
            Intent intent = new Intent(New_MiddleCategory.this, croping_image.class);
            intent.putExtra("imageUri", mCropImageUri.toString());
            //   intent.putExtra("number", pic_reader.getString("number_pick_image", ""));
            intent.putExtra("page", "middle_category");
            save_cash();
            startActivity(intent);
        } else {
            //Toast.makeText(this, "Required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bundle address2 = data.getExtras();

                //  lng = address2.getString("lng");
                //   lat = address2.getString("lat");
                // Log.i("mohsenjamali", "onActivityResult: " + lng + " " + lat);
                //   if (!sp.getString("namesenf", "nothing to show").equals("nothing to show") || !sp.getString("Username", "nothing to show").equals("nothing to show")) {

                //    uploadToNet(lng, lat);
             /*   } else {
                    final SharedPreferences sms_number = getApplicationContext().getSharedPreferences("sms_number", 0);
                    wait.show();


                    int sms_no = sms_number.getInt("sms_no", 0);

                    if (sms_no < 10) {

                        Date date = new Date(System.currentTimeMillis()); //or simply new Date();
                        sms_no++;
                        sms_number.edit().putInt("sms_no", sms_no).apply();
                        sms_number.edit().putLong("time", date.getTime()).apply();

                        login_sms(changeNumber_en(phone_e.getText().toString()), 1, "user", "yes_map");

                    } else if (sms_no >= 10) {

                        //getting the current time in milliseconds, and creating a Date object from it:
                        Date date = new Date(System.currentTimeMillis()); //or simply new Date();
                        //converting it back to a milliseconds representation:
                        long millis = date.getTime();
                        long startMillis = sms_number.getLong("time", 0L);
                        Calendar startTime = Calendar.getInstance();
                        startTime.setTimeInMillis(millis);
                        long now = System.currentTimeMillis();
                        long difference = now - startMillis;
                        long differenceInSeconds = difference / DateUtils.SECOND_IN_MILLIS;

                        if (differenceInSeconds >= 3600) {

                            sms_number.edit().clear().apply();
                            login_sms(changeNumber_en(phone_e.getText().toString()), 1, "user", "yes_map");

                        } else {

                            wait.dismiss();
                            Toast.makeText(new_ads.this, "شما در یک ساعت گذشته بیش از 10 بار درخواست کد فعال سازی داده اید", Toast.LENGTH_LONG).show();
                            Toast.makeText(new_ads.this, "لطفا یک ساعت دیگر درخواست کد فعال سازی نمائید", Toast.LENGTH_LONG).show();

                        }
                    }
                }*/
            }
        }
        if (requestCode == 200) {

            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = getPickImageResultUri(data);

                // For API >= 23 we need to check specifically that we have permissions to read external storage,
                // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
                boolean requirePermissions = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        isUriRequiresPermissions(imageUri)) {

                    // request permissions and handle the result in onRequestPermissionsResult()
                    requirePermissions = true;
                    mCropImageUri = imageUri;

                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                }

                if (!requirePermissions) {
                    Intent intent = new Intent(New_MiddleCategory.this, croping_image.class);
                    intent.putExtra("imageUri", imageUri.toString());
                    // intent.putExtra("number", pic_reader.getString("number_pick_image", ""));
                    intent.putExtra("page", "middle_category");
                    save_cash();
                    startActivity(intent);
                    //  mCropImageView.setImageUriAsync(imageUri);
                }
            }
        }
    }
}
