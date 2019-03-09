package sercandevops.com.twitterclone;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import sercandevops.com.twitterclone.Models.TweetModel;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private  List<TweetModel> modelList;
    private Context context;
    private final String  URL = "http://10.0.2.2/twitterclone/tweetSil.php";
    private boolean silmeislemi;

    public MyAdapter(List<TweetModel> modelList,Context context,boolean silmeislemi) {
        this.modelList = modelList;
        this.context = context;
        this.silmeislemi = silmeislemi;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LinearLayout rootView = (LinearLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tweet_list_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(rootView);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        final TweetModel tweet = modelList.get(i);

        viewHolder.adsoyad.setText(tweet.getAdSoyadi());
        viewHolder.texttv.setText(tweet.getTweetText());
        viewHolder.kullaniciAdi.setText(tweet.getKullaniciAdi());

        Picasso.Builder picasso = new Picasso.Builder(context);
        Picasso pc = picasso.build();
       if (!tweet.getProfilPath().equals("")) {
           pc.load(tweet.getProfilPath()).into(viewHolder.circleImageView);
       }

        if (!tweet.getResimPath().equals("")) {
            pc.load(tweet.getResimPath()).into(viewHolder.imImageview);
        }
        Date simdi = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date tarih = null;

        try {
            tarih = df.parse(tweet.getTarih());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int fark = (int) (simdi.getTime() - tarih.getTime());
        int gun = fark / (1000 * 60 * 60 * 24);
        int saat = fark / (1000 * 60 * 60);
        int dakika = fark / (1000 * 60);
        int saniye = fark / (1000);

        if (saniye == 0)
            viewHolder.tarihtv.setText("şimdi");

        if (saniye > 0 && dakika == 0)
            viewHolder.tarihtv.setText(saniye + " s");

        if (dakika > 0 && saat == 0)
            viewHolder.tarihtv.setText(dakika + "dk");

        if (saat > 0 && gun == 0)
            viewHolder.tarihtv.setText(saat + " sa");

        if (gun > 0)
            viewHolder.tarihtv.setText(gun + " gun");

        if(silmeislemi)
        {
        viewHolder.linearlayout_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {

                viewHolder.linearlayout_item.setAlpha(0.5f);

                new AlertDialog.Builder(context)
                        .setTitle("Tweeet sil")
                        .setMessage("tweeti silmek istediginizden eminmisiniz.?")
                        .setPositiveButton("tamam", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                istekGonderSil(i, tweet.getUuid(), viewHolder.linearlayout_item);
                            }
                        })
                        .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                viewHolder.linearlayout_item.setAlpha(1);
                            }
                        }).show();
                return true;
            }
        });
        }
    }

    public void istekGonderSil(final int i,final String uuid,final View layout)
    {
       // Toast.makeText(context,"oglum",Toast.LENGTH_SHORT).show();
        final ProgressDialog loading = ProgressDialog.show(context,"tweet siliniyor.","Lütfen bekleyiniz.");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                Log.e("Silme işlemi",response);

                String durum = null,mesaj = null;
                try
                {
                    JSONObject  jsonObject = new JSONObject(response);
                    durum = jsonObject.getString("status");
                    mesaj = jsonObject.getString("mesaj");

                }catch (JSONException e)
                {
                e.printStackTrace();
                }

                if(durum.equals("200"))
                {
                    modelList.remove(i);
                    Toast.makeText(context,mesaj,Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }else {
                   Toast.makeText(context,mesaj,Toast.LENGTH_SHORT).show();
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
                params.put("uuid",uuid);

                if(!modelList.get(i).getResimPath().equals(""))
                {
                    params.put("path",modelList.get(i).getResimPath());
                }

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);

    }//FUNC

    @Override
    public int getItemCount() {
        if (modelList == null)
        return 0;

        return modelList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView adsoyad,kullaniciAdi,tarihtv,texttv;
        public CircleImageView circleImageView;
        public ImageView imImageview;
        private LinearLayout linearlayout_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            adsoyad = itemView.findViewById(R.id.tv_adsoyad_tweet);
            kullaniciAdi = itemView.findViewById(R.id.tv_nick_tweet);
            tarihtv = itemView.findViewById(R.id.tv_tarih);
            circleImageView = itemView.findViewById(R.id.profile_image_tweet);
            imImageview = itemView.findViewById(R.id.imageview);
            texttv = itemView.findViewById(R.id.tv_tweet);
            linearlayout_item = itemView.findViewById(R.id.linearlayout_item);
        }

    }//classs
}
