package es.anjon.dyl.wedding.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import es.anjon.dyl.wedding.R;
import es.anjon.dyl.wedding.models.Guest;

public class TablePlanAdapter extends RecyclerView.Adapter<TablePlanAdapter.ViewHolder> {

    private List<Guest> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    public TablePlanAdapter(List<Guest> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public TablePlanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Guest guest = mDataset.get(position);
        holder.mTextView.setText(guest.getName());
        if (guest.isTable()) {
            holder.mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f);
        } else {
            holder.mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
