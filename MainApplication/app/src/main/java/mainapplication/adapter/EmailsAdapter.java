package mainapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mainapplication.R;

/**
 * Created by Luis Gouveia on 12/12/2017.
 */

public class EmailsAdapter extends RecyclerView.Adapter<EmailsAdapter.ViewHolder> {

    private Context mContext;
    public List<String> mEmails;

    public EmailsAdapter(Context context) {
        mContext = context;
        mEmails = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.email_card, parent, false);
        return new ViewHolder(itemView);
    }

    public void updateData(String email) {
        mEmails.add(email);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String email = mEmails.get(position);
        holder.email.setText(email);
    }

    @Override
    public int getItemCount() {
        return mEmails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView email;

        public ViewHolder(View itemView) {
            super(itemView);
            email = (TextView) itemView.findViewById(R.id.email);
        }
    }
}

