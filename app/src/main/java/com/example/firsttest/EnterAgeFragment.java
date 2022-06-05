package com.example.firsttest;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EnterAgeFragment extends Fragment {
    private EditText editText;
    private View view;
    SendEventListener sendEventListener;
    ImageView exitImageViewButton;

    //처음 실행됨
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            sendEventListener = (SendEventListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + "must implements SendEventListener");
        }
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        view = inflater.inflate(R.layout.fragment_enter_age, container, false);
        editText = (EditText) view.findViewById(R.id.edit_modify_userAge);
        editText.requestFocus();

        showKeyboard();

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String userAge = editText.getText().toString();
                if (editText.length() == 0) {
                    Toast.makeText(getActivity().getApplicationContext(), "올바른 값을 입력하세요.", Toast.LENGTH_LONG).show();
                } else {
                    switch (actionId) {
                        case EditorInfo.IME_ACTION_DONE:
                            sendEventListener.updateAge(userAge);
                            hideKeyboard();
                            view.setVisibility(View.GONE);

                            break;
                        default:
                            sendEventListener.updateAge(userAge);
                            hideKeyboard();
                            view.setVisibility(View.GONE);
                            break;
                    }
                }
                return true;
            }
        });


        exitImageViewButton = (ImageView) view.findViewById(R.id.btn_exitEnterUserAge);
        exitImageViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                view.setVisibility(View.GONE);
            }
        });

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void hideKeyboard()
    {
        if (getActivity() != null && getActivity().getCurrentFocus() != null)
        {
            // 프래그먼트기 때문에 getActivity() 사용
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    private void showKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
