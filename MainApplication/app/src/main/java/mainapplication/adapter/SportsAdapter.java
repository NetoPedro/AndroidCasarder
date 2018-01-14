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
 * Created by Luis Gouveia on 12/01/2018.
 */

    public class SportsAdapter extends RecyclerView.Adapter<SportsAdapter.ViewHolder> {

        private Context mContext;
        private SportDTO[] mSports;
        private List<Integer> previous;

        public SportsAdapter(Context context, SportDTO[] sports) {
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
        }

        @Override
        public int getItemCount() {
            return mSports.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView name;
            View itemView;


            public ViewHolder(final View itemView) {
                super(itemView);
                this.itemView = itemView;
                name = (TextView) itemView.findViewById(R.id.name);

            }
        }
    }
