package com.kda.kdatalk.ui.main;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kda.kdatalk.R;
import com.kda.kdatalk.ui.main.learn.fragment.LearnFragment;
import com.kda.kdatalk.ui.main.message.fragment.MessageFragment;
import com.kda.kdatalk.ui.main.newfeed.fragment.FragmentNewFeed;
import com.kda.kdatalk.ui.main.profile.ProfileFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.nav_bar)
    BottomNavigationView nav_bar;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        nav_bar.setItemIconTintList(null);
        nav_bar.setOnNavigationItemSelectedListener(this);
        setUpViewPager();
    }

    private void setUpViewPager() {

        MainPagerAdapter viewPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        Fragment newfeed_fragment = FragmentNewFeed.newInstance();
        Fragment mess_fragment = MessageFragment.newInstance();
        Fragment search_fragment = MessageFragment.newInstance();
        Fragment learn_fragment = LearnFragment.newInstance();
        Fragment profile_fragment = ProfileFragment.newInstance();

        viewPagerAdapter.addFragment(newfeed_fragment);
        viewPagerAdapter.addFragment(mess_fragment);
        viewPagerAdapter.addFragment(search_fragment);
        viewPagerAdapter.addFragment(learn_fragment);
        viewPagerAdapter.addFragment(profile_fragment);


//        viewPagerAdapter.addFragment(newfeed_fragment); // newfeed
//        newfeed_fragment = MessageFragment.newInstance();
//        viewPagerAdapter.addFragment(newfeed_fragment);// message
//        newfeed_fragment = MessageFragment.newInstance();
//        viewPagerAdapter.addFragment(newfeed_fragment); // search
//        newfeed_fragment = MessageFragment.newInstance();
//        viewPagerAdapter.addFragment(newfeed_fragment); // learn
//        newfeed_fragment = ProfileFragment.newInstance();
//        viewPagerAdapter.addFragment(newfeed_fragment);

        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    nav_bar.getMenu().getItem(0).setChecked(false);
                }
                nav_bar.getMenu().getItem(position).setChecked(true);
                prevMenuItem = nav_bar.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.item_newfeed:
                viewPager.setCurrentItem(0);
                break;
            case R.id.item_message:
                viewPager.setCurrentItem(1);
                break;

            case R.id.item_search:
                viewPager.setCurrentItem(2);
                break;

            case R.id.item_learn:
                viewPager.setCurrentItem(3);
                break;

            case R.id.item_profile:
                viewPager.setCurrentItem(4);
                break;

            default:
                break;
        }
        return false;
    }
}
