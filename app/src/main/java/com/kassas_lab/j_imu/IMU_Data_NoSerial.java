package com.kassas_lab.j_imu;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class IMU_Data_NoSerial extends Activity implements Runnable, SensorEventListener{

    private static final int PERIOD = 1000;

    private SensorManager sensorManager;

    private View root;

    private boolean ACC_PRESENT = false;
    private boolean GYRO_PRESENT = false;
    private boolean MAG_PRESENT = false;

    float[] ACCValues = new float[3];
    float[] GYROValues = new float[3];
    float[] MAGValues = new float[3];

    float[] linear_Acc = new float[3];
    float[] gravity = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imu__data__no_serial);

        TextView t;

        // Reget and configure all the sensors.
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null)
        {
            // Acc Exists
            ACC_PRESENT = true;
            t = (TextView)findViewById(R.id.RawXValue);
            t.setText(R.string.Waiting);
            t = (TextView)findViewById(R.id.RawYValue);
            t.setText(R.string.Waiting);
            t = (TextView)findViewById(R.id.RawZValue);
            t.setText(R.string.Waiting);
        }
        else
        {
            // Acc doesn't exist.
            ACC_PRESENT = false;
            // Disable all the text.
            t = (TextView)findViewById(R.id.RawXValue);
            t.setText(R.string.NA);
            t = (TextView)findViewById(R.id.RawYValue);
            t.setText(R.string.NA);
            t = (TextView)findViewById(R.id.RawZValue);
            t.setText(R.string.NA);
        }

        if(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null)
        {
            // Gyro Exists
            GYRO_PRESENT = true;
            t = (TextView)findViewById(R.id.GyroXValue);
            t.setText(R.string.Waiting);
            t = (TextView)findViewById(R.id.GyroYValue);
            t.setText(R.string.Waiting);
            t = (TextView)findViewById(R.id.GyroZValue);
            t.setText(R.string.Waiting);
        }
        else
        {
            // Gyro doesn't exist.
            GYRO_PRESENT = false;
            // Disable all the text.
            t = (TextView)findViewById(R.id.GyroXValue);
            t.setText(R.string.Waiting);
            t = (TextView)findViewById(R.id.GyroYValue);
            t.setText(R.string.Waiting);
            t = (TextView)findViewById(R.id.GyroZValue);
            t.setText(R.string.Waiting);
        }

        if(sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null)
        {
            // Gyro Exists
            MAG_PRESENT = true;
            t = (TextView)findViewById(R.id.RollValue);
            t.setText(R.string.Waiting);
            t = (TextView)findViewById(R.id.PitchValue);
            t.setText(R.string.Waiting);
            t = (TextView)findViewById(R.id.YawValue);
            t.setText(R.string.Waiting);
        }
        else
        {
            // Gyro doesn't exist.
            MAG_PRESENT = false;
            // Disable all the text.
            t = (TextView)findViewById(R.id.RollValue);
            t.setText(R.string.NA);
            t = (TextView)findViewById(R.id.PitchValue);
            t.setText(R.string.NA);
            t = (TextView)findViewById(R.id.YawValue);
            t.setText(R.string.NA);
        }

        root=findViewById(android.R.id.content);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(ACC_PRESENT)
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        if(GYRO_PRESENT)
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_GAME);
        if(MAG_PRESENT)
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);

        run();
    }

    @Override
    public void onPause() {
        root.removeCallbacks(this);
        sensorManager.unregisterListener(this);

        super.onPause();
    }

    @Override
    public void run()
    {
        TextView t;
        if(ACC_PRESENT)
        {
            t = (TextView)findViewById(R.id.RawXValue);
            t.setText("" + ACCValues[0]);
            t = (TextView)findViewById(R.id.RawYValue);
            t.setText("" + ACCValues[1]);
            t = (TextView)findViewById(R.id.RawZValue);
            t.setText("" + ACCValues[2]);

            t = (TextView)findViewById(R.id.CorrectedXValue);
            t.setText("" + linear_Acc[0]);
            t = (TextView)findViewById(R.id.CorrectedYValue);
            t.setText("" + linear_Acc[1]);
            t = (TextView)findViewById(R.id.CorrectedZValue);
            t.setText("" + linear_Acc[2]);
        }
        if(GYRO_PRESENT)
        {
            t = (TextView)findViewById(R.id.GyroXValue);
            t.setText("" + GYROValues[0]);
            t = (TextView)findViewById(R.id.GyroYValue);
            t.setText("" + GYROValues[1]);
            t = (TextView)findViewById(R.id.GyroZValue);
            t.setText("" + GYROValues[2]);
        }

        if(MAG_PRESENT)
        {
            t = (TextView)findViewById(R.id.RollValue);
            t.setText("" + MAGValues[0]);
            t = (TextView)findViewById(R.id.PitchValue);
            t.setText("" + MAGValues[1]);
            t = (TextView)findViewById(R.id.YawValue);
            t.setText("" + MAGValues[2]);
        }
        root.postDelayed(this, PERIOD);
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if(ACC_PRESENT && event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            ACCValues[0] = event.values[0];
            ACCValues[1] = event.values[1];
            ACCValues[2] = event.values[2];

            final float alpha = 0.8f;

            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            linear_Acc[0] = event.values[0] - gravity[0];
            linear_Acc[1] = event.values[1] - gravity[1];
            linear_Acc[2] = event.values[2] - gravity[2];
        }
        if(GYRO_PRESENT && event.sensor.getType() == Sensor.TYPE_GYROSCOPE)
        {
            GYROValues[0] = event.values[0];
            GYROValues[1] = event.values[1];
            GYROValues[2] = event.values[2];
        }

        if(MAG_PRESENT && event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
        {
            MAGValues[0] = event.values[0];
            MAGValues[1] = event.values[1];
            MAGValues[2] = event.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
