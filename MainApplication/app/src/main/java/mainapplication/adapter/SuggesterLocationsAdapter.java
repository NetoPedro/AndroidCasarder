package mainapplication.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import mainapplication.R;
import mainapplication.activity.SearchResultsActivity;
import mainapplication.dto.SuggestionDTO;
import mainapplication.utils.DownloadImageTask;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Luis Gouveia on 11/01/2018.
 */


public class SuggesterLocationsAdapter extends RecyclerView.Adapter<SuggesterLocationsAdapter.ViewHolder> {

    private Activity mContext;
    private List<HashMap<String, Object>> suggestionDTOs;

    public SuggesterLocationsAdapter(Activity context, ArrayList<HashMap<String, Object>> suggestions) {
        mContext = context;
        if(suggestions != null){
            suggestionDTOs = suggestions;
        }else{
            suggestionDTOs = new ArrayList<>();
        }


    }
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggester_location_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, int position){
        HashMap<String, Object> dto = suggestionDTOs.get(position);
        holder.name.setText((String) dto.get("term"));
        holder.extras.putExtra("city", (String) dto.get("term"));
        holder.extras.putExtra("name", "");
        holder.extras.putExtra("resultSelected", true);
    }


    @Override
    public int getItemCount() {
        return suggestionDTOs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        Intent extras;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.location_name);
            extras = new Intent(mContext, SearchResultsActivity.class);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(extras);
                }
            });

        }
    }
}
