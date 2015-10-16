package com.khasang.fixmynumber.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.khasang.fixmynumber.Model.ContactItem;
import com.khasang.fixmynumber.R;

import java.util.List;

/**
 * Created by Raenar on 16.10.2015.
 */
public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.ViewHolder> {

    private List<ContactItem> contactsList;

    public ContactsListAdapter(List<ContactItem> contactsList) {
        this.contactsList = contactsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final ContactItem contact = contactsList.get(i);
        viewHolder.checkBox.setChecked(contact.isChecked());
        viewHolder.name.setText(contact.getName());
        viewHolder.number.setText(contact.getNumberOriginal());

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                contact.setIsChecked(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;
        private TextView name;
        private TextView number;

        public ViewHolder(View v) {
            super(v);
            checkBox = (CheckBox) v.findViewById(R.id.checkBox);
            name = (TextView) v.findViewById(R.id.contactName);
            number = (TextView) v.findViewById(R.id.contactNumber);
        }
    }
}
