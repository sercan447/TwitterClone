package sercandevops.com.twitterclone.Models;

import android.widget.ImageView;

public class TweetModel {

    private int Id;
    private String adSoyadi,kullaniciAdi,profilPath,resimPath,tweetText;
    private String tarih;
    private String uuid;
    private String mail;

    public TweetModel() {
    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
