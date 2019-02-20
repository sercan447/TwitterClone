package sercandevops.com.twitterclone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter/* extends FragmentPagerAdapter */{
/*
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitlelist = new ArrayList<>();
    private String id;

    public ViewPagerAdapter(FragmentManager fm,String id) {
        super(fm);
        this.id = id;
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
*/

}
