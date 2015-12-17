package com.khasang.fixmynumber.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.khasang.fixmynumber.Adapter.SavedContactsAdapter;
import com.khasang.fixmynumber.R;

import java.util.ArrayList;

/**
 * Created by Raenar on 16.12.2015.
 */
public class RestoreFragment1 extends Fragment {

    public static final String CURRENT_CONTACTS_LIST = "currentContactsList";
    private RecyclerView recyclerViewSavedContacts;
    private OnButtonClickListener listener;
    Bundle savedData;
    ArrayList<String> currentContactsList;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = ((OnButtonClickListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnButtonClickListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_restore_list, container, false);
        recyclerViewSavedContacts = (RecyclerView) v.findViewById(R.id.recyclerViewSavedContacts);
        ((Button) v.findViewById(R.id.buttonDelete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnButtonClick(v);
            }
        });
        ((Button) v.findViewById(R.id.buttonLoad)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnButtonClick(v);
            }
        });
        return v;
    }

    public void setList(ArrayList<String> savedContactsList) {
        currentContactsList = savedContactsList;
        SavedContactsAdapter savedContactsAdapter = new SavedContactsAdapter(savedContactsList, getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerViewSavedContacts.setAdapter(savedContactsAdapter);
        recyclerViewSavedContacts.setLayoutManager(layoutManager);
    }

    public void updateList() {
        recyclerViewSavedContacts.getAdapter().notifyDataSetChanged();
        ((SavedContactsAdapter) recyclerViewSavedContacts.getAdapter()).resetSelection();

    }

    @Override
    public void onPause() {
        super.onPause();
        savedData = new Bundle();
        savedData.putStringArrayList(CURRENT_CONTACTS_LIST, currentContactsList);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (savedData!=null){
            currentContactsList = savedData.getStringArrayList(CURRENT_CONTACTS_LIST);
            setList(currentContactsList);
        }
    }
}
