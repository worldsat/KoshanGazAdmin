package ir.koshangas.pasargad.product;

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
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.system.ErrnoException;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ir.koshangas.pasargad.DateVolley;
import ir.koshangas.pasargad.R;
import ir.koshangas.pasargad.croping_image;

public class New_Product extends AppCompatActivity {
    private Uri mCropImageUri;
    private EditText name_edt, description_edt, id_edt, price_edt, discount_edt, votes_edt, olaviat_edt, percent_edt;
    private Bundle address;
    private SharedPreferences pic_reader;
    private String[] picReader = new String[7];
    private SwitchCompat switchAvailable, switchSpecial, switchShowPrice,switchNewProduct,switchKhadamat;
    private String switch_number = "1", switch_special = "0", switch_ShowPrice = "1", switch_newProduct = "0", switch_khadamat = "0";
    private SharedPreferences pic_database;
    private ImageView[] del = new ImageView[7];
    private DateVolley volley;

    String id = "23";
    private ImageView[] upload_image = new ImageView[7];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        //toolbar
        Toolbar my_toolbar = findViewById(R.id.app_bar_show_ads);
        setSupportActionBar(my_toolbar);
        Button back_icon = findViewById(R.id.back_icon_rtl);
        back_icon.setVisibility(View.VISIBLE);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(New_Product.this, product.class);
                New_Product.this.startActivity(intent);
            }
        });
        TextView title_toolbar = findViewById(R.id.title_toolbar_rtl);
        title_toolbar.setText("ایجاد محصول جدید");
        volley = new DateVolley();
        //toolbar end
        address = getIntent().getExtras();
        name_edt = findViewById(R.id.title_edt);
        description_edt = findViewById(R.id.description_edt);
        id_edt = findViewById(R.id.id_edt);
        price_edt = findViewById(R.id.price_edt);
        discount_edt = findViewById(R.id.discount_edt);
        votes_edt = findViewById(R.id.votes_edt);
        olaviat_edt = findViewById(R.id.olaviat_edt);
        percent_edt = findViewById(R.id.percent_edt);
        id = id_edt.getText().toString();

        Button send_cat_btn = findViewById(R.id.send_cat_btn);
        pic_reader = getApplicationContext().getSharedPreferences("picture", 0);
        pic_database = getApplicationContext().getSharedPreferences("pic_database", 0);
        switchAvailable = findViewById(R.id.switchPicture);
        switchSpecial = findViewById(R.id.switchSpecial);
        switchShowPrice = findViewById(R.id.switchShowPrice);
        switchNewProduct = findViewById(R.id.switchNewProduct);
        switchKhadamat = findViewById(R.id.switchKhadamat);

        if (name_edt.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        firstload();
        if (pic_reader.getString("Editable?", "nothing to show").equals("yes")) {
            title_toolbar.setText("ویرایش محصول");
            glide();

            if (address.getString("is_edit", "nothing").equals("true")) {
                edit_ads();

            }
        } else if (pic_reader.getString("Editable?", "nothing to show").equals("no")) {
            title_toolbar.setText("محصول جدید");

        }

        percent_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence percentDiscount, int start, int before, int count) {

                try {
                    if (!discount_edt.getText().toString().isEmpty()) {
                        if (!percent_edt.getText().toString().isEmpty()) {
                            int price1 = Integer.valueOf(discount_edt.getText().toString());
                            int percent = Integer.valueOf(percentDiscount.toString());
                            int finalPrice = (price1) - (price1 * percent) / 100;
                            price_edt.setText(String.valueOf(finalPrice));
                        }
                    } else {
                        Toast.makeText(New_Product.this, "لطفا قیمت بدون تخفیف رو وارد نمائید", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(New_Product.this, "خطا در محاسبه تخفیف", Toast.LENGTH_SHORT).show();
                    percent_edt.setText("");
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pic_reader = getApplicationContext().getSharedPreferences("picture", 0);

        send_cat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!name_edt.getText().toString().isEmpty() || !id_edt.getText().toString().isEmpty() || !description_edt.getText().toString().isEmpty()) {
                    if (switchAvailable.isChecked()) {
                        switch_number = "1";
                    } else {
                        switch_number = "0";
                    }

                    if (switchSpecial.isChecked()) {
                        switch_special = "1";
                    } else {
                        switch_special = "0";
                    }

                    if (switchShowPrice.isChecked()) {
                        switch_ShowPrice = "1";
                    } else {
                        switch_ShowPrice = "0";
                    }
                    if (switchNewProduct.isChecked()) {
                        switch_newProduct = "1";
                    } else {
                        switch_newProduct = "0";
                    }
                    if (switchKhadamat.isChecked()) {
                        switch_khadamat = "1";
                    } else {
                        switch_khadamat = "0";
                    }
                    if (address.getString("is_edit", "nothing").equals("true")) {
                        // Log.i("mohsenjamali", "onClick: 1");
                        volley.connect_product(New_Product.this, "activity_product", "update", description_edt.getText().toString(), name_edt.getText().toString(), id_edt.getText().toString(), address.getString("id"), switch_number, switch_special, switch_ShowPrice,switch_newProduct,switch_khadamat
                              ,  price_edt.getText().toString(), discount_edt.getText().toString(), votes_edt.getText().toString(), olaviat_edt.getText().toString(), percent_edt.getText().toString());
                        Delete_image(address.getString("id"));
                    } else if (pic_reader.getString("Editable?", "nothing to show").equals("yes")) {
                        // Log.i("mohsenjamali", "onClick: 2");
                        id = pic_reader.getString("id", " ");
                        volley.connect_product(New_Product.this, "activity_product", "update", description_edt.getText().toString(), name_edt.getText().toString(), id_edt.getText().toString(), id, switch_number, switch_special, switch_ShowPrice,switch_newProduct,switch_khadamat
                                , price_edt.getText().toString(), discount_edt.getText().toString(), votes_edt.getText().toString(), olaviat_edt.getText().toString(), percent_edt.getText().toString());
                        Delete_image(id);
                    } else {
                        volley.connect_product(New_Product.this, "activity_product", "send", description_edt.getText().toString(), name_edt.getText().toString(), id_edt.getText().toString(), address.getString("id"), switch_number, switch_special, switch_ShowPrice,switch_newProduct,switch_khadamat
                                , price_edt.getText().toString(), discount_edt.getText().toString(), votes_edt.getText().toString(), olaviat_edt.getText().toString(), percent_edt.getText().toString());
                    }

                } else {
                    Toast.makeText(New_Product.this, "لطفا گزینه های خالی را پر نمائید", Toast.LENGTH_LONG).show();
                }
            }
        });


        if (getIntent().getByteArrayExtra("byteArray") != null) {
            Bitmap bit = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("byteArray"), 0, getIntent().getByteArrayExtra("byteArray").length);
            //image_product.setImageBitmap(bit);
        }
        read_cash();
        upload();
    }

    private void Delete_image(String id) {
        for (int i = 0; i < 7; i++) {
            final String i_str = String.valueOf(i);
            final String i_str_delete = String.valueOf(i - 1);
            final int i_2 = i;


            if (i > 0 && pic_reader.getString("pic_del" + i_str, "").equals("Delete")) {
                // volley.del_image(New_Product.this, i_str_delete, id);
                pic_reader.edit().putString("pic" + i_str, "null").apply();
                String pic = "pic_" + id + "_" + i_str;
                pic_database.edit().putString(pic, "nothing to show").apply();
            }
            // Log.i("mohsenjamali", "Delete_image: "+pic_reader.getString("pic0", ""));
            if (i == 0 && pic_reader.getString("pic_del0", "").equals("Delete")) {
                //   volley.del_image(New_Product.this, i_str_delete,id);
                pic_reader.edit().putString("pic0", "null").apply();
                String pic = "pic_" + id + "_" + "0";
                //  Log.i("mohsenjamali", "Delete_image: " + pic);
                pic_database.edit().putString(pic, "nothing to show").apply();
            }


        }
    }

    private void firstload() {
        del[0] = findViewById(R.id.del_product);
        del[1] = findViewById(R.id.del1);
        del[2] = findViewById(R.id.del2);
        del[3] = findViewById(R.id.del3);
        del[4] = findViewById(R.id.del4);
        del[5] = findViewById(R.id.del5);
        del[6] = findViewById(R.id.del6);
        for (int i = 0; i < 7; i++) {
            final String i_str = String.valueOf(i);
            final int i_2 = i;

            del[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    upload_image[i_2].setImageResource(R.mipmap.camera);
                    pic_reader.edit().putString("pic_del" + i_str, "Delete").apply();

                    del[i_2].setVisibility(View.GONE);
                }
            });
        }
        upload_image[0] = findViewById(R.id.image_product);
        upload_image[1] = findViewById(R.id.image_product1);
        upload_image[2] = findViewById(R.id.image_product2);
        upload_image[3] = findViewById(R.id.image_product3);
        upload_image[4] = findViewById(R.id.image_product4);
        upload_image[5] = findViewById(R.id.image_product5);
        upload_image[6] = findViewById(R.id.image_product6);

        for (int i = 0; i < 7; i++) {

            if (upload_image[i].getDrawable() == null) {
                upload_image[i].setImageResource(R.mipmap.camera);
                del[i].setVisibility(View.GONE);
            }
            //  Log.i("mohsenjamali", "firstload: "+upload_image[i].getDrawable().toString());
        }


    }

    private void edit_ads() {

        name_edt.setText(address.getString("name", ""));
        description_edt.setText(address.getString("description", ""));
        id_edt.setText(address.getString("category_id", ""));
        price_edt.setText(address.getString("price", ""));
        votes_edt.setText(address.getString("votes", ""));
        discount_edt.setText(address.getString("discount", ""));
        olaviat_edt.setText(address.getString("olaviat", ""));
        percent_edt.setText(address.getString("percentDiscount", ""));

//        try {
//            int noTakhfifPrice = Integer.valueOf(discount_edt.getText().toString());
//            int finalPrice = Integer.valueOf(price_edt.getText().toString());
//            int ekhtelaf = noTakhfifPrice - finalPrice;
//            int percent = (ekhtelaf * 100) / noTakhfifPrice;
//            percent_edt.setText(String.valueOf(percent));
//
//        } catch (Exception e) {
//            Toast.makeText(this, "عدم محاسبه تخفیف", Toast.LENGTH_SHORT).show();
//        }

        if (address.getString("isAvailable", "").equals("1")) {
            switchAvailable.setChecked(true);
        } else if (address.getString("isAvailable", "").equals("0")) {
            switchAvailable.setChecked(false);
        }
        if (address.getString("special", "").equals("1")) {
            switchSpecial.setChecked(true);
        } else if (address.getString("special", "").equals("0")) {
            switchSpecial.setChecked(false);
        }
        if (address.getString("ShowPrice", "").equals("1")) {
            switchShowPrice.setChecked(true);
        } else if (address.getString("ShowPrice", "").equals("0")) {
            switchShowPrice.setChecked(false);
        }
        if (address.getString("ShowNewProduct", "").equals("1")) {
            switchNewProduct.setChecked(true);
        } else if (address.getString("ShowNewProduct", "").equals("0")) {
            switchNewProduct.setChecked(false);
        }
        if (address.getString("ShowKhadamat", "").equals("1")) {
            switchKhadamat.setChecked(true);
        } else if (address.getString("ShowKhadamat", "").equals("0")) {
            switchKhadamat.setChecked(false);
        }
        // Log.i("mohsenjamali", "mohsen4: " + address.getString("isAvailable", ""));

    }

    private void glide() {

        String main_image_link = "http://www.koshangaspasargad.ir/koshangas/" + pic_database.getString("main_image_" + pic_reader.getString("id", ""), "");

        Glide.with(New_Product.this)
                .load(main_image_link)
                .error(R.mipmap.camera)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .placeholder(R.mipmap.camera)
                .into(upload_image[0]);
        //   if (!pic_reader.getString("pic0", "nothing to show").equals("nothing to show")) {
        del[0].setVisibility(View.VISIBLE);
        //   }

        for (int n = 1; n < 7; n++) {

            String id = pic_reader.getString("id", "");

            if (!address.getString("id", "").equals("")) {
                id = address.getString("id", "");
            }
            String pic = "pic_" + id + "_" + n;
            // Glide.get(New_Product.this).clearMemory();

            if (!pic_database.getString(pic, "nothing to show").equals("nothing to show")) {
                String link2 = "http://www.koshangaspasargad.ir/koshangas/" + pic_database.getString("pic_" + id + "_" + n, "n");
                // Log.i("mohsenjamali", "glide2: " + pic + "  " + link2);
                del[n].setVisibility(View.VISIBLE);

                Glide.with(New_Product.this)
                        .load(link2)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .error(R.mipmap.camera)
                        .placeholder(R.mipmap.camera)
                        .into(upload_image[n]);
            }
        }
    }

    //upload picture
    private void upload() {


        for (int i = 0; i < 7; i++) {
            picReader[i] = pic_reader.getString("pic" + String.valueOf(i), "nothing to show");
        }

        for (int i = 0; i < 7; i++) {

            String i_str = String.valueOf(i);
            if (!pic_reader.getString("pic" + i_str, "nothing to show").equals("nothing to show")) {

                Bitmap[] img_shared = new Bitmap[7];
                img_shared[i] = decodeBase64(pic_reader.getString("pic" + i_str, "nothing to show"));
                Glide.clear(upload_image[i]);
                upload_image[i].setImageBitmap(img_shared[i]);
                del[i].setVisibility(View.VISIBLE);

            }
        }

        if (getIntent().getByteArrayExtra("byteArray") != null) {
            Bitmap bit = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("byteArray"), 0, getIntent().getByteArrayExtra("byteArray").length);
            Bundle address = getIntent().getExtras();

            int Image_number = Integer.parseInt(address.getString("number"));

            if (Image_number == 0) {
                upload_image[0].setImageBitmap(bit);
            } else if (Image_number == 1) {
                upload_image[1].setImageBitmap(bit);
            } else if (Image_number == 2) {
                upload_image[2].setImageBitmap(bit);
            } else if (Image_number == 3) {
                upload_image[3].setImageBitmap(bit);
            } else if (Image_number == 4) {
                upload_image[4].setImageBitmap(bit);
            } else if (Image_number == 5) {
                upload_image[5].setImageBitmap(bit);
            } else if (Image_number == 6) {
                upload_image[6].setImageBitmap(bit);
            }

        }

        for (int i = 0; i < 7; i++) {
            final String i_str = String.valueOf(i);
            upload_image[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    save_cash();
                    startActivityForResult(getPickImageChooserIntent(i_str), 200);
                }
            });
        }

    }


    private void save_cash() {

        SharedPreferences.Editor edit = pic_reader.edit();

        if (!name_edt.getText().toString().isEmpty()) {
            edit.putString("name", name_edt.getText().toString());
        }
        if (!description_edt.getText().toString().isEmpty()) {
            edit.putString("description", description_edt.getText().toString());
        }
        if (!id_edt.getText().toString().isEmpty()) {
            edit.putString("category_id", id_edt.getText().toString());
        }
        if (!price_edt.getText().toString().isEmpty()) {
            edit.putString("price", price_edt.getText().toString());
        }
        if (!votes_edt.getText().toString().isEmpty()) {
            edit.putString("votes", votes_edt.getText().toString());
        }

        if (!discount_edt.getText().toString().isEmpty()) {
            edit.putString("discount", discount_edt.getText().toString());
        }
        if (!olaviat_edt.getText().toString().isEmpty()) {
            edit.putString("olaviat", olaviat_edt.getText().toString());
        }
        if (!percent_edt.getText().toString().isEmpty()) {
            edit.putString("percentDiscount", percent_edt.getText().toString());
        }

        if (switchAvailable.isChecked()) {
            edit.putString("isAvailable", "1");
        } else {
            edit.putString("isAvailable", "0");
        }
        if (switchSpecial.isChecked()) {
            edit.putString("special", "1");
        } else {
            edit.putString("special", "0");
        }
        if (switchShowPrice.isChecked()) {
            edit.putString("ShowPrice", "1");
        } else {
            edit.putString("ShowPrice", "0");
        }
        if (switchNewProduct.isChecked()) {
            edit.putString("ShowKhadamat", "1");
        } else {
            edit.putString("ShowKhadamat", "0");
        }
        if (switchKhadamat.isChecked()) {
            edit.putString("ShowNewProduct", "1");
        } else {
            edit.putString("ShowNewProduct", "0");
        }
        edit.apply();
        // Log.i("mohsenjamali", "mohsen2: " + switchAvailable.isChecked());
    }


    private void read_cash() {
        if (!pic_reader.getString("name", " ").equals(" ")) {
            name_edt.setText(pic_reader.getString("name", " "));
        }
        if (!pic_reader.getString("description", " ").equals(" ")) {
            description_edt.setText(pic_reader.getString("description", " "));
        }
        if (!pic_reader.getString("category_id", " ").equals(" ")) {
            id_edt.setText(pic_reader.getString("category_id", " "));
        }
        if (!pic_reader.getString("price", " ").equals(" ")) {
            price_edt.setText(pic_reader.getString("price", " "));
        }
        if (!pic_reader.getString("olaviat", " ").equals(" ")) {
            olaviat_edt.setText(pic_reader.getString("olaviat", " "));
        }
        if (!pic_reader.getString("votes", " ").equals(" ")) {
            votes_edt.setText(pic_reader.getString("votes", " "));
        }

        if (!pic_reader.getString("discount", " ").equals(" ")) {
            discount_edt.setText(pic_reader.getString("discount", " "));
        }
        if (!pic_reader.getString("percentDiscount", " ").equals(" ")) {
            percent_edt.setText(pic_reader.getString("percentDiscount", " "));
        }
        if (pic_reader.getString("isAvailable", " ").equals("1")) {
            switchAvailable.setChecked(true);
        } else if (pic_reader.getString("isAvailable", " ").equals("0")) {
            switchAvailable.setChecked(false);
        }

        if (pic_reader.getString("special", " ").equals("1")) {
            switchSpecial.setChecked(true);
        } else if (pic_reader.getString("special", " ").equals("0")) {
            switchSpecial.setChecked(false);
        }
        if (pic_reader.getString("ShowPrice", " ").equals("1")) {
            switchShowPrice.setChecked(true);
        } else if (pic_reader.getString("ShowPrice", " ").equals("0")) {
            switchShowPrice.setChecked(false);
        }
        if (pic_reader.getString("ShowKhadamat", " ").equals("1")) {
            switchKhadamat.setChecked(true);
        } else if (pic_reader.getString("ShowKhadamat", " ").equals("0")) {
            switchKhadamat.setChecked(false);
        }
        if (pic_reader.getString("ShowNewProduct", " ").equals("1")) {
            switchNewProduct.setChecked(true);
        } else if (pic_reader.getString("ShowNewProduct", " ").equals("0")) {
            switchNewProduct.setChecked(false);
        }
        //  Log.i("mohsenjamali", "mohsen3: " + pic_reader.getString("isAvailable", " "));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(New_Product.this, product.class);
        New_Product.this.startActivity(intent);
    }


    public Intent getPickImageChooserIntent(String number) {

// Determine Uri of camera image to  save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

// collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
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
        pic_reader.edit().putString("number_pick_image", number).apply();

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
            Intent intent = new Intent(New_Product.this, croping_image.class);
            intent.putExtra("imageUri", mCropImageUri.toString());
            intent.putExtra("number", pic_reader.getString("number_pick_image", ""));
            intent.putExtra("page", "activity_product");
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
                    Intent intent = new Intent(New_Product.this, croping_image.class);
                    intent.putExtra("imageUri", imageUri.toString());
                    intent.putExtra("number", pic_reader.getString("number_pick_image", ""));
                    intent.putExtra("page", "activity_product");
                    startActivity(intent);
                    //  mCropImageView.setImageUriAsync(imageUri);
                }
            }
        }
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
