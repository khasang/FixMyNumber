package com.khasang.fixmynumber.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khasang.fixmynumber.R;

/**
 * Created by Raenar on 18.10.2015.
 */
public class StepFragment extends Fragment {
    private int pageNumber;

    public StepFragment() {
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_step1, container, false);
        switch (pageNumber) {
            case 0:
                break;
            case 1:
                rootView = (ViewGroup) inflater.inflate(R.layout.fragment_step2, container, false);
                break;
        }
        return rootView;
    }
}
