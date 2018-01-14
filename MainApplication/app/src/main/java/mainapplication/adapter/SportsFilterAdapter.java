package mainapplication.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mainapplication.R;
import mainapplication.dto.SportDTO;
import mainapplication.utils.SearchFilters;

/**
 * Created by Luis Gouveia on 10/01/2018.
 */

public class SportsFilterAdapter extends RecyclerView.Adapter<SportsFilterAdapter.ViewHolder> {

    private Context mContext;
    private SportDTO[] mSports;
    private List<Integer> previous;

    public SportsFilterAdapter(Context context, SportDTO[] sports) {
        mContext = context;
        mSports = sports;
        previous = SearchFilters.getCategories();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_text_card, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SportDTO category = mSports[position];
        holder.name.setText(category.getName());
        holder.sport = category.getId();
        if (previous.contains(category.getId())) {
            holder.itemView.callOnClick();
        }
    }

    @Override
    public int getItemCount() {
        return mSports.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        int sport;
        View itemView;
        boolean saved;


        public ViewHolder(final View itemView) {
            super(itemView);
            this.itemView = itemView;
            name = (TextView) itemView.findViewById(R.id.name);
            saved = false;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!SearchFilters.getSports().contains(sport)) {
                        SearchFilters.addSport(sport);
                        itemView.setBackgroundTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.colorAccent, null)));

                    } else if (previous.contains(sport) && !saved) {
                        saved = true;
                        itemView.setBackgroundTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.colorAccent, null)));
                    } else {
                        SearchFilters.removeSport(sport);
                        itemView.setBackgroundTintList(ColorStateList.valueOf(mContext.getResources().getColor(android.R.color.white, null)));
                    }
                }
            });

        }
    }
}