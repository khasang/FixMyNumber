package com.khasang.fixmynumber.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.khasang.fixmynumber.Model.ContactItem;
import com.khasang.fixmynumber.R;

import java.util.ArrayList;

/**
 * Created by Raenar on 18.10.2015.
 */
public class StepFragment2 extends Fragment {
    ArrayList<ContactItem> contactsList;
    private Fragment2ViewsUpdateListener fragment2ViewsUpdateListener;

    public interface Fragment2ViewsUpdateListener {
        public void onFragment2ViewsUpdated(View v, EditText ed1, EditText ed2);
    }


    public StepFragment2() {
    }

    public void setContactsList(ArrayList<ContactItem> contactsList) {
        this.contactsList = contactsList;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragment2ViewsUpdateListener = ((Fragment2ViewsUpdateListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "must implement Fragment2ViewsUpdateListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_step2, container, false);
        final EditText editTextS1 = ((EditText) rootView.findViewById(R.id.editTextS1));
        final EditText editTextS2 = ((EditText) rootView.findViewById(R.id.editTextS2));
        final TextView textViewChange = ((TextView) rootView.findViewById(R.id.textViewChange));
        final TextView textViewTo = ((TextView) rootView.findViewById(R.id.textViewTo));
        setCustomTextVisible(false, editTextS1, editTextS2, textViewChange, textViewTo);

        rootView.findViewById(R.id.radioButtonSwap78).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment2ViewsUpdateListener.onFragment2ViewsUpdated(v, editTextS1, editTextS2);
                setCustomTextVisible(false, editTextS1, editTextS2, textViewChange, textViewTo);
            }
        });
        rootView.findViewById(R.id.radioButtonSwap87).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment2ViewsUpdateListener.onFragment2ViewsUpdated(v, editTextS1, editTextS2);
                setCustomTextVisible(false, editTextS1, editTextS2, textViewChange, textViewTo);
            }
        });
//                rootView.findViewById(R.id.radioButtonSwap700).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        fragment2ViewsUpdateListener.onFragment2ViewsUpdated(v, editTextS1, editTextS2);
//                    }
//                });
//                rootView.findViewById(R.id.radioButtonSwap80).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        fragment2ViewsUpdateListener.onFragment2ViewsUpdated(v, editTextS1, editTextS2);
//                    }
//                });
        rootView.findViewById(R.id.radioButtonSwapCustom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment2ViewsUpdateListener.onFragment2ViewsUpdated(v, editTextS1, editTextS2);
                setCustomTextVisible(true, editTextS1, editTextS2, textViewChange, textViewTo);
            }
        });
        return rootView;
    }

    private void setCustomTextVisible(boolean isVisible, EditText editTextS1, EditText editTextS2,
                                      TextView textViewChange, TextView textViewTo) {
        if (isVisible) {
            editTextS1.setVisibility(View.VISIBLE);
            editTextS2.setVisibility(View.VISIBLE);
            textViewChange.setVisibility(View.VISIBLE);
            textViewTo.setVisibility(View.VISIBLE);
        } else {
            editTextS1.setVisibility(View.INVISIBLE);
            editTextS2.setVisibility(View.INVISIBLE);
            textViewChange.setVisibility(View.INVISIBLE);
            textViewTo.setVisibility(View.INVISIBLE);
        }
    }
}
