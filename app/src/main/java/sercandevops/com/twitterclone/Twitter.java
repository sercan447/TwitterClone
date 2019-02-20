package sercandevops.com.twitterclone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.gesture.Prediction;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Twitter extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    private static final String URL_PROFIL_BILGILERI = "http://10.0.2.2/twitterclone/profilBilgileri.php";
    Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons={R.drawable.ic_home,R.drawable.ic_notifications,R.drawable.ic_mesaj};
    RequestQueue requestQueue;
    SharedPreferences sharedPreferences;

    CircleImageView profile_image;
    TextView eposta,adsoyad;

    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_tweeet_at_butonu);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                startActivity(new Intent(Twitter.this,TweetGonder.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        //NAV HEADER ICERISINDEK NESNELERE BOYLE ULAŞTIRIYORSUNUZ..
        LinearLayout layout = (LinearLayout) navigationView.getHeaderView(0);

         adsoyad = layout.findViewById(R.id.tv_adsoyad_nav);
         eposta = layout.findViewById(R.id.tv_email_nav);
         profile_image = layout.findViewById(R.id.img_profile_image);

         id = sharedPreferences.getString("id","-1");

        setProfilBilgileri(id);

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                toolbar.setTitle(tab.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(sharedPreferences.getBoolean("ProfilChanged",false))
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("ProfilChanged",false);
            editor.commit();
            setProfilBilgileri(sharedPreferences.getString("id","-1"));
        }

    }

    private void setupTabIcons()
    {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }
    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFlag(new Anasayfa(),"Anasayfa");
        adapter.addFlag(new Bildirimler(),"Bildirimler");
        adapter.addFlag(new Mesajlar(),"Mesajlar");

        viewPager.setAdapter(adapter);
    }



    private void setProfilBilgileri(final String id)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PROFIL_BILGILERI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("Json verisi : ",response);

                String durum = "",mesaj="",adsoyad="",avatar="",mail="";

                try{
                    JSONObject jsonObject = new JSONObject(response);
                    durum = jsonObject.getString("status");
                    mesaj = jsonObject.getString("mesaj");
                    adsoyad = jsonObject.getString("adsoyad");
                    avatar = jsonObject.getString("avatar");
                    mail = jsonObject.getString("mail");


                }catch (Exception e)
                {
                    Log.e("JSON BILGILER HATA :",e.getLocalizedMessage());
                }


                if(durum.equals("200")) {
                    setProfil(mail,adsoyad,avatar);
                }else{
                    Toast.makeText(getApplicationContext(),mesaj,Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("JSON BILGILER HATA 2 :",error.getLocalizedMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> degerler = new HashMap<>();
                degerler.put("id",id);
                return degerler;
            }
        };

        requestQueue.add(stringRequest);
    }//func

    private void setProfil(String stadsoyad,String mail,String avatar)
    {
        eposta.setText(mail);
        adsoyad.setText(stadsoyad);

        Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
        Picasso pic = builder.build();
        pic.load(avatar).into(profile_image);
       // Picasso.get().load(avatar).resize(70, 70).centerCrop().into(profile_image);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.twitter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu_profile) {
            // profil activitye geçiş
            Intent intent = new Intent(Twitter.this,ProfilActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_cikis) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("benihatirla",false);
                editor.putString("id","-1");
                editor.commit();

            Intent intent = new Intent(Twitter.this,GirisEkrani.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitlelist = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            if (i == 0) {
                Fragment fragment = mFragmentList.get(i);
                Bundle bundle = new Bundle();
                bundle.putString("id",id);
                fragment.setArguments(bundle);
                return fragment;
            }
            return mFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitlelist.get(position);
        }

        public void addFlag(Fragment fragment, String title)
        {
            mFragmentList.add(fragment);
            mFragmentTitlelist.add(title);
        }


    }//class
}
