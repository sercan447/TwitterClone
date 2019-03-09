package sercandevops.com.twitterclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TweetGonder extends AppCompatActivity {

    private TextView sayac;
    private EditText text;
    private ImageButton imButton;
    private ImageView resim;
    private FloatingActionButton fab;
    private Bitmap bitmap;
    private static final int SADECE_RESIM=1;
    private static final int SADECE_TEXT=2;
    private static final int TEXT_VE_RESIM=3;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String URL_TWEETLER = "http://10.0.2.2/twitterclone/tweetler.php";
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;
        private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_gonder);

        sayac = findViewById(R.id.tv_savac);
        text = findViewById(R.id.edit_twwetat);
        resim =findViewById(R.id.imageViewim);
        imButton = findViewById(R.id.imbutton);
        fab = findViewById(R.id.fab_tweetat);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        id = sharedPreferences.getString("id","-1");
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int i = 280 - s.toString().trim().length();


                sayac.setText(String.valueOf(i));

                if(i < 0){
                    sayac.setTextColor(Color.RED);
                    fab.hide();
                }else {
                    sayac.setTextColor(Color.parseColor("#444444"));
                    fab.show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimSecmeIstegi();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text.getText().toString().trim().length() != 0 && bitmap != null)
                {
                    istekGonder(TEXT_VE_RESIM);
                }

                if(text.getText().toString().trim().length() != 0 && bitmap == null)
                {
                    istekGonder(SADECE_TEXT);
                }

                if (text.getText().toString().trim().length() == 0 && bitmap != null)
                {
                    istekGonder(SADECE_RESIM);
                }

                if(text.getText().toString().trim().length() == 0 && bitmap == null)
                {
                    Toast.makeText(getApplicationContext(),"Hiçbir twwet oluşturulamadı.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void istekGonder(final int istekTuru) {

        final ProgressDialog loading = ProgressDialog.show(TweetGonder.this,"tweet gonderiliyor..","LÜtfen bekleyiniz");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_TWEETLER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    Log.d("JSON BİLGİSİ ",response );
                    loading.dismiss();

                    String durum = null,mesaj = null;

                    try
                    {
                        JSONObject jsonObject = new JSONObject(response);
                        durum =jsonObject.getString("status");
                        mesaj = jsonObject.getString("mesaj");

                    }catch (JSONException json)
                    {
                        json.printStackTrace();
                    }finally {

                    }

                    if(durum != null && durum.equals("200"))
                    {
                       // Snackbar.make(findViewById(R.id.fab_tweeet_at_butonu),"Tweet başarılı bir şekilde atıldı.",Snackbar.LENGTH_LONG).show();
                        text.setText("");
                        resim.setImageBitmap(null);
                    }else
                    {
                        Snackbar.make(findViewById(R.id.fab_tweetat),mesaj,Snackbar.LENGTH_LONG).show();
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loading.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                String uuid = UUID.randomUUID().toString();

                switch (istekTuru)
                {
                    case SADECE_RESIM:
                    String image = getStringImage(bitmap);

                    params.put("id",id);
                    params.put("uuid",uuid);
                    params.put("resim",image);
                    params.put("istek_turu",String.valueOf(SADECE_RESIM));
                    break;

                    case SADECE_TEXT:
                        params.put("id",id);
                        params.put("uuid",uuid);
                        params.put("text",text.getText().toString());
                        params.put("istek_turu",String.valueOf(SADECE_TEXT));
                        break;

                    case TEXT_VE_RESIM:
                        String image2 = getStringImage(bitmap);

                        params.put("id",id);
                        params.put("uuid",uuid);
                        params.put("resim",image2);
                        params.put("text",text.getText().toString());
                        params.put("istek_turu",String.valueOf(TEXT_VE_RESIM));
                        break;
                }
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }//func
    public void resimSecmeIstegi()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"resim seçiniz."),PICK_IMAGE_REQUEST);
    }

    public String getStringImage(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] imagebytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imagebytes,Base64.DEFAULT);
        return encodedImage;
    }

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
            resim.setImageBitmap(bitmap);
        }
    }//func
}
