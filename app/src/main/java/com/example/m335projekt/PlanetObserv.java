package com.example.m335projekt;

import android.content.res.Resources;
import android.media.Image;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;


public class PlanetObserv {

    private Python py;
    private PyObject pyf;
    private String planetName;


    PlanetObserv(String planetName){

        py = Python.getInstance();
        pyf = py.getModule("planetary"); //Python file name
        this.planetName = planetName;
    }

    public int[] getAltAndAz(double longitude,double latitude){
        PyObject obj = pyf.callAttr("planetInfo", longitude, latitude, this.planetName); //methode name
        String altAndAzDegree = obj.toString();


        String[] altAndAzSplit = altAndAzDegree.split("deg");
        String[] altitudeSplit = altAndAzSplit[0].split("\\(<Angle ");
        String[] azimuthSplit = altAndAzSplit[1].split("<Angle ");

        int altitude = Integer.parseInt(altitudeSplit[1]);
        int azimuth = Integer.parseInt(azimuthSplit[1]);

        return new int[]{altitude,azimuth};

    }

    public void changeIconDependingOnPlanet(ImageView imageView){

        switch (planetName){
            case "Sun":
                imageView.setImageResource(R.drawable.sunimage);
                break;
            case "Moon":
                imageView.setImageResource(R.drawable.moonimage);
                break;
            case "Mercury":
                imageView.setImageResource(R.drawable.mercuryimage);
                break;
            case "Mars":
                imageView.setImageResource(R.drawable.marsimage);
                break;
            case "JUPITER BARYCENTER":
                imageView.setImageResource(R.drawable.jupiterimage);
                break;
            case "Saturn BARYCENTER":
                imageView.setImageResource(R.drawable.saturnimage);
                break;
            case "Uranus BARYCENTER":
                imageView.setImageResource(R.drawable.uranusimage);
                break;
            case "Neptune BARYCENTER":
                imageView.setImageResource(R.drawable.neptunimage);
                break;
            case "Pluto BARYCENTER":
                imageView.setImageResource(R.drawable.plutoimage);
                break;

        }
    }
}
