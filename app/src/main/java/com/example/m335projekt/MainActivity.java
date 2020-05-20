package com.example.m335projekt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import static java.lang.Math.round;

public class MainActivity extends AppCompatActivity {

    //Location sensor variables
    private LocationManager locationManager;
    private LocationListener locationListener;
    //Magnetic and gravitational sensor variables
    private SensorManager sensorManager;
    private SensorEventListener sensorlistener;
    // Gravity rotational data
    private float gravity[];
    // Magnetic rotational data
    private float magnetic[]; //for magnetic rotational data
    private float accels[] = new float[3];
    private float mags[] = new float[3];
    private float[] values = new float[3];
    // azimuth, pitch and roll
    private float azimuth;
    private float pitch;
    private float roll;
    //view variables
    private ImageView planetIcon;
    private ImageView yawCircle;
    private ImageView pitchCircle;
    private TextView viewAlt;
    private TextView viewAz;
    private Spinner spinnerPlanet;
    private Button settingsBTN;
    //Device location
    private double longitude;
    private double latitude;
    //Planet location
    private PlanetObserv planetObserv;
    private int planetAltitude;
    private int planetAzimuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Loads python information into Java
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        //Init views
        pitchCircle = (ImageView) findViewById(R.id.pitchLocation);
        yawCircle = (ImageView) findViewById(R.id.yawCircle);
        viewAlt = (TextView) findViewById(R.id.textALT);
        viewAz = (TextView) findViewById(R.id.textAZ);
        planetIcon = (ImageView) findViewById(R.id.planetExampleIcon);
        settingsBTN = (Button) findViewById(R.id.settingsBTN);

        //Init Dropdown menu
        spinnerPlanet = (Spinner) findViewById(R.id.planetChooser);
        ArrayAdapter<String> planetAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.planetNames));
        planetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlanet.setAdapter(planetAdapter);

        //settings onclick listener
        settingsBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettingsActivity();
            }
        });

        //Dropdown listener handler
        spinnerPlanet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                planetObserv = null;
                switch (position) {
                    case 0:
                        //Sun first position
                        createPlanetToObserve("Sun");
                        break;
                    case 1:
                        //Moon second position
                        createPlanetToObserve("Moon");
                        break;
                    case 2:
                        //Mercury third position
                        createPlanetToObserve("Mercury");
                        break;
                    case 3:
                        //Mars 4th position
                        createPlanetToObserve("Mars");
                        break;
                    case 4:
                        //Jupiter 5th position
                        createPlanetToObserve("JUPITER BARYCENTER");
                        break;
                    case 5:
                        //Saturn 6th position
                        createPlanetToObserve("Saturn BARYCENTER");
                        break;
                    case 6:
                        //Uranus 7th position
                        createPlanetToObserve("Uranus BARYCENTER");
                        break;
                    case 7:
                        //Neptune 8th position
                        createPlanetToObserve("Neptune BARYCENTER");
                        break;
                    case 8:
                        //Pluto 9th position
                        createPlanetToObserve("Pluto BARYCENTER");
                        break;
                    default:
                        //Sun first position
                        createPlanetToObserve("Sun");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Sun first position
                createPlanetToObserve("Sun");
            }
        });

        //Location manager and listener
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                if (planetObserv != null) {
                    int[] altAndAz = planetObserv.getAltAndAz(longitude, latitude);
                    planetAltitude = altAndAz[0];
                    planetAzimuth = altAndAz[1];
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
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            }, 10);
            return;
        } else {
            locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                        return;
                    } catch (SecurityException e) {
                    }
                    return;
                }
        }
    }

    //opens settings Activity
    public void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    //creates a new Planet
    public void createPlanetToObserve(String planetName) {
        planetObserv = new PlanetObserv(planetName, planetIcon);
    }

    //onPause
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener((SensorEventListener) this);
        locationManager.removeUpdates(locationListener);
    }

    //onResume
    @Override
    protected void onResume() {
        super.onResume();
        //Sensor magnetic and gravitational
        sensorManager = getSystemService(SensorManager.class);
        sensorlistener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                switch (event.sensor.getType()) {
                    case Sensor.TYPE_MAGNETIC_FIELD:
                        mags = event.values.clone();
                        break;
                    case Sensor.TYPE_ACCELEROMETER:
                        accels = event.values.clone();
                        break;
                }

                //some math to calculate the current Rotation of the device
                if (mags != null && accels != null) {
                    gravity = new float[9];
                    magnetic = new float[9];
                    SensorManager.getRotationMatrix(gravity, magnetic, accels, mags);
                    float[] outGravity = new float[9];
                    SensorManager.remapCoordinateSystem(gravity, SensorManager.AXIS_X, SensorManager.AXIS_Z, outGravity);
                    SensorManager.getOrientation(outGravity, values);

                    azimuth = values[0] * 57.2957795f;
                    pitch = values[1] * 57.2957795f;
                    roll = values[2] * 57.2957795f;
                    mags = null;
                    accels = null;
                }
                //rotates the Circle in the middle of the screen (view)
                changeViewBasedOnRotationAndLocation();
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        sensorManager.registerListener(sensorlistener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorlistener, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);
    }

    //Changes the view rotation and position of certain images in the view
    private void changeViewBasedOnRotationAndLocation() {
        /*
        IMPORTANT!
        The roll and pitch Parameter are different when deploying on a real Device !
        roll = azimuth

        This is setup for the Emulator!
         */
        yawCircle.setRotation(planetAzimuth + (round((roll * -1))));
        pitchCircle.setTranslationY(planetAltitude + (round(pitch * -1)));
        viewAlt.setText("Altitude: " + planetAltitude);
        viewAz.setText("Azimuth: " + planetAzimuth);
    }

}
