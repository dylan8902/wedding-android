package es.anjon.dyl.wedding.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import es.anjon.dyl.wedding.R;

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
        holder.mTextView.setText(mDataset.get(position).getName());
        Log.d("GUEST", "onBindViewHolder:" + mDataset.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
