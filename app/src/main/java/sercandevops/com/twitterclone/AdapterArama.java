package sercandevops.com.twitterclone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import sercandevops.com.twitterclone.Models.TweetModel;

public class AdapterArama extends BaseAdapter {

    List<TweetModel> modelist;
    Context context;
    Activity activity;

    public AdapterArama(List<TweetModel> modelist, Context context,Activity activity) {
        this.modelist = modelist;
        this.context = context;
        this.activity = activity;

    }

    @Override
    public int getCount() {
        if (modelist.size() == 0)
        return 0;

        return modelist.size();
    }

    @Override
    public Object getItem(int position) {
        if (modelist.size() == 0)
            return null;

        return modelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {

        if(modelist.size() == 0)
            return null;

        final LinearLayout layout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.kisi_listesi_item,parent,false);

        final TextView adsoyad = (TextView)layout.findViewById(R.id.tv_aramaAdsoyad);
        final TextView kullaniciadi = layout.findViewById(R.id.tv_aramakullaniciAdi);
        TextView mail = layout.findViewById(R.id.tv_aramaMail);
        final ImageView profilImage = layout.findViewById(R.id.imageview_arama);

        final TweetModel kisi = modelist.get(position);

        adsoyad.setText(kisi.getAdSoyadi());
        kullaniciadi.setText(kisi.getKullaniciAdi());
        mail.setText(kisi.getMail());


        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context,kisi.getMail(),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context,KisiTweetlerActivity.class);
                intent.putExtra("id",kisi.getId());

                intent.putExtra("path",kisi.getProfilPath());
                intent.putExtra("kullaniciadi",kisi.getKullaniciAdi());
                intent.putExtra("adsoyad",kisi.getAdSoyadi());
                intent.putExtra("mail",kisi.getMail());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                        View vprofil = profilImage;
                        View vadsoyad = adsoyad;
                        View vkullaniciAdi = kullaniciadi;

                    Pair<View,String> pairProfileFoto = Pair.create(vprofil,"profilImage");
                    Pair<View,String> pairAdsoyad = Pair.create(vadsoyad,"adsoyad");
                    Pair<View,String> pairKullaniciadi = Pair.create(vkullaniciAdi,"kullaniciadi");

                    AppCompatActivity ata = (AppCompatActivity)context;

                ActivityOptionsCompat optionsCompat = (ActivityOptionsCompat)
                        ActivityOptionsCompat.makeSceneTransitionAnimation(activity,pairProfileFoto,pairAdsoyad,pairKullaniciadi);

                    context.startActivity(intent,optionsCompat.toBundle());
                }else{
                    context.startActivity(intent);
                }
            }
        });

        return layout;
    }
}
