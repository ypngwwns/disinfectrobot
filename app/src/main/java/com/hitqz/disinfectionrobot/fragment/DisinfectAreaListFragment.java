package com.hitqz.disinfectionrobot.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hitqz.disinfectionrobot.activity.SetDisinfectAreaActivity;
import com.hitqz.disinfectionrobot.dapter.TimedTaskAdapter;
import com.hitqz.disinfectionrobot.databinding.FragmentDisinfectAreaListBinding;

import java.util.ArrayList;
import java.util.List;

public class DisinfectAreaListFragment extends Fragment {

    FragmentDisinfectAreaListBinding mBinding;
    private TimedTaskAdapter mTimedTaskAdapter;
    private List<Object> mList;

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
        mList.add(new Object());
        mList.add(new Object());
        mList.add(new Object());
        mList.add(new Object());
        mTimedTaskAdapter = new TimedTaskAdapter(getContext(), mList);
        mBinding.lvTimedTask.setAdapter(mTimedTaskAdapter);
        mTimedTaskAdapter.setOnClickListener(new View.OnClickListener() {
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
    }
}