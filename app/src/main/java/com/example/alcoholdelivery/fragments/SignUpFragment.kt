package com.example.alcoholdelivery.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.alcoholdelivery.MainActivity2
import com.example.alcoholdelivery.R
import com.example.alcoholdelivery.databinding.FragmentSignUpBinding
import com.example.alcoholdelivery.fragments.LoginFragment.Companion.auth
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class SignUpFragment : Fragment() {

    private lateinit var signUpBinding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        signUpBinding = FragmentSignUpBinding.inflate(inflater, container, false)

        auth =  FirebaseAuth.getInstance()

        signUpBinding.createAccountBtn.setOnClickListener {
            signUpBinding.progressBar.visibility = View.VISIBLE
            signUpBinding.errorTextView.visibility = View.GONE
            registerAccount()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        signUpBinding.alreadyHaveAccount.setOnClickListener {

            findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }
        signUpBinding.signUpWithGoogleBtn.setOnClickListener {
            signUpBinding.errorTextView.visibility = View.GONE
            googleSignInClient.signOut()
            signUpBinding.progressBar.visibility = View.VISIBLE
            startActivityForResult(googleSignInClient.signInIntent, 10)


        }

        return signUpBinding.root
    }

    private fun registerAccount() {
        val email = signUpBinding.txtEmail.editText?.text.toString()
        val password = signUpBinding.txtPassword.editText?.text.toString()

        if (email.isNotEmpty()&&password.isNotEmpty()){
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful){
                    val intent = Intent(activity, MainActivity2::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                    signUpBinding.progressBar.visibility = View.GONE
                }
            }.addOnFailureListener {
                signUpBinding.errorTextView.visibility = View.VISIBLE
                signUpBinding.errorTextView.text = it.localizedMessage
                signUpBinding.progressBar.visibility = View.GONE
            }
        }else{
            signUpBinding.errorTextView.visibility = View.VISIBLE
            signUpBinding.errorTextView.text = "Please provide complete details"
            signUpBinding.progressBar.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10 && resultCode == RESULT_OK){
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken)
            }
            catch (e: Exception){
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential  = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    signUpBinding.progressBar.visibility = View.GONE
                    val intent = Intent(activity, MainActivity2::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                    signUpBinding.progressBar.visibility = View.GONE
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }


}