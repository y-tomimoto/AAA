package com.company.takitate.ui.map

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.company.takitate.R
import com.company.takitate.databinding.FragmentMapBottomSheetBinding
import com.company.takitate.viewmodel.RecruitAPIResponseShopViewModel
import com.squareup.picasso.Picasso


class MapBottomSheetFragment : Fragment() {

  private lateinit var binding: FragmentMapBottomSheetBinding

  // プロパティデリゲートでviewModelを取得する方法
  private val viewModel:RecruitAPIResponseShopViewModel by activityViewModels()

  // ForcusされたShopの画像をloadするために利用。
  private val recruitAPIResponseFocusedShopViewModel: RecruitAPIResponseShopViewModel by activityViewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    binding = DataBindingUtil.inflate(
      inflater,
      R.layout.fragment_map_bottom_sheet,
      container,
      false
    ) // bindingクラスを生成
    binding.viewModel = viewModel
    binding.lifecycleOwner = this // LiveDataが値の更新を検知するように設定する
    val fab: View = binding.fab
    fab.setOnClickListener { view ->
      // ReviewFragmentへ以降する。このとき、店舗IDをFragmentに渡す。
      findNavController().navigate(R.id.action_Map_to_AddReview)
    }

    val animationDrawable = binding.contentBox.background  as AnimationDrawable
    animationDrawable.setEnterFadeDuration(2000)
    animationDrawable.setExitFadeDuration(4000)
    animationDrawable.start()


    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    // ここでViewModelから流れてきた値を受け取る.
    recruitAPIResponseFocusedShopViewModel.focusedShop.observe(viewLifecycleOwner, Observer { shop ->
      // 対象がupdateされたとき、画像をbindする。
      Picasso.get()
        .load(shop.logo_image)
        .placeholder(R.drawable.ic_baseline_add_24)
        .error(R.drawable.ic_baseline_add_24)
        .into(binding.profileImage);
    })
  }
}
