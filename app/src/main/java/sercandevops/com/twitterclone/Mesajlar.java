package sercandevops.com.twitterclone;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Mesajlar extends Fragment {


    public Mesajlar() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        TextView view = new TextView(getContext());
        view.setText("Mesajlar fragmenti");
        view.setTextSize(30);
        view.setGravity(Gravity.CENTER);


        return view;
    }

}
