package com.cookandroid.allgamers

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cookandroid.allgamers.databinding.ItemMainPageMoreBinding

class MainPageMoreViewHolder(var binding : ItemMainPageMoreBinding) : RecyclerView.ViewHolder(binding.root)

class MainPageMoreAdapter(val itemList: MutableList<GameData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MainPageMoreViewHolder(ItemMainPageMoreBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MainPageMoreViewHolder).binding

        val datas = itemList[position]
        //뷰에 데이터 출력
        binding.mainPageMoreName.text = datas.name
        Glide.with(holder.itemView.context)
            .load(datas.iconImageURL)
            .into(binding.mainPageMoreImageMain)

        //뷰에 이벤트 추가
        binding.mainPageItemRoot.setOnClickListener {
            //아이템 클릭 이벤트
            val intent = Intent(it.context, GameProfileActivity::class.java)
            intent.putExtra("itemData", datas.name)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = itemList.size
}