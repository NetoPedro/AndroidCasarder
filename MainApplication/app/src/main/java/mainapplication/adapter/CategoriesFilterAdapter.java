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
import mainapplication.dto.CategoryDTO;
import mainapplication.utils.SearchFilters;

/**
 * Created by Luis Gouveia on 22/12/2017.
 */

public class CategoriesFilterAdapter extends RecyclerView.Adapter<CategoriesFilterAdapter.ViewHolder> {

    private Context mContext;
    private CategoryDTO[] mCategories;
    private List<Integer> previous;

    public CategoriesFilterAdapter(Context context, CategoryDTO[] categories) {
        mContext = context;
        mCategories = categories;
        previous = SearchFilters.getCategories();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_text_card, parent, false);
        return new ViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CategoryDTO category = mCategories[position];
        holder.name.setText(category.getName());
        holder.category = category.getId();
        if(previous.contains(category.getId())){
            holder.itemView.callOnClick();
        }
    }

    @Override
    public int getItemCount() {
        return mCategories.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        int category;
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
                    if (!SearchFilters.getCategories().contains(category)) {
                            SearchFilters.addCategory(category);
                            itemView.setBackgroundTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.colorAccent, null)));

                    } else if(previous.contains(category) && !saved) {
                        saved = true;
                        itemView.setBackgroundTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.colorAccent, null)));
                    }else{
                        SearchFilters.removeCategory(category);
                        itemView.setBackgroundTintList(ColorStateList.valueOf(mContext.getResources().getColor(android.R.color.white, null)));
                    }
                }
            });

        }
    }
}
