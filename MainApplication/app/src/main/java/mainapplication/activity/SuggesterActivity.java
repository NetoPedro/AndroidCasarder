package mainapplication.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import mainapplication.R;
import mainapplication.adapter.SportsFilterAdapter;
import mainapplication.adapter.SuggesterFacilitiesAdapter;
import mainapplication.adapter.SuggesterLocationsAdapter;
import mainapplication.dto.SportDTO;
import mainapplication.dto.SuggestDTO;
import mainapplication.dto.SuggesterDTO;
import mainapplication.dto.SuggestionsDTO;
import mainapplication.restclient.RestProperties;

public class SuggesterActivity extends AppCompatActivity {

    public static String text;
    private SuggestLocationsTask locationsTask = new SuggestLocationsTask();
    private SuggestFacilitiesTask facilitiesTask = new SuggestFacilitiesTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggester);
        SearchView search = (SearchView) findViewById(R.id.search_field);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                text = query;
                locationsTask.cancel(true);
                facilitiesTask.cancel(true);
                locationsTask = new SuggestLocationsTask();
                locationsTask.execute();
                facilitiesTask = new SuggestFacilitiesTask();
                facilitiesTask.execute();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                text = newText;
                locationsTask.cancel(true);
                facilitiesTask.cancel(true);
                locationsTask = new SuggestLocationsTask();
                locationsTask.execute();
                facilitiesTask = new SuggestFacilitiesTask();
                facilitiesTask.execute();
                return true;
            }
        });
        search.callOnClick();
        findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent extras = new Intent(SuggesterActivity.this, SearchResultsActivity.class);
                extras.putExtra("city", text);
                extras.putExtra("name", text);
                startActivity(extras);
            }
        });
    }


    public class SuggestLocationsTask extends AsyncTask<Void, Void, SuggestDTO> {

        @Override
        protected SuggestDTO doInBackground(Void... params) {


            RestProperties webProperties = new RestProperties(SuggesterActivity.this);
            final UriComponents uri = UriComponentsBuilder.newInstance().scheme(webProperties.getNormalScheme())
                    .host(webProperties.getDnsHost()).port(webProperties.getSolrPort())
                    .path(webProperties.getSolrBaseUri() +"/locations/suggest").queryParam("suggest.q", text).build();

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<SuggesterDTO> result = restTemplate.getForEntity(uri.toUri(), SuggesterDTO.class);

            SuggestDTO response = result.getBody().getSuggest();

            return response;
        }

        @Override
        protected void onPostExecute(final SuggestDTO suggestions) {

            LinearLayoutManager layoutManager = new LinearLayoutManager(SuggesterActivity.this ,LinearLayoutManager.VERTICAL, false);
            RecyclerView locationsView = (RecyclerView) findViewById(R.id.location_view);
            if (suggestions.getNumFound() != 0) {
                findViewById(R.id.location_layout).setVisibility(View.VISIBLE);
                locationsView.setLayoutManager(layoutManager);
                locationsView.setNestedScrollingEnabled(false);
                locationsView.setAdapter(new SuggesterLocationsAdapter(SuggesterActivity.this, suggestions.getSuggestions()));

            }else{
                findViewById(R.id.location_layout).setVisibility(View.GONE);
            }
        }
    }


    public class SuggestFacilitiesTask extends AsyncTask<Void, Void, SuggestDTO> {

        @Override
        protected SuggestDTO doInBackground(Void... params) {


            RestProperties webProperties = new RestProperties(SuggesterActivity.this);
            final UriComponents uri = UriComponentsBuilder.newInstance().scheme(webProperties.getNormalScheme())
                    .host(webProperties.getDnsHost()).port(webProperties.getSolrPort())
                    .path(webProperties.getSolrBaseUri() + webProperties.getFacilitiesBaseUri() + "/suggest").queryParam("suggest.q", text).build();

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<SuggesterDTO> result = restTemplate.getForEntity(uri.toUri(), SuggesterDTO.class);

            SuggestDTO response = result.getBody().getSuggest();

            return response;
        }

        @Override
        protected void onPostExecute(final SuggestDTO suggestions) {

            LinearLayoutManager layoutManager = new LinearLayoutManager(SuggesterActivity.this ,LinearLayoutManager.VERTICAL, false);
            RecyclerView locationsView = (RecyclerView) findViewById(R.id.facilities_view);
            if (suggestions.getNumFound() != 0) {
                findViewById(R.id.facilities_layout).setVisibility(View.VISIBLE);
                locationsView.setLayoutManager(layoutManager);
                locationsView.setNestedScrollingEnabled(false);
                locationsView.setAdapter(new SuggesterFacilitiesAdapter(SuggesterActivity.this, suggestions.getSuggestions()));

            }else{
                findViewById(R.id.facilities_layout).setVisibility(View.GONE);
            }
        }
    }

}
