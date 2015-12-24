package com.khasang.fixmynumber.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khasang.fixmynumber.Adapter.ContactsListAdapter;
import com.khasang.fixmynumber.Adapter.DuplicatesAdapter;
import com.khasang.fixmynumber.Model.ContactItem;
import com.khasang.fixmynumber.R;

import java.util.ArrayList;

/**
 * Created by Raenar on 24.12.2015.
 */
public class DuplicateFragment extends Fragment {

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_duplicates, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerViewContacts);
        return v;
    }

    public void setList(Activity activity, ArrayList<ContactItem> contactsList, ArrayList<Integer> groupList) {
        DuplicatesAdapter adapter = new DuplicatesAdapter(activity, contactsList, groupList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }
}
