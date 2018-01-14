package mainapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mainapplication.R;
import mainapplication.activity.ChooseSupplementalEquipmentsActivity;
import mainapplication.dto.FacilityDTO;
import mainapplication.dto.TimeSlotDTO;

/**
 * Created by Luis Gouveia on 11/12/2017.
 */

public class TimeSlotsAdapter extends RecyclerView.Adapter<TimeSlotsAdapter.ViewHolder> {

    private Context mContext;
    private TimeSlotDTO[] mTimeSlots;
    private FacilityDTO mFacility;

    public TimeSlotsAdapter(Context context, FacilityDTO facility, TimeSlotDTO[] timeSlots) {
        mContext = context;
        mTimeSlots = timeSlots;
        mFacility = facility;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_slot_card, parent, false);
        return new ViewHolder(itemView);
    }

    public void updateData(TimeSlotDTO[] timeSlots) {
        mTimeSlots = timeSlots;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TimeSlotDTO timeSlot = mTimeSlots[position];
        String[] time = timeSlot.getBeginTime().split(" ");
        holder.beginDate.setText(time[1] + " " + time[2]);
        time = timeSlot.getEndTime().split(" ");
        holder.endDate.setText(time[1] + " " + time[2]);
        holder.price.setText(String.valueOf(timeSlot.getPrice() + "€"));
        holder.extras = new Intent(mContext, ChooseSupplementalEquipmentsActivity.class).putExtra("facility", mFacility).putExtra("timeslot", timeSlot);
    }

    @Override
    public int getItemCount() {
        return mTimeSlots.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView beginDate;
        TextView endDate;
        TextView price;
        Intent extras;


        public ViewHolder(View itemView) {
            super(itemView);
            beginDate = (TextView) itemView.findViewById(R.id.begin_hour);
            endDate = (TextView) itemView.findViewById(R.id.end_hour);
            price = (TextView) itemView.findViewById(R.id.price);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(extras);
                }
            });

        }
    }
}
