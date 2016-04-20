package in.ernet.iitg.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.app.Activity;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class update_Data extends Activity {

    private ProgressDialog pDialog;
    JSONObject jsonObject = new JSONObject();
    String File_Data=new String();
    String File_Name=new String();
    String Update_File_Name= "Update.txt";
    HashMap<String,String> Send_Data= new HashMap<String, String>();
    private static String url_Send_Data = "http://10.9.3.30/BTP/Send_Data.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_UPDATE = "fileupdate";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Button btnCreateProduct = (Button) findViewById(R.id.update_Start);
        btnCreateProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //File_Name="rotation.txt";
                //readFromFile();
                readFromUpdate();
                new SendData().execute();
            }
        });
    }
    public void readFromUpdate(){
        String sdCardState;
        sdCardState= Environment.getExternalStorageState();
        StringBuilder text = new StringBuilder();
        File_Data=new String();
        if(Environment.MEDIA_MOUNTED.equals(sdCardState)){
            File root= Environment.getExternalStorageDirectory();
            File dir= new File(root.getAbsolutePath()+"/IITGAPP");
            if(!dir.exists()){
                dir.mkdir();
            }
            File file= new File(dir,Update_File_Name);
            try{
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    File_Name=line;
                    Log.d(File_Name, TAG_UPDATE);
                    readFromFile();
                }
                br.close();
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }
            catch ( IOException e){
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(),"Complete folder Read ",Toast.LENGTH_LONG).show();

            File file1=new File(dir, Update_File_Name);
            try {
                FileOutputStream fileOutputStream= new FileOutputStream(file1);
                fileOutputStream.close();
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }
            catch ( IOException e){
                e.printStackTrace();
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"SDcard Not Available",Toast.LENGTH_LONG).show();
        }
    }
    public void readFromFile(){
        String sdCardState;
        sdCardState= Environment.getExternalStorageState();
        StringBuilder text = new StringBuilder();
        File_Data=new String();
        if(Environment.MEDIA_MOUNTED.equals(sdCardState)){
            File root= Environment.getExternalStorageDirectory();
            File dir= new File(root.getAbsolutePath()+"/IITGAPP");
            if(!dir.exists()){
                dir.mkdir();
            }
            File file= new File(dir,File_Name);
            text.append(File_Name);
            text.append('\n');
            try{
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                br.close();
            }
            catch (FileNotFoundException e){
                e.printStackTrace();
            }
            catch ( IOException e){
                e.printStackTrace();
            }
            File_Data=text.toString();
            Toast.makeText(getApplicationContext(),"File Read ",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"SDcard Not Available",Toast.LENGTH_LONG).show();
        }
    }
    class SendData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(update_Data.this);
            pDialog.setMessage("Sending Data..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }


        protected String doInBackground(String... args) {
            Send_Data.put("DATA",File_Data);
            performPostCall(url_Send_Data, Send_Data);
            return null;
        }
        public String  performPostCall(String requestURL,HashMap<String, String> postDataParams) {
            URL url;
            String response = "";
            try {
                url = new URL(requestURL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));
                writer.flush();
                writer.close();
                os.close();
                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line=br.readLine()) != null) {
                        response+=line;
                    }
                }
                else {
                    response="";

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }
        private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for(Map.Entry<String, String> entry : params.entrySet()){
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            return result.toString();
        }
    }
}
