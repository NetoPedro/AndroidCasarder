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
import mainapplication.adapter.CategoriesAdapter;
import mainapplication.adapter.EquipmentsAdapter;
import mainapplication.dto.CategoryDTO;
import mainapplication.restclient.RestProperties;

public class FacilityCategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_categories);
        new GetCategoriesTask().execute();
    }


    public class GetCategoriesTask extends AsyncTask<Void, Void, CategoryDTO[]> {

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
        protected CategoryDTO[] doInBackground(Void... params) {


            RestProperties webProperties = new RestProperties(FacilityCategoriesActivity.this);
            final UriComponents uri = UriComponentsBuilder.newInstance().scheme(webProperties.getScheme())
                    .host(webProperties.getHost())
                    .path("/pt" + webProperties.getAppBaseUri()+ webProperties.getFacilitiesBaseUri() + "/" + getIntent().getIntExtra("id", 0)+ "/" +
                            "Categories").build();

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<CategoryDTO[]> result = restTemplate.getForEntity(uri.toUri(), CategoryDTO[].class);

            CategoryDTO[] response = result.getBody();

            return response;
        }

        @Override
        protected void onPostExecute(final CategoryDTO[] sports) {

            if (sports != null) {
                LinearLayoutManager layoutManager = new LinearLayoutManager(FacilityCategoriesActivity.this ,LinearLayoutManager.VERTICAL, false);
                RecyclerView fieldsView = (RecyclerView) findViewById(R.id.view);
                fieldsView.setVisibility(View.VISIBLE);
                ProgressBar bar = (ProgressBar) findViewById(R.id.progress_bar);
                bar.setVisibility(View.GONE);
                fieldsView.setLayoutManager(layoutManager);
                fieldsView.setHasFixedSize(true);
                fieldsView.setNestedScrollingEnabled(false);
                fieldsView.setAdapter(new CategoriesAdapter(FacilityCategoriesActivity.this, sports));

            }
        }
    }
}
