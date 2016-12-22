package kz.drw.kaztest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import kz.drw.kaztest.utils.Constants;


public class TestSelect extends Fragment {

    View view;
    LinearLayout layCorpusA, layCorpusB, layTraining;
    String type="";
    public TestSelect() {
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.select_test, container, false);
        Bundle bundle = getArguments();
        if(bundle!=null) {
            type = getArguments().getString("type");
        }

        layCorpusA = (LinearLayout) view.findViewById(R.id.layCorpusA);
        layCorpusB = (LinearLayout) view.findViewById(R.id.layCorpusB);
        layTraining = (LinearLayout) view.findViewById(R.id.layTraining);

        layCorpusA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MainActivity.userID.equals(""))
                {   OpenTest(1);
                    Constants.Corpus="A"; }
                else Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.isNotAuthorization), Toast.LENGTH_SHORT).show();

            }
        });
        layCorpusB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MainActivity.userID.equals(""))
                { OpenTest(2);
                    Constants.Corpus="B"; }
                else Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.isNotAuthorization), Toast.LENGTH_SHORT).show();
            }
        });
        layTraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MainActivity.userID.equals(""))
                    OpenTraining(3);
                else Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.isNotAuthorization), Toast.LENGTH_SHORT).show();

            }
        });
        return  view;
    }

    private void OpenTest(int select) {
        Bundle bundle = new Bundle();
        bundle.putString("select", select+"");
        bundle.putString("isA","1");
        Fragment fragment;
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            fragment = new Programms();
            fragment.setArguments(bundle);
            ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }
    private void OpenTraining(int select) {
        Bundle bundle = new Bundle();
        bundle.putString("select", select+"");
        bundle.putString("isA","0");
        Fragment fragment;
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            fragment = new Programms();
            fragment.setArguments(bundle);
            ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }


}
