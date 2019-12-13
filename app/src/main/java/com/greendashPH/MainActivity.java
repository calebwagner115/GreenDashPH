package com.greendashPH;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    int electricid;
    int waterid;
    int gasid;
    int thermostatid;
    BottomNavigationView bottom_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        electricid = R.id.electricityButton;
        waterid=R.id.waterButton;
        gasid=R.id.gasButton;
        thermostatid=R.id.thermostatButton;
        bottom_menu = findViewById(R.id.bottom_navigation);
        bottom_menu.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {
        @Override
        public boolean onNavigationItemSelected(MenuItem item)
        {
            switch (item.getItemId())
            {
                case R.id.action_timer:
                    Intent r = new Intent(MainActivity.this, Alarm.class);
                    startActivity(r);
                    finish();
                    break;
//                case R.id.action_daily:
//                    Intent s = new Intent(MainActivity.this, SleepTrack.class);
//                    startActivity(s);
//                    finish();
//                    break;
                case R.id.action_history:
                    Intent t = new Intent(MainActivity.this, Alarm.class);
                    startActivity(t);
                    finish();
                    break;
                case R.id.action_setting:
                    Intent u = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(u);
                    //finish();
                    break;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            //TODO Open setings
            Intent setting = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(setting);
        }
        if (item.getItemId() == R.id.logout) {
            Intent login = new Intent(MainActivity.this, LoginScreen.class);
            startActivity(login);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
    public void breakdown(View v){
        Intent breakdownIntent = new Intent(MainActivity.this, BreakdownUsage.class);
        int selectedbreakdown = ((ImageButton) v).getId();

        switch(selectedbreakdown) {
            case R.id.electricityButton: breakdownIntent.putExtra("Breakdown","electricity");
                                    break;
            case R.id.waterButton: breakdownIntent.putExtra("Breakdown","water");
                                    break;
            case R.id.gasButton: breakdownIntent.putExtra("Breakdown","gas");
                                    break;
            case R.id.thermostatButton: breakdownIntent.putExtra("Breakdown","thermostat");
                                    break;
            default: breakdownIntent.putExtra("Breakdown","electricity");
                    break;
        }
        startActivity(breakdownIntent);

    }

}
