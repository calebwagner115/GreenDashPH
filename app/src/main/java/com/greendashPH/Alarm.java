package com.greendashPH;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Parcel;
import android.os.PersistableBundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class Alarm extends AppCompatActivity {
    BottomNavigationView bottom_menu;
    int perm=-1;
    String status;
    PendingIntent pendingIntent;
    Button SetAlarm;
    AlarmManager alarmManager;

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString("status", status); // Saving the Variable theWord

        Bundle b = new Bundle();
        pendingIntent.describeContents();
        //outState.putBundle("PendingIntent", );// Saving the ArrayList fiveDefns
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);

//        theWord = savedInstanceState.getString("theWord"); // Restoring theWord
//        fiveDefns = savedInstanceState.getStringArrayList("fiveDefns"); //Restoring fiveDefns
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        status="off";
        requestPermissions(new String[] {Manifest.permission.WAKE_LOCK}, 1);


        if (checkSelfPermission(Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.WAKE_LOCK}, 1);
        }


         alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        bottom_menu = findViewById(R.id.bottom_navigation);
        bottom_menu.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        textAlarmPrompt = (TextView) findViewById(R.id.alarmprompt);
        SetAlarm = (Button) findViewById(R.id.startAlaram);


        //buttonstartSetDialog = (Button) findViewById(R.id.startAlaram);
        SetAlarm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(SetAlarm.getText().equals("Set Alarm Time")) {
                    textAlarmPrompt.setText("");
                    openTimePickerDialog(false);

                }
                else{
                    SetAlarm.setText("Set Alarm Time");
                    alarmManager.cancel(pendingIntent);
                    textAlarmPrompt.setText("No Alarm Set");
                    status="off";
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Start your camera handling here
                    perm=1;
                    Toast.makeText(getApplicationContext(),"You Access", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),"You declined to allow the app to access your camera", Toast.LENGTH_SHORT).show();

                }
                break;

        }
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
//                    Intent r = new Intent(Alarm.this, Alarm.class);
//                    startActivity(r);
//                    finish();
                    break;
                case R.id.action_daily:
                    Intent s = new Intent(Alarm.this, SleepTrack.class);
                    startActivity(s);
                    finish();
                    break;
                case R.id.action_history:
                    Intent t = new Intent(Alarm.this, MainActivity.class);
                    startActivity(t);
                    finish();
                    break;
                case R.id.action_setting:
                    Intent u = new Intent(Alarm.this, SettingsActivity.class);
                    startActivity(u);
                    //finish();
                    break;
            }
            return false;
        }
    };

    public void setAlarm(View v){

        AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        if(status.equals("off")){
            intent.putExtra("status", "on");
            status="on";
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            Calendar time = Calendar.getInstance();
            time.setTimeInMillis(System.currentTimeMillis());
            time.add(Calendar.SECOND, 5);
            alarmMgr.set(AlarmManager.RTC_WAKEUP, time.getTimeInMillis(), pendingIntent);
        } else{
            Toast.makeText(getApplicationContext(), "stop", Toast.LENGTH_LONG).show();
            intent.putExtra("status", "off");
            status="off";
            sendBroadcast(intent);
        }

    }

    TimePicker myTimePicker;
    Button buttonstartSetDialog;
    TextView textAlarmPrompt;
    TimePickerDialog timePickerDialog;

    final static int RQS_1 = 1;


    private void openTimePickerDialog(boolean is24r) {
        Calendar calendar = Calendar.getInstance();

        timePickerDialog = new TimePickerDialog(Alarm.this,
                onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), is24r);
        timePickerDialog.setTitle("Set Alarm Time");

        timePickerDialog.show();

    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            Calendar calNow = Calendar.getInstance();
            Calendar calSet = (Calendar) calNow.clone();

            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);

            if (calSet.compareTo(calNow) <= 0) {
                // Today Set time passed, count to tomorrow
                calSet.add(Calendar.DATE, 1);
            }

            setAlarm(calSet);
        }
    };

    private void setAlarm(Calendar targetCal) {

        Intent intent = new Intent(this, AlarmReceiver.class);
        if(status.equals("off")&&SetAlarm.getText().equals("Set Alarm Time")){
            intent.putExtra("status", "on");
            status="on";
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            Calendar time = Calendar.getInstance();
            time.setTimeInMillis(System.currentTimeMillis());
            alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
            textAlarmPrompt.setText("\n\n***\n" + "Alarm is set " + targetCal.getTime() + "\n" + "***\n");
            SetAlarm.setText("Cancel Alarm");
        }

    }

    public void stopAlarm(View v){
        if(status.equals("on")){
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra("status", "off");
            sendBroadcast(intent);
            status="off";
        }
    }

    public static void alarmActivated(){

    }


}
