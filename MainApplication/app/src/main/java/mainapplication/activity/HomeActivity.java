package mainapplication.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import mainapplication.R;
import mainapplication.adapter.FacilitiesCollapsedCardAdapter;
import mainapplication.dto.FacilitySearchDTO;
import mainapplication.restclient.RestProperties;

public class HomeActivity extends Fragment {

    private Activity mParent;
    protected View rootView;
    protected Context mContext;
    protected FusedLocationProviderClient mFusedLocationClient;
    private String mLatitude;
    private String mLongitude;
    private getPopularFacilitiesTask task1;
    //private getNearbyFacilitiesTask task2;

    public static HomeActivity newInstance(Activity parent) {
        HomeActivity fragment = new HomeActivity();
        fragment.setmParent(parent);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void setmParent(Activity mParent) {
        this.mParent = mParent;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.activity_home, container, false);
        mContext = rootView.getContext();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        final Button name = (Button) rootView.findViewById(R.id.facility_name_field);
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent extras = new Intent(rootView.getContext(), SuggesterActivity.class);
                Pair<View, String> p1 = Pair.create(mParent.findViewById(R.id.search_layout), "searchBackground");
                Pair<View, String> p2 = Pair.create(mParent.findViewById(R.id.facility_name_field), "search");
                Pair<View, String> p3 = Pair.create(mParent.findViewById(R.id.search_button), "searchButton");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mParent, p1, p2);
                startActivity(extras, options.toBundle());
            }
        });
        Button searchButton = (Button) rootView.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent extras = new Intent(rootView.getContext(), SearchResultsActivity.class);
                startActivity(extras);
            }
        });
        task1 = new getPopularFacilitiesTask();
        task1.execute();
       // task2 = new getNearbyFacilitiesTask();
        //task2.execute();
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        task1.cancel(true);
        //task2.cancel(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        task1 = new getPopularFacilitiesTask();
        task1.execute();
    }

    @Override
    public void onStop() {
        super.onStop();
        task1.cancel(true);
        //task2.cancel(true);
    }

    private void getLocation(){
        if (ContextCompat.checkSelfPermission(mParent,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                mLatitude = String.valueOf(location.getLatitude());
                                mLongitude = String.valueOf(location.getLongitude());
                            }
                        }
                    });
        }else{
            ActivityCompat.requestPermissions(mParent,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1340);
            getLocation();
        }

    }

    public class getPopularFacilitiesTask extends AsyncTask<Void, Void, FacilitySearchDTO> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RecyclerView fieldsView = (RecyclerView) rootView.findViewById(R.id.popular_facilities_view);
            fieldsView.setVisibility(View.GONE);
            ProgressBar bar = (ProgressBar) rootView.findViewById(R.id.progress_bar_popular);
            bar.setIndeterminate(true);
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected FacilitySearchDTO doInBackground(Void... params) {


            RestProperties webProperties = new RestProperties(mContext);
            final UriComponents uri = UriComponentsBuilder.newInstance().scheme(webProperties.getScheme())
                    .host(webProperties.getHost())
                    .path("/pt" + webProperties.getAppBaseUri()
                             + webProperties.getFacilitiesBaseUri()).queryParam("sortBy", "rating").queryParam("limit", 6) .build();
            RestTemplate restTemplate = new RestTemplate();
            System.out.println(uri.toString());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<FacilitySearchDTO> result = restTemplate.getForEntity(uri.toUri(), FacilitySearchDTO.class);

            FacilitySearchDTO response = result.getBody();

            return response;
        }

        @Override
        protected void onPostExecute(final FacilitySearchDTO search) {
            if (search != null) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this.getContext() ,LinearLayoutManager.HORIZONTAL, false);
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
                RecyclerView fieldsView = (RecyclerView) rootView.findViewById(R.id.popular_facilities_view);
                fieldsView.setVisibility(View.VISIBLE);
                ProgressBar bar = (ProgressBar) rootView.findViewById(R.id.progress_bar_popular);
                bar.setVisibility(View.GONE);
                fieldsView.setLayoutManager(staggeredGridLayoutManager);
                fieldsView.setHasFixedSize(true);
                fieldsView.setNestedScrollingEnabled(false);
                fieldsView.setAdapter(new FacilitiesCollapsedCardAdapter(HomeActivity.this.getActivity(), search.getFacilitiesDTO()));
            }
        }
    }
    /*public class getNearbyFacilitiesTask extends AsyncTask<Void, Void, FacilitySearchDTO> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RecyclerView fieldsView = (RecyclerView) rootView.findViewById(R.id.nearby_facilities_view);
            fieldsView.setVisibility(View.GONE);
            ProgressBar bar = (ProgressBar) rootView.findViewById(R.id.progress_bar_nearby);
            bar.setIndeterminate(true);
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected FacilitySearchDTO doInBackground(Void... params) {


            RestProperties webProperties = new RestProperties(mContext);
            final UriComponents uri = UriComponentsBuilder.newInstance().scheme(webProperties.getScheme())
                    .host(webProperties.getHost())
                    .path("/pt" + webProperties.getAppBaseUri()
                            + webProperties.getFacilitiesBaseUri()).queryParam("sortBy", "distance").queryParam("latitude", mLatitude)
                    .queryParam("longitude", mLongitude).queryParam("limit", 6) .build();
            System.out.println(uri.toString());
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<FacilitySearchDTO> result = restTemplate.getForEntity(uri.toUri(), FacilitySearchDTO.class);

            FacilitySearchDTO response = result.getBody();

            return response;
        }

        @Override
        protected void onPostExecute(final FacilitySearchDTO search) {
            if (search.getResultsFound() != 0) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this.getContext() ,LinearLayoutManager.HORIZONTAL, false);
                RecyclerView fieldsView = (RecyclerView) rootView.findViewById(R.id.nearby_facilities_view);
                fieldsView.setVisibility(View.VISIBLE);
                ProgressBar bar = (ProgressBar) rootView.findViewById(R.id.progress_bar_nearby);
                bar.setVisibility(View.GONE);
                fieldsView.setLayoutManager(layoutManager);
                fieldsView.setHasFixedSize(true);
                fieldsView.setNestedScrollingEnabled(false);
                fieldsView.setAdapter(new FacilitiesCollapsedCardAdapter(HomeActivity.this.getActivity(), search.getFacilitiesDTO()));
            }
        }
    }*/

}
