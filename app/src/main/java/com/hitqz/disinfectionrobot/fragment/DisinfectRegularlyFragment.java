package com.hitqz.disinfectionrobot.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hitqz.disinfectionrobot.activity.DisinfectRegularlyActivity;
import com.hitqz.disinfectionrobot.adapter.TimedTaskAdapter;
import com.hitqz.disinfectionrobot.databinding.FragmentDisinfectRegularlyBinding;

import java.util.ArrayList;
import java.util.List;

public class DisinfectRegularlyFragment extends Fragment {
    public static final String TAG = DisinfectRegularlyFragment.class.getSimpleName();

    FragmentDisinfectRegularlyBinding mBinding;
    private TimedTaskAdapter mTimedTaskAdapter;
    private List<Object> mList;

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
                ((DisinfectRegularlyActivity) getActivity()).go2EditTask();
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
                ((DisinfectRegularlyActivity) getActivity()).go2EditTask();
            }
        });
    }
}