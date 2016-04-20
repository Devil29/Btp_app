package in.ernet.iitg.app;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.text.format.Time;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class Show_rotation extends Activity implements SensorEventListener {
    Sensor rotate;
    SensorManager sm;
    TextView rotation;
    private static final String TAG = "DUDE";
    private Vector<String> FileData= new Vector<String>();
    private int state=0;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_rotation);
        sm=(SensorManager)getSystemService(SENSOR_SERVICE);
        rotate=sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sm.registerListener(this, rotate, SensorManager.SENSOR_DELAY_NORMAL);
        rotation= (TextView)findViewById(R.id.Rotation);
    }

    public void onSensorChanged(SensorEvent event){
        //Log.d(String.valueOf(event.values[0]), TAG);
        //acceleration.setText("X: " + String.valueOf(event.values[0]));
        Time now = new Time();
        now.setToNow();
        rotation.setText("X: " + event.values[0] + "\nY: " + event.values[1] + "\nZ: " + event.values[2]);
        if(state==1) {
            FileData.add(Float.toString(event.values[0]) + "," + Float.toString(event.values[1]) + "," + Float.toString(event.values[2]) + "," + now.format("%Y_%m_%d_%H_%M_%S")  + "\n" );
        }
        else if(state==2){
            Log.d("Size of file data is  " + FileData.size(), TAG);
            state=0;
        }
        Spinner s= (Spinner)findViewById(R.id.rate);
        if((s.getSelectedItem().toString()).compareTo("Slow")==0){
            Log.d("SLOW", TAG);
            sm.unregisterListener(this);
            sm.registerListener(this, rotate, (SensorManager.SENSOR_DELAY_NORMAL));
        }
        else if((s.getSelectedItem().toString()).compareTo("Normal")==0){
            Log.d("Normal", TAG);
            sm.unregisterListener(this);
            sm.registerListener(this, rotate, (SensorManager.SENSOR_DELAY_NORMAL)*5);
        }
        else if((s.getSelectedItem().toString()).compareTo("Fast")==0){
            Log.d("Fast", TAG);
            sm.unregisterListener(this);
            sm.registerListener(this, rotate, (SensorManager.SENSOR_DELAY_NORMAL)*20);
        }
        else if((s.getSelectedItem().toString()).compareTo("Very_Fast")==0){
            Log.d("ITS very fast", TAG);
            sm.unregisterListener(this);
            sm.registerListener(this, rotate, (SensorManager.SENSOR_DELAY_NORMAL)*100);
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy){

    }

    public void Startdata(View view){
        Toast.makeText(getApplicationContext(),"Saving data Started",Toast.LENGTH_LONG).show();
        state=1;
    }
    public void Stopdata(View view){
        //Toast.makeText(getApplicationContext(),"Saving data Stoped",Toast.LENGTH_LONG).show();
        Time now = new Time();
        now.setToNow();
        if(FileData.size()==0){
            Toast.makeText(getApplicationContext(),"No Data to write ",Toast.LENGTH_LONG).show();
            return;
        }
        String sdCardState;
        String currtime;
        sdCardState= Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(sdCardState)){
            File root= Environment.getExternalStorageDirectory();
            File dir= new File(root.getAbsolutePath()+"/IITGAPP");
            if(!dir.exists()){
                dir.mkdir();
            }
            currtime=now.format("%Y_%m_%d_%H_%M_%S");
            File file= new File(dir,"rotation" + currtime +  ".txt");
            try{
                FileOutputStream fileOutputStream= new FileOutputStream(file);
                for(int i=0;i<FileData.size();i++){
                    fileOutputStream.write(FileData.get(i).getBytes());
                }
                fileOutputStream.close();
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }
            catch ( IOException e){
                e.printStackTrace();
            }
            File file1=new File(dir, "Update.txt");
            try {
                FileOutputStream fileOutputStream= new FileOutputStream(file1,true);
                fileOutputStream.write(("rotation" + currtime + ".txt" + "\n").getBytes());
                fileOutputStream.close();
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }
            catch ( IOException e){
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(),"Size of file data is  " + FileData.size(),Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"SDcard Not Available",Toast.LENGTH_LONG).show();
        }
        FileData= new Vector<String>();
        state=2;
    }
}

