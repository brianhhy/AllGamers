package com.cookandroid.allgamers

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.allgamers.databinding.FragmentHomeBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {


    companion object{
        const val TAG : String = "로그"
        fun newInstance() : HomeFragment{
            return HomeFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "HomeFragment - onCreate() called")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "HomeFragment - onAttach() called")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "HomeFragment - onCreateView() called")
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        val db = Firebase.firestore

        super.onCreate(savedInstanceState)

        //더보기 클릭
        val Games = mutableListOf<GameData>()
        val layoutAdapter = MainPageMoreAdapter(Games)
        val layoutManager = GridLayoutManager(activity,2)
        layoutManager.orientation = RecyclerView.VERTICAL
        binding.homeRecyclerView.layoutManager = layoutManager

        //게임 데이터 가져오기
        val gameListCol = db.collection("Games")
        gameListCol.get().addOnSuccessListener { document ->
            if (document != null) {
                for (item in document) {
                    Games.add(GameData(item["name"].toString(), item["iconImageURL"].toString()))
                }
                Log.d("MainPageMoreFG", Games.toString())
                layoutAdapter.notifyDataSetChanged()
            } else {
                Log.d("MainPageMoreFG", "No Game Data")
            }

        }.addOnFailureListener { e ->
            Log.d("MainPageMoreFG", "$e")
        }
        binding.homeRecyclerView.adapter = layoutAdapter
        return binding.root
    }
}