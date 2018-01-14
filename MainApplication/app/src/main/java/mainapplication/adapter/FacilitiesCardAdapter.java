package mainapplication.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mainapplication.R;
import mainapplication.activity.FacilityActivity;
import mainapplication.dto.FacilityDTO;
import mainapplication.dto.SolrFacilityDTO;
import mainapplication.utils.DownloadImageTask;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Luis Gouveia on 09/12/2017.
 */

public class FacilitiesCardAdapter extends RecyclerView.Adapter<FacilitiesCardAdapter.ViewHolder> {

    private Activity mContext;
    private List<SolrFacilityDTO> mFacilities;

    public FacilitiesCardAdapter(Activity context, SolrFacilityDTO[] facilities) {
        mContext = context;
        if(facilities != null){
            mFacilities = new ArrayList<>(Arrays.asList(facilities));
        }else{
            mFacilities = new ArrayList<>();
        }


    }
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.facility_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, int position){
        FacilityDTO facility = new FacilityDTO(mFacilities.get(position));
        holder.name.setText(facility.getName());
        holder.type.setText(facility.getDescription());
        if(facility.getPromotionNext30Days() != null) {
            String[] begin =  facility.getPromotionNext30Days().getBeginTime().split("[/ ]");
            String[] end = facility.getPromotionNext30Days().getEndTime().split("[/ ]");
            holder.promotion.setText("-" + facility.getPromotionNext30Days().getDiscount() + "%    " + begin[0] + "/" + begin[1] + " - " + end[0] + "/" + end[1]);
            holder.promotion.setVisibility(View.VISIBLE);
        }
        holder.rating.setRating(Float.parseFloat(String.valueOf(facility.getAverageRating())));
        holder.downloadTask = new DownloadImageTask(facility.getId(), holder.image, facility.getImageURL());
        holder.downloadTask.execute();
        holder.extras = new Intent(mContext, FacilityActivity.class);
        holder.extras.putExtra("facility", facility);
    }

    public void updateData(SolrFacilityDTO[] facilities) {
        mFacilities = new ArrayList<>(Arrays.asList(facilities));
        notifyDataSetChanged();
    }

    public void addData(SolrFacilityDTO[] facilities) {
        mFacilities.addAll(new ArrayList<>(Arrays.asList(facilities)));
        notifyDataSetChanged();
    }

    public SolrFacilityDTO[] getItems(){
        return mFacilities.toArray(new SolrFacilityDTO[mFacilities.size()]);
    }
    @Override
    public int getItemCount() {
        return mFacilities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView type;
        TextView promotion;
        GifImageView image;
        RatingBar rating;
        Intent extras;
        DownloadImageTask downloadTask;

        public ViewHolder(final View itemView) {
            super(itemView);
            image = (GifImageView) itemView.findViewById(R.id.facility_image);
            name = (TextView) itemView.findViewById(R.id.facility_name);
            type = (TextView) itemView.findViewById(R.id.facility_type);
            rating = (RatingBar) itemView.findViewById(R.id.rating);
            promotion = (TextView) itemView.findViewById(R.id.promotion);
            final ConstraintLayout layout = (ConstraintLayout) itemView.findViewById(R.id.card_layout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pair<View, String> p1 = Pair.create((View) image, "image");
                    Pair<View, String> p2 = Pair.create((View) name, "name");
                    Pair<View, String> p3 = Pair.create((View) type, "type");
                    Pair<View, String> p4 = Pair.create((View) rating, "rating_stars");
                    Pair<View, String> p5 = Pair.create((View) layout, "layout");
                    Pair<View, String> p6 = Pair.create((View) itemView.findViewById(R.id.promotion), "promotion");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mContext, p1, p2, p3, p4, p6);
                    mContext.startActivity(extras,options.toBundle());
                }
            });

        }
    }
}
