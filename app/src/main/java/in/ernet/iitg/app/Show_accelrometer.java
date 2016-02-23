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
import android.widget.TextView;

import org.w3c.dom.Text;

public class Show_accelrometer extends Activity implements SensorEventListener {
    Sensor accelerometer;
    SensorManager sm;
    TextView acceleration;
    private static final String TAG = "vishal";

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_show_accelrometer);
        sm=(SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //TextView T= (TextView)findViewById(R.id.parameters);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        acceleration= (TextView)findViewById(R.id.accelrometer);
    }

    public void onSensorChanged(SensorEvent event){
        //Log.d(String.valueOf(event.values[0]), TAG);
        //acceleration.setText("X: " + String.valueOf(event.values[0]));
        acceleration.setText("X: " + event.values[0] +"\nY: " +  event.values[1] + "\nZ: " + event.values[2] );
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }
}
