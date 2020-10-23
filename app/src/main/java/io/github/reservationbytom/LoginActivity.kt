package io.github.reservationbytom

import android.Manifest
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.facebook.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.location.*
import com.google.gson.GsonBuilder
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.extensions.LayoutContainer
import okhttp3.ResponseBody
import org.json.JSONObject
import org.threeten.bp.DayOfWeek
import org.threeten.bp.temporal.WeekFields
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit


// 通常で実行されて、Viewクラスを拡張しているとか？？
fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInVisible() {
    visibility = View.INVISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

fun dpToPx(dp: Int, context: Context): Int =
    TypedValue.applyDimension(

        TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
        context.resources.displayMetrics
    ).toInt()

internal fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

internal inline fun Boolean?.orFalse(): Boolean = this ?: false

internal fun Context.getDrawableCompat(@DrawableRes drawable: Int) = ContextCompat.getDrawable(this, drawable)

internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

internal fun TextView.setTextColorRes(@ColorRes color: Int) = setTextColor(context.getColorCompat(color))

fun daysOfWeekFromLocale(): Array<DayOfWeek> {
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    var daysOfWeek = DayOfWeek.values()
    // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
    if (firstDayOfWeek != DayOfWeek.MONDAY) {
        val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
        val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
        daysOfWeek = rhs + lhs
    }
    return daysOfWeek
}

fun GradientDrawable.setCornerRadius(
    topLeft: Float = 0F,
    topRight: Float = 0F,
    bottomRight: Float = 0F,
    bottomLeft: Float = 0F
) {
    cornerRadii = arrayOf(
        topLeft, topLeft,
        topRight, topRight,
        bottomRight, bottomRight,
        bottomLeft, bottomLeft
    ).toFloatArray()
}


interface HasToolbar {
    val toolbar: Toolbar? // Return null to hide the toolbar
}

interface HasBackButton


abstract class BaseFragment : Fragment() {

    override fun onStart() {
        super.onStart()

        if (this is HasBackButton) {
            val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
            actionBar?.title = if (titleRes != null) context?.getString(titleRes!!) else ""
            actionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onStop() {
        super.onStop()

        if (this is HasBackButton) {
            val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
            actionBar?.title = context?.getString(R.string.app_name)
            actionBar?.setDisplayHomeAsUpEnabled(false)
        }
    }

    abstract val titleRes: Int?
}

class LoginActivity : AppCompatActivity() {
    // ここでインスタンスを作成している。
    // これが、1つのViewに割り当てられる

    // 位置情報を取得できるクラス
    private lateinit var fusedLocationClient : FusedLocationProviderClient

    private val REQUEST_CODE = 1000

    private val callbackManager: CallbackManager = CallbackManager.Factory.create();

    interface IGetRestaurants{
        @GET("maps/api/place/findplacefromtext/json?input=Museum%20of%20Contemporary%20Art%20Australia&inputtype=textquery&fields=photos,formatted_address,name,rating,opening_hours,geometry&key=${BuildConfig.GOOGLE_MAP_API_KEY}")
        fun getRestaurants(): Call<ResponseBody>
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().serializeNulls().create()))
        .baseUrl("https://maps.googleapis.com/")
        .build()

    private val service: IGetRestaurants = retrofit.create(IGetRestaurants::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        AndroidThreeTen.init(this);

        val scheduler = getSystemService(JobScheduler::class.java)
        println(TimeUnit.MINUTES.toMillis(1))
        val jobInfo = JobInfo.Builder(
            1, // JobID
            ComponentName(this, GetLocationService::class.java)) // job
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY) // 通信環境
            .build()

        // ここでjobを登録
        scheduler.schedule(jobInfo)

        val call = service.getRestaurants()

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                try {
                    if (response != null) {
                        println(response.code())
                    }
                    if (response != null) {
                        println(response.body()?.string())
                    }
                } catch (e: IOException) {
                    Log.d("onResponse", "IOException")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

        // 位置情報を更新
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            val permissions = arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
            return
        }

        fusedLocationClient = FusedLocationProviderClient(this)

        // どのような取得方法を要求
        val locationRequest = LocationRequest().apply {
            // 精度重視(電力大)と省電力重視(精度低)を両立するため2種類の更新間隔を指定
            // 今回は公式のサンプル通りにする。
            interval = 10000                                   // 最遅の更新間隔(但し正確ではない。)
            fastestInterval = 5000                             // 最短の更新間隔
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY  // 精度重視
        }

        // コールバック
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                // 更新直後の位置が格納されているはず
                val location = locationResult?.lastLocation ?: return
                println("========called=======")
                println(location)
                println(location.latitude)
                println(location.longitude)

            }
        }

        println("========start=======")
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        println("========end=======")


        val loginButton = findViewById<LoginButton>(R.id.login_button);
        val testButton = findViewById<Button>(R.id.testButton);

        testButton.setOnClickListener  {

            // もしかすると、Activityに遷移したあと、そこで新たに生成する必要がありそう。
            // ここでは、該当の Activityを呼ぶか。
            val intent = Intent(applicationContext,MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)

        }
        Log.d("tag","dadadada")
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, object: FacebookCallback<LoginResult> {
            val callback = object :  GraphRequest.GraphJSONObjectCallback{
                override fun onCompleted(`object`: JSONObject?, response: GraphResponse?) {
                    println("==============")
                    if (`object` != null) {
                        println("==============")
                        print(`object`)
                        print(`object`.getString("email"))
                        println("==============")
                    }
                    println("==============")
                }

            }
            override fun onSuccess(result: LoginResult?) {
                val accessToken = result?.accessToken
                val request = GraphRequest.newMeRequest(accessToken, callback)
                val parameters = Bundle()
                // ここでrequestに付与したparameterがレスポンスとして帰ってくる。
                parameters.putString("fields", "id, name, email, gender")
                request.parameters = parameters
                request.executeAsync()
                // ここでlogin処理を噛ましているみたい。
                // おそらく、このresult内にlogin情報が入っていそう。
                println("==============")
                println("==============")
                println(result)
                println("==============")
                println("==============")
                val intent = Intent(applicationContext,MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
//                val intent = Intent(applicationContext,Home::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                startActivity(intent)
                Log.d("tag","da")
            }

            override fun onCancel() {
                Log.d("tag","dada")

            }

            override fun onError(error: FacebookException?) {
                Log.d("tag","dadada")

            }

        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode,
            resultCode, data)
    }

}

data class TestObject (
    val Item:String,
    val title: String
)

//class HomeActivity : AppCompatActivity() {
//        private val examplesAdapter = HomeOptionsAdapter {
//        //ここが引数になっている。
//        val fragment = it.createView() // ここにはdata内に宣言されているものが入る。
//        // ここで、戻り値をエル。
//        supportFragmentManager.beginTransaction() // ここでfragmentを実行している //  あー、これは返し方を定義しているのね、、、
//            .run {
//                return@run setCustomAnimations(
//                    R.anim.slide_in_right,
//                    R.anim.slide_out_left,
//                    R.anim.slide_in_left,
//                    R.anim.slide_out_right
//                )
//            }
//            .add(
//                R.id.homeContainer,
//                fragment,
//                fragment.javaClass.simpleName
//            ) // ここにrepもあるのね。そしてここにはtagが入る
//
//            // 3つ目の引数はなんだ？
//            .addToBackStack(fragment.javaClass.simpleName) // これで戻るボタンを押すとfragmentに戻れる。一回これなしでやってみる。今のアクティビテぃの後続としてfragmentを採用するかたちか。
//            .commit() // これが設定の反映ね。
//        // ここまでがtapしたときの動き。これがhome画面からtapしたときの遷移になる。
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.home_activity) // この画面ではなく、別の画面を起動する。
//        setSupportActionBar(homeToolbar)
//        AndroidThreeTen.init(this);
//        val examplesRv = findViewById<RecyclerView>(R.id.examplesRv)
//        // このexam- はレイアウトになる。どこでinitしているのかは謎。importか？
//        // ここで、RecyclerViewに各要素を割り当て
//        examplesRv.layoutManager =
//            LinearLayoutManager(this, RecyclerView.VERTICAL, false)// これはどう並べるか
//        examplesRv.adapter =
//            examplesAdapter // ここでadaptorを採用 このときはそのままぶち込み案件 // これは、1つの viewにどのデータを割り当てるか
//        examplesRv.addItemDecoration(
//            DividerItemDecoration(
//                this,
//                RecyclerView.VERTICAL
//            )
//        ) //  ここで枠線を採用
//    }
//        override fun onOptionsItemSelected(item: MenuItem): Boolean { // いまどのフラグメントにいるのかを保持している
//        return when (item.itemId) {
//            android.R.id.home -> onBackPressed().let { true }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//}
