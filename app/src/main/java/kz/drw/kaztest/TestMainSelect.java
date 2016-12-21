package kz.drw.kaztest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import kz.drw.kaztest.utils.Constants;


public class TestMainSelect extends Fragment {

    View view;
    LinearLayout layRating, layGosTest, layEntTest, layPDDTest;

    public TestMainSelect() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_tests, container, false);
        Constants.isTest=false;
        layEntTest = (LinearLayout) view.findViewById(R.id.layEntTest);
        layRating = (LinearLayout) view.findViewById(R.id.layRating);
        layGosTest = (LinearLayout) view.findViewById(R.id.layGosTest);
        layPDDTest = (LinearLayout) view.findViewById(R.id.layPDDTest);
        
        layGosTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenTest();
            }
        });
        layPDDTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenTestDev();
            }
        });
        layEntTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenTestDev();
            }
        });

        layRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MainActivity.userID.equals(""))
                    OpenRating();
                else Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.isNotAuthorization), Toast.LENGTH_SHORT).show();

            }
        });

        return  view;
    }

    private void OpenTest() {
        Bundle bundle = new Bundle();
        bundle.putString("type", "gos");
        Fragment fragment;
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        fragment = new TestSelect();
        fragment.setArguments(bundle);
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.commit();

    }
        private void OpenTestDev() {
        Fragment fragment;
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        fragment = new isDev();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    private void OpenRating() {
        startActivity(new Intent(getActivity(), Rating.class));
    }


}
