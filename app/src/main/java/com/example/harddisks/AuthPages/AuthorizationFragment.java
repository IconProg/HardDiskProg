package com.example.harddisks.AuthPages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.harddisks.MainPages.MainPage;
import com.example.harddisks.R;
import com.google.firebase.auth.FirebaseAuth;

public class AuthorizationFragment extends Fragment {

    public interface OnAuthorizationSuccessListener {
        void onAuthorizationSuccess();
    }

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private OnAuthorizationSuccessListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnAuthorizationSuccessListener) {
            listener = (OnAuthorizationSuccessListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnAuthorizationSuccessListener");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_authorization, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);

        Button enter = view.findViewById(R.id.enterAuth);
        Button registration = view.findViewById(R.id.regAuth);

        EditText login = view.findViewById(R.id.email_auth);
        EditText password = view.findViewById(R.id.password_auth);

        enter.setOnClickListener(v -> {
            if(!login.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                mAuth.signInWithEmailAndPassword(login.getText().toString(), password.getText().toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // navController.popBackStack();
                        listener.onAuthorizationSuccess();
                    } else {
                        Toast.makeText(getContext(), "Логин или пароль введены неверно", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(getContext(), "Введите логин и пароль", Toast.LENGTH_LONG).show();
            }
        });

        registration.setOnClickListener(v -> {
            navController.navigate(R.id.action_authorization_to_registration);
        });


    }
}