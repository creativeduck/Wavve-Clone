package com.ssacproject.secondweek.view

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.gson.Gson
import com.ssacproject.secondweek.*
import com.ssacproject.secondweek.data.CustomMovieList
import com.ssacproject.secondweek.data.MovieInfoDetails
import com.ssacproject.secondweek.data.TmdbMovieDetails

class PlayActivity : AppCompatActivity() {
    lateinit var image2: ImageButton
    lateinit var detailExplain: ConstraintLayout
    lateinit var explainIcon: ImageView
    lateinit var detail: TableLayout
    lateinit var movieTitle: TextView
    lateinit var rating: TextView
    lateinit var genre: TextView
    lateinit var showtime: TextView
    lateinit var audit: TextView
    lateinit var poster: ImageView
    lateinit var countryAndYear: TextView
    lateinit var year: TextView
    lateinit var detail_genre: TextView
    lateinit var director: TextView
    lateinit var actors: TextView
    lateinit var overview: TextView
    lateinit var urls: HashMap<String, String>
    lateinit var share: ImageButton
    lateinit var custom1: CustomRecommendView
    lateinit var custom2: CustomRecommendView
    lateinit var custom3: CustomRecommendView
    lateinit var videobox: RelativeLayout
    lateinit var playerView: PlayerView
    lateinit var simpleExoPlayer: SimpleExoPlayer
    lateinit var exoFullScreen: ImageButton
    var currentTitle: String? = null
    var state_flag: Boolean = false
    var parcelItem: ParcelItem? = null
    var duration: Int = 0
    var posterPath: String? = null
    private val kobisApiKey: String = "6ddebf77f1bb19cef16e7f57c7e579c0"

    // ????????? ??? ?????? ???????????? ????????? ?????? ??????
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        urls = HashMap<String, String>()
        // url??? ???????????? ??????????????? ?????????.
        urls["????????????"] = "url"
        urls["?????? ??????: ????????? ??????"] = "url"
        urls["???????????????"] = "url"
        urls["??????????????? 2"] = "url"
        urls["??????????????? 3"] = "url"
        urls["????????? ????????? ??????"] = "url"
        urls["?????? ??????"] = "url"
        urls["?????????: ????????? ?????????"] = "url"
        urls["?????????4 : ????????????"] = "url"
        urls["?????? ??? 3D"] = "url"
        urls["????????? ??????: ????????? ??????"] = "url"


        movieTitle = findViewById(R.id.movieTitle)
        rating = findViewById(R.id.rating)
        genre = findViewById(R.id.genre)
        showtime = findViewById(R.id.showtime)
        audit = findViewById(R.id.audit)
        poster = findViewById(R.id.poster)
        countryAndYear = findViewById(R.id.countryAndYear)
        year = findViewById(R.id.year)
        detail_genre = findViewById(R.id.detail_genre)
        director = findViewById(R.id.director)
        actors = findViewById(R.id.actors)
        overview = findViewById(R.id.overview)
        playerView = findViewById(R.id.playerView)
        share = findViewById(R.id.share)
        image2 = findViewById(R.id.image2)
        videobox = findViewById(R.id.videobox)

        // ??????????????? ??? ????????? ?????? ??????
        parcelItem = intent.getParcelableExtra<ParcelItem>("parcelItem")
        movieTitle.setText(parcelItem?.title)
        rating.setText(parcelItem?.rating.toString())
        genre.setText(parcelItem?.genre)
        showtime.setText(parcelItem?.showtime)
        audit.setText(parcelItem?.audit)
        val cny = "${parcelItem?.country}, ${parcelItem?.year}"
        countryAndYear.setText(cny)
        year.setText(parcelItem?.year)
        detail_genre.setText(parcelItem?.genre)
        director.setText(parcelItem?.director)
        actors.setText(parcelItem?.actors)
        overview.setText(parcelItem?.overview)
        val imgId: String? = parcelItem?.poster
        posterPath = "http://image.tmdb.org/t/p/w200${imgId}"
        Glide.with(this)
            .load(posterPath)
            .fitCenter()
            .into(poster)

        currentTitle = parcelItem?.title
        // ????????? URL ????????? ??? ?????? ?????? ?????? ????????? ????????? ??????.
        simpleExoPlayer = SimpleExoPlayer.Builder(this).build()
        playerView.player = simpleExoPlayer
        val videoUrl = urls.get(movieTitle.text.toString())
        val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
        val userAgent = Util.getUserAgent(this, this.applicationInfo.name)
        val factory = DefaultDataSourceFactory(this, userAgent)
        val progressiveMediaSource = ProgressiveMediaSource.Factory(factory).createMediaSource(mediaItem)
        simpleExoPlayer.setMediaSource(progressiveMediaSource)
        simpleExoPlayer.prepare()

        // ?????? ????????? ?????? ????????? ?????? ?????? ????????? ??????
        val btnPlay: Button = findViewById(R.id.btnPlay)
        btnPlay.setOnClickListener({
            poster.visibility = View.INVISIBLE
            videobox.visibility = View.VISIBLE
            simpleExoPlayer.play()
        })

        exoFullScreen = playerView.findViewById(R.id.exo_fullscreen)
        exoFullScreen.setOnClickListener {
            val intent = Intent(this, FullScreenActivity::class.java)
            intent.putExtra("url", videoUrl)
            setSharedData("prefPlay", "position${currentTitle}", simpleExoPlayer.currentPosition.toInt())
            intent.putExtra("title", currentTitle)
            startActivity(intent)
        }
        
        // ????????????????????? ????????? ??????
        custom1 = findViewById(R.id.custom1)
        val mLayoutManager1: LinearLayoutManager = LinearLayoutManager(this)
        mLayoutManager1.orientation = LinearLayoutManager.HORIZONTAL
        val recycler1 = custom1.recycler
        recycler1.layoutManager = mLayoutManager1
        val mAdapter1: HorizontalAdapter = HorizontalAdapter()
        val data1: ArrayList<Movies> = ArrayList<Movies>()
        data1.add(Movies(R.drawable.poster_chocolate, "????????? ????????? ??????"))
        data1.add(Movies(R.drawable.poster_stepup3, "?????? ??? 3D"))
        data1.add(Movies(R.drawable.poster_stepup4, "?????????4 : ????????????"))
        data1.add(Movies(R.drawable.poster_spiderman, "???????????????"))
        data1.add(Movies(R.drawable.poster_spiderman2, "??????????????? 2"))
        data1.add(Movies(R.drawable.poster_spiderman3, "??????????????? 3"))
        mAdapter1.setData(data1)
        mAdapter1.setOnItemClickListener(object: HorizontalAdapter.ItemClickListener {
            override fun onItemClick(view: View, pos: Int) {
                findMovieCode(data1.get(pos).getText(), Item())
            }
        })
        recycler1.adapter = mAdapter1

        custom2 = findViewById(R.id.custom2)
        val mLayoutManager2: LinearLayoutManager = LinearLayoutManager(this)
        mLayoutManager2.orientation = LinearLayoutManager.HORIZONTAL
        val recycler2 = custom2.recycler
        recycler2.layoutManager = mLayoutManager2
        val mAdapter2: HorizontalAdapter = HorizontalAdapter()
        val data2: ArrayList<Movies> = ArrayList<Movies>()
        data2.add(Movies(R.drawable.poster_inception, "?????????"))
        data2.add(Movies(R.drawable.poster_madmax, "?????? ??????: ????????? ??????"))
        data2.add(Movies(R.drawable.poster_zootopia, "????????????"))
        data2.add(Movies(R.drawable.poster_xman_first, "?????????: ????????? ?????????"))
        data2.add(Movies(R.drawable.poster_sourcecode, "?????? ??????"))
        mAdapter2.setData(data2)
        mAdapter2.setOnItemClickListener(object: HorizontalAdapter.ItemClickListener {
            override fun onItemClick(view: View, pos: Int) {
                findMovieCode(data2.get(pos).getText(), Item())
            }
        })
        recycler2.adapter = mAdapter2

        custom3 = findViewById(R.id.custom3)
        var mLayoutManager3 = LinearLayoutManager(this)
        mLayoutManager3.orientation = LinearLayoutManager.HORIZONTAL
        var recycler3 = custom3.recycler
        recycler3.layoutManager = mLayoutManager3
        var mAdapter3 = HorizontalAdapter()
        var data3: ArrayList<Movies> = ArrayList<Movies>()
        data3.add(Movies(R.drawable.poster, "??????????????? ?????????"))
        data3.add(Movies(R.drawable.poster_attack, "????????? ??????: ????????? ??????"))
        data3.add(Movies(R.drawable.poster_midnight, "???????????????"))
        data3.add(Movies(R.drawable.poster_callcenter, "????????? ?????????"))
        data3.add(Movies(R.drawable.poster_parasite, "?????????"))
        data3.add(Movies(R.drawable.poster_hocc, "????????? ????????? ??????"))
        mAdapter3.setOnItemClickListener(object: HorizontalAdapter.ItemClickListener {
            override fun onItemClick(view: View, pos: Int) {
                findMovieCode(data3.get(pos).getText(), Item())
            }
        })
        mAdapter3.setData(data3)
        recycler3.adapter = mAdapter3

        image2.setOnClickListener({
            if (state_flag==false) {
                image2.setImageResource(R.drawable.heart_selected)
                Toast.makeText(applicationContext, "????????? ????????????", Toast.LENGTH_SHORT).show()
                state_flag = true
            }
            else {
                image2.setImageResource(R.drawable.heart_unselected)
                Toast.makeText(applicationContext, "?????? ???????????? ???????????????", Toast.LENGTH_SHORT).show()
                state_flag = false
            }
        })
        
        // ????????? ???????????? ????????? ?????? ???????????? ??????
        detailExplain = findViewById(R.id.detailExplain)
        explainIcon = findViewById(R.id.explainIcon)
        explainIcon.setImageResource(R.drawable.ic_show_explain)
        detail = findViewById(R.id.detail)
        var showFlag:Boolean = false
        detailExplain.setOnClickListener({
            if (showFlag == false) {
                detail.visibility = View.VISIBLE
                explainIcon.setImageResource(R.drawable.ic_close_explain)
                showFlag = true
            }
            else {
                detail.visibility = View.GONE
                explainIcon.setImageResource(R.drawable.ic_show_explain)
                showFlag = false
            }
        })
        // sharesheet??? ????????? ?????? ?????? ????????????
        share.setOnClickListener({
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, parcelItem?.title)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        })
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        parcelItem = intent!!.getParcelableExtra<ParcelItem>("parcelItem")
        movieTitle.setText(parcelItem?.title)
        rating.setText(parcelItem?.rating.toString())
        genre.setText(parcelItem?.genre)
        showtime.setText(parcelItem?.showtime)
        audit.setText(parcelItem?.audit)
        val cny = "${parcelItem?.country}, ${parcelItem?.year}"
        countryAndYear.setText(cny)
        year.setText(parcelItem?.year)
        detail_genre.setText(parcelItem?.genre)
        director.setText(parcelItem?.director)
        actors.setText(parcelItem?.actors)
        overview.setText(parcelItem?.overview)
        val imgId: String? = parcelItem?.poster
        posterPath = "http://image.tmdb.org/t/p/w200${imgId}"
        Glide.with(this)
            .load(posterPath)
            .fitCenter()
            .into(poster)

        currentTitle = parcelItem?.title
        val videoUrl = urls.get(movieTitle.text.toString())
        val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
        val userAgent = Util.getUserAgent(this, this.applicationInfo.name)
        val factory = DefaultDataSourceFactory(this, userAgent)
        val progressiveMediaSource = ProgressiveMediaSource.Factory(factory).createMediaSource(mediaItem)
        simpleExoPlayer.setMediaSource(progressiveMediaSource)
        simpleExoPlayer.prepare()
        val position = getSharedIntData("prefPlay", "position${currentTitle}")
        simpleExoPlayer.seekTo(position.toLong())

        exoFullScreen.setOnClickListener {
            val newintent = Intent(this, FullScreenActivity::class.java)
            newintent.putExtra("url", videoUrl)
            setSharedData("prefPlay", "position${currentTitle}", simpleExoPlayer.currentPosition.toInt())
            newintent.putExtra("title", currentTitle)
            startActivity(newintent)
        }

        state_flag = getSharedBooleanData("prefHeart", "heart${currentTitle}")
        // ?????? ????????? ????????? ????????? ??????
        if (state_flag == false) {
            image2.setImageResource(R.drawable.heart_unselected)
        } else {
            image2.setImageResource(R.drawable.heart_selected)
        }
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onStart() {
        super.onStart()
        state_flag = getSharedBooleanData("prefHeart", "heart${currentTitle}")
        // ?????? ????????? ????????? ????????? ??????
        if (state_flag == false) {
            image2.setImageResource(R.drawable.heart_unselected)
        } else {
            image2.setImageResource(R.drawable.heart_selected)
        }
    }
    // onResume() ???????????? ???????????? ????????? ?????? ?????? ???????????? ?????? ?????? ????????????
    // ????????? ??? ????????? onPause() ???????????? ????????? ???????????? ????????? ?????? ????????? ????????? ??????????????? ????????? ???????????? ?????? ????????????.
    override fun onResume() {
        super.onResume()
        val position = getSharedIntData("prefPlay", "position${currentTitle}")
        simpleExoPlayer.seekTo(position.toLong())

    }

    override fun onPause() {
        super.onPause()
        simpleExoPlayer.stop()
        duration = simpleExoPlayer.duration.toInt()
        val progress = simpleExoPlayer.currentPosition.toInt()
        simpleExoPlayer.playWhenReady = true
        // ?????? ????????? ????????????. ???????????? ?????? ????????? ?????????????????? ?????? ????????????.
        setSharedData("prefPlay", "position${currentTitle}", progress)
        setSharedData("prefHeart", "heart${currentTitle}", state_flag)
        simpleExoPlayer.playWhenReady = false

        val record = Record(null, currentTitle!!, posterPath!!, (progress/4200).toLong(), (duration/4200).toLong())
        val check = MainActivity2.helper!!.selectItemRecord(currentTitle!!)
        if (check != -1) {
            MainActivity2.helper!!.updateRecord(check, record)
        } else {
            MainActivity2.helper!!.insertRecord(record)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        simpleExoPlayer.release()
    }

    fun setSharedData(name: String, key: String, data: Int) {
        val pref: SharedPreferences = getSharedPreferences(name, Activity.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit()
        editor.putInt(key, data)
        editor.apply()
    }
    fun setSharedData(name: String, key: String, data: Boolean) {
        val pref: SharedPreferences = getSharedPreferences(name, Activity.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit()
        editor.putBoolean(key, data)
        editor.apply()
    }

    fun getSharedIntData(name: String, key: String): Int {
        val pref: SharedPreferences = getSharedPreferences(name, Activity.MODE_PRIVATE)
        return pref.getInt(key, 0)
    }
    fun getSharedBooleanData(name: String, key: String): Boolean {
        val pref: SharedPreferences = getSharedPreferences(name, Activity.MODE_PRIVATE)
        return pref.getBoolean(key, false)
    }

    fun setSharedData(name: String, key: String, data: String) {
        val pref: SharedPreferences = getSharedPreferences(name, Activity.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = pref.edit()
        editor.putString(key, data)
        editor.apply()
    }

    // ?????? ?????? ????????? ?????? ?????? ???????????? ??? ?????? ?????????
    fun findMovieCode(movieName: String, item: Item) {
        val url = "https://kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.json?key=${kobisApiKey}&movieNm=${movieName}"
        val request = object: StringRequest(
            Request.Method.GET,
            url,
            {
                if (it.indexOf("faultInfo") > -1) {
                    var builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setTitle("??? ????????? ??????").setMessage("?????? ???????????? ?????? ??? ?????? ???????????? ??? ?????? ???????????????\nhttp://kobis.or.kr/kobisopenapi")
                    var dialog: AlertDialog = builder.create()
                    dialog.show()
                } else {
                    processResponseCustom(it, item, movieName)
                }
            },
            {
                Log.e("Error", "?????? ?????? -> ${it.message}")
            }
        ) {}
        request.setShouldCache(false)
        MainActivity2.requestQueue?.add(request)
    }
    // ?????? ?????? ????????? ?????? ?????? ????????????
    fun processResponseCustom(response: String, item: Item, movieName: String) {
        val gson = Gson()
        val customMovieList = gson.fromJson(response, CustomMovieList::class.java)
        val movielist = customMovieList.movieListResult.movieList
        for (i in 0 until(movielist!!.size)) {
            var mItem = movielist?.get(i)
            var tmptitle = mItem.movieNm
            if (tmptitle.equals(movieName)) {
                sendDetailsCustom(movielist?.get(i)?.movieCd, item)
            }
        }
    }
    // ?????? ?????? ????????? ?????? ???????????? ????????????
    fun sendDetailsCustom(movieCd: String?, item: Item) {
        if (movieCd != null) {
            val url = "https://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key=${kobisApiKey}&movieCd=${movieCd}"
            val request = object: StringRequest(
                Request.Method.GET,
                url,
                {
                    processDetailsResponseCutsom(it, item)
                },
                {
                    Log.e("Error", "?????? ?????? -> ${it.message}")
                }
            ) {}
            request.setShouldCache(false)
            MainActivity2.requestQueue?.add(request)
        }
    }
    fun processDetailsResponseCutsom(response: String, item: Item) {
        val gson = Gson()
        val movieInfoDetails = gson.fromJson(response, MovieInfoDetails::class.java)
        val movieDetails = movieInfoDetails.movieInfoResult.movieInfo
        item.showtime = movieDetails.showTm
        item.title = movieDetails.movieNm
        item.year = movieDetails.openDt
        if (movieDetails.nations.size > 0) {
            item.country = movieDetails.nations.get(0).nationNm
        }
        if (movieDetails.genres.size > 0) {
            item.genre = movieDetails.genres.get(0).genreNm
        }
        if (movieDetails.directors.size > 0) {
            item.director = movieDetails.directors.get(0).peopleNm
        }
        if (movieDetails.actors.size > 0) {
            item.actors = movieDetails.actors.get(0).peopleNm
        }
        if (movieDetails.audits.size > 0) {
            item.audit = movieDetails.audits.get(0).watchGradeNm
        }
        sendTMDBSearchCustom(item.title, item)

    }
    // ?????? ????????? ???????????? TMDB?????? ?????? ?????? ?????? ?????????
    fun sendTMDBSearchCustom(movieName: String?, item: Item) {
        if(movieName != null) {
            val apiKey = "52023190e7574a346ef78d1397106945"
            val url = "https://api.themoviedb.org/3/search/movie?api_key=${apiKey}&query=${movieName}&language=ko-KR&page=1"
            val request = object: StringRequest(
                Request.Method.GET,
                url,
                {
                    processTMDBSearchResponseCustom(it, item)
                },
                {
                    Log.e("Error", "?????? ?????? -> ${it.message}")
                }
            ) {}
            request.setShouldCache(false)
            MainActivity2.requestQueue?.add(request)
        }
    }
    fun processTMDBSearchResponseCustom(response: String, item: Item) {
        val gson = Gson()
        val tmdbMovieDetails = gson.fromJson(response, TmdbMovieDetails::class.java)
        val movieResult = tmdbMovieDetails.results
        for (i in 0 until(movieResult.size)) {
            var movieresult = movieResult.get(i)
            var tmptitle = movieresult.title?.replace(" ", "")
            var notitle = item.title?.replace(" ", "")

            if (tmptitle.equals(notitle)) {
                item.poster = movieresult.poster_path
                item.overview = movieresult.overview
                item.rating = movieresult.vote_average

                var intent: Intent = Intent(applicationContext, PlayActivity::class.java)
                var parcelItem: ParcelItem = ParcelItem(item.title, item.rating, item.genre, item.showtime,
                    item.audit, item.country, item.year, item.director,
                    item.actors, item.overview, item.poster)
                intent.putExtra("parcelItem", parcelItem)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
            }
        }
    }
}
