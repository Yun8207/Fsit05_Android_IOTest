package tw.alex.iotest;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sp;

    private SharedPreferences.Editor editor;

    private File sdroot;

    private File approot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE},
                    123);
        } else {
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            init();
        }else{
            finish();
        }

    }

    private void init(){
        sp = getSharedPreferences("gamedata",MODE_PRIVATE);
        editor = sp.edit();

        sdroot = Environment.getExternalStorageDirectory();
        approot = new File(sdroot, "Android/data/" + getPackageName());

        if(!approot.exists()){
            approot.mkdir();
        }
       // Log.v("alex",sdroot.getAbsolutePath());
    }

    public void test1(View view){

        editor.putString("username", "Alex");
        editor.putInt("stage", 7);
        editor.putBoolean("sound", true);
        editor.commit();

        showDialog("message","saved");

    }
    public void test2(View view){

        String username = sp.getString("username","nobody");
        boolean isSound = sp.getBoolean("sound", false);
        int stage = sp.getInt("stage", 0);

        showDialog("data","UserName:" + username + "\n" +
        "Stage:" + stage + "\n" +
        "Sound:" + (isSound?"On":"Of"));
    }

    private void showDialog(String title, String mesg){
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(title);
//        builder.setMessage(mesg);
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(mesg)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("cancel", null)
                .setNeutralButton("later",null)
                .create()
                .show();

    }


    public void test3(View view) {

        try {
            FileOutputStream fout = openFileOutput("test1.txt", MODE_PRIVATE);
            fout.write("Hello, World".getBytes());
            fout.flush();
            fout.close();
            showDialog("Message","Save ok");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void test4(View view) {

        try {
            FileInputStream fin = openFileInput("test1.txt");

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(fin));
            String mesg = reader.readLine();
            fin.close();
            showDialog("received:", mesg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void test5(View view) {
        try{
            File file1 = new File(sdroot, "alex.txt");
            FileOutputStream fout =
                    new FileOutputStream(file1);
            fout.write("Hello, Alex".getBytes());
            fout.flush();
            fout.close();
            showDialog("Ok","Ok");

        }catch(Exception e){
            Log.v("alex","test5" + e.toString());
        }
    }

    public void test6(View view){
        try{
            File file1 = new File(approot, "alex.txt");
            FileOutputStream fout =
                    new FileOutputStream(file1);
            fout.write("Hello, Alex".getBytes());
            fout.flush();
            fout.close();
            showDialog("Ok2","Ok2");

        }catch(Exception e){
            Log.v("alex","test6" + e.toString());
        }

    }

    public void test7(View view){
        try {
            File file1 = new File(sdroot, "alex.txt");
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(new FileInputStream(file1)));
            String mesg = reader.readLine();
            reader.close();
            showDialog("test7", mesg);
        }catch(Exception e){
            
        }
    }
}
