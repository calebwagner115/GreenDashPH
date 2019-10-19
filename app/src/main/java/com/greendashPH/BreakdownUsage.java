package com.greendashPH;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class BreakdownUsage extends AppCompatActivity {
    String breakdown;
    ImageView breakdownImage;
    TextView bkdTitle;


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


        switch (breakdown){
            case "electricity":breakdownImage.setBackgroundResource(R.drawable.electricity);
                                bkdTitle.setText("ELECTRICITY");
                                break;
            case "water":breakdownImage.setBackgroundResource(R.drawable.water);
                            bkdTitle.setText("WATER");
                            break;
            case "gas":breakdownImage.setBackgroundResource(R.drawable.gas);
                bkdTitle.setText("GAS");
                break;
            case "waste":breakdownImage.setBackgroundResource(R.drawable.waste);
                bkdTitle.setText("WASTE");
                break;
        }

    }
}
