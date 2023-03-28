package kg.zukhridin.nework.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kg.zukhridin.nework.R
import kg.zukhridin.nework.databinding.FragmentNewPostGalleryBinding

class NewPostGalleryFragment : Fragment() {
    private lateinit var binding: FragmentNewPostGalleryBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewPostGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

}