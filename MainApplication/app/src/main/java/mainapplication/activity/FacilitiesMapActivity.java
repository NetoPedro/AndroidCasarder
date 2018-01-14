package mainapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import mainapplication.R;
import mainapplication.dto.SolrFacilityDTO;

public class FacilitiesMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    SolrFacilityDTO[] facilities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilities_map);
        facilities = (SolrFacilityDTO[]) getIntent().getSerializableExtra("facilities");
        if(facilities == null){
            facilities = new SolrFacilityDTO[0];
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (int i = 0; i < facilities.length; i++) {
            SolrFacilityDTO facility = facilities[i];
            LatLng location = new LatLng(facility.getLatitude(), facility.getLongitude());
            mMap.addMarker(new MarkerOptions().position(location).title(facility.getName()));
        }
    }
}
