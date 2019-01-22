package ir.koshangas.pasargad;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.system.ErrnoException;
import android.util.Base64;
import android.view.View;
import android.widget.Button;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ir.koshangas.pasargad.category.New_Category;
import ir.koshangas.pasargad.middleCategory.New_MiddleCategory;
import ir.koshangas.pasargad.middleCategory2.New_MiddleCategory2;
import ir.koshangas.pasargad.product.New_Product;
import ir.koshangas.pasargad.slider.New_Slider;

public class croping_image extends AppCompatActivity {
    private CropImageView mCropImageView;
    private Uri mCropImageUri;
    private Bitmap cropped;
    private Intent i;
    private String number_upload_img;
    private SharedPreferences pic_writer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.croping_image);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        final Bundle address = getIntent().getExtras();
        number_upload_img = address.getString("number");

        mCropImageView = (CropImageView) findViewById(R.id.cropImageView);
        mCropImageView.setCropShape(CropImageView.CropShape.RECTANGLE);
        if (address.getString("page").equals("slider")) {
            mCropImageView.setAspectRatio(2, 1);
        }
        if (address.getString("imageUri") != null) {
            Uri myUri = Uri.parse(address.getString("imageUri"));

            mCropImageView.setImageUriAsync(myUri);
        }

        Button send = (Button) findViewById(R.id.send);
        Button test = (Button) findViewById(R.id.button3);

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(getPickImageChooserIntent(), 200);
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (number_upload_img.equals("0")) {
                //                   cropped = mCropImageView.getCroppedImage(288, 288);
                //               } else {
                cropped = mCropImageView.getCroppedImage(512, 512);
                //               }

                if (cropped != null) {
                    if (address.getString("page").equals("activity_category")) {
                        i = new Intent(croping_image.this, New_Category.class);
                    } else if (address.getString("page").equals("activity_product")) {
                        i = new Intent(croping_image.this, New_Product.class);
                    } else if (address.getString("page").equals("middle_category")) {
                        i = new Intent(croping_image.this, New_MiddleCategory.class);
                    } else if (address.getString("page").equals("middle_category2")) {
                        i = new Intent(croping_image.this, New_MiddleCategory2.class);
                    } else if (address.getString("page").equals("slider")) {
                        cropped = mCropImageView.getCroppedImage(1600, 266);
                        i = new Intent(croping_image.this, New_Slider.class);
                    }
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    cropped.compress(Bitmap.CompressFormat.JPEG, 90, bs);

                    pic_writer = getApplicationContext().getSharedPreferences("picture", 0);

                    SharedPreferences.Editor edit = pic_writer.edit();
                    String image = getStringImage(cropped);

                    if (address.getString("page").equals("activity_category") || address.getString("page").equals("middle_category")) {
                        edit.putString("pic_category", image);
                        edit.putString("pic_category_edit", "true");
                    } else if (address.getString("page").equals("activity_product") || address.getString("page").equals("slider")) {
                        edit.putString("pic" + address.getString("number"), image);
                        //    edit.putString("pic_product_edit", "true");

                    }

                    //baraye delete va update aks asli activity_product
                    if (address.getString("page").equals("activity_product")) {
                        if (address.getString("number").equals("0")) {
                            edit.putString("pic_del0", "NoDelete");
                        }
                    }


                    edit.apply();
                    i.putExtra("byteArray", bs.toByteArray());
                    i.putExtra("number", number_upload_img);
                    i.putExtra("scroll", "yes");
                    startActivity(i);
                }
            }
        });
    }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 95, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    /**
     * On load image button click, start pick  image chooser activity.
     */
    public void onLoadImageClick(View view) {
        startActivityForResult(getPickImageChooserIntent(), 200);
    }

    /**
     * Crop the image and set it back to the  cropping view.
     */
    public void onCropImageClick(View view) {
        Bitmap cropped = mCropImageView.getCroppedImage(500, 500);
        if (cropped != null)
            mCropImageView.setImageBitmap(cropped);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri imageUri = getPickImageResultUri(data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage,
            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
            boolean requirePermissions = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    isUriRequiresPermissions(imageUri)) {

                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true;
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }

            if (!requirePermissions) {
                mCropImageView.setImageUriAsync(imageUri);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mCropImageView.setImageUriAsync(mCropImageUri);
        } else {
            //Toast.makeText(this, "Required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Create a chooser intent to select the  source to get image from.<br/>
     * The source can be camera's  (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br/>
     * All possible sources are added to the  intent chooser.
     */
    public Intent getPickImageChooserIntent() {

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

        return chooserIntent;
    }

    /**
     * Get URI to image received from capture  by camera.
     */
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    /**
     * Get the URI of the selected image from  {@link #getPickImageChooserIntent()}.<br/>
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
}