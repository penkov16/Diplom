package ru.netology.nmedia.ui

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.databinding.FragmentEditPostBinding
import ru.netology.nmedia.utils.AndroidUtils
import ru.netology.nmedia.utils.StringArg
import ru.netology.nmedia.viewModel.AuthViewModel
import ru.netology.nmedia.viewModel.PostViewModel

@AndroidEntryPoint
class EditPostFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg
    }

    private val postVewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private val authViewModel: AuthViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditPostBinding.inflate(
            inflater,
            container,
            false
        )

        arguments?.textArg?.let(binding.edit::setText)
        binding.edit.requestFocus()

        val pickPhotoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                when (it.resultCode) {
                    ImagePicker.RESULT_ERROR -> {
                        Snackbar.make(
                            binding.root,
                            ImagePicker.getError(it.data),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    Activity.RESULT_OK -> {
                        val uri: Uri? = it.data?.data
                        postVewModel.changePhoto(uri, uri?.toFile())
                    }
                }
            }

        binding.takeImage.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(2048)
                .provider(ImageProvider.GALLERY)
                .galleryMimeTypes(
                    arrayOf(
                        "image/png",
                        "image/jpeg",
                    )
                )
                .createIntent(pickPhotoLauncher::launch)
        }

        binding.takePhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(2048)
                .provider(ImageProvider.CAMERA)
                .createIntent(pickPhotoLauncher::launch)
        }

        binding.removePhoto.setOnClickListener {
            postVewModel.changePhoto(null, null)
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            postVewModel.cancelEdit()
            findNavController().navigateUp()
        }

        binding.ok.setOnClickListener {
            postVewModel.save(binding.edit.text.toString())
            AndroidUtils.hideKeyboard(requireView())
        }

        postVewModel.postCreated.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        postVewModel.photo.observe(viewLifecycleOwner) {
            if (it.uri == null) {
                binding.photoContainer.visibility = View.GONE
                return@observe
            }

            binding.photoContainer.visibility = View.VISIBLE
            binding.photo.setImageURI(it.uri)
        }

        authViewModel.data.observe(viewLifecycleOwner) {
            if (authViewModel.data.value?.id == 0L) {
                findNavController().navigateUp()
            }
        }

        return binding.root
    }
}