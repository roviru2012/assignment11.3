package com.varunkumar.myapplication_113;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;



public class MainActivity extends Activity {


    public Button addData;
    public Button deleteFile;
    public TextView textView;
    public EditText editText;
    private static final String DATA_FILE = "test.txt";


    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addData = (Button) findViewById(R.id.button);
        deleteFile = (Button) findViewById(R.id.button2);

        textView = (TextView) findViewById(R.id.textView2);
        editText = (EditText) findViewById(R.id.editText);

        checkExternalMedia();

        addData.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                ReadWriteFile rwf = new ReadWriteFile();
                rwf.execute(editText.getText().toString());
            }
        });

        deleteFile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                textView.setText("");
                File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "" );
                File myFile = new File(dir, DATA_FILE);
                myFile.delete();

            }
        });

    }

    public class ReadWriteFile extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            saveTextFile(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String data = getTextFile();
            textView.setText(data);
            editText.setText("");
        }

    }

    public void saveTextFile(String content) {
        FileOutputStream fileOutputStream = null;

        try {
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "" );
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File myFile = new File(dir, DATA_FILE);

            if (!myFile.exists()) {

                myFile.createNewFile();

            }

            fileOutputStream = new FileOutputStream(myFile);
            fileOutputStream.write(content.getBytes());

        } catch (FileNotFoundException e) {
            Log.e("FILE", "Couldn't find that file");
            e.printStackTrace();

        } catch (IOException e) {
            Log.e("FILE", "IO Error");
            e.printStackTrace();

        } finally {
            try {

                if (fileOutputStream != null) {
                    fileOutputStream.close();

                }
            } catch (IOException e) {
                e.printStackTrace();


            }

        }

    }


    public String getTextFile() {

        FileInputStream fileInputStream = null;
        String fileData = null;

        try {

            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "" );
            File myFile = new File(dir, DATA_FILE);
            fileInputStream = new FileInputStream(myFile);
            int size = fileInputStream.available();
            byte[] buffer = new byte[size];
            fileInputStream.read(buffer);
            fileInputStream.close();
            fileData = new String(buffer, "UTF-8");

        } catch (FileNotFoundException e) {
            Log.e("FILE", "Couldn't find that file");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.e("FILE", "IO Error");
            e.printStackTrace();
        } finally {

            try {
                if (fileInputStream != null) {

                    fileInputStream.close();

                }
            }
            catch (IOException e) {
                e.printStackTrace();

            }
        }
        return fileData;
    }


    private void checkExternalMedia(){

        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {

            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;

        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {

            // Can only read the media

            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;

        } else {
            // Can't read or write

            mExternalStorageAvailable = mExternalStorageWriteable = false;

        }
        String text = "External Media: readable="+mExternalStorageAvailable+" writable="+mExternalStorageWriteable;

        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();

    }
}