package ru.netology.nmedia.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.EventAdapter
import ru.netology.nmedia.adapter.EventOnInteractionListener
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.databinding.FragmentEventsListBinding
import ru.netology.nmedia.dto.Coords
import ru.netology.nmedia.dto.Event
import ru.netology.nmedia.ui.EditEventFragment.Companion.longArg
import ru.netology.nmedia.viewModel.AuthViewModel
import ru.netology.nmedia.viewModel.EventViewModel
import ru.netology.nmedia.viewModel.UserViewModel
import javax.inject.Inject

@AndroidEntryPoint
class EventsListFragment : Fragment() {

    private val eventViewModel: EventViewModel by viewModels(ownerProducer = ::requireParentFragment)
    private val userViewModel: UserViewModel by viewModels(ownerProducer = ::requireParentFragment)
    private val authViewModel: AuthViewModel by viewModels(ownerProducer = ::requireParentFragment)

    @Inject
    lateinit var appAuth: AppAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        authViewModel.data.observe(viewLifecycleOwner) {
            if (it.id == 0L) {
                findNavController().navigate(R.id.authFragment)
            }
        }

        val binding = FragmentEventsListBinding.inflate(inflater, container, false)

        val adapter = EventAdapter(
            object : EventOnInteractionListener {
                override fun onLike(event: Event) {
                    when (event.likedByMe) {
                        true -> eventViewModel.dislikeById(event.id)
                        false -> eventViewModel.likeById(event.id)
                    }
                }

                override fun onEdit(event: Event) {
                    eventViewModel.edit(event)
                }

                override fun onRemove(event: Event) {
                    eventViewModel.removeById(event.id)
                }

                override fun onJoin(event: Event) {
                    when (event.participatedByMe) {
                        false -> eventViewModel.joinById(event.id)
                        true -> eventViewModel.rejectById(event.id)
                    }
                }

                override fun onMap(coords: Coords) {
                    //TODO("Not yet implemented")
                }

                override fun onFullscreenAttachment(attachmentUrl: String) {
                    //TODO("Not yet implemented")
                }

            },
            appAuth,
            userViewModel,
            viewLifecycleOwner
        )

        binding.list.adapter = adapter

        lifecycleScope.launchWhenCreated {
            eventViewModel.data.collectLatest(adapter::submitData)
            userViewModel.loadUsers()
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_eventsListFragment_to_editEventFragment)
        }

        eventViewModel.edited.observe(viewLifecycleOwner) { event ->
            adapter.refresh()
            if (event.id != 0L) {
                findNavController().navigate(
                    R.id.action_eventsListFragment_to_editEventFragment,
                    Bundle().apply { longArg = event.id })
            }
        }

        return binding.root
    }
}