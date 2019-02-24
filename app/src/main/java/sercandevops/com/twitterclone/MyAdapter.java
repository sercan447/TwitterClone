package sercandevops.com.twitterclone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import sercandevops.com.twitterclone.Models.TweetModel;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<TweetModel> modelList;
    private Context context;
    public MyAdapter(List<TweetModel> modelList,Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LinearLayout rootView = (LinearLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tweet_list_item,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(rootView);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

            TweetModel tweet = modelList.get(i);

        viewHolder.adsoyad.setText(tweet.getAdSoyadi());
       viewHolder.texttv.setText(tweet.getTweetText());
        viewHolder.kullaniciAdi.setText(tweet.getKullaniciAdi());

        Picasso.Builder picasso = new Picasso.Builder(context);
        Picasso pc = picasso.build();
        if(!tweet.getProfilPath().equals(""))
        {
            pc.load(tweet.getProfilPath()).into(viewHolder.circleImageView);
        }

      if (!tweet.getResimPath().equals(""))
        {
            pc.load(tweet.getResimPath()).into(viewHolder.imImageview);
        }
        Date simdi = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date tarih = null;

        try{
            tarih = df.parse(tweet.getTarih());
        }catch (ParseException e)
        {
            e.printStackTrace();
        }
        int fark = (int) (simdi.getTime() - tarih.getTime());
        int gun = fark/(1000*60*60*24);
        int saat = fark / (1000*60*60);
        int dakika = fark /(1000*60);
        int saniye = fark/(1000);

        if(saniye == 0)
                viewHolder.tarihtv.setText("şimdi");

        if(saniye>0 && dakika == 0)
            viewHolder.tarihtv.setText(saniye+" s");

        if(dakika > 0 && saat == 0)
            viewHolder.tarihtv.setText(dakika+ "dk");

        if(saat > 0 && gun == 0)
            viewHolder.tarihtv.setText(saat + " sa");

        if(gun > 0)
            viewHolder.tarihtv.setText(gun + " gun");



    }

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            adsoyad = itemView.findViewById(R.id.tv_adsoyad_tweet);
            kullaniciAdi = itemView.findViewById(R.id.tv_nick_tweet);
            tarihtv = itemView.findViewById(R.id.tv_tarih);
            circleImageView = itemView.findViewById(R.id.profile_image_tweet);
            imImageview = itemView.findViewById(R.id.imageview);
            texttv = itemView.findViewById(R.id.tv_tweet);
        }

    }//classs
}
