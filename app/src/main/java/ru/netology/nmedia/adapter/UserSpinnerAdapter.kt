package ru.netology.nmedia.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.User
import ru.netology.nmedia.view.loadCircleCrop


class UserSpinnerAdapter constructor(
    private val users: List<User>,
    private val context: Context,
) : BaseAdapter() {

    override fun getCount() = users.size

    override fun getItem(item: Int): User = users[item]

    override fun getItemId(item: Int): Long = item.toLong()

    override fun getView(item: Int, view: View?, viewGroup: ViewGroup?): View {

        val rView = when (view == null) {
            true -> LayoutInflater.from(context).inflate(R.layout.card_users_vertical, viewGroup, false)
            else -> view
        }

        val user = users[item]
        val name = rView.findViewById<TextView>(R.id.name)
        val avatar = rView.findViewById<ImageView>(R.id.avatar)

        name.text = user.name
        user.avatar?.let { avatar.loadCircleCrop(it, R.drawable.ic_empty_avatar) }


        return rView
    }
}