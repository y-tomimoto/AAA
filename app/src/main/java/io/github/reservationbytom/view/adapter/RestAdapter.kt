package io.github.reservationbytom.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.reservationbytom.R
import io.github.reservationbytom.databinding.RestListItemBinding
import io.github.reservationbytom.service.model.Rest
import io.github.reservationbytom.view.callback.RestClickCallback

class RestAdapter(private val restClickCallback: RestClickCallback?) :
  RecyclerView.Adapter<RestAdapter.RestViewHolder>() { // 1行文のVIew: RestAdapter.RestViewHolder。Genericsで型を指定。

  private var restList: List<Rest>? = null

  fun setRestList(restList: List<Rest>) {

    println("rub")
    if (this.restList == null) {
      this.restList = restList // 引数で受け取ったrestList
      println("null")

      // RecyclerViewの組み込み。
      // positionStartの位置からitemCountの範囲において、データの変更があったことを登録されているすべてのobserverに通知する。
      notifyItemRangeInserted(0, restList.size)

    } else {
      println("dada")
      // 既に this.restList が値を持っていた場合
      val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

        // コンストラクタとしてcallbackを渡している

        // DiffUtil: https://developer.android.com/reference/android/support/v7/util/DiffUtil.html
        override fun getOldListSize(): Int {
          // null check: https://scache.hatenablog.com/entry/2017/12/07/032601
          // 事前に宣言したrestList
          // this@class => 親クラスを補足する: https://qiita.com/AAkira/items/99028e20fcfe1a79af95
          return requireNotNull(this@RestAdapter.restList).size
        }

        override fun getNewListSize(): Int {
          // 引数で受け取ったrestList
          return restList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
          val oldList = this@RestAdapter.restList
          return oldList?.get(oldItemPosition)?.id == restList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
          val rest = restList[newItemPosition]
          val old = restList[oldItemPosition]

          return rest.id == old.id
        }
      })

      this.restList = restList

      // adapterの更新
      result.dispatchUpdatesTo(this)
    }
  }

  // 2番目にcall
  // onCreateViewHolder は RecyclerView から、与えられたデータ数だけcallされる
  // findViewByIDを保管する。内部にはViewを持つ
  // ここでResされるViewHolderを元に、行を管理するViewがRenderingされる。
  override fun onCreateViewHolder(parent: ViewGroup, viewtype: Int): RestViewHolder {

    // BindingViewとしてViewを保持
    val binding =
      DataBindingUtil.inflate(
        LayoutInflater.from(parent.context),
        R.layout.rest_list_item, parent, // ViewGroup Parent を指定
        false
      ) as RestListItemBinding

    binding.callback = restClickCallback // callbackはlayout内の値にattach。constructorから受け取る。

    // Binding View を持つ ViewHolder を返している
    println("return ViewHolder!!")
    return RestViewHolder(
      binding
    )
  }

  // Modelに値をset。
  // 生成したViewHolderに対して、受け取った値をsetする
  override fun onBindViewHolder(holder: RestViewHolder, position: Int) {
    holder.binding.rest = restList?.get(position)
    // バインドを即時反映するためのexecutePendingBindings: https://techbooster.org/android/application/17971/
    holder.binding.executePendingBindings()
    println("Binding data to ViewHolder ...!!")
  }

  // RecyclerView.Adapterに自作のAdapterがattachされたとき、まずはここがcallsされる
  override fun getItemCount(): Int {
    println("count: ${restList?.size}")
    return restList?.size ?: 0
  }

  // ViewHolder Class。継承の際、Constructorを指定
  // ViewHolder: http://hamup.hatenablog.com/entry/2019/05/12/115907#:~:text=ViewHolder,%E3%82%92RecyclerView%E3%81%AB%E6%A0%BC%E7%B4%8D%E3%81%99%E3%82%8B%E3%80%82
  // Googleが推奨するパターンを達成するためのClass
  // binding.root = ItemView 引数で受け取ったbindingから、rootのlayoutタグを指定している。
  open class RestViewHolder(val binding: RestListItemBinding) : RecyclerView.ViewHolder(binding.root)

}
