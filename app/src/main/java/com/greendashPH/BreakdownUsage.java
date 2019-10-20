package com.greendashPH;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedHashMap;

public class BreakdownUsage extends AppCompatActivity {
    String breakdown;
    ImageView breakdownImage;
    TextView bkdTitle;
    LinearLayout breakdowntitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakdown_usage);
        Intent breakdownusage = getIntent();
        getSupportActionBar().hide();
        this.breakdown = breakdownusage.getStringExtra("Breakdown");
        System.out.println(breakdown);

        breakdownImage = (ImageView) findViewById(R.id.usageimage);
        bkdTitle = findViewById(R.id.usageType);
        breakdowntitle = findViewById(R.id.breakdowntitle);



        switch (breakdown){
            case "electricity":breakdownImage.setBackgroundResource(R.drawable.electricity);
                                bkdTitle.setText("ELECTRICITY");
                                breakdowntitle.setBackgroundColor(ContextCompat.getColor(this, R.color.electric));
                                break;
            case "water":breakdownImage.setBackgroundResource(R.drawable.water);
                            bkdTitle.setText("WATER");
                            breakdowntitle.setBackgroundColor(ContextCompat.getColor(this, R.color.water));
                            break;
            case "gas":breakdownImage.setBackgroundResource(R.drawable.gas);
                        bkdTitle.setText("GAS");
                        breakdowntitle.setBackgroundColor(ContextCompat.getColor(this, R.color.gas));
                        break;
            case "waste":breakdownImage.setBackgroundResource(R.drawable.waste);
                        bkdTitle.setText("WASTE");
                        breakdowntitle.setBackgroundColor(ContextCompat.getColor(this, R.color.waste));
                        break;
        }

    }


}
