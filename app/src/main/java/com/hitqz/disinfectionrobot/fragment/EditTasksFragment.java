package com.hitqz.disinfectionrobot.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hitqz.disinfectionrobot.adapter.SelectDisinfectAreaAdapter;
import com.hitqz.disinfectionrobot.databinding.FragmentEditTasksBinding;

import java.util.ArrayList;
import java.util.List;

public class EditTasksFragment extends Fragment {

    public static final String TAG = EditTasksFragment.class.getSimpleName();
    FragmentEditTasksBinding mBinding;
    private SelectDisinfectAreaAdapter mSelectDisinfectAreaAdapter;
    private List<String> mList;
    private boolean mSelectedAllArea = true;

    private EditTasksFragment() {
        // Required empty public constructor
    }

    public static EditTasksFragment newInstance() {
        EditTasksFragment fragment = new EditTasksFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentEditTasksBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList = new ArrayList<>();
        mList.add("大厅");
        mList.add("一号会议室");
        mList.add("二号会议室");
        mSelectDisinfectAreaAdapter = new SelectDisinfectAreaAdapter(mList);
        mBinding.lvDisinfectionArea.setAdapter(mSelectDisinfectAreaAdapter);

        mBinding.includeLayoutCommonTitleBar.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        onSelectChanged();
        mBinding.cbAllArea.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mSelectedAllArea == isChecked) {
                    return;
                }
                mSelectedAllArea = isChecked;
                onSelectChanged();
            }
        });
        mBinding.cbPartArea.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mSelectedAllArea == !isChecked) {
                    return;
                }
                mSelectedAllArea = !isChecked;
                onSelectChanged();
            }
        });
    }

    private void onSelectChanged() {

        if (mSelectedAllArea) {
            mBinding.cbAllArea.setChecked(true);
            mBinding.cbPartArea.setChecked(false);
            mBinding.tvSelectDisinfectionArea.setVisibility(View.GONE);
            mBinding.lvDisinfectionArea.setVisibility(View.GONE);
        } else {
            mBinding.cbAllArea.setChecked(false);
            mBinding.cbPartArea.setChecked(true);
            mBinding.tvSelectDisinfectionArea.setVisibility(View.VISIBLE);
            mBinding.lvDisinfectionArea.setVisibility(View.VISIBLE);
        }
    }
}