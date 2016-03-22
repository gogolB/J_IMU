package com.kassas_lab.j_imu;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;

public class IMU_Data_NoSerial extends Activity {

    private SensorManager sensorManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imu__data__no_serial);

        // Reget and configure all the sensors.
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);


    }
}
