package kg.zukhridin.nework.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.MainActivity
import kg.zukhridin.nework.R
import kg.zukhridin.nework.custom.CustomToast
import kg.zukhridin.nework.custom.OpenActivity
import kg.zukhridin.nework.databinding.FragmentUserAuthenticationBinding
import kg.zukhridin.nework.utils.CheckNetwork
import kg.zukhridin.nework.viewmodel.UserViewModel
import javax.inject.Inject

@AndroidEntryPoint
class UserAuthenticationFragment : Fragment() {
    private lateinit var binding: FragmentUserAuthenticationBinding
    private val userVM: UserViewModel by viewModels()

    @Inject
    lateinit var checkNetwork: CheckNetwork
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentUserAuthenticationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (checkNetwork.networkAvailable()) {
            binding.internetWarning.visibility = View.GONE
            binding.loginForm.visibility = View.VISIBLE
            lifecycleScope.launchWhenCreated {
                userLoginFromVM()
            }

            binding.askAccount.setOnClickListener {
                findNavController().navigate(R.id.action_authenticationFragment_to_registrationFragment)
            }
        } else {
            binding.internetWarning.visibility = View.VISIBLE
            binding.loginForm.visibility = View.GONE
        }
    }

    private suspend fun userLoginFromVM() {
        binding.logIn.setOnClickListener {
            binding.btnProgress.visibility = View.VISIBLE
            lifecycleScope.launchWhenCreated {
                val registration = userVM.userAuthentication(
                    binding.loginEditText.text.toString(),
                    binding.password.text.toString()
                )
                if (registration.first) {
                    OpenActivity.startFromFragment(requireActivity(), MainActivity::class.java)
                } else {
                    binding.btnProgress.visibility = View.GONE
                    binding.logIn.visibility = View.VISIBLE
                    binding.passwordInputLayout.error = registration.second?.reason.toString()
                }
            }
        }
    }
}