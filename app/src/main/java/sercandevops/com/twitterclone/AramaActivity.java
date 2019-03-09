package sercandevops.com.twitterclone;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sercandevops.com.twitterclone.Models.TweetModel;

public class AramaActivity extends AppCompatActivity {

    private MaterialSearchView searchView;
    private ListView listView;
    private AdapterArama adapter;
    private String id;

    private static final String URL =   "http://10.0.2.2/twitterclone/selectusers.php";
    private RequestQueue requestQueue;
    private List<String> tavsiyeler;
    private List<TweetModel> modelList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arama);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarArama);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        listView = (ListView)findViewById(R.id.listviewarama);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setHint("Kişiler de arayın");

        tavsiyeler = new ArrayList<>();
        modelList = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AramaActivity.this);
        id = preferences.getString("id","-1");

        istekGonder("");

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Snackbar.make(findViewById(R.id.container),"ontextquery submit",Snackbar.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Snackbar.make(findViewById(R.id.toolbar_container),"onquerytext change",Snackbar.LENGTH_LONG).show();
                istekGonder(newText);
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

            }
        });

    }

    public void istekGonder(final String newText)
    {

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("json verisi armaa",response);
                tavsiyeler.clear();
                modelList.clear();

                String durum = null,mesaj = null;
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    durum = jsonObject.getString("status");
                    mesaj = jsonObject.getString("mesaj");
                }catch (JSONException e)
                {
                    e.printStackTrace();
                }

                JSONArray kullanicilar = null;
                if(durum.equals("200"))
                {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        kullanicilar = jsonObject.getJSONArray("kullanicilar");

                        Log.e("verıler geliyor",kullanicilar.toString());

                    }catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                    for (int i=0;   i < kullanicilar.length(); i++)
                    {
                        try
                        {
                            JSONObject kisi = kullanicilar.getJSONObject(i);
                            TweetModel model = new TweetModel();

                            tavsiyeler.add(kisi.getString("adsoyad"));
                            tavsiyeler.add(kisi.getString("kullaniciadi"));

                            model.setAdSoyadi(kisi.getString("adsoyad"));
                            model.setKullaniciAdi(kisi.getString("kullaniciadi"));
                            model.setProfilPath(kisi.getString("avatar"));
                            model.setMail(kisi.getString("mail"));
                            model.setId(kisi.getInt("id"));

                            modelList.add(model);
                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }//dongu sonu
                }else
                {
                    Snackbar.make(findViewById(R.id.container),mesaj,Snackbar.LENGTH_LONG).show();
                }
                String[] tavsiyelerArray = new String[tavsiyeler.size()];
                for (int i=0;i<tavsiyeler.size();i++)
                {
                    String s = tavsiyeler.get(i);
                    tavsiyelerArray[i] = s;
                }
                //searview.setSuggestions(tavsiyelerArray);

                adapter = new AdapterArama(modelList,getApplicationContext(),AramaActivity.this);
                listView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",id);
                params.put("word",newText);

                return params;
            }
        };
        requestQueue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.twitter,menu);
        MenuItem searchItem = menu.findItem(R.id.tweetkisi_arama);

        searchView.setMenuItem(searchItem);

        return true;
    }

    @Override
    public void onBackPressed() {

        if(searchView.isSearchOpen())
        {
            searchView.closeSearch();
        }else
        {
            super.onBackPressed();
        }

    }
}
