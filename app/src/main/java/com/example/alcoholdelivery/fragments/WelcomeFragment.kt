package com.example.alcoholdelivery.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.alcoholdelivery.MainActivity2
import com.example.alcoholdelivery.R
import com.example.alcoholdelivery.databinding.FragmentWelcomeBinding
import com.example.alcoholdelivery.fragments.LoginFragment.Companion.auth
import com.google.firebase.auth.FirebaseAuth


class WelcomeFragment : Fragment() {
    private lateinit var welcomeBinding: FragmentWelcomeBinding

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        //if user is logged in he will be redirected to home fragment
        if(currentUser != null){
            val intent = Intent(activity, MainActivity2::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        welcomeBinding = FragmentWelcomeBinding.inflate(inflater,container, false)


// navigating to loginFragment
        welcomeBinding.loginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        }

        //navigating to SignupFragment
        welcomeBinding.signUpBtn.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_signUpFragment)
        }

        return welcomeBinding.root
    }


}