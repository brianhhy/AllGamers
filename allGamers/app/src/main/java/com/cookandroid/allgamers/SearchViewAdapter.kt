package com.cookandroid.allgamers

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cookandroid.allgamers.databinding.ItemSearchViewBinding


class SearchViewHolder(val binding : ItemSearchViewBinding) : RecyclerView.ViewHolder(binding.root)

    class SearchViewAdapter(val itemList: ArrayList<GameData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
        Filterable {
        var filteredItems = ArrayList<GameData>()
        var itemFilter = ItemFilter()
        init {
            filteredItems.addAll(itemList)
            Log.d("SearchView", "filteredItems : $filteredItems")
            Log.d("SearchView", "itemList : $itemList")
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            SearchViewHolder(
                ItemSearchViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val binding = (holder as SearchViewHolder).binding
            Log.d("SearchView", "filteredItems : $filteredItems")
            val datas = filteredItems[position]

            //뷰에 데이터 출력
            binding.searchViewItemName.text = datas.name
            //glide 하기 위해 gradle에 관련 아이템들 추가
            Glide.with(holder.itemView.context)
                .load(datas.iconImageURL)
                .into(binding.searchViewItemImage)

            //뷰에 이벤트 추가
            binding.searchViewItemRoot.setOnClickListener {

                Log.d("asdfasdf","asdfasdf")
                //클릭한 게임의 이름을 넘겨 activity를 시작한다
                val intent = Intent(it.context, GameProfileActivity::class.java)
                intent.putExtra("itemData", datas.name)
                it.context.startActivity(intent)

            }
        }

        override fun getItemCount(): Int = filteredItems.size

        override fun getFilter(): ItemFilter {
            return itemFilter
        }
        inner class ItemFilter : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val filterString = p0.toString()
                val results = FilterResults()
                Log.d("SearchView", "charSequence : $p0")

                //검색이 필요없을 경우를 위해 원본 배열을 복제
                val filteredList: ArrayList<GameData> = ArrayList()
                //공백제외 아무런 값이 없을 경우 -> 원본 배열
                if (filterString.trim { it <= ' '}.isEmpty()) {
                    results.values = itemList
                    results.count = itemList.size

                    return results
                    //공백제외 -> 이름으로만 검색
                } else {
                    for (i in itemList) {
                        if (i.name.toLowerCase().contains(filterString)) {
                            filteredList.add(i)
                        }
                    }
                    results.values = filteredList
                    results.count = filteredList.size

                    return results
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filteredItems.clear()
                filteredItems.addAll(p1?.values as ArrayList<GameData>)
                notifyDataSetChanged()
            }
        }
    }
