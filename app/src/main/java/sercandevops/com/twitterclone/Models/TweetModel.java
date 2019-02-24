package sercandevops.com.twitterclone.Models;

import android.widget.ImageView;

public class TweetModel {
    private String adSoyadi,kullaniciAdi,profilPath,resimPath,tweetText;
    private String tarih;

    public TweetModel() {
    }

    public TweetModel(String adSoyadi, String kullaniciAdi, String profilPath, String resimPath, String tweetText, String tarih) {
        this.adSoyadi = adSoyadi;
        this.kullaniciAdi = kullaniciAdi;
        this.profilPath = profilPath;
        this.resimPath = resimPath;
        this.tweetText = tweetText;
        this.tarih = tarih;
    }


    public String getAdSoyadi() {
        return adSoyadi;
    }

    public void setAdSoyadi(String adSoyadi) {
        this.adSoyadi = adSoyadi;
    }

    public String getKullaniciAdi() {
        return kullaniciAdi;
    }

    public void setKullaniciAdi(String kullaniciAdi) {
        this.kullaniciAdi = kullaniciAdi;
    }

    public String getProfilPath() {
        return profilPath;
    }

    public void setProfilPath(String profilPath) {
        this.profilPath = profilPath;
    }

    public String getResimPath() {
        return resimPath;
    }

    public void setResimPath(String resimPath) {
        this.resimPath = resimPath;
    }

    public String getTweetText() {
        return tweetText;
    }

    public void setTweetText(String tweetText) {
        this.tweetText = tweetText;
    }

    public String getTarih() {
        return tarih;
    }

    public void setTarih(String tarih) {
        this.tarih = tarih;
    }
}
