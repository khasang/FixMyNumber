package com.khasang.fixmynumber.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.khasang.fixmynumber.Model.ContactItem;
import com.khasang.fixmynumber.R;

import java.util.ArrayList;

/**
 * Created by Raenar on 24.12.2015.
 */
public class DuplicatesAdapter extends RecyclerView.Adapter<DuplicatesAdapter.ViewHolder> {
    ArrayList<ContactItem> contactsList;
    ArrayList<Integer> groupList;
    Context context;

    public DuplicatesAdapter(Context context, ArrayList<ContactItem> contactsList, ArrayList<Integer> groupList) {
        this.contactsList = contactsList;
        this.groupList = groupList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_cardview,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int group = groupList.get(position);
        Log.d("Adapter", "grp = "+ group);
        for (int i = 0; i < contactsList.size(); i++) {
            ContactItem contactItem = contactsList.get(i);
            if (contactItem.getGroup() == group){
                Log.d("Adapter", "Contact in grp = "+contactItem.getName());
                LinearLayout layout = new LinearLayout(context);
                TextView tvName = new TextView(context);
                TextView tvNumber = new TextView(context);
                CheckBox checkbox = new CheckBox(context);
                tvName.setText(contactItem.getName());
                tvNumber.setText(contactItem.getNumberOriginal());
                layout.addView(checkbox);
                layout.addView(tvName);
                layout.addView(tvNumber);
                holder.layout.addView(layout);
            }
        }
        Log.d("Adapter", "/grp");

    }

    @Override
    public int getItemCount() {
        if (groupList != null){
            return groupList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        public ViewHolder(View v) {
            super(v);
            layout = (LinearLayout) v.findViewById(R.id.cardLayout);
        }
    }
}
