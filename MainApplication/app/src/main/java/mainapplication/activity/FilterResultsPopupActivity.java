package mainapplication.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import mainapplication.R;
import mainapplication.adapter.CategoriesFilterAdapter;
import mainapplication.adapter.SportsFilterAdapter;
import mainapplication.dto.CategoryDTO;
import mainapplication.dto.SportDTO;
import mainapplication.restclient.RestProperties;
import mainapplication.utils.SearchFilters;

public class FilterResultsPopupActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_results_popup);
        setUpDistanceSubMenu();
        setUpRatingsSubMenu();
        setUpCategoriesSubMenu();
        setUpSportsSubMenu();
        setUpSearchButton();
    }


    private void setUpDistanceSubMenu(){
        final TextView distanceValue = (TextView) findViewById(R.id.distance_value);
        distanceValue.setText("0km");
        final SeekBar distanceBar = (SeekBar) findViewById(R.id.distance_slider);
        distanceBar.setMax(250);
        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                distanceValue.setText(String.valueOf(progress) + "km");
                SearchFilters.setDistance(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };
        distanceBar.setOnSeekBarChangeListener(listener);
        final Button distanceButton = (Button) findViewById(R.id.distance_button);
        final LinearLayout layout = (LinearLayout) findViewById(R.id.distance_chooser_layout);
        if(SearchFilters.getDistance() != 0){
            SeekBar distance = (SeekBar) findViewById(R.id.distance_slider);
            listener.onProgressChanged(distance, SearchFilters.getDistance(), false);
        }
        distanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(layout.getVisibility() == View.GONE){
                    layout.setVisibility(View.VISIBLE);
                    distanceButton.setForeground(getResources().getDrawable(R.drawable.arrow_up_icon, null));
                }else{
                    layout.setVisibility(View.GONE);
                    distanceButton.setForeground(getResources().getDrawable(R.drawable.arrow_down_icon, null));
                }
            }
        });
    }

    private void setUpRatingsSubMenu(){
        final Button ratingButton = (Button) findViewById(R.id.rating_button);
        final LinearLayout layout = (LinearLayout) findViewById(R.id.rating_chooser_layout);
        if(SearchFilters.getRating() != 0){
            RatingBar rating = (RatingBar) findViewById(R.id.rating_chooser);
            rating.setRating(SearchFilters.getRating());
        }

        if(SearchFilters.getPriceRating() != 0){
            RatingBar rating = (RatingBar) findViewById(R.id.price_rating_chooser);
            rating.setRating(SearchFilters.getPriceRating());
        }
        if(SearchFilters.getPremisesRating() != 0){
            RatingBar rating = (RatingBar) findViewById(R.id.premises_rating_chooser);
            rating.setRating(SearchFilters.getPremisesRating());
        }
        if(SearchFilters.getAccessRating() != 0){
            RatingBar rating = (RatingBar) findViewById(R.id.access_rating_chooser);
            rating.setRating(SearchFilters.getAccessRating());
        }

        ratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(layout.getVisibility() == View.GONE){
                    layout.setVisibility(View.VISIBLE);
                    ratingButton.setForeground(getResources().getDrawable(R.drawable.arrow_up_icon, null));
                }else{
                    layout.setVisibility(View.GONE);
                    ratingButton.setForeground(getResources().getDrawable(R.drawable.arrow_down_icon, null));
                }
            }
        });


    }

    private void setUpCategoriesSubMenu(){
        final Button categoriesButton = (Button) findViewById(R.id.categories_button);
        final LinearLayout layout = (LinearLayout) findViewById(R.id.categories_chooser_layout);
        categoriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(layout.getVisibility() == View.GONE){
                    layout.setVisibility(View.VISIBLE);
                    categoriesButton.setForeground(getResources().getDrawable(R.drawable.arrow_up_icon, null));
                }else{
                    layout.setVisibility(View.GONE);
                    categoriesButton.setForeground(getResources().getDrawable(R.drawable.arrow_down_icon, null));
                }
            }
        });
        new GetCategoriesTask().execute();
    }

    private void setUpSportsSubMenu(){
        final Button sportsButton = (Button) findViewById(R.id.sports_button);
        final LinearLayout layout = (LinearLayout) findViewById(R.id.sports_chooser_layout);
        sportsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(layout.getVisibility() == View.GONE){
                    layout.setVisibility(View.VISIBLE);
                    sportsButton.setForeground(getResources().getDrawable(R.drawable.arrow_up_icon, null));
                }else{
                    layout.setVisibility(View.GONE);
                    sportsButton.setForeground(getResources().getDrawable(R.drawable.arrow_down_icon, null));
                }
            }
        });
        new GetSportsTask().execute();
    }

    private void setUpSearchButton(){
        final Button search = (Button) findViewById(R.id.search_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RatingBar rating = (RatingBar) findViewById(R.id.rating_chooser);
                SearchFilters.setRating(rating.getRating());
                RatingBar pricerating = (RatingBar) findViewById(R.id.price_rating_chooser);
                SearchFilters.setPriceRating(pricerating.getRating());
                RatingBar premisesrating = (RatingBar) findViewById(R.id.premises_rating_chooser);
                SearchFilters.setPremisesRating(premisesrating.getRating());
                RatingBar accessrating = (RatingBar) findViewById(R.id.access_rating_chooser);
                SearchFilters.setAccessRating(accessrating.getRating());
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SearchFilters.clearFilters();
    }

    public class GetCategoriesTask extends AsyncTask<Void, Void, CategoryDTO[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RecyclerView fieldsView = (RecyclerView) findViewById(R.id.categories_chooser);
            fieldsView.setVisibility(View.GONE);
            ProgressBar bar = (ProgressBar) findViewById(R.id.load_more_progress_bar);
            bar.setIndeterminate(true);
            bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected CategoryDTO[] doInBackground(Void... params) {


            RestProperties webProperties = new RestProperties(FilterResultsPopupActivity.this);
            final UriComponents uri = UriComponentsBuilder.newInstance().scheme(webProperties.getScheme())
                    .host(webProperties.getHost())
                    .path("/pt" + webProperties.getAppBaseUri()
                            + "/Categories").build();
            RestTemplate restTemplate = new RestTemplate();
            System.out.println(uri.toString());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<CategoryDTO[]> result = restTemplate.getForEntity(uri.toUri(), CategoryDTO[].class);

            CategoryDTO[] response = result.getBody();

            return response;
        }

        @Override
        protected void onPostExecute(final CategoryDTO[] facilities) {
            if (facilities != null) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(FilterResultsPopupActivity.this ,LinearLayoutManager.VERTICAL, false);
                RecyclerView fieldsView = (RecyclerView) findViewById(R.id.categories_chooser);
                fieldsView.setVisibility(View.VISIBLE);
                ProgressBar bar = (ProgressBar) findViewById(R.id.load_more_progress_bar);
                bar.setVisibility(View.GONE);
                fieldsView.setLayoutManager(layoutManager);
                fieldsView.setHasFixedSize(true);
                fieldsView.setNestedScrollingEnabled(false);
                fieldsView.setAdapter(new CategoriesFilterAdapter(FilterResultsPopupActivity.this, facilities));
            }
        }
    }

    public class GetSportsTask extends AsyncTask<Void, Void, SportDTO[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RecyclerView fieldsView = (RecyclerView) findViewById(R.id.sports_chooser);
            fieldsView.setVisibility(View.GONE);
            ProgressBar bar = (ProgressBar) findViewById(R.id.load_more_sports_progress_bar);
            bar.setIndeterminate(true);
            bar.setVisibility(View.VISIBLE);
        }
        @Override
        protected SportDTO[] doInBackground(Void... params) {


            RestProperties webProperties = new RestProperties(FilterResultsPopupActivity.this);
            final UriComponents uri = UriComponentsBuilder.newInstance().scheme(webProperties.getScheme())
                    .host(webProperties.getHost())
                    .path("/pt" + webProperties.getAppBaseUri()
                            + webProperties.getSportsBaseUri()).build();

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<SportDTO[]> result = restTemplate.getForEntity(uri.toUri(), SportDTO[].class);

            SportDTO[] response = result.getBody();

            return response;
        }

        @Override
        protected void onPostExecute(final SportDTO[] sports) {

            if (sports != null) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(FilterResultsPopupActivity.this ,LinearLayoutManager.VERTICAL, false);
                RecyclerView fieldsView = (RecyclerView) findViewById(R.id.sports_chooser);
                fieldsView.setVisibility(View.VISIBLE);
                ProgressBar bar = (ProgressBar) findViewById(R.id.load_more_sports_progress_bar);
                bar.setVisibility(View.GONE);
                fieldsView.setLayoutManager(layoutManager);
                fieldsView.setHasFixedSize(true);
                fieldsView.setNestedScrollingEnabled(false);
                fieldsView.setAdapter(new SportsFilterAdapter(FilterResultsPopupActivity.this, sports));

            }
        }
    }

}
