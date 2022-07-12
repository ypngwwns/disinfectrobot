package com.hitqz.disinfectionrobot.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hitqz.disinfectionrobot.dapter.TimedTaskAdapter;
import com.hitqz.disinfectionrobot.databinding.FragmentDisinfectRegularlyBinding;

import java.util.ArrayList;

public class DisinfectRegularlyFragment extends Fragment {

    FragmentDisinfectRegularlyBinding mBinding;

    private DisinfectRegularlyFragment() {
        // Required empty public constructor
    }

    public static DisinfectRegularlyFragment newInstance() {
        DisinfectRegularlyFragment fragment = new DisinfectRegularlyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentDisinfectRegularlyBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.lvTimedTask.setAdapter(new TimedTaskAdapter(getContext(), new ArrayList<>()));
    }
}