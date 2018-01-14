package mainapplication.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import mainapplication.R;
import mainapplication.activity.FacilityActivity;
import mainapplication.dto.FacilityDTO;
import mainapplication.utils.DownloadImageTask;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Luis Gouveia on 04/12/2017.
 */

public class FacilitiesCollapsedCardAdapter extends RecyclerView.Adapter<FacilitiesCollapsedCardAdapter.ViewHolder> {

    private Activity mContext;
    private FacilityDTO[] mFacilities;

    public FacilitiesCollapsedCardAdapter(Activity context, FacilityDTO[] facilities) {
        mContext = context;
        mFacilities = facilities;

    }
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent,int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.facility_condensed_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder (ViewHolder holder,int position){
        FacilityDTO facility = mFacilities[position];
        holder.itemName.setText(facility.getName());
        holder.downloadTask = new DownloadImageTask(facility.getId(), holder.image, facility.getImageURL());
        holder.downloadTask.execute();
        if(facility.getPromotionNext30Days() != null) {
            String[] begin =  facility.getPromotionNext30Days().getBeginTime().split("[/ ]");
            String[] end = facility.getPromotionNext30Days().getEndTime().split("[/ ]");
            holder.promotion.setText("-" + facility.getPromotionNext30Days().getDiscount() + "%    " + begin[0] + "/" + begin[1] + " - " + end[0] + "/" + end[1]);
            holder.promotion.setVisibility(View.VISIBLE);
        }
        holder.extras = new Intent(mContext, FacilityActivity.class);
        holder.extras.putExtra("facility", facility);
    }

    @Override
    public int getItemCount() {
        return mFacilities.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemName;
        TextView promotion;
        GifImageView image;
        Intent extras;
        DownloadImageTask downloadTask;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (GifImageView) itemView.findViewById(R.id.item_image);
            itemName = (TextView) itemView.findViewById(R.id.item_name);
            promotion = (TextView) itemView.findViewById(R.id.promotion);
            final TextView promotion = (TextView) itemView.findViewById(R.id.promotion);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pair<View, String> p1 = Pair.create((View) image, "image");
                    Pair<View, String> p2 = Pair.create((View) itemName, "name");
                    Pair<View, String> p3 = Pair.create((View) promotion, "promotion");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mContext, p1, p2, p3);
                    mContext.startActivity(extras,options.toBundle());
                }
            });

        }
    }



}

