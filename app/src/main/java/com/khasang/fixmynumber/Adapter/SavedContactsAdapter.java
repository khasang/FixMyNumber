package com.khasang.fixmynumber.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khasang.fixmynumber.Activity.FragmentActivity;
import com.khasang.fixmynumber.Helper.DBHelper;
import com.khasang.fixmynumber.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SavedContactsAdapter extends RecyclerView.Adapter<SavedContactsAdapter.ViewHolder> {

    private List<String> savedContactsList;
    Context context;
    private SavedContactsItemClickListener savedContactsItemClickListener;
    private int selectedPos = -1;

    public interface SavedContactsItemClickListener {
        public void onSavedContactsItemClick(String name);
    }

    public SavedContactsAdapter(List<String> savedContactsList, Context context) {
        this.savedContactsList = savedContactsList;
        this.context = context;
        try {
            savedContactsItemClickListener = ((SavedContactsItemClickListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement SavedContactsItemClickListener");
        }
    }

    public void resetSelection() {
        selectedPos = -1;
        notifyItemChanged(selectedPos);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_contacts_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        String savedContactsName = savedContactsList.get(position);
        String formattedName = savedContactsName.replace("contacts", "");

        SimpleDateFormat format = new SimpleDateFormat(DBHelper.dateFormat);
        try {
            Date newDate = format.parse(formattedName);
            format = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
            formattedName = context.getString(R.string.contacts) + " " + format.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        viewHolder.name.setText(formattedName);
        viewHolder.setPosition(position);
//        viewHolder.itemView.setSelected(selectedPos == position);
        if ((selectedPos == position) && (selectedPos != -1)) {
            viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
            viewHolder.name.setTextColor(Color.WHITE);
        } else {
            viewHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
            viewHolder.name.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return savedContactsList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        int position;

        public void setPosition(int position) {
            this.position = position;
        }

        public ViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(selectedPos);
                    selectedPos = getLayoutPosition();
                    notifyItemChanged(selectedPos);
                    savedContactsItemClickListener.onSavedContactsItemClick(savedContactsList.get(position));
                }
            });
            name = (TextView) v.findViewById(R.id.savedContactsName);
        }
    }
}
