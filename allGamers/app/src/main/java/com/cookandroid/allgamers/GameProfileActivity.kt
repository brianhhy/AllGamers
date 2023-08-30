package com.cookandroid.allgamers

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cookandroid.allgamers.databinding.ActivityGameProfileBinding
import com.google.firebase.firestore.FirebaseFirestore

class GameProfileActivity : AppCompatActivity() {
    lateinit var binding : ActivityGameProfileBinding
    private val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var gameN : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val gameName = intent.getStringExtra("itemData")
        gameN = gameName.toString()

        db.collection("Games").document(gameName.toString()).get().addOnSuccessListener {
            Glide.with(baseContext)
                .load(it["iconImageURL"])
                .into(binding.gameProfileImageMain)
        }
        val gameProfileCol = db.collection("Games").document(gameName.toString()).collection("GameProfile")
        gameProfileCol.document("GameProfile").get().addOnSuccessListener { document ->
            //게임 설명 데이터 바인딩(파이어베이스)
            binding.gameProfileName.text = document["name"].toString()
            binding.gameProfileDeveloper.text = document["developer"].toString()
            binding.gameProfileDistributor.text = document["distributor"].toString()
            binding.gameProfileRating.text = document["rating"].toString()
            binding.gameProfileDevice.text = document["device"].toString()

            val imageLayoutManager = LinearLayoutManager(baseContext)   //activity가 아닌 baseContext
            imageLayoutManager.orientation = RecyclerView.HORIZONTAL
            //이미지 리사이클러뷰
            val list = document.get("image") as MutableList<String>
            val gameProfileAdapter = GameProfileAdapter(list)
            binding.gameProfileImageRecyclerview.layoutManager = imageLayoutManager
            binding.gameProfileImageRecyclerview.adapter = gameProfileAdapter

        }
        gameProfileCol.document("ManagerReview").get().addOnSuccessListener { document ->
            val reviewLayoutManager = LinearLayoutManager(baseContext)
            reviewLayoutManager.orientation = RecyclerView.VERTICAL
            val reviewList = mutableListOf<ReviewData>()
            val reviewGood = document.get("good") as MutableList<String>
            val reviewBad = document.get("bad") as MutableList<String>
            for (i in reviewGood) {
                reviewList.add(ReviewData(i, true))
            }
            for (i in reviewBad) {
                reviewList.add(ReviewData(i, false))
            }
            val gameProfileReviewAdapter = GameProfileReviewAdapter(reviewList)
            binding.gameProfileStoreRecyclerview.layoutManager = reviewLayoutManager
            binding.gameProfileStoreRecyclerview.adapter = gameProfileReviewAdapter
        }
        val TodayslayoutManager = LinearLayoutManager(baseContext)  //activity -> baseContext
        TodayslayoutManager.orientation = RecyclerView.HORIZONTAL
        val gameListCol = db.collection("Games")
        var genre = true
        gameListCol.document(gameN).get().addOnSuccessListener{
            genre = it["genre"] as Boolean
            Log.d("aszx","$genre")
            if(genre){
                binding.recyclerViewMainPageTodaysGame.visibility = VISIBLE
                binding.recyclerViewMainPageIndieGame.visibility = GONE
            }
            else if(!genre){
                binding.recyclerViewMainPageTodaysGame.visibility = GONE
                binding.recyclerViewMainPageIndieGame.visibility = VISIBLE
            }
        }
        val todaysGameList = mutableListOf<GameData>()
        binding.recyclerViewMainPageTodaysGame.layoutManager = TodayslayoutManager
        val todaysGameAdapter = MainPageAdapter(todaysGameList)
        //연관된 게임 가져오기
        gameListCol.whereEqualTo("rpg", true).get().addOnSuccessListener { document ->
            if (document != null) {
                for (item in document) {
                    todaysGameList.add(
                        GameData(
                            item["name"].toString(),
                            item["iconImageURL"].toString()

                        )
                    )
                    Log.d("asdfasdf", "$todaysGameList")
                    todaysGameAdapter.notifyDataSetChanged()
                }
                Log.d("MainPageFG", "TodaysGameList : $todaysGameList")
            } else {
                Log.d("MainPageFG", "No Game Data")
            }
        }.addOnFailureListener { e ->
            Log.d("MainPageFG", "$e")

        }

        binding.recyclerViewMainPageTodaysGame.adapter = todaysGameAdapter
        val indieGameList = mutableListOf<GameData>()
        val indieGameLayoutManager = LinearLayoutManager(baseContext)
        indieGameLayoutManager.orientation = RecyclerView.HORIZONTAL
        binding.recyclerViewMainPageIndieGame.layoutManager = indieGameLayoutManager
        val indieGameAdapter = MainPageAdapter(indieGameList)

        gameListCol.whereEqualTo("fps", true).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    for (item in document) {
                        indieGameList.add(GameData(item["name"].toString(),item["iconImageURL"].toString()))
                    }
                    indieGameAdapter.notifyDataSetChanged()
                    Log.d("MainPageFG", "IndieGameList : $indieGameList")
                } else {
                    Log.d("MainPageFG", "No Game Data")
                }
            }.addOnFailureListener { e ->
                Log.d("MainPageFG", "$e")
            }
        binding.recyclerViewMainPageIndieGame.adapter = indieGameAdapter

    }
}