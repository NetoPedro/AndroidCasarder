package mainapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mainapplication.R;
import mainapplication.dto.SupplementalEquipmentDTO;

/**
 * Created by Luis Gouveia on 28/12/2017.
 */

public class EquipmentsListItemAdapter extends RecyclerView.Adapter<EquipmentsListItemAdapter.ViewHolder> {

        private Context mContext;
        private SupplementalEquipmentDTO[] mEquipments;


        public EquipmentsListItemAdapter(Context context, SupplementalEquipmentDTO[] equipments) {
            mContext = context;
            mEquipments = equipments;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_list_single_text_item, parent, false);
            return new ViewHolder(itemView);
        }



        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            SupplementalEquipmentDTO equipment = mEquipments[position];
            holder.name.setText(equipment.getName());
        }

        @Override
        public int getItemCount() {
            return mEquipments.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView name;

            public ViewHolder(final View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.text);

            }
        }
}
