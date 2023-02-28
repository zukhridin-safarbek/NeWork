package kg.zukhridin.nework.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kg.zukhridin.nework.MainActivity
import kg.zukhridin.nework.databinding.FragmentAuthenticationBinding
import kg.zukhridin.nework.viewmodel.UserViewModel

@AndroidEntryPoint
class AuthenticationFragment : Fragment() {
    private lateinit var binding: FragmentAuthenticationBinding
    private val vm: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAuthenticationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.user.observe(viewLifecycleOwner){
            println(it)
        }
       binding.signIn.setOnClickListener {
           vm.authorization(binding.login.text.toString(), binding.password.text.toString())
           vm.responseCode.observe(viewLifecycleOwner) { response ->
               if (response.code.toString()[0] == '2') {
                   val intent = Intent(requireContext(), MainActivity::class.java)
                   startActivity(intent)
                   requireActivity().finish()
               }else{
                   Toast.makeText(requireContext(),
                       "code - ${response.code}, message - ${response.message}",
                       Toast.LENGTH_SHORT).show()
               }
           }
       }
    }
}