package mainapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mainapplication.R;
import mainapplication.dto.AtendeeRatingDTO;
import mainapplication.dto.RatingDTO;

/**
 * Created by Luis Gouveia on 06/12/2017.
 */

public class RatingsAdapter extends RecyclerView.Adapter<RatingsAdapter.ViewHolder> {

    private Context mContext;
    private View itemView;
    private List<AtendeeRatingDTO> mRatings;

    public RatingsAdapter(Context context, RatingDTO ratings) {
        mContext = context;
        mRatings = new ArrayList<>();
        mRatings.addAll(ratings.getBookerRatings());
        mRatings.addAll(ratings.getParticipantRatings());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ratings_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AtendeeRatingDTO rating = mRatings.get(position);
        holder.date.setText(rating.getDate());
        holder.username.setText(rating.getRaterUserName());
        holder.comment.setText(rating.getComment());
        holder.averageRating.setText(String.valueOf((rating.getAverage())));
        holder.averageRatingBar.setRating(Float.parseFloat(String.valueOf(rating.getAverage())));
        if(rating.isBooker()){
            holder.priceRatingBar.setRating(Float.valueOf(String.valueOf(rating.getPriceValue())));
        }else{
            holder.priceRatingBar.setVisibility(View.INVISIBLE);
            TextView label = (TextView) itemView.findViewById(R.id.price_rating_label);
            label.setVisibility(View.INVISIBLE);
        }

        holder.premisesRatingBar.setRating(Float.valueOf(String.valueOf(rating.getPremisesValue())));
        holder.accessRatingBar.setRating(Float.valueOf(String.valueOf(rating.getAccessesValue())));
    }

    @Override
    public int getItemCount() {
        return mRatings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView username;
        TextView comment;
        TextView averageRating;
        RatingBar averageRatingBar;
        RatingBar priceRatingBar;
        RatingBar premisesRatingBar;
        RatingBar accessRatingBar;
        RelativeLayout collapsedRating;
        RelativeLayout expandedRating;

        public ViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date_of_rating);
            username = (TextView) itemView.findViewById(R.id.rater_name);
            comment = (TextView) itemView.findViewById(R.id.rating_comment);
            averageRating = (TextView) itemView.findViewById(R.id.average_rating_value);
            averageRatingBar = (RatingBar) itemView.findViewById(R.id.average_rating_bar);
            priceRatingBar = (RatingBar) itemView.findViewById(R.id.price_rating);
            premisesRatingBar = (RatingBar) itemView.findViewById(R.id.premises_rating);
            accessRatingBar = (RatingBar) itemView.findViewById(R.id.access_rating);
            collapsedRating = (RelativeLayout) itemView.findViewById(R.id.collapsed_ratings);
            expandedRating = (RelativeLayout) itemView.findViewById(R.id.expanded_ratings);
            collapsedRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    collapsedRating.setVisibility(View.GONE);
                    expandedRating.setVisibility(View.VISIBLE);
                }
            });

            expandedRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    expandedRating.setVisibility(View.GONE);
                    collapsedRating.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
