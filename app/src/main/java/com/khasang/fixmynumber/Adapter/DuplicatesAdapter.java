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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_cardview_template, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int group = groupList.get(position);
        ArrayList<ContactItem> duplicatesList = new ArrayList<>();
        Log.d("Adapter", "grp = " + group);
        for (int i = 0; i < contactsList.size(); i++) {
            ContactItem contactItem = contactsList.get(i);
            if (contactItem.getGroup() == group) {
                duplicatesList.add(contactItem);
            }
        }
        holder.hideAllViews();
        if (duplicatesList.size() < 5) {
            for (int i = 0; i < duplicatesList.size(); i++) {
                String name = duplicatesList.get(i).getName();
                String number = duplicatesList.get(i).getNumberOriginal();
                switch (i) {
                    case 0:
                        holder.layoutDuplicate1.setVisibility(View.VISIBLE);
                        holder.name1.setText(name);
                        holder.number1.setText(number);
                        break;
                    case 1:
                        holder.layoutDuplicate2.setVisibility(View.VISIBLE);
                        holder.name2.setText(name);
                        holder.number2.setText(number);
                        break;
                    case 2:
                        holder.layoutDuplicate3.setVisibility(View.VISIBLE);
                        holder.name3.setText(name);
                        holder.number3.setText(number);
                        break;
                    case 3:
                        holder.layoutDuplicate4.setVisibility(View.VISIBLE);
                        holder.name4.setText(name);
                        holder.number4.setText(number);
                        break;
                    case 4:
                        holder.layoutDuplicate5.setVisibility(View.VISIBLE);
                        holder.name5.setText(name);
                        holder.number5.setText(number);
                        break;
                }
                Log.d("Adapter", "Contact in grp = " + name + " i = " + i);
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
        LinearLayout layoutDuplicate1;
        LinearLayout layoutDuplicate2;
        LinearLayout layoutDuplicate3;
        LinearLayout layoutDuplicate4;
        LinearLayout layoutDuplicate5;
        CheckBox checkBox1;
        TextView name1;
        TextView number1;
        CheckBox checkBox2;
        TextView name2;
        TextView number2;
        CheckBox checkBox3;
        TextView name3;
        TextView number3;
        CheckBox checkBox4;
        TextView name4;
        TextView number4;
        CheckBox checkBox5;
        TextView name5;
        TextView number5;

        public ViewHolder(View v) {
            super(v);
            layoutDuplicate1 = (LinearLayout) v.findViewById(R.id.item1);
            checkBox1 = (CheckBox) layoutDuplicate1.findViewById(R.id.checkBox);
            name1 = (TextView) layoutDuplicate1.findViewById(R.id.contactName);
            number1 = (TextView) layoutDuplicate1.findViewById(R.id.contactNumber);

            layoutDuplicate2 = (LinearLayout) v.findViewById(R.id.item2);
            checkBox2 = (CheckBox) layoutDuplicate2.findViewById(R.id.checkBox);
            name2 = (TextView) layoutDuplicate2.findViewById(R.id.contactName);
            number2 = (TextView) layoutDuplicate2.findViewById(R.id.contactNumber);

            layoutDuplicate3 = (LinearLayout) v.findViewById(R.id.item3);
            checkBox3 = (CheckBox) layoutDuplicate3.findViewById(R.id.checkBox);
            name3 = (TextView) layoutDuplicate3.findViewById(R.id.contactName);
            number3 = (TextView) layoutDuplicate3.findViewById(R.id.contactNumber);

            layoutDuplicate4 = (LinearLayout) v.findViewById(R.id.item4);
            checkBox4 = (CheckBox) layoutDuplicate4.findViewById(R.id.checkBox);
            name4 = (TextView) layoutDuplicate4.findViewById(R.id.contactName);
            number4 = (TextView) layoutDuplicate4.findViewById(R.id.contactNumber);

            layoutDuplicate5 = (LinearLayout) v.findViewById(R.id.item5);
            checkBox5 = (CheckBox) layoutDuplicate5.findViewById(R.id.checkBox);
            name5 = (TextView) layoutDuplicate5.findViewById(R.id.contactName);
            number5 = (TextView) layoutDuplicate5.findViewById(R.id.contactNumber);
        }

        public void hideAllViews() {
            layoutDuplicate1.setVisibility(View.GONE);
            layoutDuplicate2.setVisibility(View.GONE);
            layoutDuplicate3.setVisibility(View.GONE);
            layoutDuplicate4.setVisibility(View.GONE);
            layoutDuplicate5.setVisibility(View.GONE);
        }
    }
}
