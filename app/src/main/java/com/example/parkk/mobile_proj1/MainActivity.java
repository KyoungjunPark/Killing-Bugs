package com.example.parkk.mobile_proj1;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private GraphicsView screen_circle;
    private boolean isBulletClicked = false;
    private boolean isParticlesClicked = false;
    private ImageButton leftButton;
    private ImageButton rightButton;
    private ImageButton particlesButton;
    private ImageButton bulletButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        leftButton = (ImageButton) findViewById(R.id.leftButton);
        rightButton = (ImageButton) findViewById(R.id.rightButton);
        particlesButton = (ImageButton) findViewById(R.id.particlesButton);
        bulletButton = (ImageButton) findViewById(R.id.bulletButton);

        screen_circle = (GraphicsView) findViewById(R.id.screen_circle);

        leftButton.setOnTouchListener(new RepeatListener(400, 1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screen_circle.sprayLeft();
            }
        }));
        rightButton.setOnTouchListener(new RepeatListener(400, 1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screen_circle.sprayRight();
            }
        }));
        particlesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isParticlesClicked){
                    screen_circle.shootParticles();
                    particlesButton.setImageResource(R.drawable.particles_button);
                    isParticlesClicked = true;
                } else{
                    screen_circle.pauseParticles();
                    particlesButton.setImageResource(R.drawable.particles_button_pressed);
                    isParticlesClicked = false;
                }
            }
        });
        bulletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isBulletClicked) {
                    screen_circle.shootBullet();
                    bulletButton.setImageResource(R.drawable.pill_button);
                    isBulletClicked = true;
                }else{
                    screen_circle.pauseShootBullet();
                    bulletButton.setImageResource(R.drawable.pill_button_pressed);
                    isBulletClicked = false;
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
