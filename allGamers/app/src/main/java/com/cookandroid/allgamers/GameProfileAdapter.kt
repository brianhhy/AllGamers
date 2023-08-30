package com.cookandroid.allgamers

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.*
import com.bumptech.glide.Glide
import com.cookandroid.allgamers.databinding.ItemGameProfileImageBinding
import com.cookandroid.allgamers.databinding.ItemGameProfileReviewBinding
import com.google.firebase.firestore.FirebaseFirestore


//게임 프로필 이미지
//뷰홀더
class GameProfileViewHolder(val binding : ItemGameProfileImageBinding) : ViewHolder(binding.root)
//어댑터
class GameProfileAdapter(val itemList: MutableList<String>) : Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        GameProfileViewHolder(ItemGameProfileImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = (holder as GameProfileViewHolder).binding

        //데이터 변수
        val datas = itemList[position]
        //뷰에 데이터 출력
        Glide.with(holder.itemView.context)
            .load(datas)
            .into(binding.mainPageItemImage)
        //뷰에 이벤트 추가
        binding.mainPageItemRoot.setOnClickListener {
            //아이템 클릭 이벤트

        }
    }

    override fun getItemCount(): Int = itemList.size
}

//게임 프로필 장단점
class GameProfileReviewViewHolder(val binding : ItemGameProfileReviewBinding) : ViewHolder(binding.root)

class GameProfileReviewAdapter(val itemList: MutableList<ReviewData>) : Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            GameProfileReviewViewHolder(ItemGameProfileReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = (holder as GameProfileReviewViewHolder).binding
        val datas = itemList[position]
        binding.reviewImage.run {
            //장단점 확인 후 아이콘 추가(PM -> true면 장점 )
            if (datas.PM)
                setImageResource(R.drawable.ic_good)
            else
                setImageResource(R.drawable.ic_bad)
        }
        binding.reviewTitle.text = datas.title
    }

    override fun getItemCount(): Int = itemList.size

}

