package es.anjon.dyl.wedding.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import es.anjon.dyl.wedding.R;
import es.anjon.dyl.wedding.models.QuizTableResult;

public class QuizResultAdapter extends RecyclerView.Adapter<QuizResultAdapter.ViewHolder> {

    private List<QuizTableResult> mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mNameView;
        public TextView mScoreView;

        public ViewHolder(View v) {
            super(v);
            mNameView = v.findViewById(R.id.table_name);
            mScoreView = v.findViewById(R.id.table_score);
        }
    }

    public QuizResultAdapter(List<QuizTableResult> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public QuizResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quiz_table_result_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        QuizTableResult tableResult = mDataset.get(position);
        holder.mNameView.setText(tableResult.getTable());
        holder.mScoreView.setText(tableResult.getScore());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
