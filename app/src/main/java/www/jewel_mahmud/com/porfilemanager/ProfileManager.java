package www.jewel_mahmud.com.porfilemanager;

import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.app.Service;
import android.content.Intent;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

/**
 * Created by wohhie on 25/4/2016.
 */

public class ProfileManager extends Service implements SensorEventListener {

    private double lightValue;
    private boolean active;
    private double maxValue = 40.0;
    private double minValue = 10.0;


    private double valueX;
    private double valueY;
    private double valueZ;


    AudioManager audioManager;
    SensorManager sensorManager;


    @Override
    public IBinder onBind(Intent arg0) {

        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        lightValue = 0.0;

        //CREATE AUDIO MANAGER
        audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);

        //CREATE SENSOR
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //TAKE LIGHT SENSOR
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        //TAKE ACCELEORMETER
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        /**
         * FOR light.
         */
        if( lightSensor != null ){
            //avaiable.setText("sensor.TYPE_LIGHT is Available.");
            Toast.makeText(this, "Light Sensor is Available", Toast.LENGTH_LONG).show();
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);

        }


        /**
         * FOR accelerometer.
         */
        if( accelerometer != null ){
            //SENSOR IS NOT VALID
            //available.setText("sensor.TYPE_ACCELEROMETER is Available");
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        //FOR LIGHT SENSOR
        if(event.sensor.getType() == Sensor.TYPE_LIGHT){
            //if type LIGHT == true
//            lightSensorData.setText("" + event.values[0]);
            lightValue = event.values[0];

            if(lightValue < 8.0){

//                Toast.makeText(MainActivity.this, "Now in Vibration Mode.", Toast.LENGTH_SHORT);
                setVibrate();

            }else if(lightValue >= 10.0){
//                Toast.makeText(MainActivity.this, "Now in Vibration Mode.", Toast.LENGTH_LONG);
                setRinging();

            }
        }


        //FOR ACCELEROMETER
        if( event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
//            mode.setText("sensor.TYPE_ACCELEROMETER Not Available.");
            valueX = event.values[0];
            valueY = event.values[1];
            valueZ = event.values[2];

//            valX.setText("Value X : " + valueX);
//            valY.setText("Value Y : " + valueY);
//            valZ.setText("Value Z : " + valueZ);


            if( (valueX <= 2.0 && valueX >= -1.5 ) && (valueY >= 0.5 && valueY <= 2.5)  && valueZ >= 9.1 ){
                setRinging();


            }else if( (valueX >= -3.0 && valueX <= 3.0) && (valueY >= 0.5 && valueY <= 1.8) && (valueZ <= -7.5 && valueZ >= -9.0) ){
                setVibrate();
            }

            else if( valueZ <= 5.0 ){
                setSilent();

            }else{
//                mode.setText("No Mode Activated.");
            }


        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void setVibrate(){
//        mode.setText("Mode : Vibration Mode Activated.");
        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        SystemClock.sleep(30);
    }


    public void setRinging(){
//        mode.setText("Mode : Ringing Mode Activated.");
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        SystemClock.sleep(30);
    }


    public void setSilent(){
//        mode.setText("Mode : Silent Mode Activated.");
        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        SystemClock.sleep(30);
    }
}
