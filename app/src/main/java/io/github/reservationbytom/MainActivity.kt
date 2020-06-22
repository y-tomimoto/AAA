package io.github.reservationbytom

import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.home_activity.*
import org.threeten.bp.DayOfWeek
import org.threeten.bp.temporal.WeekFields
import java.util.*
import androidx.recyclerview.widget.RecyclerView
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.jakewharton.threetenabp.AndroidThreeTen


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
        if (this is HasToolbar) {
            requireActivity().homeToolbar.makeGone()
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        }

        if (this is HasBackButton) {
            val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
            actionBar?.title = if (titleRes != null) context?.getString(titleRes!!) else ""
            actionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onStop() {
        super.onStop()
        if (this is HasToolbar) {
            requireActivity().homeToolbar.makeVisible()
            (requireActivity() as AppCompatActivity).setSupportActionBar(requireActivity().homeToolbar)
        }

        if (this is HasBackButton) {
            val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
            actionBar?.title = context?.getString(R.string.app_name)
            actionBar?.setDisplayHomeAsUpEnabled(false)
        }
    }

    abstract val titleRes: Int?
}


// @StringRes : https://maku77.github.io/android/fw/res-annotation.html
data class ExampleItem(@StringRes val titleRes: Int, @StringRes val subtitleRes: Int, val createView: () -> BaseFragment)

// これがインスタンス。これを渡す。
class HomeOptionsAdapter(val onClick: (ExampleItem) -> Unit) :
        RecyclerView.Adapter<HomeOptionsAdapter.HomeOptionsViewHolder>() { // これはクラス内のクラスを継承している。そして、ここでは、ジェネリクスとして、viewHolderを渡している。

    // これはadapterを継承している。そして、HomeOptionsViewHolderは、実際に割り当てるviewをもっている
    // これはただのデータクラス。コンストラクタに関数を引き渡している。これはBaaeFragmentをreturnする関数がすべて

    // これはlist
    val examples = listOf(
        ExampleItem(
            R.string.example_3_title,
            R.string.example_3_subtitle
        ) { Example3Fragment() }
    )


    // ここで、それぞれの値が割り振られるViewをholderとして生成している
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeOptionsViewHolder { // 戻り値の型がHomeOptionsViewHolderということ、継承しているのは下記
        return HomeOptionsViewHolder( //　ここでviewを渡してinstance化している
                LayoutInflater.from(parent.context).inflate(R.layout.home_options_item_view, parent, false)
        )
    }

    override fun onBindViewHolder(viewHolder: HomeOptionsViewHolder, position: Int) {
        viewHolder.bind(examples[position])
    }

    override fun getItemCount(): Int = examples.size

    // ここでクラスを生成している。これは、実際にviewが割り振られる列のこと
    inner class HomeOptionsViewHolder(override val containerView: View) :
            RecyclerView.ViewHolder(containerView), LayoutContainer { // 継承する際は、Viewholderとしている。
        // 実態化したら、データから、配列番目のアクションを起動している
        init {
            itemView.setOnClickListener {
                onClick(examples[adapterPosition])// ここにクリックされた時に挙動を書いてある。クリックしたらこの関数を実行するみたいだな、
            }
        }
        // ここで、実際のviewに対して、受け取った値、いわゆるデータをbindする
        fun bind(item: ExampleItem) { // つまり今回、ViewHolderを生成するクラスで、view自体を外部からもらってViewに割り当てるクラスで、エラーがおきている、
            val context = itemView.context
            val itemOptionTitle = containerView.findViewById<TextView>(R.id.itemOptionTitle)
            val itemOptionSubtitle = containerView.findViewById<TextView>(R.id.itemOptionSubtitle)

            //  このitemは、ExampleItemのことを表している
            itemOptionTitle.text = if (item.titleRes != 0) context.getString(item.titleRes) else null
            itemOptionTitle.isVisible = itemOptionTitle.text.isNotBlank()

            itemOptionSubtitle.text = if (item.subtitleRes != 0) context.getString(item.subtitleRes) else null
            itemOptionSubtitle.isVisible = itemOptionSubtitle.text.isNotBlank()
        }
    }

}

class MainActivity : AppCompatActivity() {
    // ここでインスタンスを作成している。
    // これが、1つのViewに割り当てられる

    private val callbackManager: CallbackManager = CallbackManager.Factory.create();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val loginButton = findViewById<LoginButton>(R.id.login_button);
        val testButton = findViewById<Button>(R.id.testButton);

        testButton.setOnClickListener  {
            val intent = Intent(applicationContext,HomeActivity::class.java)
            startActivity(intent)
        }
        Log.d("tag","dadadada")
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, object: FacebookCallback<LoginResult> {

            override fun onSuccess(result: LoginResult?) {
                val intent = Intent(applicationContext,HomeActivity::class.java)
                startActivity(intent)
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

class HomeActivity : AppCompatActivity() {
        private val examplesAdapter = HomeOptionsAdapter {
        //ここが引数になっている。
        val fragment = it.createView() // ここにはdata内に宣言されているものが入る。
        // ここで、戻り値をエル。
        supportFragmentManager.beginTransaction() // ここでfragmentを実行している //  あー、これは返し方を定義しているのね、、、
            .run {
                return@run setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
            }
            .add(
                R.id.homeContainer,
                fragment,
                fragment.javaClass.simpleName
            ) // ここにrepもあるのね。そしてここにはtagが入る

            // 3つ目の引数はなんだ？
            .addToBackStack(fragment.javaClass.simpleName) // これで戻るボタンを押すとfragmentに戻れる。一回これなしでやってみる。今のアクティビテぃの後続としてfragmentを採用するかたちか。
            .commit() // これが設定の反映ね。
        // ここまでがtapしたときの動き。これがhome画面からtapしたときの遷移になる。
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity) // この画面ではなく、別の画面を起動する。
        setSupportActionBar(homeToolbar)
        AndroidThreeTen.init(this);
        val examplesRv = findViewById<RecyclerView>(R.id.examplesRv)
        // このexam- はレイアウトになる。どこでinitしているのかは謎。importか？
        // ここで、RecyclerViewに各要素を割り当て
        examplesRv.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)// これはどう並べるか
        examplesRv.adapter =
            examplesAdapter // ここでadaptorを採用 このときはそのままぶち込み案件 // これは、1つの viewにどのデータを割り当てるか
        examplesRv.addItemDecoration(
            DividerItemDecoration(
                this,
                RecyclerView.VERTICAL
            )
        ) //  ここで枠線を採用
    }
        override fun onOptionsItemSelected(item: MenuItem): Boolean { // いまどのフラグメントにいるのかを保持している
        return when (item.itemId) {
            android.R.id.home -> onBackPressed().let { true }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
