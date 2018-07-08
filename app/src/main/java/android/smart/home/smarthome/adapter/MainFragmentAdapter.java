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
 * Created by Administrator on 2017/11/13.
 *
 */

public class MainFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private List<String> titles;
    private int[] imageResId;
    private int[] imageResIdSelected;
    private Context context;

    public MainFragmentAdapter(FragmentManager fm, Context context, List<Fragment> fragments,
                               List<String> titles, int[] imageResId, int[] imageResIdSelected) {
        super(fm);
        this.context=context;
        this.fragments=fragments;
        this.titles=titles;
        this.imageResId=imageResId;
        this.imageResIdSelected=imageResIdSelected;
    }

    public View getTabView(int position,boolean selected){
        View view= LayoutInflater.from(context).inflate(R.layout.custom_tab,null);
        TextView textView = (TextView) view.findViewById(R.id.tv_tab);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_tab);
        textView.setText(titles.get(position));
        if(selected){
            textView.setTextColor(context.getResources().getColor(R.color.deepskyblue));
            imageView.setImageResource(imageResIdSelected[position]);
        }else{
            textView.setTextColor(context.getResources().getColor(R.color.gray));
            imageView.setImageResource(imageResId[position]);
        }
        return view;
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
