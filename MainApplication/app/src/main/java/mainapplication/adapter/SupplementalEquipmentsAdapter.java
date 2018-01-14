package mainapplication.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mainapplication.R;
import mainapplication.dto.SupplementalEquipmentDTO;

/**
 * Created by Luis Gouveia on 12/12/2017.
 */

public class SupplementalEquipmentsAdapter extends RecyclerView.Adapter<SupplementalEquipmentsAdapter.ViewHolder> {

    private Context mContext;
    private SupplementalEquipmentDTO[] mEquipments;
    public List<SupplementalEquipmentDTO> selectedEquipments;

    public SupplementalEquipmentsAdapter(Context context, SupplementalEquipmentDTO[] equipments) {
        mContext = context;
        mEquipments = equipments;
        selectedEquipments = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.supplemental_equipment_card, parent, false);
        return new ViewHolder(itemView);
    }

    public void updateData(SupplementalEquipmentDTO[] timeSlots) {
        mEquipments = timeSlots;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SupplementalEquipmentDTO equipment = mEquipments[position];
        holder.name.setText(equipment.getName());
        holder.price.setText(String.valueOf(equipment.getPrice()));
        holder.equipment = equipment;

    }

    @Override
    public int getItemCount() {
        return mEquipments.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView price;
        SupplementalEquipmentDTO equipment;


        public ViewHolder(final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            price = (TextView) itemView.findViewById(R.id.price);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!selectedEquipments.contains(equipment)) {
                        selectedEquipments.add(equipment);
                        itemView.setBackgroundTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.colorAccent, null)));
                    } else {
                        selectedEquipments.remove(equipment);
                        itemView.setBackgroundTintList(ColorStateList.valueOf(mContext.getResources().getColor(android.R.color.white, null)));
                    }
                }
            });

        }
    }
}