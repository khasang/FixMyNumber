package com.khasang.fixmynumber.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khasang.fixmynumber.Adapter.ContactsListAdapter;
import com.khasang.fixmynumber.Model.ContactItem;
import com.khasang.fixmynumber.R;

import java.util.ArrayList;


public class StepFragment1 extends Fragment {

    ArrayList<ContactItem> contactsList;
    private Fragment1ViewsCreateListener fragment1ViewsCreateListener;


    public interface Fragment1ViewsCreateListener {
        public void onFragment1ViewsCreated(ViewGroup v);
    }

    public interface Fragment1ContactClickListener {
        public void onFragment1ContactClick();
    }


    public StepFragment1() {
    }

    public void setContactsList(ArrayList<ContactItem> contactsList) {
        this.contactsList = contactsList;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragment1ViewsCreateListener = ((Fragment1ViewsCreateListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement Fragment1ViewsCreateListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_step1, container, false);

        RecyclerView recyclerViewContacts = (RecyclerView) rootView.findViewById(R.id.recyclerViewContacts);
        ContactsListAdapter adapter = new ContactsListAdapter(getActivity(), contactsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewContacts.setAdapter(adapter);
        recyclerViewContacts.setLayoutManager(layoutManager);
        fragment1ViewsCreateListener.onFragment1ViewsCreated(rootView);

        return rootView;
    }

}
