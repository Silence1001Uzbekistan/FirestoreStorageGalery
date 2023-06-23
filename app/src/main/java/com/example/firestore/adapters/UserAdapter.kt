package com.example.firestore.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firestore.databinding.ItemUserBinding
import com.example.firestore.models.User
import com.squareup.picasso.Picasso

class UserAdapter(var list: List<User>) : RecyclerView.Adapter<UserAdapter.Vh>() {

    inner class Vh(var itemUserBinding: ItemUserBinding) :
        RecyclerView.ViewHolder(itemUserBinding.root) {

        fun onBind(user: User) {

            itemUserBinding.nameTv.text = user.name
            itemUserBinding.ageTv.text = user.age.toString()

            if (user.imgUrl != null) {

                Picasso.get().load(user.imgUrl).into(itemUserBinding.img)

            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {

        return Vh(ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    }

    override fun getItemCount(): Int {

        return list.size

    }

    override fun onBindViewHolder(holder: Vh, position: Int) {

        holder.onBind(list[position])

    }

}