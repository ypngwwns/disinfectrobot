package com.hitqz.disinfectionrobot.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.hitqz.disinfectionrobot.databinding.FragmentDeployBinding;

public class DeployFragment extends Fragment {

    FragmentDeployBinding mBinding;

    private DeployFragment() {
        // Required empty public constructor
    }

    public static DeployFragment newInstance() {
        DeployFragment fragment = new DeployFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentDeployBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();
    }
}