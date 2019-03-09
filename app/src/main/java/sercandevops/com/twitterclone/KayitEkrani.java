package sercandevops.com.twitterclone;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.inputmethodservice.Keyboard;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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

public class KayitEkrani extends AppCompatActivity {

   private TextInputLayout til_adsoyad,til_email,til_kullaniciadi,til_parola;
   private TextInputEditText ed_adsoyad,ed_email,ed_kullaniciadi,ed_parola;
   private FrameLayout framelayout_kayitEkrani;

    private ImageView imageKayit;
    private RequestQueue requestQueue;
    private static final String URL_KAYIT = "http://10.0.2.2/twitterclone/register.php";
    private SharedPreferences preferences;

    ObjectAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ekrani);

        Initialize();

        //KLAVYE GIZLEME
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        /*
        InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = KayitEkrani.this.getCurrentFocus();

        if(view == null)
            view = new View(KayitEkrani.this);
        imm.hideSoftInputFromWindow(view.getWindowToken(),0);
*/


      /*  if(!internetBaglantisiKontrol())
        {
            Snackbar.make(framelayout_kayitEkrani,"Internet baglantınızı Kontrol ediniz.",Snackbar.LENGTH_SHORT).show();
        }else {

            Snackbar.make(framelayout_kayitEkrani,"a",Snackbar.LENGTH_SHORT).show();
        }*/

        TamEkranAnimasyon();
        tiklamaOlayi();
    }

    public void Initialize()
    {
        imageKayit = findViewById(R.id.imagekayit);
        til_adsoyad = findViewById(R.id.til_adsoyad_kayit);
        til_email = findViewById(R.id.til_email_kayit);
        til_kullaniciadi = findViewById(R.id.til_kullaniciAdi_kayit);
        til_parola = findViewById(R.id.til_sifre_kayit);
        ed_adsoyad = findViewById(R.id.ed_adsoyad_kayit);
        ed_email = findViewById(R.id.ed_email_kayit);
        ed_kullaniciadi = findViewById(R.id.ed_kullaniciAdi_kayit);
        ed_parola = findViewById(R.id.ed_sifre_kayit);
        framelayout_kayitEkrani = findViewById(R.id.framelayout_kayitEkrani);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }//func

    private void tiklamaOlayi()
    {

        findViewById(R.id.tv_kayit_zatenHesabvar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(KayitEkrani.this,GirisEkrani.class);
                NavUtils.navigateUpTo(KayitEkrani.this,intent);
            }
        });

        findViewById(R.id.tv_kayit_zatenHesabvar).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    ((TextView)v).setTextColor(Color.parseColor("#dd999999"));
                } if(event.getAction() == MotionEvent.ACTION_UP)
                {
                    ((TextView)v).setTextColor(Color.WHITE);
                }

                return false;
            }
        });

        findViewById(R.id.fab_kayit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean durumadsoyad = TextUtils.isEmpty(ed_adsoyad.getText());
                boolean durummail = TextUtils.isEmpty(ed_email.getText());
                boolean durumsifre = TextUtils.isEmpty(ed_parola.getText());
                boolean durumkullaniciadi = TextUtils.isEmpty(ed_kullaniciadi.getText());

                til_email.setError(null);
                til_kullaniciadi.setError(null);
                til_parola.setError(null);
                til_adsoyad.setError(null);

                if(durumadsoyad || durummail || durumsifre || durumkullaniciadi || !ed_email.getText().toString().contains("@"))
                {
                    if(durumadsoyad)
                        til_adsoyad.setError("Lütren ad soyad giriniz");
                    if (durumsifre)
                        til_parola.setError("lütfen şifrenizi giriniz.");
                    if (durumkullaniciadi)
                        til_kullaniciadi.setError("lütfen kullanıcı adınızı giriniz.");
                    if(durummail)
                        til_email.setError("lütfen Mailinizi giriniz");
                    else if(!ed_email.getText().toString().contains("@"))
                        til_email.setError("Lütfen geçerli bir mail adresi giriniz");
                }else
                {
                    // kayıt istegi
                    istekGonder();
                }
            }
        });

    }//FUNC

    public void istekGonder()
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL_KAYIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("JSON",response);

                try
                {
                    JSONObject json = new JSONObject(response);
                  //  String id = json.getString("id");
                    String durum = json.getString("status");


                    if(durum.equals("404"))
                    {
                        Snackbar.make(framelayout_kayitEkrani,"Sunucu ile bağlantı kurulamadı",Snackbar.LENGTH_LONG).show();
                    }else if(durum.equals("400"))
                    {
                        Snackbar.make(framelayout_kayitEkrani,"Verilen bilgilerle kayıt yapılamadı.. kullanıcı adı daha once kullanılmışç",Snackbar.LENGTH_LONG).show();

                    }else if(durum.equals("200"))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                        builder.setMessage("Kayıt işlemi başarılı bir şekilde yapıldı. Lütfen Maile adresinizi kontrol ediniz");
                        builder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });

                        builder.show();

                        SharedPreferences.Editor editor = preferences.edit();
                           // editor.putString("id",id);
                            //editor.commit();

                        //ana ekrana gidecek
                    }
                }catch (JSONException e)
                {
                    Log.e("parse hatası : ",e.getLocalizedMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("JSON",error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> degerler = new HashMap<>();
                degerler.put("kullaniciad",ed_kullaniciadi.getText().toString());
                degerler.put("sifre",ed_parola.getText().toString());
                degerler.put("adsoyad",ed_adsoyad.getText().toString());
                degerler.put("mail",ed_email.getText().toString());

                return degerler;
            }
        };

        requestQueue.add(request);
    }//FUNC

    public void TamEkranAnimasyon()
    {
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager wm = window.getWindowManager();
        Display ekran = wm.getDefaultDisplay();

        Point point = new Point();
        ekran.getSize(point);

        int genislik = point.x;
        int yukseklik = point.y;

        imageKayit.getLayoutParams().width = (int) (yukseklik * 1.78);
        imageKayit.getLayoutParams().height = yukseklik;

         animator =  ObjectAnimator.ofFloat(imageKayit,"x",0,-(yukseklik*1.78f - genislik),0,
                -(yukseklik*1.78f - genislik));
        animator.setDuration(210000);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }//func

    private boolean  internetBaglantisiKontrol()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null &&  networkInfo.isConnected())
        {
            return true;
        }else {
            return false;
        }
    }//FUNC

    @Override
    protected void onPause() {
        super.onPause();

        if(animator != null)
            animator.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(animator.isPaused())
            animator.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(animator != null)
            animator.cancel();
    }
}
