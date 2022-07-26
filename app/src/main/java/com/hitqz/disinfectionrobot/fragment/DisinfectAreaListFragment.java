package com.hitqz.disinfectionrobot.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hitqz.disinfectionrobot.activity.SetDisinfectAreaActivity;
import com.hitqz.disinfectionrobot.adapter.DisinfectAreaAdapter;
import com.hitqz.disinfectionrobot.databinding.FragmentDisinfectAreaListBinding;
import com.hitqz.disinfectionrobot.dialog.DisinfectAreaNameDialog;

import java.util.ArrayList;
import java.util.List;

public class DisinfectAreaListFragment extends BaseFragment {

    FragmentDisinfectAreaListBinding mBinding;
    private DisinfectAreaAdapter mDisinfectAreaAdapter;
    private List<String> mList;

    private DisinfectAreaListFragment() {
        // Required empty public constructor
    }

    public static DisinfectAreaListFragment newInstance() {
        DisinfectAreaListFragment fragment = new DisinfectAreaListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentDisinfectAreaListBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList = new ArrayList<>();
        mList.add("大厅");
        mList.add("会议室1");
        mList.add("会议室2");
        mList.add("会议室2");
        mDisinfectAreaAdapter = new DisinfectAreaAdapter(mList);
        mBinding.lvTimedTask.setAdapter(mDisinfectAreaAdapter);
        mDisinfectAreaAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SetDisinfectAreaActivity) getActivity()).go2EditDisinfectArea();
            }
        });
        mBinding.includeLayoutCommonTitleBar.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        mBinding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisinfectAreaNameDialog dialog = new DisinfectAreaNameDialog();
                dialog.setOnClickListener(new DisinfectAreaNameDialog.OnClickListener() {
                    @Override
                    public void onConfirm(String text) {
                        ((SetDisinfectAreaActivity) getActivity()).go2EditDisinfectArea();
                    }
                });
                dialog.show(getFragmentManager(), DisinfectAreaNameDialog.TAG);
            }
        });
    }
}