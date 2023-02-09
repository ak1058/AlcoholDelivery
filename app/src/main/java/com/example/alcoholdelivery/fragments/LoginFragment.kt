package com.example.alcoholdelivery.fragments

import android.app.Activity
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

import com.example.alcoholdelivery.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginFragment : Fragment() {
    private lateinit var loginBinding: FragmentLoginBinding

    companion object{
        lateinit var auth: FirebaseAuth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        loginBinding = FragmentLoginBinding.inflate(inflater, container, false)

        //initialising auth
        auth =  FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        loginBinding.loginBtn.setOnClickListener {
            loginBinding.progressBar.visibility = View.VISIBLE
            loginBinding.errorTextView.visibility = View.GONE
            loginAccount()
        }
        loginBinding.dontHaveAccountRegister.setOnClickListener {

            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }

        loginBinding.signInWithGoogleBtn.setOnClickListener {
            googleSignInClient.signOut()
            loginBinding.errorTextView.visibility = View.GONE
            loginBinding.progressBar.visibility = View.VISIBLE
            startActivityForResult(googleSignInClient.signInIntent, 10)


        }


        return loginBinding.root
    }

    private fun loginAccount() {
        val email = loginBinding.txtEmail.editText?.text.toString()
        val password = loginBinding.txtPassword.editText?.text.toString()

        if (email.isNotEmpty()&&password.isNotEmpty()){
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful){
                    val intent = Intent(activity, MainActivity2::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                    loginBinding.progressBar.visibility = View.GONE
                }
            }.addOnFailureListener {
                loginBinding.errorTextView.visibility = View.VISIBLE
                loginBinding.errorTextView.text = it.localizedMessage
                loginBinding.progressBar.visibility = View.GONE
            }
        }else{
            loginBinding.errorTextView.visibility = View.VISIBLE
            loginBinding.errorTextView.text = "Please provide complete details"
            loginBinding.progressBar.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10 && resultCode == Activity.RESULT_OK){
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
                    loginBinding.progressBar.visibility = View.GONE
                    val intent = Intent(activity, MainActivity2::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                    loginBinding.progressBar.visibility = View.GONE
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }

}