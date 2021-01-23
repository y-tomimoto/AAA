package com.company.takitate.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.company.takitate.R
import com.company.takitate.databinding.FragmentMapBottomSheetBinding
import com.company.takitate.viewmodel.RecruitAPIResponseShopViewModel
import com.google.android.material.snackbar.Snackbar

class MapBottomSheetFragment : Fragment() {

  private lateinit var binding: FragmentMapBottomSheetBinding

  // プロパティデリゲートでviewModelを取得する方法
  private val viewModel:RecruitAPIResponseShopViewModel by activityViewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    binding = DataBindingUtil.inflate(inflater,R.layout.fragment_map_bottom_sheet, container,false) // bindingクラスを生成
    binding.vm = viewModel
    binding.lifecycleOwner = this // LiveDataが値の更新を検知するように設定する

    val fab: View = binding.fab
    fab.setOnClickListener { view ->
      // ReviewFragmentへ以降する。このとき、店舗IDをFragmentに渡す。
      println(viewModel.focusedShop.value?.id)
      findNavController().navigate(R.id.action_Map_to_AddReview)
    }


    return binding.root
  }
}
