package mainapplication.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import mainapplication.R;
import mainapplication.adapter.BookingsCardAdapter;
import mainapplication.adapter.FacilitiesCardAdapter;
import mainapplication.dto.BookingDTO;
import mainapplication.restclient.RestProperties;
import mainapplication.utils.EndlessRecyclerViewScrollListener;

import static android.content.Context.MODE_PRIVATE;

public class MyBookingsActivity extends Fragment {

    private View rootView;
    private Context mContext;
    private Activity parent;
    private BookingDTO[] mBookings;
    private GetBookingsTask task;
    private boolean author = true;
    boolean loading = false;
    private static boolean pageAuthor = true;
    private BookingsCardAdapter adapter;
    private RecyclerView recyclerView;
    private boolean loadMore = true;


    public static MyBookingsActivity newInstance() {
        MyBookingsActivity fragment = new MyBookingsActivity();
        return fragment;
    }

    public void setParent(Activity parent) {
        this.parent = parent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.activity_my_bookings, container, false);
        mContext = rootView.getContext();
        task = new GetBookingsTask(0);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.booking_tab_layout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().toString().equalsIgnoreCase(getResources().getString(R.string.author))){
                    author = true;
                    pageAuthor = true;
                }else{
                    author = false;
                    pageAuthor = false;
                }
                task = new GetBookingsTask(0);
                task.execute();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setUpRecyclerView();
        task.execute();

        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();
        if(!task.isCancelled()){
            task.cancel(true);
            task = new GetBookingsTask(0);
            task.execute();
        }else{
            task = new GetBookingsTask(0);
            task.execute();
        }

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.booking_tab_layout);
        if(pageAuthor){
            tabLayout.getTabAt(0).select();
        }else{
            tabLayout.getTabAt(1).select();
        }

    }

    @Override
    public void onPause() {
        task.cancel(true);
        super.onPause();

    }

    @Override
    public void onStop() {
        task.cancel(true);
        super.onStop();

    }


    public void setUpRecyclerView(){
        recyclerView = (RecyclerView) rootView.findViewById(R.id.next_bookings_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new BookingsCardAdapter(parent, mContext, mBookings, author));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                if(loadMore){
                    loading = true;
                task = new GetBookingsTask(totalItemsCount);
                task.execute();
                }
            }
        });
        adapter = new BookingsCardAdapter(parent, mContext,mBookings,author);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }
    public class GetBookingsTask extends AsyncTask<Void, Void, BookingDTO[]> {

        private int mOffset;
        private Parcelable recyclerViewState;

        TextView noBookings = (TextView) rootView.findViewById(R.id.no_results_label);

        public GetBookingsTask(int offset) {
            this.mOffset = offset;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            noBookings.setVisibility(View.GONE);
            if(mOffset == 0){
                recyclerView.setVisibility(View.GONE);
                rootView.findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
            }else{
                rootView.findViewById(R.id.load_more_progress_bar).setVisibility(View.VISIBLE);
            }
            recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

        }

        @Override
        protected BookingDTO[] doInBackground(Void... params) {

            RestProperties webProperties = new RestProperties(mContext);
            Calendar fromDate = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            int day = (Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            Calendar toDate = Calendar.getInstance();
            toDate.set(Calendar.DAY_OF_MONTH, day+30);
            final UriComponents uri = UriComponentsBuilder.newInstance().scheme(webProperties.getScheme())
                    .host(webProperties.getHost())
                    .path("/pt" + webProperties.getAppBaseUri()
                            + webProperties.getTimeSlotsBaseUri() + "/booked").queryParam("from", format.format(fromDate.getTime()))
                    .queryParam("to", format.format(toDate.getTime())).queryParam("isAuthor", author).queryParam("limit", 15).queryParam("offset", mOffset).build();


            System.out.println(uri.toString());
            HttpHeaders headers = new HttpHeaders();
            String token = mContext.getSharedPreferences("userPreferences", MODE_PRIVATE).getString("token", null);
            headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(headers);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<BookingDTO[]> result = null;
            HttpStatus response;
            try {
                result = restTemplate.exchange(uri.toUri(), HttpMethod.GET, request, BookingDTO[].class);

            }catch (HttpClientErrorException e){
                System.out.println(e.toString());
            }

            return result.getBody();
        }


        @Override
        protected void onPostExecute(final BookingDTO[] bookings) {
            if(mOffset == 0){
                parent.findViewById(R.id.progress_bar).setVisibility(View.GONE);
            }else{
                parent.findViewById(R.id.load_more_progress_bar).setVisibility(View.GONE);
            }
            if (bookings.length != 0) {

                mBookings = bookings;
                recyclerView.setVisibility(View.VISIBLE);
                if(mOffset != 0){
                    adapter.addData(mBookings);
                }else {
                    adapter.updateData(mBookings);

                }

           }else{
                if(mOffset == 0) {
                    loadMore = false;
                    recyclerView.setVisibility(View.GONE);
                    noBookings.setVisibility(View.VISIBLE);
                }
            }
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }

    }


}
