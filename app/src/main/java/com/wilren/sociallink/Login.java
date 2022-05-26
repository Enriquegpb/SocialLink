package com.wilren.sociallink;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.wilren.sociallink.Fragments.LoginAdapter;

public class Login extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager;
    TextView logAppText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        logAppText = findViewById(R.id.SLtext);
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.login_animation);
        logAppText.startAnimation(myanim);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager.setAdapter(new LoginAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Login");
                    break;
                case 1:
                    tab.setText("Signup");
                    break;
            }
        }).attach();

    }


}