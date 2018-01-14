package mainapplication.activity;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;

import mainapplication.R;
import mainapplication.adapter.TimeSlotsAdapter;
import mainapplication.dto.FacilityDTO;
import mainapplication.dto.TimeSlotDTO;
import mainapplication.restclient.RestProperties;


public class TimeSlotsActivity extends AppCompatActivity {

    private FacilityDTO mFacility;
    private String selectedDate;
    private TimeSlotsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slots);
        mFacility = (FacilityDTO) getIntent().getSerializableExtra("facility");
        CalendarView calendar = (CalendarView) findViewById(R.id.calendar_view);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                String newMonth, newDay;
                month++;
                if(month<10){
                    newMonth = "0" + month;
                }else{
                    newMonth = String.valueOf(month);
                }
                if(dayOfMonth<10){
                    newDay = "0" + dayOfMonth;
                }else{
                    newDay = String.valueOf(dayOfMonth);
                }

                selectedDate = year + "-" + newMonth + "-" + newDay;
                new getTimeSlotsTask().execute();
            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        selectedDate = sdf.format(new Date(calendar.getDate()));
        new getTimeSlotsTask().execute();

    }


    public class getTimeSlotsTask extends AsyncTask<Void, Void, TimeSlotDTO[]> {

        TextView text = (TextView) findViewById(R.id.no_results);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            text.setVisibility(View.GONE);
            findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
        }

        @Override
        protected TimeSlotDTO[] doInBackground(Void... params) {

            RestProperties webProperties = new RestProperties(TimeSlotsActivity.this);
            final UriComponents uri = UriComponentsBuilder.newInstance().scheme(webProperties.getScheme())
                    .host(webProperties.getHost())
                    .path("/pt" + webProperties.getAppBaseUri()
                            + webProperties.getTimeSlotsBaseUri()).queryParam("facilityId", mFacility.getId()).queryParam("availableOn", selectedDate).build();

            System.out.println(uri);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<TimeSlotDTO[]> result = restTemplate.getForEntity(uri.toUri(), TimeSlotDTO[].class);

            TimeSlotDTO[] response = result.getBody();

            return response;
        }

        @Override
        protected void onPostExecute(final TimeSlotDTO[] timeslots) {
            findViewById(R.id.progress_bar).setVisibility(View.GONE);
            if (timeslots.length != 0) {
                if(adapter == null){
                    LinearLayoutManager layoutManager = new LinearLayoutManager(TimeSlotsActivity.this, LinearLayoutManager.VERTICAL, false);
                    RecyclerView fieldsView = (RecyclerView) findViewById(R.id.time_slots_view);

                    fieldsView.setLayoutManager(layoutManager);
                    fieldsView.setHasFixedSize(true);
                    fieldsView.setNestedScrollingEnabled(false);
                    adapter = new TimeSlotsAdapter(TimeSlotsActivity.this, mFacility, timeslots);
                    fieldsView.setAdapter(adapter);
                }else{
                    adapter.updateData(timeslots);

                }
            }else{
                text.setVisibility(View.VISIBLE);
            }
        }

    }
}
