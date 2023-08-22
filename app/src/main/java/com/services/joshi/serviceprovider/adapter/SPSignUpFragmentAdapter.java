package com.services.joshi.serviceprovider.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.services.joshi.serviceprovider.activities.serviceprovider.spsignupfragments.SPSignUpFragmentFive;
import com.services.joshi.serviceprovider.activities.serviceprovider.spsignupfragments.SPSignUpFragmentFour;
import com.services.joshi.serviceprovider.activities.serviceprovider.spsignupfragments.SPSignUpFragmentOne;
import com.services.joshi.serviceprovider.activities.serviceprovider.spsignupfragments.SPSignUpFragmentThree;
import com.services.joshi.serviceprovider.activities.serviceprovider.spsignupfragments.SPSignUpFragmentTwo;

public class SPSignUpFragmentAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 5;

    public SPSignUpFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new SPSignUpFragmentOne();
                return fragment;
            case 1:
                fragment = new SPSignUpFragmentTwo();
                return fragment;
            case 2:
                fragment = new SPSignUpFragmentThree();
                return fragment;
            case 3:
                fragment = new SPSignUpFragmentFour();
                return fragment;
            case 4:
                fragment = new SPSignUpFragmentFive();
                return fragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
