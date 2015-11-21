package com.khasang.fixmynumber.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.khasang.fixmynumber.Adapter.ContactsListAdapter;
import com.khasang.fixmynumber.Adapter.ContactsListToChangeAdapter;
import com.khasang.fixmynumber.Model.ContactItem;
import com.khasang.fixmynumber.R;

import java.util.ArrayList;

/**
 * Created by Raenar on 18.10.2015.
 */
public class StepFragment extends Fragment {
    private int pageNumber;
    ArrayList<ContactItem> contactsList;
    private Fragment1ViewsCreateListener fragment1ViewsCreateListener;
    private Fragment2ViewsCreateListener fragment2ViewsCreateListener;
    private Fragment3ViewsCreateListener fragment3ViewsCreateListener;

    public interface Fragment1ViewsCreateListener {
        public void onFragment1ViewsCreated(ViewGroup v);
    }

    public interface Fragment2ViewsCreateListener {
        public void onFragment2ViewsCreated(View v, EditText ed1, EditText ed2);
    }

    public interface Fragment3ViewsCreateListener {
        public void onFragment3ViewsCreated(ViewGroup v);
    }


    public StepFragment() {
    }

    public void setContactsList(ArrayList<ContactItem> contactsList) {
        this.contactsList = contactsList;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
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
        try {
            fragment2ViewsCreateListener = ((Fragment2ViewsCreateListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement Fragment2ViewsCreateListener");
        }
        try {
            fragment3ViewsCreateListener = ((Fragment3ViewsCreateListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement Fragment3ViewsCreateListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_step1, container, false);

        switch (pageNumber) {
            case 0:
                RecyclerView recyclerViewContacts = (RecyclerView) rootView.findViewById(R.id.recyclerViewContacts);
                ContactsListAdapter adapter = new ContactsListAdapter(contactsList);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                recyclerViewContacts.setAdapter(adapter);
                recyclerViewContacts.setLayoutManager(layoutManager);
                fragment1ViewsCreateListener.onFragment1ViewsCreated(rootView);
                break;
            case 1:
                rootView = (ViewGroup) inflater.inflate(R.layout.fragment_step2, container, false);
                final EditText editTextS1 = ((EditText) rootView.findViewById(R.id.editTextS1));
                final EditText editTextS2 = ((EditText) rootView.findViewById(R.id.editTextS2));
                rootView.findViewById(R.id.radioButtonSwap78).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment2ViewsCreateListener.onFragment2ViewsCreated(v, editTextS1, editTextS2);
                    }
                });
                rootView.findViewById(R.id.radioButtonSwap87).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment2ViewsCreateListener.onFragment2ViewsCreated(v, editTextS1, editTextS2);
                    }
                });
//                rootView.findViewById(R.id.radioButtonSwap700).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        fragment2ViewsCreateListener.onFragment2ViewsCreated(v, editTextS1, editTextS2);
//                    }
//                });
//                rootView.findViewById(R.id.radioButtonSwap80).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        fragment2ViewsCreateListener.onFragment2ViewsCreated(v, editTextS1, editTextS2);
//                    }
//                });
                rootView.findViewById(R.id.radioButtonSwapCustom).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment2ViewsCreateListener.onFragment2ViewsCreated(v, editTextS1, editTextS2);
                    }
                });
                break;
            case 2:
                rootView = (ViewGroup) inflater.inflate(R.layout.fragment_step3, container, false);
                RecyclerView recyclerViewContactsToChange = (RecyclerView) rootView.findViewById(R.id.recyclerViewContactsToChange);
                ContactsListToChangeAdapter adapter2 = new ContactsListToChangeAdapter(contactsList);
                LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
                recyclerViewContactsToChange.setAdapter(adapter2);
                recyclerViewContactsToChange.setLayoutManager(layoutManager2);
                fragment3ViewsCreateListener.onFragment3ViewsCreated(rootView);
                break;
        }
        return rootView;
    }
}
