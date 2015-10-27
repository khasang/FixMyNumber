package com.khasang.fixmynumber.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.khasang.fixmynumber.R;

import java.util.List;

public class SavedContactsAdapter extends RecyclerView.Adapter<SavedContactsAdapter.ViewHolder> {

    private List<String> savedContactsList;
    Context context;
    private SavedContactsItemClickListener savedContactsItemClickListener;

    public interface SavedContactsItemClickListener {
        public void onSavedContactsItemClick (String name);
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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_contacts_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        String savedContactsName = savedContactsList.get(position);
        viewHolder.name.setText(savedContactsName);
        viewHolder.setPosition(position);
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
                    Toast.makeText(context, "Selected table = "+savedContactsList.get(position), Toast.LENGTH_SHORT).show();
                    savedContactsItemClickListener.onSavedContactsItemClick(savedContactsList.get(position));
                }
            });
            name = (TextView) v.findViewById(R.id.savedContactsName);
        }
    }
}
