package mainapplication.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mainapplication.R;
import mainapplication.activity.BookingActivity;
import mainapplication.activity.RateBookingActivity;
import mainapplication.dto.BookingDTO;
import mainapplication.dto.SolrFacilityDTO;
import mainapplication.utils.DownloadImageTask;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Luis Gouveia on 18/12/2017.
 */

public class BookingsCardAdapter extends RecyclerView.Adapter<BookingsCardAdapter.ViewHolder> {

    private Activity mContext;
    private View itemView;
    private Context cont;
    private List<BookingDTO> mBookings;
    private boolean mAuthor;

    public BookingsCardAdapter(Activity context, Context cont, BookingDTO[] bookings, boolean author) {
        mContext = context;
        if(bookings != null){
            mBookings = new ArrayList<>(Arrays.asList(bookings));
        }else{
            mBookings = new ArrayList<>();
        }
        this.cont = cont;
        mAuthor = author;
    }
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder (ViewHolder holder,int position){
        BookingDTO booking = mBookings.get(position);
        holder.name.setText(booking.getFacilityName());
        holder.date.setText(booking.getBeginTime().split(" ")[0]);
        holder.hour.setText(booking.getBeginTime().split(" ")[1]);
        holder.downloadTask = new DownloadImageTask(booking.getFacilityId(), holder.image, booking.getImageURL());
        holder.downloadTask.execute();
        Calendar date = Calendar.getInstance();
        SimpleDateFormat format =  new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa", Locale.ENGLISH);
        Date endDate = null;
        try {
            endDate = format.parse(booking.getEndTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(endDate.after(date.getTime())) {
            holder.extras = new Intent(cont, BookingActivity.class);
        }else{
            holder.extras = new Intent(cont, RateBookingActivity.class);
        }

        holder.extras.putExtra("booking", booking);
        holder.extras.putExtra("author", mAuthor);
    }


    public void updateData( BookingDTO[] bookings) {
        mBookings = new ArrayList<>(Arrays.asList(bookings));
        notifyDataSetChanged();
    }
    public void addData(BookingDTO[] bookings) {
        mBookings.addAll(new ArrayList<>(Arrays.asList(bookings)));
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mBookings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView date;
        TextView hour;
        GifImageView image;
        Intent extras;
        DownloadImageTask downloadTask;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (GifImageView) itemView.findViewById(R.id.facility_image);
            name = (TextView) itemView.findViewById(R.id.facility_name);
            date = (TextView) itemView.findViewById(R.id.date);
            hour = (TextView) itemView.findViewById(R.id.hour);

            final ConstraintLayout layout = (ConstraintLayout) itemView.findViewById(R.id.card_layout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(extras);
                }
            });

        }
    }
}
