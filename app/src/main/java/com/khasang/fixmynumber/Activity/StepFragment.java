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
import android.widget.Toast;

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
    private FromFragmentToActivity fromFragmentToActivity;

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
            fromFragmentToActivity = ((FromFragmentToActivity) context);
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    +"must implement FromFragmentToActivity");
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
                break;
            case 1:
                rootView = (ViewGroup) inflater.inflate(R.layout.fragment_step2, container, false);
                rootView.findViewById(R.id.radioButtonSwap78).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fromFragmentToActivity.actionToActivity("+7","8");
                    }
                });
                rootView.findViewById(R.id.radioButtonSwap87).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fromFragmentToActivity.actionToActivity("8", "+7");
                    }
                });
                rootView.findViewById(R.id.radioButtonSwap700).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fromFragmentToActivity.actionToActivity("+7","00");
                    }
                });
                rootView.findViewById(R.id.radioButtonSwap80).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fromFragmentToActivity.actionToActivity("8", "0");
                    }
                });
                final ViewGroup finalRootView = rootView;
                rootView.findViewById(R.id.radioButtonSwapCustom).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String s1 = ((EditText) finalRootView.findViewById(R.id.editTextS1)).getText().toString();
                        String s2 = ((EditText) finalRootView.findViewById(R.id.editTextS2)).getText().toString();
                        fromFragmentToActivity.actionToActivity(s1,s2);
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
                break;
        }
        return rootView;
    }
}
