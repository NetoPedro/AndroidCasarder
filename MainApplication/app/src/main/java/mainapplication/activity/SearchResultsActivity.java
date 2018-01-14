package mainapplication.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import mainapplication.R;
import mainapplication.adapter.FacilitiesCardAdapter;
import mainapplication.dto.FacilityDTO;
import mainapplication.dto.FacilitySearchDTO;
import mainapplication.dto.ResponseDTO;
import mainapplication.dto.SolrFacilityDTO;
import mainapplication.dto.SolrSearchResponseDTO;
import mainapplication.dto.SportDTO;
import mainapplication.restclient.RestProperties;
import mainapplication.utils.EndlessRecyclerViewScrollListener;
import mainapplication.utils.SearchFilters;

public class SearchResultsActivity extends AppCompatActivity {

    String name;
    String locality;
    String mLatitude;
    String mLongitude;
    boolean mResultSelected;
    boolean loading = false;
    private SolrFacilityDTO[] mFacilities;
    private FacilitiesCardAdapter adapter;
    private RecyclerView recyclerView;
    private Parcelable recyclerViewState;
    private getSearchResultsTask task;
    private Intent mapIntent;
    private boolean loadMore = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SearchFilters.clearFilters();
        setContentView(R.layout.activity_search_results);
        name = getIntent().getStringExtra("name");
        locality = getIntent().getStringExtra("city");
        if(name == null){
            name ="";
        }
        if(locality == null){
            locality ="";
        }

        mResultSelected = getIntent().getBooleanExtra("resultSelected", false);
        Button filter = (Button) findViewById(R.id.filter_button);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchResultsActivity.this, FilterResultsPopupActivity.class));
            }
        });

        setUpRecyclerView();
        setUpSortTab();
    }


    @Override
    protected void onStart() {
        super.onStart();
        getLocation();
        task = new getSearchResultsTask(0);
        task.execute();
    }

    @Override
    protected void onStop() {
        super.onStop();
        task.cancel(true);
    }

    private void getLocation(){
        FusedLocationProviderClient  mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ContextCompat.checkSelfPermission(this,
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
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1340);
            getLocation();
        }

    }

    public void setUpRecyclerView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(SearchResultsActivity.this, LinearLayoutManager.VERTICAL, false);
         recyclerView = (RecyclerView) findViewById(R.id.search_results);

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
               if(loadMore) {
                   loading = true;
                   task = new getSearchResultsTask(totalItemsCount);
                   task.execute();
               }
            }
        });
        adapter = new FacilitiesCardAdapter(SearchResultsActivity.this, mFacilities);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    public void setUpSortTab(){
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sort_tab);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String sortType = tab.getText().toString();
                SearchFilters.setOrderByDistance(false);
                SearchFilters.setOrderByName(false);
                SearchFilters.setOrderByRating(false);
                switch(sortType){
                    case "Name":
                        SearchFilters.setOrderByName(true);
                        break;
                    case "Distance":
                        SearchFilters.setOrderByDistance(true);
                        break;
                    case "Rating":
                        SearchFilters.setOrderByRating(true);
                }
                task.cancel(true);
                task = new getSearchResultsTask(0);
                task.execute();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                String sortType = tab.getText().toString();
                SearchFilters.setOrderByDistance(false);
                SearchFilters.setOrderByName(false);
                SearchFilters.setOrderByRating(false);
                switch(sortType){
                    case "Name":
                        SearchFilters.setOrderByName(true);
                        break;
                    case "Distance":
                        SearchFilters.setOrderByDistance(true);
                        break;
                    case "Rating":
                        SearchFilters.setOrderByRating(true);
                }
                task.cancel(true);
                task = new getSearchResultsTask(0);
                task.execute();
            }
        });
    }



    public class getSearchResultsTask extends AsyncTask<Void, Void, ResponseDTO> {

        private int mOffset;

        public getSearchResultsTask(int offset) {
            this.mOffset = offset;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.no_results_label).setVisibility(View.GONE);
            if(mOffset == 0){
                recyclerView.setVisibility(View.GONE);
                findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
            }else{
                findViewById(R.id.load_more_progress_bar).setVisibility(View.VISIBLE);
            }
            recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
            findViewById(R.id.map_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //empty for a reason
                }
            });
        }


        @Override
        protected ResponseDTO doInBackground(Void... params) {

            RestProperties webProperties = new RestProperties(SearchResultsActivity.this);
            UriComponents uri;
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance().scheme(webProperties.getNormalScheme())
                    .host(webProperties.getDnsHost()).port(webProperties.getSolrPort())
                    .path(webProperties.getSolrBaseUri() + webProperties.getFacilitiesBaseUri() + "/select").queryParam("start", mOffset);
            String q = "", fq = "";

            if(!name.isEmpty() && locality.isEmpty()) {
                if (mResultSelected){
                    q+=("name:" + name);
                }else{
                    q+=("name:" + name + "*");
                }
            }
            if(!locality.isEmpty() && name.isEmpty()){
                if(mResultSelected) {
                    q+=("city:" + locality);
                }else{
                    q+=("city:" + locality + "*");
                }
            }
            if(!locality.isEmpty() && !name.isEmpty()){
                q+=("city:" + locality + "* OR name:" + name + "*");
            }

            if(SearchFilters.getRating() != 0) {
                if (!fq.isEmpty()) {
                    fq += (" AND averageRating:[" + SearchFilters.getRating() + " TO *]");
                } else {
                    fq += ("averageRating:[" + SearchFilters.getRating() + " TO *]");
                }
            }

            if(SearchFilters.getPriceRating() != 0) {
                if (!fq.isEmpty()) {
                    fq += (" AND averagePriceRating:[" + SearchFilters.getPriceRating() + " TO *]");
                } else {
                    fq += ("averagePriceRating:[" + SearchFilters.getPriceRating() + " TO *]");
                }
            }
            if(SearchFilters.getPremisesRating() != 0) {
                if (!fq.isEmpty()) {
                    fq += (" AND averagePremisesRating:[" + SearchFilters.getPremisesRating() + " TO *]");
                } else {
                    fq += ("averagePremisesRating:[" + SearchFilters.getPremisesRating() + " TO *]");
                }
            }
            if(SearchFilters.getAccessRating() != 0) {
                if (!fq.isEmpty()) {
                    fq += (" AND averageAccessesRating:[" + SearchFilters.getAccessRating() + " TO *]");
                } else {
                    fq += ("averageAccessesRating:[" + SearchFilters.getAccessRating() + " TO *]");
                }
            }

            List<Integer> cats;
            String ids1 =null, ids2 = null;
            if(SearchFilters.getCategories().size() != 0){
                cats = SearchFilters.getCategories();
                ids1 = cats.get(0).toString();
                for (int i = 1; i < cats.size(); i++) {
                    ids1+=" OR " + cats.get(i);
                }

            }

            List<Integer> sports;
            if(SearchFilters.getSports().size() != 0) {
                sports = SearchFilters.getSports();
                ids2 = sports.get(0).toString();
                for (int i = 1; i < sports.size(); i++) {
                    ids2 += " OR " + sports.get(i);
                }
            }
            if(!fq.isEmpty()) {
                if (ids1 != null && ids2 != null) {
                    fq += (" AND (categories:(" + ids1 + ") OR sports:(" + ids2 + "))");
                } else if (ids1 == null && ids2 != null) {
                    fq += (" AND sports:(" + ids2 + ")");
                } else  if (ids1 != null && ids2 == null){
                    fq += (" AND categories:(" + ids1 + ")");
                }
            }else{
                if (ids1 != null && ids2 != null) {
                    fq += ("(categories:(" + ids1 + ") OR sports:(" + ids2 + "))");
                } else if (ids1 == null && ids2 != null) {
                    fq += ("sports:(" + ids2 + ")");
                } else if (ids1 != null && ids2 == null){
                    fq += ("categories:(" + ids1 + ")");
                }
            }


            if(SearchFilters.isOrderByDistance()){
                if(fq.isEmpty()) {
                    if(SearchFilters.getDistance() == 0) {
                        fq += ("{!geofilt}&sfield=lanlon&pt=" + mLatitude + "," + mLongitude + "&d=" + 10000);
                    }else{
                        fq += ("{!geofilt}&sfield=lanlon&pt=" + mLatitude + "," + mLongitude + "&d=" + SearchFilters.getDistance());
                    }
                }else{
                    if(SearchFilters.getDistance() == 0) {
                        fq += (" AND {!geofilt}&sfield=lanlon&pt=" + mLatitude + "," + mLongitude + "&d=" + 10000);
                    }else{
                        fq += (" AND {!geofilt}&sfield=lanlon&pt=" + mLatitude + "," + mLongitude + "&d=" + SearchFilters.getDistance());
                    }
                }
            }
            uriBuilder.queryParam("q", q);
            if(!fq.isEmpty()) {
                uriBuilder.queryParam("fq", fq);
            }

            if(SearchFilters.isOrderByRating()){
                uriBuilder.queryParam("sort", "averageRating desc");
            }else if(SearchFilters.isOrderByDistance()){
                uriBuilder.queryParam("sort", "geodist() asc");
            }else{
                uriBuilder.queryParam("sort", "weight desc");
            }

            uri = uriBuilder.build();
            System.out.println(uri.toString());
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<SolrSearchResponseDTO> result = restTemplate.getForEntity(uri.toUri(), SolrSearchResponseDTO.class);

            ResponseDTO response = result.getBody().getResponse();
            System.out.println(result.getBody().getResponse().getDocs().length);

            return response;
        }

        @Override
        protected void onPostExecute(final ResponseDTO search) {
            if(mOffset == 0){
                findViewById(R.id.progress_bar).setVisibility(View.GONE);
            }else{
                findViewById(R.id.load_more_progress_bar).setVisibility(View.GONE);
            }
            if (search.getNumFound()!= 0) {
                recyclerView.setVisibility(View.VISIBLE);
                mFacilities = search.getDocs();
                if(mOffset != 0){
                    adapter.addData(mFacilities);
                }else {
                    adapter.updateData(mFacilities);

                }
                mapIntent = new Intent(SearchResultsActivity.this, FacilitiesMapActivity.class);
                mapIntent.putExtra("facilities", adapter.getItems());
                findViewById(R.id.map_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(mapIntent);
                    }
                });
            }else{
                if(mOffset == 0) {
                    loadMore = false;
                    findViewById(R.id.no_results_label).setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }

    }
}