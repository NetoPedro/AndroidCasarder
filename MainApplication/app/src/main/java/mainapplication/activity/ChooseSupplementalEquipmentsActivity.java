package mainapplication.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import mainapplication.R;
import mainapplication.adapter.SupplementalEquipmentsAdapter;
import mainapplication.dto.FacilityDTO;
import mainapplication.dto.SupplementalEquipmentDTO;
import mainapplication.restclient.RestProperties;

public class ChooseSupplementalEquipmentsActivity extends AppCompatActivity {

    private FacilityDTO mFacility;
    private SupplementalEquipmentsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_supplemental_equipments);
        mFacility = (FacilityDTO) getIntent().getSerializableExtra("facility");
        new GetEquipmentsTask().execute();
        Button continueButton = (Button) findViewById(R.id.continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChooseSupplementalEquipmentsActivity.this, InviteFriendsActivity.class).putExtra("facility", mFacility)
                        .putExtra("timeslot", getIntent().getSerializableExtra("timeslot"))
                        .putExtra("equipments", adapter.selectedEquipments.toArray(new SupplementalEquipmentDTO[adapter.selectedEquipments.size()])));
            }
        });
    }

    public class GetEquipmentsTask extends AsyncTask<Void, Void, SupplementalEquipmentDTO[]> {

        @Override
        protected SupplementalEquipmentDTO[] doInBackground(Void... params) {

            RestProperties webProperties = new RestProperties(ChooseSupplementalEquipmentsActivity.this);
            final UriComponents uri = UriComponentsBuilder.newInstance().scheme(webProperties.getScheme())
                    .host(webProperties.getHost())
                    .path("/pt" + webProperties.getAppBaseUri()
                            + webProperties.getFacilitiesBaseUri() +"/" + mFacility.getId() + "/SupplementalEquipment").build();

            System.out.println(uri);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<SupplementalEquipmentDTO[]> result = restTemplate.getForEntity(uri.toUri(), SupplementalEquipmentDTO[].class);

            SupplementalEquipmentDTO[] response = result.getBody();

            return response;
        }

        @Override
        protected void onPostExecute(final SupplementalEquipmentDTO[] equipments) {
            if (equipments != null) {
                    RecyclerView equipmentView = (RecyclerView) findViewById(R.id.equipments_view);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(ChooseSupplementalEquipmentsActivity.this, LinearLayoutManager.VERTICAL, false);
                    equipmentView.setLayoutManager(layoutManager);
                    equipmentView.setHasFixedSize(true);
                    equipmentView.setNestedScrollingEnabled(false);
                    adapter = new SupplementalEquipmentsAdapter(ChooseSupplementalEquipmentsActivity.this, equipments);
                    equipmentView.setAdapter(adapter);
            }
        }

    }
}
