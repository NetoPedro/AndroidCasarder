package mainapplication.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.Map;

import mainapplication.R;
import mainapplication.adapter.EquipmentsListItemAdapter;
import mainapplication.adapter.ParticipantsListItemAdapter;
import mainapplication.dto.BookingDTO;
import mainapplication.dto.TimeSlotDTO;
import mainapplication.restclient.RestProperties;
import mainapplication.utils.DownloadImageTask;
import mainapplication.utils.QRCodeGenerator;
import pl.droidsonroids.gif.GifImageView;

public class BookingActivity extends AppCompatActivity {


    private BookingDTO booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        Intent extras = getIntent();
        booking = (BookingDTO) extras.getSerializableExtra("booking");
        boolean author = extras.getBooleanExtra("author", true);
        GifImageView image = (GifImageView) findViewById(R.id.fac_image);
        new DownloadImageTask(booking.getFacilityId(), image, booking.getImageURL()).execute();
        TextView facilityName = (TextView) findViewById(R.id.fac_name);
        facilityName.setText(booking.getFacilityName());
        TextView date = (TextView) findViewById(R.id.booking_date);
        date.setText(booking.getBeginTime().split(" ")[0]);
        TextView beginHour = (TextView) findViewById(R.id.booking_begin_hour);
        beginHour.setText(booking.getBeginTime().split(" ")[1] + " " + booking.getBeginTime().split(" ")[2]);
        TextView endHour = (TextView) findViewById(R.id.booking_end_hour);
        System.out.println(booking.getBeginTime());
        endHour.setText(booking.getEndTime().split(" ")[1] + " " + booking.getEndTime().split(" ")[2]);


        TextView totalPrice = (TextView) findViewById(R.id.booking_total_price);
        RelativeLayout participantsList = (RelativeLayout) findViewById(R.id.participants_layout);
        RelativeLayout equipmentsList = (RelativeLayout) findViewById(R.id.equipments_layout);
        RelativeLayout priceLayout = (RelativeLayout) findViewById(R.id.price);
        Button cancel = (Button) findViewById(R.id.cancel_button);
        if(author) {
            setUpEquipmentsView();
            setUpParticipantsView();
            float price = Float.parseFloat(String.valueOf(booking.getTotalPrice()));
            totalPrice.setText(String.valueOf(price));
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new CancelBookingTask().execute();
                }
            });
        }else{

            priceLayout.setVisibility(View.GONE);
            participantsList.setVisibility(View.GONE);
            equipmentsList.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
        }


       ImageView qrCode = (ImageView) findViewById(R.id.qr_code);
        qrCode.setImageBitmap(QRCodeGenerator.generateQRCode(booking.getQrCode()));


    }

    private void setUpEquipmentsView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(BookingActivity.this ,LinearLayoutManager.VERTICAL, false);
        final RecyclerView equipmentsList = (RecyclerView) findViewById(R.id.equipments_list_view);
        equipmentsList.setLayoutManager(layoutManager);
        equipmentsList.setHasFixedSize(true);
        equipmentsList.setNestedScrollingEnabled(false);
        equipmentsList.setAdapter(new EquipmentsListItemAdapter(BookingActivity.this, booking.getBookedSupplementalEquipments()));
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(BookingActivity.this ,LinearLayoutManager.VERTICAL, false);
        final RecyclerView participantsList = (RecyclerView) findViewById(R.id.participants_list_view);
        participantsList.setLayoutManager(layoutManager);
        participantsList.setHasFixedSize(true);
        participantsList.setNestedScrollingEnabled(false);
        participantsList.setAdapter(new ParticipantsListItemAdapter(BookingActivity.this, booking.getParticipantsEmail()));
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

    public class CancelBookingTask extends AsyncTask<Void, Void, HttpStatus> {

        @Override
        protected HttpStatus doInBackground(Void... params) {

            RestProperties webProperties = new RestProperties(BookingActivity.this);
            final UriComponents uri = UriComponentsBuilder.newInstance().scheme(webProperties.getScheme())
                    .host(webProperties.getHost())
                    .path("/pt" + webProperties.getAppBaseUri()
                            + webProperties.getTimeSlotsBaseUri() + "/" + booking.getTimeSlotId() + "/Booking/Cancel").build();

            HttpHeaders headers = new HttpHeaders();
            String token = getSharedPreferences("userPreferences", MODE_PRIVATE).getString("token", null);
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<TimeSlotDTO> result;
            HttpStatus response;
            try {
                result = restTemplate.exchange(uri.toUri(), HttpMethod.PATCH, request, TimeSlotDTO.class);
                response = result.getStatusCode();
            }catch (HttpClientErrorException e){
                response = HttpStatus.UNAUTHORIZED;
            }
            return response;
        }


        @Override
        protected void onPostExecute(final HttpStatus status) {
            if(status.equals(HttpStatus.OK)){
                finish();
            }
        }

    }

}
