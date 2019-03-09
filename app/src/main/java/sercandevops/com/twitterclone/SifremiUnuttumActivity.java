package sercandevops.com.twitterclone;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

public class SifremiUnuttumActivity extends AppCompatActivity {


    private TextInputLayout til_mail;
    private TextInputEditText ed_mail;
    private final String URL = "http://10.0.2.2/twitterclone/sifreyiSifirla.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifremi_unuttum);


        til_mail =  (TextInputLayout) findViewById(R.id.til_emailSifremiUnuttum);
        ed_mail = (TextInputEditText) findViewById(R.id.ed_emailsifremiUnuttum);



        findViewById(R.id.fab_sifemriunuttum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                til_mail.setError(null);

                if(ed_mail.getText().toString().trim().length() == 0)
                {
                    til_mail.setError("lütfen mail adresinizi giriniz.");

                } else if(!ed_mail.getText().toString().contains("@"))
                {
                til_mail.setError("geçerli bir mail adresi giriniz.");
                }else
                {
                    istekGonder(ed_mail.getText().toString());
                }
            }
        });

    }

    public void istekGonder(final String email)
    {
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("json verisi",response);

                JSONObject jsonObject = null;
                String durum = null;
                String mesaj = null;

                try{

                    jsonObject = new JSONObject(response);
                    durum = jsonObject.getString("status");
                    mesaj = jsonObject.getString("mesaj");


                }catch (JSONException e)
                {
                    Log.e("json parse hatası ",e.getLocalizedMessage());
                }

                if(durum.equals("200"))
                {
                    new AlertDialog.Builder(SifremiUnuttumActivity.this)
                            .setMessage("Şifrenizi yenilemeniz için size bir adet mail gönderildi. Lütfen e-postanıze bakınız")
                            .show();
                }else
                {
                    Snackbar.make(findViewById(R.id.root_sifremiunuttum),mesaj,Snackbar.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> degerler = new HashMap<>();
                degerler.put("mail",email);

                return degerler;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(SifremiUnuttumActivity.this);
        requestQueue.add(request);

    }//gunc
}
