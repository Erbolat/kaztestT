package kz.drw.kaztest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class isDev extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dev, container, false);

       // Toast.makeText(getActivity().getApplicationContext(), "Нет данных", Toast.LENGTH_SHORT).show();
        return  view;
        
    }

}