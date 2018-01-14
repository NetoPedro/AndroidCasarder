package mainapplication.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import mainapplication.R;
import mainapplication.adapter.EquipmentsAdapter;
import mainapplication.adapter.SportsFilterAdapter;
import mainapplication.dto.BaseEquipmentDTO;
import mainapplication.restclient.RestProperties;

public class FacilityEquipmentsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_equipments);
        new GetEquipmentsTask().execute();
    }


    public class GetEquipmentsTask extends AsyncTask<Void, Void, BaseEquipmentDTO[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RecyclerView fieldsView = (RecyclerView) findViewById(R.id.view);
            fieldsView.setVisibility(View.GONE);
            ProgressBar bar = (ProgressBar) findViewById(R.id.progress_bar);
            bar.setIndeterminate(true);
            bar.setVisibility(View.VISIBLE);
        }
        @Override
        protected BaseEquipmentDTO[] doInBackground(Void... params) {


            RestProperties webProperties = new RestProperties(FacilityEquipmentsActivity.this);
            final UriComponents uri = UriComponentsBuilder.newInstance().scheme(webProperties.getScheme())
                    .host(webProperties.getHost())
                    .path("/pt" + webProperties.getAppBaseUri()+ webProperties.getFacilitiesBaseUri()  + "/" + getIntent().getIntExtra("id", 0)
                            + "/BaseEquipment").build();

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<BaseEquipmentDTO[]> result = restTemplate.getForEntity(uri.toUri(), BaseEquipmentDTO[].class);

            BaseEquipmentDTO[] response = result.getBody();

            return response;
        }

        @Override
        protected void onPostExecute(final BaseEquipmentDTO[] sports) {

            if (sports != null) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(FacilityEquipmentsActivity.this ,LinearLayoutManager.VERTICAL, false);
                RecyclerView fieldsView = (RecyclerView) findViewById(R.id.view);
                fieldsView.setVisibility(View.VISIBLE);
                ProgressBar bar = (ProgressBar) findViewById(R.id.progress_bar);
                bar.setVisibility(View.GONE);
                fieldsView.setLayoutManager(layoutManager);
                fieldsView.setHasFixedSize(true);
                fieldsView.setNestedScrollingEnabled(false);
                fieldsView.setAdapter(new EquipmentsAdapter(FacilityEquipmentsActivity.this, sports));

            }
        }
    }
}
