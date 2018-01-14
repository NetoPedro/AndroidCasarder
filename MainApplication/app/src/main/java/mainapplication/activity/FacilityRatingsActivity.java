package mainapplication.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import mainapplication.R;
import mainapplication.adapter.RatingsAdapter;
import mainapplication.dto.FacilityDTO;
import mainapplication.dto.RatingDTO;
import mainapplication.restclient.RestProperties;

public class FacilityRatingsActivity extends AppCompatActivity {

    FacilityDTO facility;

    GetRatingsTask task;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_ratings);
        facility = (FacilityDTO) getIntent().getSerializableExtra("facility");
        setUpRatings();
        task = new GetRatingsTask();
        task.execute();
    }

    @Override
    public void finish() {
        super.finish();
        task.cancel(true);
    }

    private void setUpRatings(){

        /** global ratings */
        TextView averageRating = (TextView) findViewById(R.id.rating_value);
        averageRating.setText(String.valueOf(facility.getAverageRating()).substring(0, 3));
        RatingBar averageRatingBar = (RatingBar) findViewById(R.id.rating);
        averageRatingBar.setRating(Float.valueOf(String.valueOf(facility.getAverageRating())));

        /** price ratings */
        TextView priceRating = (TextView) findViewById(R.id.price_rating_value);
        priceRating.setText(String.valueOf(facility.getAverageRating()).substring(0, 3));
        RatingBar priceRatingBar = (RatingBar) findViewById(R.id.access_rating);
        priceRatingBar.setRating(Float.valueOf(String.valueOf(facility.getAverageRating())));

        /** premises ratings */
        TextView premisesRating = (TextView) findViewById(R.id.premises_rating_value);
        premisesRating.setText(String.valueOf(facility.getAverageRating()).substring(0, 3));
        RatingBar premisesRatingBar = (RatingBar) findViewById(R.id.premises_rating);
        premisesRatingBar.setRating(Float.valueOf(String.valueOf(facility.getAverageRating())));

        /** access ratings */
        TextView accessRating = (TextView) findViewById(R.id.access_rating_value);
        accessRating.setText(String.valueOf(facility.getAverageRating()).substring(0, 3));
        RatingBar accessRatingBar = (RatingBar) findViewById(R.id.price_rating);
        accessRatingBar.setRating(Float.valueOf(String.valueOf(facility.getAverageRating())));
    }

    public class GetRatingsTask extends AsyncTask<Void, Void, RatingDTO> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.ratings_view).setVisibility(View.GONE);
            findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
            findViewById(R.id.no_results_label).setVisibility(View.GONE);

        }

        @Override
        protected RatingDTO doInBackground(Void... params) {


            RestProperties webProperties = new RestProperties(FacilityRatingsActivity.this);
            final UriComponents uri = UriComponentsBuilder.newInstance().scheme(webProperties.getScheme())
                    .host(webProperties.getHost())
                    .path("/pt" + webProperties.getAppBaseUri()
                            + webProperties.getRatingsBaseUri()).queryParam("facilityId", facility.getId()).build();
            System.out.println(uri.toString());
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<RatingDTO> result;
            try {
                 result = restTemplate.getForEntity(uri.toUri(), RatingDTO.class);
            }catch (HttpClientErrorException e){
                result = new ResponseEntity<RatingDTO>(HttpStatus.NO_CONTENT);

            }
            RatingDTO response = result.getBody();

            return response;
        }

        @Override
        protected void onPostExecute(final RatingDTO ratings) {
            if (ratings != null) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(FacilityRatingsActivity.this ,LinearLayoutManager.VERTICAL, false);
                RecyclerView fieldsView = (RecyclerView) findViewById(R.id.ratings_view);
                findViewById(R.id.progress_bar).setVisibility(View.GONE);
                fieldsView.setVisibility(View.VISIBLE);
                fieldsView.setLayoutManager(layoutManager);
                fieldsView.setHasFixedSize(true);
                fieldsView.setAdapter(new RatingsAdapter(FacilityRatingsActivity.this, ratings));
            }else{
                findViewById(R.id.no_results_label).setVisibility(View.VISIBLE);
                findViewById(R.id.progress_bar).setVisibility(View.GONE);
            }
        }
    }

}
