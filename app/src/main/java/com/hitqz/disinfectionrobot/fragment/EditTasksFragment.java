package com.hitqz.disinfectionrobot.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hitqz.disinfectionrobot.adapter.DisinfectAreaAdapter;
import com.hitqz.disinfectionrobot.databinding.FragmentEditTasksBinding;

import java.util.ArrayList;
import java.util.List;

public class EditTasksFragment extends Fragment {

    FragmentEditTasksBinding mBinding;
    private DisinfectAreaAdapter mDisinfectAreaAdapter;

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
        List<String> list = new ArrayList<>();
        list.add("大厅");
        list.add("一号会议室");
        list.add("二号会议室");
        mDisinfectAreaAdapter = new DisinfectAreaAdapter(list);
        mBinding.lvDisinfectionArea.setAdapter(mDisinfectAreaAdapter);
    }
}