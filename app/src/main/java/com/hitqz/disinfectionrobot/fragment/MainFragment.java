package com.hitqz.disinfectionrobot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hitqz.disinfectionrobot.activity.DisinfectRegularlyActivity;
import com.hitqz.disinfectionrobot.activity.ManualControlActivity;
import com.hitqz.disinfectionrobot.activity.StartDisinfectActivity;
import com.hitqz.disinfectionrobot.activity.ViewDirectionsActivity;
import com.hitqz.disinfectionrobot.constant.RechargeStatusType;
import com.hitqz.disinfectionrobot.data.RobotStatus;
import com.hitqz.disinfectionrobot.databinding.FragmentMainBinding;

public class MainFragment extends BaseFragment {

    FragmentMainBinding mBinding;

    private MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentMainBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.cvViewDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewDirectionsActivity.class);
                startActivity(intent);
            }
        });
        mBinding.cvDisinfectRegularly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DisinfectRegularlyActivity.class);
                startActivity(intent);
            }
        });
        mBinding.btnStartDisinfect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), StartDisinfectActivity.class);
                startActivity(intent);
            }
        });
        mBinding.cvManualControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ManualControlActivity.class);
                startActivity(intent);
            }
        });
    }

    public void refresh(RobotStatus robotStatus) {
        mBinding.tvSsid.setText(robotStatus.getPowerInfo().getRobotSn());
        mBinding.infoText.setText(robotStatus.getPowerInfo().getPower() + "");
        mBinding.subInfoText.setText(RechargeStatusType.statusMap.get(robotStatus.getPowerInfo().getChargeStatus()));
    }
}