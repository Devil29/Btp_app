package in.ernet.iitg.app;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.Console;

public class Show_magnetometer extends Activity implements SensorEventListener {

    Sensor magnetometer;
    SensorManager sm;
    TextView mag;
    private static final String TAG = "vishal";

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_show_magnetometer);
        sm=(SensorManager)getSystemService(SENSOR_SERVICE);
        magnetometer=sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sm.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        mag= (TextView)findViewById(R.id.Magnetometer);
    }
    //final private String tag =  vishal;
    public void onSensorChanged(SensorEvent event){
        //Log.d("I am Here",TAG);
        Log.d(String.valueOf(event.values[0]), TAG);
        //mag.setText("values  " + event.values.length);
        //mag.setText("X: " + String.valueOf(event.values[0]) );
        //mag.setText("X: " + String.valueOf(event.values[0]) );
        mag.setText("X: " + event.values[0] +"\nY: " +  event.values[1] + "\nZ: " + event.values[2] );
        Spinner s = (Spinner)findViewById(R.id.rate_magneto);
        if((s.getSelectedItem().toString()).compareTo("Slow")==0){
            Log.d("SLOW", TAG);
            sm.unregisterListener(this);
            sm.registerListener(this, magnetometer, (SensorManager.SENSOR_DELAY_NORMAL));
        }
        else if((s.getSelectedItem().toString()).compareTo("Normal")==0){
            Log.d("Normal", TAG);
            sm.unregisterListener(this);
            sm.registerListener(this, magnetometer, (SensorManager.SENSOR_DELAY_NORMAL)*5);
        }
        else if((s.getSelectedItem().toString()).compareTo("Fast")==0){
            Log.d("Fast", TAG);
            sm.unregisterListener(this);
            sm.registerListener(this, magnetometer, (SensorManager.SENSOR_DELAY_NORMAL)*20);
        }
        else if((s.getSelectedItem().toString()).compareTo("Very_Fast")==0){
            Log.d("ITS very fast", TAG);
            sm.unregisterListener(this);
            sm.registerListener(this, magnetometer, (SensorManager.SENSOR_DELAY_NORMAL)*100);
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

}
