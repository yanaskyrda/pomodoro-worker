package com.diploma;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.diploma.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements TabLayoutMediator.TabConfigurationStrategy {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private final List<String> tabsTitles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager2 = findViewById(R.id.viewPager);
        setViewPagerAdapter();
        //set default page Main
        viewPager2.setCurrentItem(1);

        tabLayout = findViewById(R.id.tabLayout);

        new TabLayoutMediator(tabLayout, viewPager2, this).attach();
    }

    public void setViewPagerAdapter() {
        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(this);
        List<Fragment> fragmentsList = new ArrayList<>();
        fragmentsList.add(new VideoTabFragment());
        fragmentsList.add(new MainTabFragment());
        fragmentsList.add(new MusicTabFragment());
        pagerAdapter.setTabFragments(fragmentsList);
        tabsTitles.addAll(pagerAdapter.getTabTitles());
        this.viewPager2.setAdapter(pagerAdapter);
    }

    @Override
    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
        tab.setText(tabsTitles.get(position));
    }
}
