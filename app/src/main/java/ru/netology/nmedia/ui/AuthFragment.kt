package ru.netology.nmedia.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.FragmentAuthBinding
import ru.netology.nmedia.model.AuthErrorType
import ru.netology.nmedia.viewModel.AuthViewModel
import ru.netology.nmedia.viewModel.PostViewModel

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private val postViewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private val authViewModel: AuthViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAuthBinding.inflate(inflater, container, false)

        with(binding) {

            btSignIn.setOnClickListener {
                val login = login.text.toString()
                val pass = password.text.toString()
                authViewModel.updateUser(login, pass)
            }

            authViewModel.data.observe(viewLifecycleOwner) {
                if (it.id != 0L) {
                    findNavController().navigate(R.id.action_authFragment_to_homeFragment)
                }
            }

            btSignUp.setOnClickListener {
                findNavController().navigate(R.id.action_authFragment_to_registrationFragment)
            }
        }

        authViewModel.errorState.observe(viewLifecycleOwner) {
            when (it.type) {
                AuthErrorType.PASSWORD -> Toast.makeText(context, getString(R.string.incorrect_password), Toast.LENGTH_LONG).show()
                else-> Unit
            }
        }

        return binding.root
    }
}