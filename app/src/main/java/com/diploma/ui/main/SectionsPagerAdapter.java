package com.diploma.ui.main;

import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.diploma.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SectionsPagerAdapter extends FragmentStateAdapter {

    private List<Fragment> tabFragments;
    private final List<String> tabTitles = new ArrayList<>();

    public SectionsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return tabFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return tabFragments.size();
    }

    public void setTabFragments(List<Fragment> tabFragments) {
        this.tabFragments = tabFragments;
        tabTitles.clear();
        if (tabFragments != null && !tabFragments.isEmpty()) {
            tabTitles.addAll(Arrays.asList(Resources.getSystem().getStringArray(R.array.tab_titles)));
        }
    }

    public List<String> getTabTitles() {
        return tabTitles;
    }
}