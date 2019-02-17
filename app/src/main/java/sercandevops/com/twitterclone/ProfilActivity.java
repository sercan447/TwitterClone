package sercandevops.com.twitterclone;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilActivity extends AppCompatActivity {

    private CircleImageView img_profile,img_popup;
    private final int PICK_IMAGE_REQUEST = 44;
    private final int GALERI_REQUEST = 57;
    private Bitmap bitmap;
    private String id;
    private static final String  URL_PROFIL_GUNCELLE = "http://10.0.2.2/twitterclone/profilfotoyukle.php";
    private ConstraintLayout constraintlayot;
    SharedPreferences preferences;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        Initialize();
        clickDurumu();
    }

    private void Initialize()
    {
        img_profile = findViewById(R.id.img_profile_image_profile2);
        img_popup = findViewById(R.id.img_popupsecenek);
        constraintlayot = findViewById(R.id.constraintlayot);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        id = preferences.getString("id","0");
    }

    private void clickDurumu()
    {
        img_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(ProfilActivity.this,img_popup);
                popupMenu.getMenu().add("Kamera");
                popupMenu.getMenu().add("Galeri");
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getTitle() == "Galeri")
                        {
                            IzinDenetimi();
                        }
                        if (item.getTitle() == "Kamera")
                        {

                        }
                        return true;
                    }
                });
            }
        });
    }//FUNC
private void IzinDenetimi()
{
      /*
        String[] izinler = {Manifest.permission.READ_EXTERNAL_STORAGE};

        if (ActivityCompat.checkSelfPermission(ProfilActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(ProfilActivity.this,izinler,GALERI_REQUEST);

        }
        */
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
    {
         resimRecmeIstegi();
    }else {
            resimRecmeIstegi();
    }
}//FUNCC
    private void resimRecmeIstegi()
    {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Resim Seçiniz.."),PICK_IMAGE_REQUEST);
    }//func


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null)
            {
                Uri filepath = data.getData();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("RESIM HATA ",e.getLocalizedMessage());
                }
                img_profile.setImageBitmap(bitmap);
            }
    }//func

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Toast.makeText(getApplicationContext(),"ONREQUEST",Toast.LENGTH_SHORT).show();
    }

    public void click(View view) {
        switch (view.getId())
        {
            case R.id.fab_profil_image:
                if (bitmap != null)
                {
                    profilGuncellemeIstegiGonder();
                }else
                {
                    Snackbar.make(constraintlayot,"Lütfen bir resim seçiniz.",Snackbar.LENGTH_LONG).show();
                }
                break;
        }

    }//click


    public void profilGuncellemeIstegiGonder()
    {
        final ProgressDialog progressDialog = new ProgressDialog(ProfilActivity.this);
        progressDialog.setTitle("Profil Güncelleniyor...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PROFIL_GUNCELLE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                Log.e("Json verisi : " ,response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String image = getStringImage(bitmap);

                Map<String,String> params = new HashMap<>();
                params.put("id",id);
                params.put("profil",image);

                return params;
            }
        };

        requestQueue.add(stringRequest);
    }//func
    public String getStringImage(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes,Base64.DEFAULT);

        return encodedImage;
    }
}
