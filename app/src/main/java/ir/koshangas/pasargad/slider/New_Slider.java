package ir.koshangas.pasargad.slider;

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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ir.koshangas.pasargad.MainActivity;
import ir.koshangas.pasargad.R;
import ir.koshangas.pasargad.croping_image;
import ir.koshangas.pasargad.product.product;

public class New_Slider extends AppCompatActivity {
    private Uri mCropImageUri;
    private SharedPreferences pic_reader;
    private String[] picReader = new String[7];
    private ImageView[] del = new ImageView[7];
    private ImageView[] upload_image = new ImageView[7];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        //toolbar
        Toolbar my_toolbar = findViewById(R.id.app_bar_show_ads);
        setSupportActionBar(my_toolbar);
        Button back_icon = findViewById(R.id.back_icon_rtl);
        back_icon.setVisibility(View.VISIBLE);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(New_Slider.this, product.class);
                New_Slider.this.startActivity(intent);
            }
        });
        TextView title_toolbar = findViewById(R.id.title_toolbar_rtl);
        title_toolbar.setText("اسلایدر صفحه اول");

        //toolbar end
        Button send_cat_btn = findViewById(R.id.send_cat_btn);
        pic_reader = getApplicationContext().getSharedPreferences("picture", 0);

        firstload();
        glide();

        final Slider slider = new Slider();

        send_cat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                slider.connect_product(New_Slider.this, "activity_product", "update");

            }
        });

        upload();
    }

    private void firstload() {

        del[1] = findViewById(R.id.del1);
        del[2] = findViewById(R.id.del2);
        del[3] = findViewById(R.id.del3);
        del[4] = findViewById(R.id.del4);
        del[5] = findViewById(R.id.del5);
        del[6] = findViewById(R.id.del6);

        upload_image[1] = findViewById(R.id.image_product1);
        upload_image[2] = findViewById(R.id.image_product2);
        upload_image[3] = findViewById(R.id.image_product3);
        upload_image[4] = findViewById(R.id.image_product4);
        upload_image[5] = findViewById(R.id.image_product5);
        upload_image[6] = findViewById(R.id.image_product6);

        for (int i = 1; i < 7; i++) {
            final String i_str = String.valueOf(i);
            final int i_2 = i;

            del[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    upload_image[i_2].setImageResource(R.mipmap.emptybanner);
                    pic_reader.edit().putString("pic" + i_str, "null").apply();
                    del[i_2].setVisibility(View.GONE);
                }
            });
        }

        for (int i = 1; i < 7; i++) {

            if (upload_image[i].getDrawable() == null) {
                upload_image[i].setImageResource(R.mipmap.emptybanner);
                del[i].setVisibility(View.GONE);
            }
        }
    }


    private void glide() {
        getSlider getSlider = new getSlider();
        getSlider.get_banners(New_Slider.this, upload_image);
        for (int n = 1; n < 7; n++) {

            del[n].setVisibility(View.VISIBLE);

        }
    }


    private void upload() {

        for (int i = 1; i < 7; i++) {
            picReader[i] = pic_reader.getString("pic" + String.valueOf(i), "nothing to show");
            Log.i("mohsenjamali", "upload: " + i + "   " + picReader[i]);
        }

        for (int i = 1; i < 7; i++) {

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

            if (Image_number == 1) {
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

        for (int i = 1; i < 7; i++) {
            final String i_str = String.valueOf(i);
            upload_image[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    startActivityForResult(getPickImageChooserIntent(i_str), 200);
                }
            });
        }

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(New_Slider.this, MainActivity.class);
        New_Slider.this.startActivity(intent);
    }

    public Intent getPickImageChooserIntent(String number) {

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
            Intent intent = new Intent(New_Slider.this, croping_image.class);
            intent.putExtra("imageUri", mCropImageUri.toString());
            intent.putExtra("number", pic_reader.getString("number_pick_image", ""));
            intent.putExtra("page", "slider");
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
                    Intent intent = new Intent(New_Slider.this, croping_image.class);
                    intent.putExtra("imageUri", imageUri.toString());
                    intent.putExtra("number", pic_reader.getString("number_pick_image", ""));
                    intent.putExtra("page", "slider");
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
