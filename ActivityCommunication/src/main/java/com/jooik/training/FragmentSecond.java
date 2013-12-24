package com.jooik.training;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class FragmentSecond extends Fragment {

    private View mView;
    private String passInValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_second, container, false);

        // apply values...
        EditText et = (EditText)mView.findViewById(R.id.et_second_passin);
        et.setText(passInValue);

        return mView;
    }

    // ------------------------------------------------------------------------
    // GETTER & SETTER
    // ------------------------------------------------------------------------

    public String getPassInValue()
    {
        return passInValue;
    }

    public void setPassInValue(String passInValue)
    {
        this.passInValue = passInValue;
    }
}
