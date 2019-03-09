package sercandevops.com.twitterclone;

import android.app.ProgressDialog;
import android.media.Image;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import sercandevops.com.twitterclone.Models.TweetModel;

public class KisiTweetlerActivity extends AppCompatActivity {


    private TextView adsoyad,kullaniciadi,mail;
    private RecyclerView listViewKisiTweeler;
    private int id;
    private RequestQueue requestQueue;
    private ImageView profil_image_kisi;
    private List<TweetModel> modelList;
    MyAdapter adapter;
    private String URL = "http://10.0.2.2/twitterclone/getTweetler.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kisi_tweetler);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        adsoyad = findViewById(R.id.tv_kisiAdsoyad);
        kullaniciadi = findViewById(R.id.tv_kisikullaniciAdi);
        mail = findViewById(R.id.tv_kisiMail);
        listViewKisiTweeler = findViewById(R.id.listViewKisiTweeler);
        profil_image_kisi = findViewById(R.id.profil_image_kisi);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        listViewKisiTweeler.setLayoutManager(linearLayoutManager);
        listViewKisiTweeler.setHasFixedSize(true);
        listViewKisiTweeler.setItemAnimator(new DefaultItemAnimator());

        modelList = new ArrayList<>();

        Bundle argumnts = getIntent().getExtras();

        String spath = argumnts.getString("path","");
        String sadsoyad = argumnts.getString("adsoyad","");
        String skullaniciadi = argumnts.getString("kullaniciadi","");
        String smail = argumnts.getString("mail","");

        id = argumnts.getInt("id",-1);

        if(!spath.equals(""))
            Picasso.get().load(spath).into(profil_image_kisi);


            istekGonder();

    }
    private void istekGonder()
    {
      //  final ProgressDialog loading = ProgressDialog.show(getApplicationContext(),"tweetler yükleniyor...","");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

              //  loading.dismiss();

                Log.e("json bilgisi tweetler",response);

                String durum = null;
                String mesaj = null;
                JSONArray tweetler = null;
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    durum = jsonObject.getString("status");
                    mesaj = jsonObject.getString("mesaj");
                    tweetler = jsonObject.getJSONArray("tweetler");
                }catch (JSONException e)
                {
                    Log.e("PARCALARKEN SORUN OLDU ",e.getLocalizedMessage());
                }

                    if(durum.equals("200"))
                    {
                        if(tweetler.length() == 0)
                        {

                        }else
                        {
                        for(int i=0;i<tweetler.length();i++)
                        {
                            JSONObject tweet;
                            TweetModel model = new TweetModel();
                            try
                            {
                            tweet = tweetler.getJSONObject(i);
                                model.setAdSoyadi(tweet.getString("adsoyad"));
                                model.setKullaniciAdi(tweet.getString("kullaniciadi"));
                                model.setProfilPath(tweet.getString("avatar"));
                                model.setResimPath(tweet.getString("path1"));
                                model.setTweetText(tweet.getString("text1"));
                                model.setTarih(tweet.getString("date"));
                                model.setUuid(tweet.getString("uuid"));
                            }catch (JSONException e)
                            {
                                Log.e("KISI TWEETLER ACITIVTY ",e.getLocalizedMessage());
                            }

                            modelList.add(model);
                        }//for

                            if(modelList != null)
                                setAdapter();

                    }
                }else{
                    Snackbar.make(listViewKisiTweeler,mesaj,Snackbar.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // loading.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String > param = new HashMap<>();
                param.put("id",""+id);
                return param;
            }
        };

        requestQueue.add(stringRequest);
    }//funmc
    public void setAdapter()
    {
        Toast.makeText(getApplicationContext(),"MODEL YUKLENIYO",Toast.LENGTH_SHORT).show();
        adapter = new MyAdapter(modelList,KisiTweetlerActivity.this,false);
        listViewKisiTweeler.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }
}
