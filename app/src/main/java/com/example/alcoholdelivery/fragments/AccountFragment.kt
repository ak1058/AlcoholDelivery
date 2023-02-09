package com.example.alcoholdelivery.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.alcoholdelivery.MainActivity
import com.example.alcoholdelivery.MainActivity2
import com.example.alcoholdelivery.R
import com.example.alcoholdelivery.databinding.FragmentAccountBinding
import com.example.alcoholdelivery.databinding.FragmentBeerDetailBinding
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

        auth = FirebaseAuth.getInstance()

        binding.emailTxt.text = auth.currentUser?.email
        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }


        return binding.root
    }


}