//package com.example.gruper.chatgruperexample.content;
//
//
//import android.content.Intent;
//import android.databinding.DataBindingUtil;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.example.gruper.chatgruperexample.R;
//import com.example.gruper.chatgruperexample.databinding.FragmentPersonalChatBinding;
//
///**
// * Created by gruper on 09/05/17.
// */
//
//public class PersonalChatFragment extends Fragment {
//
//    FragmentPersonalChatBinding binding;
//    String userNameMember;
//    Fragment fragment;
//
//    public PersonalChatFragment()
//    {
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
////        View v = inflater.inflate(R.layout.fragment_my_group, container, false);
////        return v;
////        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(getActivity()));
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_personal_chat, container, false);
//
//        userNameMember = getActivity().getIntent().getStringExtra("userNameMember");
//        binding.userNameXml.setText(userNameMember);
//
//        return binding.getRoot();
//    }
//}
