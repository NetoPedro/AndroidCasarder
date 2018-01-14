package mainapplication.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import mainapplication.dto.BookingDTO;
import mainapplication.dto.FacilityDTO;
import mainapplication.dto.SolrFacilityDTO;
import mainapplication.dto.SupplementalEquipmentDTO;
import mainapplication.dto.TimeSlotDTO;
import mainapplication.restclient.RestProperties;
import mainapplication.utils.DownloadImageTask;
import pl.droidsonroids.gif.GifImageView;

public class FinishBookingActivity extends AppCompatActivity {


    private FacilityDTO facility ;
    private TimeSlotDTO timeslot ;
    private SupplementalEquipmentDTO[] equipments;
    private String[] invites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_booking);
        Intent extras = getIntent();

        facility = (FacilityDTO) extras.getSerializableExtra("facility");
        if (facility == null){
            facility = new FacilityDTO((SolrFacilityDTO) extras.getSerializableExtra("solrFacility"));
        }
        timeslot = (TimeSlotDTO) extras.getSerializableExtra("timeslot");
        equipments = (SupplementalEquipmentDTO[]) extras.getSerializableExtra("equipments");
        invites = (String[]) extras.getSerializableExtra("invites");
        GifImageView image = (GifImageView) findViewById(R.id.facility_image);
        new DownloadImageTask(facility.getId(), image, facility.getImageURL()).execute();
        TextView facilityName = (TextView) findViewById(R.id.facility_name);
        facilityName.setText(facility.getName());
        TextView date = (TextView) findViewById(R.id.date);
        date.setText(timeslot.getBeginTime().split(" ")[0]);
        TextView beginHour = (TextView) findViewById(R.id.begin_hour);
        beginHour.setText(timeslot.getBeginTime().split(" ")[1] + " " + timeslot.getBeginTime().split(" ")[2]);
        TextView endHour = (TextView) findViewById(R.id.end_hour);
        endHour.setText(timeslot.getEndTime().split(" ")[1] + " " + timeslot.getBeginTime().split(" ")[2]);
        TextView numberOfEquipments = (TextView) findViewById(R.id.number_equipments);
        numberOfEquipments.setText(String.valueOf(equipments.length));
        TextView numberOfInvites = (TextView) findViewById(R.id.number_invites);
        numberOfInvites.setText(String.valueOf(invites.length));
        TextView totalPrice = (TextView) findViewById(R.id.total_price);

        float equipmentsPrice = 0;
        for (int i = 0; i < equipments.length; i++) {
            equipmentsPrice+=equipments[i].getPrice();
        }
        float price = (float) (equipmentsPrice + timeslot.getPrice());
        totalPrice.setText(String.valueOf(price));

        Button book = (Button) findViewById(R.id.book_button);

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    new PostBookingTask().execute();
            }
        });
    }

    public class PostBookingTask extends AsyncTask<Void, Void, HttpStatus> {

        @Override
        protected HttpStatus doInBackground(Void... params) {

            RestProperties webProperties = new RestProperties(FinishBookingActivity.this);
            final UriComponents uri = UriComponentsBuilder.newInstance().scheme(webProperties.getScheme())
                    .host(webProperties.getHost())
                    .path("/pt" + webProperties.getAppBaseUri()
                            + webProperties.getTimeSlotsBaseUri() + "/" + timeslot.getId() + "/Book").build();

            HttpHeaders headers = new HttpHeaders();
            String token = getSharedPreferences("userPreferences", MODE_PRIVATE).getString("token", null);
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            Map<String, Object> body = new HashMap<>();
            int[] ids = new int[equipments.length];
            for (int i = 0; i < equipments.length; i++) {
                ids[i] = equipments[i].getId();
            }

            body.put("SupplementalEquipmentIds", ids);
            body.put("Participants", invites);


            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<BookingDTO> result;
            HttpStatus response;
            try {
                 result = restTemplate.exchange(uri.toUri(), HttpMethod.POST, request, BookingDTO.class);
                response = result.getStatusCode();
            }catch (HttpClientErrorException e){
                response = HttpStatus.UNAUTHORIZED;
            }



            return response;
        }


        @Override
        protected void onPostExecute(final HttpStatus status) {
            if(status.equals(HttpStatus.CREATED)){
                Toast.makeText(FinishBookingActivity.this, "Booking Successful", Toast.LENGTH_LONG).show();
                Intent newIntent = new Intent(FinishBookingActivity.this, MainActivity.class);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(newIntent);
                finish();
            }else if(status.equals(HttpStatus.UNAUTHORIZED)){
                Toast.makeText(FinishBookingActivity.this, "You must login before booking a facility", Toast.LENGTH_LONG).show();
                startActivity(new Intent(FinishBookingActivity.this, LoginActivity.class));
            }
        }

    }
}
