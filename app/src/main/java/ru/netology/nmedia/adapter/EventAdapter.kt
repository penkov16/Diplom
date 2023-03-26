package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.databinding.CardEventBinding
import ru.netology.nmedia.dto.Coords
import ru.netology.nmedia.dto.Event
import ru.netology.nmedia.view.load
import ru.netology.nmedia.view.loadCircleCrop
import ru.netology.nmedia.viewModel.UserViewModel

interface EventOnInteractionListener {
    fun onLike(event: Event)
    fun onEdit(event: Event)
    fun onRemove(event: Event)
    fun onJoin(event: Event)
    fun onMap(coords: Coords)
    fun onFullscreenAttachment(attachmentUrl: String)
}

class EventAdapter(
    private val onInteractionListener: EventOnInteractionListener,
    private val appAuth: AppAuth,
    private val userViewModel: UserViewModel,
    private val lifecycleOwner: LifecycleOwner
) : PagingDataAdapter<Event, RecyclerView.ViewHolder>(EventDiffCallback()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val event = getItem(position)
        if (holder is EventViewHolder) {
            event?.let { holder.bind(it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = CardEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding, onInteractionListener, appAuth, userViewModel, lifecycleOwner)
    }
}

class EventViewHolder(
    private val binding: CardEventBinding,
    private val onInteractionListener: EventOnInteractionListener,
    private val appAuth: AppAuth,
    private val userViewModel: UserViewModel,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(event: Event) {

        binding.apply {

            content.text = event.content
            author.name.text = event.author
            dt.text = event.datetime
            type.text = event.type.toString()
            link.text = event.link
            like.isChecked = event.likeOwnerIds.contains(appAuth.authStateFlow.value.id)
            likeCnt.text = event.likeOwnerIds.size.toString()
            speakersHeader.isVisible = event.speakerIds.isNotEmpty()
            listSpeakers.isVisible = event.speakerIds.isNotEmpty()
            participantsHeader.isVisible = event.participantsIds.isNotEmpty()
            listParticipants.isVisible = event.participantsIds.isNotEmpty()

            val avatarUrl = event.authorAvatar ?: ""
            author.avatar.loadCircleCrop(avatarUrl, R.drawable.ic_empty_avatar)

            val speakersAdapter = UserHorizontalAdapter(object : UserOnInteractionListener {
                override fun onRemove(id: Long) = Unit
            })
            listSpeakers.adapter = speakersAdapter

            val participantsAdapter = UserHorizontalAdapter(object : UserOnInteractionListener {
                override fun onRemove(id: Long) = Unit
            })
            listParticipants.adapter = participantsAdapter

            userViewModel.data.observe(lifecycleOwner) { users ->
                val speakers = users.filter { it.id in event.speakerIds }
                speakersAdapter.submitList(speakers)

                val participants = users.filter { it.id in event.participantsIds }
                participantsAdapter.submitList(participants)
            }

            like.setOnClickListener {
                onInteractionListener.onLike(event)
            }

            event.attachment?.let { eventAttachment ->
                val attachmentUrl = eventAttachment.url
                attachment.load(attachmentUrl)
                attachment.isVisible = true

                attachment.setOnClickListener {
                    onInteractionListener.onFullscreenAttachment(attachmentUrl)
                }
            }

            navigate.isVisible = (event.coords != null)
            navigate.setOnClickListener {
                event.coords?.let {
                    onInteractionListener.onMap(it)
                }
            }

            val ownedByMe = event.authorId == appAuth.authStateFlow.value.id
            btMenu.isVisible = ownedByMe
            btMenu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    menu.setGroupVisible(R.id.owned, ownedByMe)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(event)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(event)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }

            btJoin.setOnClickListener {
                onInteractionListener.onJoin(event)
            }

            when (event.participatedByMe) {
                true -> btJoin.text = btJoin.context.getString(R.string.reject)
                false -> btJoin.text = btJoin.context.getString(R.string.join)
            }
        }
    }
}

class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }
}