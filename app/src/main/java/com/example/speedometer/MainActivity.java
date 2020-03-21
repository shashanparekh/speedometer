package com.example.speedometer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Formatter;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener, View.OnClickListener {

    SwitchCompat sw_metric;
    TextView tv_speed;
    Button printdata;
    String val;
    TextView answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sw_metric=findViewById(R.id.sw_metric);
        tv_speed=findViewById(R.id.tv_speed);
        printdata=findViewById(R.id.printdata);
        answer=findViewById(R.id.anser);


        printdata.setOnClickListener(this);

        //check for gps permission
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        }else
        {
            //start the program if permission is granted
            doStuff();
        }
        this.updateSpeed(null);

        sw_metric.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.this.updateSpeed(null);
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!=null){
            CLocation myLocation=new CLocation(location,this.useMetricUnits());
            this.updateSpeed(myLocation);
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    @SuppressLint("MissingPermission")
    public void doStuff(){
        LocationManager locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        if(locationManager!=null){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
        }
        Toast.makeText(this,"Waiting for GPS connection",Toast.LENGTH_SHORT).show();
    }
    private void updateSpeed(CLocation location){
        float nCurrentSpeed=0;
        if(location!=null){
            location.setbUseMetricUnits(this.useMetricUnits());
            nCurrentSpeed=location.getSpeed();
        }
        Formatter fmt=new Formatter(new StringBuilder());
        fmt.format(Locale.US,"%5.1f",nCurrentSpeed);
        String strCurrentSpeed=fmt.toString();
        strCurrentSpeed=strCurrentSpeed.replace(" ","0");
        if(this.useMetricUnits()){
            tv_speed.setText(strCurrentSpeed+" km/h");
        }else{
            tv_speed.setText(strCurrentSpeed+" miles/h");
        }

    }
    private boolean useMetricUnits(){
        return sw_metric.isChecked();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==1000){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                doStuff();
            }else{
                finish();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int view=v.getId();
        if(view==R.id.printdata){
            val=tv_speed.getText().toString().trim();
            String namee=val;
            answer.setText(namee);

        }
    }
}
