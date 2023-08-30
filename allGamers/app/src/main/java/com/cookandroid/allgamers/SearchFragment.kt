package com.cookandroid.allgamers

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cookandroid.allgamers.databinding.FragmentSearchBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SearchFragment : Fragment() {
    companion object{
        const val TAG : String = "로그"

        fun newInstance() : SearchFragment{
            return SearchFragment()
        }
    }
    //메모리에 올라갔을때
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "ProfileFragment - onCreate() called")
    }
    //프레그먼트를 안고있는 엑티비티에 붙었을 떄
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "ProfileFragment - onAttach() called")
    }
    //뷰가 생성되었을 때 화면과 연결()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "ProfileFragment - onCreateView() called")

        val binding = FragmentSearchBinding.inflate(inflater, container, false)

        //db변수 && firebase 연동 및 파이어베이스 데이터 끌고오기
        val db = Firebase.firestore
        val searchViewLayoutManager = LinearLayoutManager(activity)
        searchViewLayoutManager.orientation = RecyclerView.VERTICAL
        val gameListCol = db.collection("Games")
        binding.searchViewRecyclerview.layoutManager = searchViewLayoutManager


        //가져오는데 성공하면 실행되는 함수
        gameListCol.get().addOnSuccessListener { document ->
            if (document != null) {
                //순서 중요! 1. 데이터 가져오기 2. 어댑터에 넣기 3. 뷰, 어댑터 연결
                val gameList = ArrayList<GameData>()
                for (item in document) {

                    gameList.add(GameData(item["name"].toString(),
                        item["iconImageURL"].toString()))
                }

                val searchViewGameAdapter = SearchViewAdapter(gameList)
                val searchViewTextListener : SearchView.OnQueryTextListener =
                    object : SearchView.OnQueryTextListener{
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            // 검색 버튼 누를 때 호출
                            return true
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            // 검색창에서 글자가 변경이 일어날 때마다 호출
                            searchViewGameAdapter.itemFilter.filter(newText)
                            Log.d("SearchViewFG", "SearchVies Text is changed : $newText")
                            return true
                        }
                    }

                binding.searchViewArea.setOnQueryTextListener(searchViewTextListener)
                binding.searchViewRecyclerview.adapter = searchViewGameAdapter
                Log.d("asdfasdfasdf", gameList.toString())
                searchViewGameAdapter.notifyDataSetChanged()
            } else {
                Log.d("SearchViewFG", "No Game Data")
            }
        }.addOnFailureListener { e ->
            Log.d("SearchViewFG", "$e")
        }

        //리사이클러뷰 안나오므로 반드시 수정
        return binding.root
    }
}