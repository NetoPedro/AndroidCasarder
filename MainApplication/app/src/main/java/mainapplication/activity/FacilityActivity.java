package mainapplication.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import mainapplication.R;
import mainapplication.dto.FacilityDTO;
import mainapplication.utils.DownloadImageTask;
import pl.droidsonroids.gif.GifImageView;

public class FacilityActivity extends AppCompatActivity implements OnMapReadyCallback{

    GoogleMap mMap;
    FacilityDTO facility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_facility);
        facility = (FacilityDTO) getIntent().getSerializableExtra("facility");
        GifImageView image = (GifImageView) findViewById(R.id.facility_hero_image);
        new DownloadImageTask(facility.getId(), image, facility.getImageURL()).execute();
        TextView name = (TextView) findViewById(R.id.facility_name);
        name.setText(facility.getName());
        TextView type = (TextView) findViewById(R.id.facility_type);
        type.setText(facility.getDescription());
        final RelativeLayout ratingLayout = (RelativeLayout) findViewById(R.id.rating_bar_layout);
        final RatingBar rating = (RatingBar) findViewById(R.id.rating);
        final TextView ratingLabel = (TextView) findViewById(R.id.rating_label);
        if(facility.getPromotionNext30Days() != null){
            TextView promotion = (TextView) findViewById(R.id.promotion);
            String[] begin =  facility.getPromotionNext30Days().getBeginTime().split("[/ ]");
            String[] end = facility.getPromotionNext30Days().getEndTime().split("[/ ]");
            promotion.setText("-" + facility.getPromotionNext30Days().getDiscount() + "%    " + begin[0] + "/" + begin[1] + " - " + end[0] + "/" + end[1]);

            promotion.setVisibility(View.VISIBLE);
        }
        rating.setRating(Float.parseFloat(String.valueOf(facility.getAverageRating())));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Button ratingsButton = (Button) findViewById(R.id.ratings_button);
        ratingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pair<View, String> p1 = Pair.create((View) rating, "rating");
                Pair<View, String> p2 = Pair.create((View) ratingLayout, "rating_layout");
                Pair<View, String> p3 = Pair.create((View) ratingLabel, "rating_label");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(FacilityActivity.this, p2);
                startActivity(new Intent(FacilityActivity.this, FacilityRatingsActivity.class).putExtra("facility", facility),
                        options.toBundle());
            }
        });

        Button bookNowButton = (Button) findViewById(R.id.book_now_button);
        bookNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FacilityActivity.this, TimeSlotsActivity.class).putExtra("facility", facility));
            }
        });

        findViewById(R.id.sports_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FacilityActivity.this, FacilitySportsActivity.class).putExtra("id", facility.getId()));
            }
        });
        findViewById(R.id.categories_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FacilityActivity.this, FacilityCategoriesActivity.class).putExtra("id", facility.getId()));
            }
        });
        findViewById(R.id.equipments_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FacilityActivity.this, FacilityEquipmentsActivity.class).putExtra("id", facility.getId()));
            }
        });


        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("http://casardercdio.ddns.net:22001/facility?facilityId=" + facility.getId()))
                .build();
        ShareButton shareButton = (ShareButton)findViewById(R.id.fb_share_button);
        shareButton.setShareContent(content);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng location = new LatLng(facility.getLatitude(), facility.getLongitude());
        mMap.addMarker(new MarkerOptions().position(location).title(facility.getName()));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(14.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        mMap.moveCamera(cameraUpdate);

    }
}
