package com.greendashPH;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int electricid;
    int waterid;
    int gasid;
    int wasteid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        electricid = R.id.electricityButton;
        waterid=R.id.waterButton;
        gasid=R.id.gasButton;
        wasteid=R.id.wasteButton;
    }
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
            case R.id.wasteButton: breakdownIntent.putExtra("Breakdown","waste");
                                    break;
            default: breakdownIntent.putExtra("Breakdown","electricity");
                    break;
        }
        startActivity(breakdownIntent);

    }

}
