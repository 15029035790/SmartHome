package android.smart.home.smarthome.adapter;

import android.content.Context;
import android.smart.home.smarthome.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017/11/11.
 *
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private List<String> titles;
    private Context context;

    public FragmentAdapter(FragmentManager fm,List<Fragment> fragments, List<String> titles) {
        super(fm);
        this.fragments=fragments;
        this.titles=titles;
    }


    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

}
