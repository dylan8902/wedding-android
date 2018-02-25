package es.anjon.dyl.wedding.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.anjon.dyl.wedding.R;
import es.anjon.dyl.wedding.adapters.TablePlanAdapter;

public class TablePlanFragment extends Fragment {

    private static final String TAG = "TablePlanFragment";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static Fragment newInstance() {
        return new TablePlanFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_table_plan, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.fragment_table_plan);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TablePlanAdapter(new String[] { "hello", "world","hello", "world","hello", "world","hello", "world","hello", "world","hello", "world","hello", "world","hello", "world","hello", "world","hello", "world","hello", "world","hello", "world","hello", "world","hello", "world","hello", "world","hello", "world","hello", "world","hello", "world","hello", "world","hello", "world","hello", "world","hello", "world","hello", "world","hello", "world","hello", "world","hello", "world","hello", "world","hello", "world","hello", "world" });
        mRecyclerView.setAdapter(mAdapter);
    }

}
