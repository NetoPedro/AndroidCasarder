package mainapplication.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

import mainapplication.R;
import mainapplication.adapter.EquipmentsListItemAdapter;
import mainapplication.adapter.ParticipantsListItemAdapter;
import mainapplication.dto.AtendeeRatingDTO;
import mainapplication.dto.BookingDTO;
import mainapplication.dto.RatingDTO;
import mainapplication.dto.TimeSlotDTO;
import mainapplication.restclient.RestProperties;
import mainapplication.utils.DownloadImageTask;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Luis Gouveia on 08/01/2018.
 */

public class RateBookingActivity extends AppCompatActivity {


    private BookingDTO booking;
    private boolean isAuthor;
    private float priceRatingValue;
    private float accessRatingValue;
    private float premisesRatingValue;
    private String comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_booking);

        Intent extras = getIntent();
        booking = (BookingDTO) extras.getSerializableExtra("booking");
        isAuthor = extras.getBooleanExtra("author", true);
        GifImageView image = (GifImageView) findViewById(R.id.fac_image);
        new DownloadImageTask(booking.getFacilityId(), image, booking.getImageURL()).execute();
        TextView facilityName = (TextView) findViewById(R.id.fac_name);
        facilityName.setText(booking.getFacilityName());
        TextView date = (TextView) findViewById(R.id.booking_date);
        date.setText(booking.getBeginTime().split(" ")[0]);
        TextView beginHour = (TextView) findViewById(R.id.booking_begin_hour);
        beginHour.setText(booking.getBeginTime().split(" ")[1] + " " + booking.getBeginTime().split(" ")[2]);
        TextView endHour = (TextView) findViewById(R.id.booking_end_hour);
        endHour.setText(booking.getEndTime().split(" ")[1] + " " + booking.getBeginTime().split(" ")[2]);


        TextView totalPrice = (TextView) findViewById(R.id.booking_total_price);
        RelativeLayout participantsList = (RelativeLayout) findViewById(R.id.participants_layout);
        RelativeLayout equipmentsList = (RelativeLayout) findViewById(R.id.equipments_layout);
        RelativeLayout priceLayout = (RelativeLayout) findViewById(R.id.price);
        Button rateButton = (Button) findViewById(R.id.rate_button);

        final RatingBar overall = (RatingBar) findViewById(R.id.overall_rating);
        final RatingBar accessesRating = (RatingBar) findViewById(R.id.access_rating);
        final RatingBar priceRating = (RatingBar) findViewById(R.id.price_rating);
        final RatingBar premisesRating = (RatingBar) findViewById(R.id.premises_rating);

        accessesRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if(isAuthor){
                    overall.setRating((v+priceRating.getRating()+premisesRating.getRating())/3);
                    System.out.println((v+priceRating.getRating()+premisesRating.getRating())/3);
                }else{
                    overall.setRating((v+premisesRating.getRating())/2);
                    System.out.println((v+premisesRating.getRating())/2);
                }
            }
        });

        premisesRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if(isAuthor){
                    overall.setRating((v+priceRating.getRating()+accessesRating.getRating())/3);
                    System.out.println((v+priceRating.getRating()+accessesRating.getRating())/3);
                }else{
                    overall.setRating((v+accessesRating.getRating())/2);
                    System.out.println((v+accessesRating.getRating())/2);
                }
            }
        });

        priceRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                overall.setRating((v+premisesRating.getRating()+accessesRating.getRating())/3);
            }
        });
        TextView priceRatingLabel = (TextView) findViewById(R.id.price_rating_label);

        
        final EditText commentField = (EditText) findViewById(R.id.rating_comment_field);


        if(isAuthor) {
            setUpEquipmentsView();
            setUpParticipantsView();
            float price = Float.parseFloat(String.valueOf(booking.getTotalPrice()));
            totalPrice.setText(String.valueOf(price));
            rateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                if(accessesRating.getRating() != 0 && premisesRating.getRating() != 0 && priceRating.getRating() != 0) {
                    accessRatingValue = accessesRating.getRating();
                    premisesRatingValue = premisesRating.getRating();
                    priceRatingValue = priceRating.getRating();
                    comment = commentField.getText().toString();
                    new RateBookingTask().execute();
                }
                }
            });
        }else{
            priceLayout.setVisibility(View.GONE);
            participantsList.setVisibility(View.GONE);
            equipmentsList.setVisibility(View.GONE);
            priceRating.setVisibility(View.GONE);
            priceRatingLabel.setVisibility(View.GONE);
            rateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(accessesRating.getRating() != 0 && premisesRating.getRating() != 0) {
                        accessRatingValue = accessesRating.getRating();
                        premisesRatingValue = premisesRating.getRating();
                        comment = commentField.getText().toString();
                        new RateBookingTask().execute();
                    }
                }
            });
        }

        fillFieldsIfRated();
    }

    private void fillFieldsIfRated(){
        if(booking.getRating() != null) {
            final AtendeeRatingDTO rating = booking.getRating();
            final RatingBar accessesRating = (RatingBar) findViewById(R.id.access_rating);
            final RatingBar priceRating = (RatingBar) findViewById(R.id.price_rating);
            final RatingBar premisesRating = (RatingBar) findViewById(R.id.premises_rating);
            final EditText commentField = (EditText) findViewById(R.id.rating_comment_field);
            accessesRating.setRating(rating.getAccessesValue());
            premisesRating.setRating(rating.getPremisesValue());
            if(isAuthor){
                priceRating.setRating(rating.getPriceValue());
                findViewById(R.id.rate_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(accessesRating.getRating() != 0 && premisesRating.getRating() != 0 && priceRating.getRating() != 0) {
                            rating.setAccessesValue((int)accessesRating.getRating());
                            rating.setPremisesValue((int)premisesRating.getRating());
                            rating.setPriceValue((int)priceRating.getRating());
                            rating.setComment(commentField.getText().toString());
                            new UpdateRatingTask(rating).execute();
                        }
                    }
                });
            }else{
                findViewById(R.id.rate_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(accessesRating.getRating() != 0 && premisesRating.getRating() != 0) {
                            rating.setAccessesValue((int)accessesRating.getRating());
                            rating.setPremisesValue((int)premisesRating.getRating());
                            rating.setComment(commentField.getText().toString());
                            new UpdateRatingTask(rating).execute();
                        }
                    }
                });
            }
            commentField.setText(rating.getComment());
        }

    }
    private void setUpEquipmentsView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(RateBookingActivity.this ,LinearLayoutManager.VERTICAL, false);
        final RecyclerView equipmentsList = (RecyclerView) findViewById(R.id.equipments_list_view);
        equipmentsList.setLayoutManager(layoutManager);
        equipmentsList.setHasFixedSize(true);
        equipmentsList.setNestedScrollingEnabled(false);
        equipmentsList.setAdapter(new EquipmentsListItemAdapter(RateBookingActivity.this, booking.getBookedSupplementalEquipments()));
        final Button button = (Button) findViewById(R.id.equipments_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(equipmentsList.getVisibility() == View.GONE){
                    equipmentsList.setVisibility(View.VISIBLE);
                    button.setForeground(getResources().getDrawable(R.drawable.arrow_up_icon, null));
                }else{
                    equipmentsList.setVisibility(View.GONE);
                    button.setForeground(getResources().getDrawable(R.drawable.arrow_down_icon, null));
                }
            }
        });
    }

    private void setUpParticipantsView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(RateBookingActivity.this ,LinearLayoutManager.VERTICAL, false);
        final RecyclerView participantsList = (RecyclerView) findViewById(R.id.participants_list_view);
        participantsList.setLayoutManager(layoutManager);
        participantsList.setHasFixedSize(true);
        participantsList.setNestedScrollingEnabled(false);
        participantsList.setAdapter(new ParticipantsListItemAdapter(RateBookingActivity.this, booking.getParticipantsEmail()));
        final Button button = (Button) findViewById(R.id.participants_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(participantsList.getVisibility() == View.GONE){
                    participantsList.setVisibility(View.VISIBLE);
                    button.setForeground(getResources().getDrawable(R.drawable.arrow_up_icon, null));
                }else{
                    participantsList.setVisibility(View.GONE);
                    button.setForeground(getResources().getDrawable(R.drawable.arrow_down_icon, null));
                }
            }
        });
    }

    public class RateBookingTask extends AsyncTask<Void, Void, HttpStatus> {

        @Override
        protected HttpStatus doInBackground(Void... params) {

            RestProperties webProperties = new RestProperties(RateBookingActivity.this);
            final UriComponents uri = UriComponentsBuilder.newInstance().scheme(webProperties.getScheme())
                    .host(webProperties.getHost())
                    .path("/pt" + webProperties.getAppBaseUri()
                            + webProperties.getRatingsBaseUri()).build();

            HttpHeaders headers = new HttpHeaders();
            String token = getSharedPreferences("userPreferences", MODE_PRIVATE).getString("token", null);
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

            Map<String, Object> body = new HashMap<>();

            body.put("BookingId", booking.getId());
            body.put("PremisesValue", (int) premisesRatingValue);
            body.put("AccessesValue", (int) accessRatingValue);
            body.put("PriceValue", (int) priceRatingValue);
            body.put("Comment", comment);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<AtendeeRatingDTO> result;
            HttpStatus response;
            try {
                result = restTemplate.exchange(uri.toUri(), HttpMethod.POST, request, AtendeeRatingDTO.class);
                response = result.getStatusCode();
            }catch (HttpClientErrorException e){
                response = HttpStatus.UNAUTHORIZED;
            }
            return response;
        }


        @Override
        protected void onPostExecute(final HttpStatus status) {
            if(status.equals(HttpStatus.CREATED)){
                finish();
            }
        }

    }

    public class UpdateRatingTask extends AsyncTask<Void, Void, HttpStatus> {

        private AtendeeRatingDTO rating;

        public UpdateRatingTask(AtendeeRatingDTO rating) {
            this.rating = rating;
        }

        @Override
        protected HttpStatus doInBackground(Void... params) {

            RestProperties webProperties = new RestProperties(RateBookingActivity.this);
            final UriComponents uri = UriComponentsBuilder.newInstance().scheme(webProperties.getScheme())
                    .host(webProperties.getHost())
                    .path("/pt" + webProperties.getAppBaseUri()
                            + webProperties.getRatingsBaseUri()+"/" + booking.getRating().getId()).build();

            HttpHeaders headers = new HttpHeaders();
            String token = getSharedPreferences("userPreferences", MODE_PRIVATE).getString("token", null);
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);


            HttpEntity<AtendeeRatingDTO> request = new HttpEntity<>(rating, headers);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<AtendeeRatingDTO> result;
            HttpStatus response;
            try {
                result = restTemplate.exchange(uri.toUri(), HttpMethod.PUT, request, AtendeeRatingDTO.class);
                response = result.getStatusCode();
            }catch (HttpClientErrorException e){
                response = HttpStatus.UNAUTHORIZED;
            }
            return response;
        }


        @Override
        protected void onPostExecute(final HttpStatus status) {
            if(status.equals(HttpStatus.NO_CONTENT)){
                finish();
            }
        }

    }

}

