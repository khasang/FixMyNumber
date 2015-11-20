package com.khasang.fixmynumber.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khasang.fixmynumber.Adapter.ContactsListToChangeAdapter;
import com.khasang.fixmynumber.Model.ContactItem;
import com.khasang.fixmynumber.R;

import java.util.ArrayList;

/**
 * Created by Raenar on 18.10.2015.
 */
public class StepFragment3 extends Fragment {
    ArrayList<ContactItem> contactsList;

    private Fragment3ViewsCreateListener fragment3ViewsCreateListener;

    public interface Fragment3ViewsCreateListener {
        public void onFragment3ViewsCreated(ViewGroup v);
    }

    public StepFragment3() {
    }

    public void setContactsList(ArrayList<ContactItem> contactsList) {
        this.contactsList = contactsList;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragment3ViewsCreateListener = ((Fragment3ViewsCreateListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement Fragment3ViewsCreateListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_step3, container, false);

        RecyclerView recyclerViewContactsToChange = (RecyclerView) rootView.findViewById(R.id.recyclerViewContactsToChange);
        ContactsListToChangeAdapter adapter = new ContactsListToChangeAdapter(contactsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewContactsToChange.setAdapter(adapter);
        recyclerViewContactsToChange.setLayoutManager(layoutManager);
        fragment3ViewsCreateListener.onFragment3ViewsCreated(rootView);

        return rootView;
    }

}
