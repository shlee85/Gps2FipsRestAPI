package com.example.gps2fipsrest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

data class User(

    //아래 어노테이션은 객체를 직렬화 및 역직렬화 할 때 사용.
    //JSON에서 userId로 표현  { "userId":1 } 만약에 변수가 userId가 아니고 id이어도 json에서는 userId가 된다.
    @SerializedName("County")
    val County : County

    /*
    @SerializedName("id")
    val id: String,

    @SerializedName("body")
    val body: String

     */

    // @SerializedName으로 일치시켜 주지않을 경우엔 클래스 변수명이 일치해야함
    // @SerializedName()로 변수명을 입치시켜주면 클래스 변수명이 달라도 알아서 매핑
)

data class County (
    val FIPS : String,
    val name : String
)

interface RetrofitService {

    //GET 예제
    //@GET("posts/1")
    @GET("block/find?latitude=59.58303&longitude=-139.02601&censusYear=2020&showall=true&format=json")
    fun getUser(): Call<User>

    //@GET("posts/{page}")
    @GET("block/find?{lat}&{lng}&format=json")
    fun getUserPage(@Path("page") page: String): Call<User>
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://geo.fcc.gov/api/census/#!/block/get_block_find")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RetrofitService::class.java)

        //service.getUserPage("1").enqueue(object : Callback<User>{
        service.getUser().enqueue(object : Callback<User>{
            override fun onResponse(call: Call<User>, response: Response<User>) {
                Log.d(TAG, "response : $response")
                if(response.isSuccessful) {
                    var result: User? = response.body()
                    Log.d(TAG, "onResponse 성공 : " + result?.toString())
                    Log.d(TAG, "result : ${result?.County?.FIPS}")
                } else {
                    Log.d(TAG, "onResponse 실패")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d(TAG, "onFailure 에러 : " + t.message.toString())
            }
        })
    }

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }
}