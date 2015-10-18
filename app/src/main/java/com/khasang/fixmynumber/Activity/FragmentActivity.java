package com.khasang.fixmynumber.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.khasang.fixmynumber.Adapter.ContactsListAdapter;
import com.khasang.fixmynumber.Model.ContactItem;
import com.khasang.fixmynumber.R;

import java.util.ArrayList;

public class FragmentActivity extends AppCompatActivity {
    ViewPager pager;
    ArrayList<ContactItem> contactsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpPager();
        createDummyContacts();
        setUpRecyclerView();


    }
    public void createDummyContacts() {
        contactsList = new ArrayList<ContactItem>();
        for (int i = 0; i < 8; i++) {
            ContactItem newItem = new ContactItem("qwerty", "12345", null, false);
            contactsList.add(newItem);
        }
    }

    public void setUpRecyclerView() {
        RecyclerView RecyclerViewContacts = (RecyclerView) findViewById(R.id.recyclerViewContacts);
        ContactsListAdapter adapter = new ContactsListAdapter(contactsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerViewContacts.setAdapter(adapter);
        RecyclerViewContacts.setLayoutManager(layoutManager);
    }



    private void setUpPager() {
        pager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        Button next = (Button) findViewById(R.id.next_button);
        Button prev = (Button) findViewById(R.id.prev_button);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(1);
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(0);
            }
        });
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter{

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            StepFragment stepFragment = new StepFragment();
            stepFragment.setPageNumber(position);
            return stepFragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
