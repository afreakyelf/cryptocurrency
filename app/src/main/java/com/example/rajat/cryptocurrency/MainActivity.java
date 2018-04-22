package com.example.rajat.cryptocurrency;


import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    public static BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setAlpha(1);

        bottomNavigationView =findViewById(R.id.navigation);
        bottomNavigationView.performClick();



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.navigation_News:
                        selectedFragment = ItemoneFragment.newInstance();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.content,selectedFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        return true;

                    case R.id.navigation_profile:
                        selectedFragment = ItemtwoFragment.newInstance();
                        transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.content,selectedFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                        return true;

                }
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content, selectedFragment);
                transaction.commit();
                return true;
            }
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, ItemoneFragment.newInstance());
        bottomNavigationView.setSelectedItemId(R.id.navigation_News);
        transaction.commit();
    }


    public void onBackPressed() {
        if(bottomNavigationView.getSelectedItemId() != R.id.navigation_News) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_News);
        }else {


            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.alertdialogforshowbookmark);

            final TextView alertmessage = (TextView)dialog.findViewById(R.id.alertmessage);
            final TextView alert = (TextView)dialog.findViewById(R.id.message);
            final Button no = (Button) dialog.findViewById(R.id.cancel_action);
            final Button yes = (Button) dialog.findViewById(R.id.login_now);

            alert.setText("Alert");
            alertmessage.setText("You want to exit ?");
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });



            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
            });

            dialog.show();



        }
    }



}
