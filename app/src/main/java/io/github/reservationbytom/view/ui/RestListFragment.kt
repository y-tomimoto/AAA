package io.github.reservationbytom.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import io.github.reservationbytom.R
import io.github.reservationbytom.databinding.FragmentRestListBinding
import io.github.reservationbytom.service.model.Rest
import io.github.reservationbytom.view.adapter.MyRestListRecyclerViewAdapter
import io.github.reservationbytom.view.adapter.RestAdapter
import io.github.reservationbytom.view.callback.RestClickCallback
import io.github.reservationbytom.view.dummy.DummyContent
import io.github.reservationbytom.viewmodel.RestListViewModel


const val TAG_OF_REST_LIST_FRAGMENT = "RestListFragment"

/**
 * A fragment representing a list of Items.
 */
class RestListFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(RestListViewModel::class.java)
    }

    private lateinit var binding: FragmentRestListBinding

    private val restAdapter: RestAdapter =
        RestAdapter(object :
            RestClickCallback {
            override fun onClick(rest: Rest) {
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED) && activity is MainActivity) {
                    (activity as MainActivity).show(rest)
                }
            }
        })

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // nullableに対する拡張関数
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT) // bundleから受け取ったカラム数を挿入
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rest_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {

            // withはレシーバとしてviewを受け取り、内部にラムダを記載
            with(view) {
                layoutManager = when {
                    // カウントが0だったら、viewにLinearLayoutManagerを代入し、
                    columnCount <= 1 -> LinearLayoutManager(context)
                    // そうでなければ、viewにGridLayoutManagerを代入する
                    else -> GridLayoutManager(context, columnCount)
                }
                // RecyclerViewのAdapterに、カスタムAdapterを挿入している
                adapter =
                    MyRestListRecyclerViewAdapter(
                        DummyContent.ITEMS
                    )
            }
        }
        return view
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: JvmStaticのユースケース
        @JvmStatic // @JvmStatic: https://minpro.net/kotlin-jvmstatic
        fun newInstance(columnCount: Int) = // コンストラクタとしてカラムを受け取っている。
            RestListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount) // 受け取ったカラムはbundleを経由してpassされている。
                }
            }
    }
}