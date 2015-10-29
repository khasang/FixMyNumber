package com.khasang.fixmynumber.Adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khasang.fixmynumber.Model.ContactItem;
import com.khasang.fixmynumber.R;

import java.util.List;

/**
 * Created by Raenar on 16.10.2015.
 */
public class ContactsListToChangeAdapter extends RecyclerView.Adapter<ContactsListToChangeAdapter.ViewHolder> {

    private List<ContactItem> contactsListToChange;

    public ContactsListToChangeAdapter(List<ContactItem> contactsList) {
        this.contactsListToChange = contactsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_to_change, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final ContactItem contact = contactsListToChange.get(i);
        viewHolder.name.setText(contact.getName());
        viewHolder.numberOriginal.setText(contact.getNumberOriginal());
        if ((contact.getNumberOriginal().equals(contact.getNumberNew())) || (contact.getNumberNew() == null)) {
            viewHolder.numberNew.setText(R.string.unchanged);
            viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
        } else {
            viewHolder.numberNew.setText(contact.getNumberNew());
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        }
    }

    @Override
    public int getItemCount() {
        return contactsListToChange.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView numberOriginal;
        private TextView numberNew;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.contactName);
            numberOriginal = (TextView) v.findViewById(R.id.contactNumberOriginal);
            numberNew = (TextView) v.findViewById(R.id.contactNumberNew);
        }
    }
}
