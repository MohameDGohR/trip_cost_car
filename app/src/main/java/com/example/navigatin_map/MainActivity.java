package com.example.navigatin_map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements LocationListener {
    private AutoCompleteTextView auto2;
    EditText auto1;
    ArrayList<String> aut1 ;
    LocationManager manager;
    double llong ;
    double llat;
    double long1,lat1;
    Location l1,l2;
    double distance ;
    EditText cpt ,cp,costing ;
    double cost ,m ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.aut1 = new ArrayList<>();
       // this.aut2 = new ArrayList<>();
        aut1.add("Alexandria");
        aut1.add("Aswan");
        aut1.add("Giza");
        aut1.add("Hurghada");

        setContentView(R.layout.activity_main);
        this.auto1 =findViewById(R.id.auto1);
        this.auto2 =findViewById(R.id.auto2);
        cpt = findViewById(R.id.cpt);
        cp = findViewById(R.id.cp);
        costing = findViewById(R.id.costing);

       // ArrayAdapter<String> ad = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,aut1);
        ArrayAdapter<String> ad2 = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,aut1);
       // auto1.setAdapter(ad);
        auto2.setAdapter(ad2);
        auto1.setEnabled(false);

    }

    public void find(View view) {
        if(!cp.getText().toString().isEmpty() && !auto2.getText().toString().isEmpty())
        {

            manager = (LocationManager) getSystemService(LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                String[]perm={Manifest.permission.ACCESS_FINE_LOCATION};
                ActivityCompat.requestPermissions(this,perm,1);
            }
            else{
                Toast.makeText(this, "gone", Toast.LENGTH_SHORT).show();
                manager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);

            }


        } else {
            Toast.makeText(this, "من فضلك ادخل البيانات ", Toast.LENGTH_SHORT).show();
            return ;
        }







    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if( requestCode == 1)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED  )
            {
                try {
                    manager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }

            }else {
                Toast.makeText(this, "denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
         llong = location.getLongitude();
         llat = location.getLatitude();
        Geocoder g = new Geocoder(this);
        try {
            List<Address> fromLocation = g.getFromLocation(llat, llong,1);
            Toast.makeText(this, ""+fromLocation.get(0).getAddressLine(0), Toast.LENGTH_LONG).show();
            //auto1.setText(""+fromLocation.get(0).getAddressLine(0));
            Geocoder d = new Geocoder(this);
            List<Address> fromLocationName = d.getFromLocationName(auto2.getText().toString(), 1);
            if(fromLocationName.get(0).hasLatitude() && fromLocationName.get(0).hasLongitude())
            {
                 lat1 = fromLocationName.get(0).getLatitude();
                long1 = fromLocationName.get(0).getLongitude();
                l1 = new Location("point A");
                l1.setLatitude(llat);
                l1.setLongitude(llong);
                l2 = new Location("point B");
                l2.setLongitude(long1);
                l2.setLatitude(lat1);
                distance = l1.distanceTo(l2);
                if(distance != 0 )
                {
                    //distance = distance /100;
                    distance = distance /1000;
                    auto1.setEnabled(true);

                    auto1.setText(distance +"  KM");

                    if(!this.cpt.getText().toString().isEmpty())
                    {
                        m = Double.parseDouble(cpt.getText().toString());
                         cost = distance/m  ;
                         cost = cost  * Double.parseDouble(cp.getText().toString()) *2.0 ;
                         costing.setText(cost +"  EGP");
                       // Toast.makeText(this, "distance "+ distance/m, Toast.LENGTH_LONG).show();
                       // Toast.makeText(this, " m  is "+ cpt.getText().toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(this, "costing  "+ cp.getText().toString(), Toast.LENGTH_LONG).show();

                    }else
                    {
                        m =5 ;
                        cost = distance/m  ;
                        cost = cost  * Double.parseDouble(cp.getText().toString()) *2.0 ;
                        costing.setText(cost +"  EGP");


                    }




                }else
                {
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                }



               // auto1.setText(lat1 +"   " + long1);


            }


        } catch (IOException e) {
            e.printStackTrace();
        }
       // Toast.makeText(this, "hello chanaged", Toast.LENGTH_SHORT).show();

       // Toast.makeText(this, ""+location.getLongitude(), Toast.LENGTH_SHORT).show();




    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void map(View view) {
        String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+llat+","+llong+"&daddr="+lat1+","+long1;
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(Intent.createChooser(intent, "Select an application"));

    }

    public void tc(View view) {
        auto1.setText("");
        auto2.setText("");
        cp.setText("");
        cpt.setText("");
        costing.setText("");
    }
}
