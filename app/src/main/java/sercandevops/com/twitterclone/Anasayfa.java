package sercandevops.com.twitterclone;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sercandevops.com.twitterclone.Models.TweetModel;


public class Anasayfa extends Fragment {

    private String id;
    private Context context;
    private List<TweetModel> modelList;
    private RecyclerView recyclerView;
    private TextView textView;
    private String URL = "http://10.0.2.2/twitterclone/getTweetler.php";
    private RequestQueue requestQueue;
    private SwipeRefreshLayout swipeRefreshLayout;

    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    public Anasayfa() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_anasayfa, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




        requestQueue = Volley.newRequestQueue(getContext());
        recyclerView = (RecyclerView) ((AppCompatActivity) context).findViewById(R.id.listTweet);
        textView = (TextView) ((AppCompatActivity) context).findViewById(R.id.textView3);
        swipeRefreshLayout = (SwipeRefreshLayout) ((AppCompatActivity) context).findViewById(R.id.refresylayout);


        modelList = new ArrayList<>();
        id = getArguments().getString("id");

        Log.e("IDLER", "" + id);
        istekGonder();

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                modelList.clear();
                istekGonderResfresh();
            }
        });//cls
    }

    private void istekGonder() {
        final ProgressDialog progressDialog = ProgressDialog.show(context, "Tweetler guncelleniyor..", "lütfen bekleyiniz.", false, false);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                Log.e("ANASAYFA JSON :", response);

                String durum = null, mesaj = null, date = null;
                JSONArray tweetler = null;

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    durum = jsonObject.getString("status");
                    mesaj = jsonObject.getString("mesaj");
                    tweetler = jsonObject.getJSONArray("tweetler");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (durum.equals("200")) {
                    for (int i = 0; i < tweetler.length(); i++) {
                        JSONObject tweet;
                        TweetModel model = new TweetModel();
                        try {
                            tweet = tweetler.getJSONObject(i);
                            model.setAdSoyadi(tweet.getString("adsoyad"));
                            model.setKullaniciAdi(tweet.getString("kullaniciadi"));
                            model.setProfilPath(tweet.getString("avatar"));
                            model.setResimPath(tweet.getString("path1"));
                            model.setTweetText(tweet.getString("text1"));
                            model.setTarih(tweet.getString("date"));
                            model.setUuid(tweet.getString("uuid"));


                        } catch (JSONException e) {
                            Log.e("json parse hatası :", e.getLocalizedMessage());
                        }

                        modelList.add(model);
                    }//for
                } else {
                    Snackbar.make(recyclerView, mesaj, Snackbar.LENGTH_LONG).show();
                }
                if (modelList == null)
                    textView.setText("HİÇ BİR TWEET bulunamadı.");
                else
                    setAdapter();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                Log.e("ANASAYFA JSONerror :", error.getLocalizedMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("id", id);

                return map;
            }
        };
        requestQueue.add(request);
    }//func


    private void setAdapter() {

        mAdapter = new MyAdapter(modelList,getActivity(),true);
        recyclerView.setAdapter(mAdapter);
    }//func

    private void istekGonderResfresh() {

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                swipeRefreshLayout.setRefreshing(false);

                Log.e("ANASAYFA JSON :", response);

                String durum = null, mesaj = null, date = null;
                JSONArray tweetler = null;

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    durum = jsonObject.getString("status");
                    mesaj = jsonObject.getString("mesaj");
                    tweetler = jsonObject.getJSONArray("tweetler");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (durum.equals("200")) {
                    for (int i = 0; i < tweetler.length(); i++) {
                        JSONObject tweet;
                        TweetModel model = new TweetModel();
                        try {
                            tweet = tweetler.getJSONObject(i);
                            model.setAdSoyadi(tweet.getString("adsoyad"));
                            model.setKullaniciAdi(tweet.getString("kullaniciadi"));
                            model.setProfilPath(tweet.getString("avatar"));
                            model.setResimPath(tweet.getString("path1"));
                            model.setTweetText(tweet.getString("text1"));
                            model.setTarih(tweet.getString("date"));

                        } catch (JSONException e) {
                            Log.e("json parse hatası :", e.getLocalizedMessage());
                        }

                        modelList.add(model);
                    }//for
                } else {
                    Snackbar.make(recyclerView, mesaj, Snackbar.LENGTH_LONG).show();
                }
                if (modelList == null) {
                    textView.setText("HİÇ BİR TWEET bulunamadı.");
                    setAdapter();
                } else {
                    setAdapter();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ANASAYFA JSONerror :", error.getLocalizedMessage());

                swipeRefreshLayout.setRefreshing(false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> map = new HashMap<>();
                map.put("id", id);

                return map;
            }
        };
        requestQueue.add(request);
    }//func
}
