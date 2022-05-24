package com.wilren.sociallink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        openApp(true);

        ImageView logAppIcon = findViewById(R.id.imageView);
        Animation myanim = AnimationUtils.loadAnimation(this, R.anim.login_animation);
        logAppIcon.startAnimation(myanim);

        Glide.with(this)
                //.load("https://images.unsplash.com/photo-1565214975484-3cfa9e56f914?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1482&q=80")
                .load(R.drawable.ic_logo2)
                .transition(DrawableTransitionOptions.withCrossFade(10))
                .centerCrop()
//                .placeholder(new ColorDrawable(this.getResources().getColor(R.color.fucsia_200)))
//                .circleCrop()
                .into(logAppIcon);
    }

    private void openApp(boolean locationPermission) {
        //Handler: hace que se retrase una accion
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Splash.this, Login.class);
                startActivity(intent);
            }
        }, 3100);
    }
}