package com.kassas_lab.j_imu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

public class SetUp_IMU extends Activity
{

    public static SetUp_IMU reference;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up__imu);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        TextView t;
        Switch s;

        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null)
        {
            t = (TextView)findViewById(R.id.AccPresent);
            t.setText(R.string.Present);
        }
        else
        {
            t = (TextView)findViewById(R.id.AccPresent);
            t.setText(R.string.NotPresent);
            s = (Switch)findViewById(R.id.AccCor);
            s.setEnabled(false);
            s = (Switch)findViewById(R.id.AccRaw);
            s.setEnabled(false);
        }


        if(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null)
        {
            t = (TextView)findViewById(R.id.GyroPresent);
            t.setText(R.string.Present);
            if(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED) == null)
            {
                s = (Switch)findViewById(R.id.GyroRaw);
                s.setEnabled(false);
            }

        }
        else
        {
            t = (TextView)findViewById(R.id.GyroPresent);
            t.setText(R.string.NotPresent);
            s = (Switch)findViewById(R.id.GyroRaw);
            s.setEnabled(false);
            s = (Switch)findViewById(R.id.GyroCor);
            s.setEnabled(false);
        }


        if(sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)!= null)
        {
            t = (TextView)findViewById(R.id.MagPresent);
            t.setText(R.string.Present);
            if(sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED) == null)
            {
                s = (Switch)findViewById(R.id.MagRaw);
                s.setEnabled(false);
            }
        }
        else
        {
            t = (TextView)findViewById(R.id.MagPresent);
            t.setText(R.string.NotPresent);
            s = (Switch)findViewById(R.id.MagRaw);
            s.setEnabled(false);
            s = (Switch)findViewById(R.id.MagCor);
            s.setEnabled(false);
        }

        setTitle(R.string.SetUpTitleString);
        reference = this;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_up__imu, menu);
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

    public void onInitButtonPress(View view)
    {
        Switch s = (Switch)findViewById(R.id.OutputOverSerial);
        if(s.isChecked())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // The okay button
            builder.setPositiveButton(R.string.Okay, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    Intent intent = new Intent(reference, IMU_Data_NoSerial.class);
                    startActivity(intent);
                }
            });

            // User hit the cancel button.
            builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog alertDialog = builder.create(); //Read Update
            alertDialog.setTitle("Serial Output");
            alertDialog.setMessage("You have selected to output the data over serial. It will output at a rate of 9600 BAUD.");

            alertDialog.show();  //<-- See This!
        }
    }
}
