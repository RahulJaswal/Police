package com.example.dharam.police;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class CarFragment extends Fragment
{
    TabLayout tabLayout;
    ViewPager viewPager;


    public CarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.fragment_car, container, false);
        //initializations
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tablayout);

        swipetab();


        return view;
    }

    //method definitions from here
    private void swipetab()
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        tabLayout.addTab(tabLayout.newTab().setText("Black Listed Cars"));
        tabLayout.addTab(tabLayout.newTab().setText("White Listed Cars"));

        viewPager.setAdapter(new MyAdapter(fragmentManager));
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


}

//MyAdapter for swipe tabs
class MyAdapter extends FragmentPagerAdapter
{

    public MyAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 1) {
            fragment = new WhiteListed();
        }
        if (position == 0) {
            fragment = new BlackListed();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
