package sercandevops.com.twitterclone;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.FrameLayout;
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

import java.util.HashMap;
import java.util.Map;

public class GirisEkrani extends AppCompatActivity {

    ImageView image;
    private static final String URL_LOGIN = "http://10.0.2.2/twitterclone/login.php";
    private TextInputLayout til_kullaniciAdi_giris,til_sifre_giris;
    private TextInputEditText ed_kullaniciAdi_giris,ed_sifre_giris;
    private RequestQueue requestQueue;
    private SharedPreferences sharedPreferences;
    private CheckBox chk_hatirla;
    private FrameLayout frameLayoutGirisEkrani;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initialize();

        if (!internetBaglantisiKontrol())
        {
            Snackbar.make(frameLayoutGirisEkrani,"NET YOK",Snackbar.LENGTH_LONG).show();
        }else
        {
            Snackbar.make(frameLayoutGirisEkrani,"NET VAR",Snackbar.LENGTH_LONG).show();
        }

        ArkaPlanDurumKontrolu();

        findViewById(R.id.tv_sifremiunuttum).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    ((TextView)v).setTextColor(Color.parseColor("#dd999999"));
                if(event.getAction() == MotionEvent.ACTION_UP)
                    ((TextView)v).setTextColor(Color.WHITE);

                return false;
            }
        });


    }
    public void initialize()
    {
        image  = findViewById(R.id.image);
        til_kullaniciAdi_giris = findViewById(R.id.til_kullaniciAdi_giris);
        til_sifre_giris = findViewById(R.id.til_sifre_giris);
        ed_kullaniciAdi_giris = findViewById(R.id.ed_kullaniciAdi_giris);
        ed_sifre_giris = findViewById(R.id.ed_sifre_giris);
        chk_hatirla = findViewById(R.id.chk_hatirla);
        frameLayoutGirisEkrani = findViewById(R.id.frameLayoutGirisEkrani);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if(sharedPreferences.getBoolean("benihatirla",false))
        {
            Intent intent = new Intent(GirisEkrani.this,Twitter.class);
            startActivity(intent);
            finish();
        }
    }
    public void click(View view)
    {
        Intent intent;
        switch (view.getId())
        {
            case R.id.btn_kayit:
                intent = new Intent(GirisEkrani.this,KayitEkrani.class);
                startActivity(intent);
                break;
            case R.id.fab_giris:
                girisDenetimi();
                break;
        }
    }//click

    public void girisDenetimi()
    {
        boolean durumkullaniciAdi = TextUtils.isEmpty(ed_kullaniciAdi_giris.getText());
        boolean durumkullaniciSifre = TextUtils.isEmpty(ed_sifre_giris.getText());

        til_kullaniciAdi_giris.setError(null);
        til_sifre_giris.setError(null);

        if(durumkullaniciAdi || durumkullaniciSifre)
        {
            if (durumkullaniciAdi)
                til_kullaniciAdi_giris.setError("Kullanıcı adı Yanlış.");
            if (durumkullaniciSifre)
                til_sifre_giris.setError("Parolanız Yanlış.");

        }else
        {
            if (!internetBaglantisiKontrol())
            {
                Snackbar.make(frameLayoutGirisEkrani,"Internet Bağlantınızı Kontrol Ediniz",Snackbar.LENGTH_LONG).show();
            }else
            {
                findViewById(R.id.fab_giris).setEnabled(false);
                girisIstegiGonder();
            }
        }
    }//FUNC

    public void girisIstegiGonder()
    {

        StringRequest request = new StringRequest(Request.Method.POST, URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //cevap geldiginde giris butonunu enable yap
                findViewById(R.id.fab_giris).setEnabled(true);

                Log.e("json gelen",response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String durum = jsonObject.getString("status");
                    String mesaj = jsonObject.getString("mesaj");

                    if (durum.equals("200"))
                    {
                        //if (chk_hatirla.isChecked()) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("id",jsonObject.getString("id"));
                            editor.putBoolean("benihatira",chk_hatirla.isChecked());
                            editor.commit();
                      //  }
                        Intent intent = new Intent(GirisEkrani.this,Twitter.class);
                        startActivity(intent);
                        finish();

                        //Snackbar.make(frameLayoutGirisEkrani,mesaj, Toast.LENGTH_SHORT).show();
                    }else
                    {
                            Snackbar.make(frameLayoutGirisEkrani,mesaj,Snackbar.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("json gelen",error.getLocalizedMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> degers = new HashMap<>();
                degers.put("kullaniciadi",ed_kullaniciAdi_giris.getText().toString());
                degers.put("sifre",ed_sifre_giris.getText().toString());

                return degers;
            }
        };

        requestQueue.add(request);
    }//func
    private void ArkaPlanDurumKontrolu()
    {
        //dınamık olarak resimi boyutlandırma işlemi
        Window window = getWindow();
        //TAM EKRAN YAPMA KODU
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        WindowManager wm = window.getWindowManager();
        Display ekran = wm.getDefaultDisplay();

        Point point = new Point();
        ekran.getSize(point);

        int genislik = point.x;
        int yukseklik = point.y;

        image.getLayoutParams().width = (int) (yukseklik * 1.78);
        image.getLayoutParams().height =  yukseklik;


        ObjectAnimator animator = ObjectAnimator.ofFloat(image,"x",0,
                -(yukseklik*1.78f-genislik),0,
                -(yukseklik*1.78f-genislik));

        animator.setDuration(210000);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    private boolean  internetBaglantisiKontrol()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected())
        {
            return true;
        }else {
            return false;
        }
    }//FUNC
}
