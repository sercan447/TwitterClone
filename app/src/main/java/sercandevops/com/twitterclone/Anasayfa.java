package sercandevops.com.twitterclone;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;
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
        recyclerView = (RecyclerView) ((AppCompatActivity)context).findViewById(R.id.listTweet);
        textView = (TextView) ((AppCompatActivity)context).findViewById(R.id.textView3);
        modelList = new ArrayList<>();
        id = getArguments().getString("id");

        Log.e("IDLER",""+id);
        istekGonder();
    }

    private void istekGonder()
    {
        final ProgressDialog progressDialog = ProgressDialog.show(context,"Tweetler guncelleniyor..","lütfen bekleyiniz.",false,false);
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                Log.e("ANASAYFA JSON :",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                Log.e("ANASAYFA JSONerror :",error.getLocalizedMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> map = new HashMap<>();
                    map.put("id",id);

                return map;
            }
        };
        requestQueue.add(request);
    }//func
}
