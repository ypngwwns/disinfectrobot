package com.hitqz.disinfectionrobot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hitqz.disinfectionrobot.activity.BuildMapActivity;
import com.hitqz.disinfectionrobot.activity.DeploymentRouteActivity;
import com.hitqz.disinfectionrobot.activity.EditMapActivity;
import com.hitqz.disinfectionrobot.activity.ToggleMapActivity;
import com.hitqz.disinfectionrobot.databinding.FragmentDeployBinding;

public class DeployFragment extends BaseFragment {

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentDeployBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.cvBuildMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BuildMapActivity.class);
                startActivity(intent);
            }
        });
        mBinding.cvDeploymentRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeploymentRouteActivity.class);
                startActivity(intent);
            }
        });
        mBinding.cvSetUpDisinfectionArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditMapActivity.class);
                startActivity(intent);
            }
        });
        mBinding.cvToggleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ToggleMapActivity.class);
                startActivity(intent);
            }
        });
    }
}