package kg.zukhridin.nework.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.MainActivity
import kg.zukhridin.nework.R
import kg.zukhridin.nework.custom.CustomToast
import kg.zukhridin.nework.custom.OpenActivity
import kg.zukhridin.nework.databinding.FragmentLogInBinding
import kg.zukhridin.nework.exceptions.ApiResult
import kg.zukhridin.nework.viewmodel.UserViewModel

@AndroidEntryPoint
class LogInFragment : Fragment() {
    private lateinit var binding: FragmentLogInBinding
    private val vm: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logIn.setOnClickListener {
            userLoginFromVM()
            userLogInCheck()
        }

        binding.askAccount.setOnClickListener {
            findNavController().navigate(R.id.action_authenticationFragment_to_registrationFragment)
        }
    }

    private fun userLoginFromVM() {
        vm.userLogIn(binding.loginEditText.text.toString(), binding.password.text.toString())
    }

    private fun userLogInCheck() {
        vm.userAuthResponseCode.observe(viewLifecycleOwner) { response ->
                println("response: $response")
            when (response) {
                is ApiResult.Success -> {
                    if (response.code.toString()[0] == '2') {
                        OpenActivity.startFromFragment(requireActivity(), MainActivity::class.java)
                    } else {
                        CustomToast.showShort(
                            requireContext(),
                            "code - ${response.code}, message - ${response.message}"
                        )
                    }
                }
                is ApiResult.Error -> {
                    CustomToast.showShort(requireContext(), response.code.toString())
                }
            }

        }
    }
}