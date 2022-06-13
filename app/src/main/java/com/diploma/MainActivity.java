package com.diploma;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.diploma.database.DatabaseHandler;
import com.diploma.timer.DistractionService;
import com.diploma.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTubeScopes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements TabLayoutMediator.TabConfigurationStrategy {

    private ViewPager2 viewPager2;
    private final List<String> tabsTitles = new ArrayList<>();
    private static final int MAIN_PAGE_INDEX = 1;
    private SensorManager sensorManager;
    private DistractionService distractionService;
    private DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHandler = DatabaseHandler.getInstance(this);

        viewPager2 = findViewById(R.id.viewPager);
        setViewPagerAdapter();
        viewPager2.setCurrentItem(MAIN_PAGE_INDEX);

        TabLayout tabLayout = findViewById(R.id.tabLayout);

        new TabLayoutMediator(tabLayout, viewPager2, this).attach();

        initializeSensorManager();
        //to clear all saved data programmatically
//        deleteDatabase("PomodoroAppSettings");
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

    public void initializeSensorManager() {
        if (sensorManager == null) {
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            distractionService = DistractionService.getInstance(sensorManager);
        }
    }
}
