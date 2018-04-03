package com.example.hanyugyeong.test02;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;



public class AddActivity extends AppCompatActivity implements LocationListener {

    String TAG = "로그 지점";

    EditText title, latitude, longitude, radius;
    Button currentLoca, addButton;
    TextView currentLatitude, currentLongitude;
    TextView rightInput;

    Intent toMainIntent;

    LocationManager lm;

    Double lat, lng;

    File file = new File("location.txt");   //파일생성
    FileOutputStream fos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setTitle("Add Location");
        setContentView(R.layout.activity_add);

        title = findViewById(R.id.title);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        radius = findViewById(R.id.radius);

        currentLoca = findViewById(R.id.currentLoca);
        addButton = findViewById(R.id.addButton);

        currentLatitude = findViewById(R.id.currentLatitude);
        currentLongitude = findViewById(R.id.currentLongitude);
        rightInput = findViewById(R.id.rightInput);

        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        try{
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0,this);
        }catch (SecurityException e){
            e.printStackTrace();
        }

        button();

    }



    public void button(){

        //이 버튼을 누르면 직접 입력하지 않아도
        // 하단에 나와있는 현재의 위도와 경도로 자동으로 입력해준다.
        currentLoca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                latitude.setText(currentLatitude.getText());
                longitude.setText(currentLongitude.getText());
            }
        });

        //장소 추가 완료 버튼
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //4가지 사항 중 하나라도 빈칸일 때, 예외처리
                if(title.getText().toString().equals("") || latitude.getText().toString().equals("")
                        || longitude.getText().toString().equals("") || radius.getText().toString().equals(""))
                    rightInput.setVisibility(View.VISIBLE);
                else {
                    toMainIntent = getIntent();
                    toMainIntent.putExtra("title",title.getText().toString());
                    toMainIntent.putExtra("latitude",latitude.getText().toString());
                    toMainIntent.putExtra("longitude",longitude.getText().toString());
                    toMainIntent.putExtra("radius",radius.getText().toString());
                    setResult(RESULT_OK,toMainIntent);
                    finish();
                    Log.d(TAG, "addButton 완료");

                    //파일을 열어 입력된 값을 씀.
                    try  {
                        fos = openFileOutput(file.toString(), Context.MODE_APPEND);
                        fos.write(title.getText().toString().getBytes());
                        fos.write("\n".getBytes());
                        fos.write(latitude.getText().toString().getBytes());
                        fos.write("\n".getBytes());
                        fos.write(longitude.getText().toString().getBytes());
                        fos.write("\n".getBytes());
                        fos.write(radius.getText().toString().getBytes());
                        fos.write("\n".getBytes());
                        fos.close();
                    }catch (IOException e){
                        Toast.makeText(getApplicationContext(), "FileNotFound 오류", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
        currentLatitude.setText(lat+"");
        currentLongitude.setText(lng+"");
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onProviderDisabled(String provider) {
    }

}
