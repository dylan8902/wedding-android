package es.anjon.dyl.wedding.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import es.anjon.dyl.wedding.R;
import es.anjon.dyl.wedding.models.Guest;
import es.anjon.dyl.wedding.adapters.TablePlanAdapter;

public class TablePlanFragment extends Fragment {

    private static final String TAG = "TablePlanFragment";
    private static final String TABLE_PLANS = "table_plans";
    private Query guestsRef;
    private ChildEventListener childEventListener;

    public TablePlanFragment() {
    }

    public static TablePlanFragment newInstance() {
        return new TablePlanFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_table_plan, container, false);
        final RecyclerView tablePlan = view.findViewById(R.id.fragment_table_plan);
        tablePlan.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        tablePlan.setLayoutManager(layoutManager);
        final List<Guest> guests = new ArrayList<>();
        final List<String> guestKeys = new ArrayList<>();
        final TablePlanAdapter adapter = new TablePlanAdapter(guests);
        tablePlan.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        guestsRef = database.getReference(TABLE_PLANS);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
                Guest guest = dataSnapshot.getValue(Guest.class);
                guest.setKey(dataSnapshot.getKey());
                guests.add(guest);
                guestKeys.add(dataSnapshot.getKey());
                adapter.notifyItemInserted(guests.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
                Guest guest = dataSnapshot.getValue(Guest.class);
                guest.setKey(dataSnapshot.getKey());
                int guestIndex = guestKeys.indexOf(dataSnapshot.getKey());
                if (guestIndex > -1) {
                    guests.set(guestIndex, guest);
                    adapter.notifyItemChanged(guestIndex);
                } else {
                    Log.w(TAG, "onChildChanged:unknown_child:" + dataSnapshot.getKey());
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
                int guestIndex = guestKeys.indexOf(dataSnapshot.getKey());
                if (guestIndex > -1) {
                    guestKeys.remove(guestIndex);
                    guests.remove(guestIndex);
                    adapter.notifyItemRemoved(guestIndex);
                } else {
                    Log.w(TAG, "onChildRemoved:unknown_child:" + dataSnapshot.getKey());
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        };
        guestsRef.addChildEventListener(childEventListener);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (childEventListener != null) {
            guestsRef.removeEventListener(childEventListener);
        }
    }

}