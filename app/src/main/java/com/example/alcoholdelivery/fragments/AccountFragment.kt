package com.example.alcoholdelivery.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.alcoholdelivery.MainActivity
import com.example.alcoholdelivery.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth


class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(inflater, container,false)

        // initialising the auth
        auth = FirebaseAuth.getInstance()

        //setting email address to the textview
        binding.emailTxt.text = auth.currentUser?.email


        // signing out the user when logoout btn is clicked and redirecting it to the welcome fragment
        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }


        return binding.root
    }


}