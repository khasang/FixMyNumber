package com.khasang.fixmynumber.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.khasang.fixmynumber.Fragment.StepFragment1;
import com.khasang.fixmynumber.Model.ContactItem;
import com.khasang.fixmynumber.R;

import java.util.List;

/**
 * Created by Raenar on 16.10.2015.
 */
public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.ViewHolder> {

    private StepFragment1.Fragment1ContactClickListener contactClickListener;
    private List<ContactItem> contactsList;

    public ContactsListAdapter(Activity activity, List<ContactItem> contactsList) {
        this.contactClickListener = null;
        try {
            contactClickListener = (StepFragment1.Fragment1ContactClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "must implement Fragment1ContactClickListener");
        }
        this.contactsList = contactsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final int i = position;
        final ContactItem contact = contactsList.get(i);
        viewHolder.checkBox.setChecked(contact.isChecked());
        viewHolder.name.setText(contact.getName());
        viewHolder.number.setText(contact.getNumberOriginal());
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = contact.isChecked();
                isChecked = !isChecked;
                contact.setIsChecked(isChecked);
                contactClickListener.onFragment1ContactClick();
            }
        });
        viewHolder.firstLetter.setText(" ");
        String firstLetter = contactsList.get(i).getName().substring(0,1);
        if (i != 0) {
            if (!firstLetter.equals(contactsList.get(i-1).getName().substring(0,1))) {
                viewHolder.firstLetter.setText(firstLetter);
            }
        } else {
            viewHolder.firstLetter.setText(firstLetter);
        }
//        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                contactsList.get(i).setIsChecked(isChecked);
//                contactsList.get(i).setName("qwe"+isChecked);
//            }
//        });
    }

    @Override
    public int getItemCount() {
       if (contactsList != null){
           return contactsList.size();
       }
        return 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private TextView name;
        private TextView firstLetter;
        private TextView number;

        public ViewHolder(View v) {
            super(v);
            checkBox = (CheckBox) v.findViewById(R.id.checkBox);
            name = (TextView) v.findViewById(R.id.contactName);
            firstLetter = (TextView) v.findViewById(R.id.contactFirstLetter);
            number = (TextView) v.findViewById(R.id.contactNumber);
        }
    }
}
