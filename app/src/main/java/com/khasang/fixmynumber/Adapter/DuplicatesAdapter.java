package com.khasang.fixmynumber.Adapter;

import android.content.Context;
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
    ArrayList<ArrayList<ContactItem>> groupArraysList;
    Context context;

    public DuplicatesAdapter(Context context, ArrayList<ContactItem> contactsList, ArrayList<Integer> groupList) {
        this.contactsList = contactsList;
        this.groupList = groupList;
        this.context = context;
        groupArraysList = new ArrayList<>();
        for (Integer group : groupList) {
            ArrayList<ContactItem> tempArray = new ArrayList<>();
            for (int i = 0; i < contactsList.size(); i++) {
                ContactItem contactItem = contactsList.get(i);
                if (contactItem.getGroup() == group) {
                    tempArray.add(contactItem);
                }
            }
            groupArraysList.add(tempArray);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_cardview_template, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int group = groupList.get(position);
        ArrayList<ContactItem> duplicatesList = groupArraysList.get(position);
        Log.d("Adapter", "grp = " + group);

        holder.hideAllViews();
        if (duplicatesList.size() < 5) {
            for (int i = 0; i < duplicatesList.size(); i++) {
                String name = duplicatesList.get(i).getName();
                if (duplicatesList.get(i).getAccountType().equals("sim")){
                    name = name + " (SIM)";
                }
                String number = duplicatesList.get(i).getNumberOriginal();
                DuplicateLayout duplicateLayout = null;
                switch (i) {
                    case 0:
                        duplicateLayout = holder.duplicateLayout1;
                        break;
                    case 1:
                        duplicateLayout = holder.duplicateLayout2;
                        break;
                    case 2:
                        duplicateLayout = holder.duplicateLayout3;
                        break;
                    case 3:
                        duplicateLayout = holder.duplicateLayout4;
                        break;
                    case 4:
                        duplicateLayout = holder.duplicateLayout5;
                        break;
                }
                if (duplicateLayout != null) {
                    duplicateLayout.layout.setVisibility(View.VISIBLE);
                    duplicateLayout.name.setText(name);
                    duplicateLayout.number.setText(number);
                    Log.d("Adapter", "Contact in grp = " + name + " i = " + i + " type "+duplicatesList.get(i).getAccountType());
                }
            }
        }
        Log.d("Adapter", "/grp");
    }

    @Override
    public int getItemCount() {
        if (groupList != null) {
            return groupList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        DuplicateLayout duplicateLayout1;
        DuplicateLayout duplicateLayout2;
        DuplicateLayout duplicateLayout3;
        DuplicateLayout duplicateLayout4;
        DuplicateLayout duplicateLayout5;

        public ViewHolder(View v) {
            super(v);
            duplicateLayout1 = new DuplicateLayout(v,R.id.item1);
            duplicateLayout2 = new DuplicateLayout(v,R.id.item2);
            duplicateLayout3 = new DuplicateLayout(v,R.id.item3);
            duplicateLayout4 = new DuplicateLayout(v,R.id.item4);
            duplicateLayout5 = new DuplicateLayout(v,R.id.item5);
        }

        public void hideAllViews() {
            duplicateLayout1.layout.setVisibility(View.GONE);
            duplicateLayout2.layout.setVisibility(View.GONE);
            duplicateLayout3.layout.setVisibility(View.GONE);
            duplicateLayout4.layout.setVisibility(View.GONE);
            duplicateLayout5.layout.setVisibility(View.GONE);
        }
    }

    public class DuplicateLayout {
        LinearLayout layout;
        CheckBox checkBox;
        TextView name;
        TextView number;

        public DuplicateLayout(View v, int id) {
            layout = (LinearLayout) v.findViewById(id);
            checkBox = (CheckBox) layout.findViewById(R.id.checkBox);
            name = (TextView) layout.findViewById(R.id.contactName);
            number = (TextView) layout.findViewById(R.id.contactNumber);
        }
    }

}
