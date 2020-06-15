package com.example.mdsdproject.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mdsdproject.R;

public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private String mSpinnerText;
    private Double number;
    private EditText editText;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.places, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Button btn = view.findViewById(R.id.button);
        btn.setOnClickListener(this);

        editText = view.findViewById(R.id.editTextNumber);

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSpinnerText = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                String failSafe = editText.getText().toString();
                if(failSafe.matches("")){
                    Toast.makeText(getActivity().getApplicationContext(), "Please specify number", Toast.LENGTH_SHORT).show();
                }else{
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    MapsFragment mapsFragment = new MapsFragment();

                    number = Double.parseDouble(editText.getText().toString());
                    Bundle bundle = new Bundle();
                    bundle.putString("location", mSpinnerText);
                    bundle.putDouble("radius", number);
                    mapsFragment.setArguments(bundle);
                    transaction.replace(R.id.container_frame_layout, mapsFragment).commit();

                }

        }
    }
}