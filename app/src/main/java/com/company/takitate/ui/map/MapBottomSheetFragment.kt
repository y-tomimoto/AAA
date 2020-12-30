package com.company.takitate.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.company.takitate.R
import com.company.takitate.databinding.FragmentMapBottomSheetBinding
import com.company.takitate.viewmodel.MapBottomSheetViewModel

class MapBottomSheetFragment : Fragment() {

  private lateinit var viewModel: MapBottomSheetViewModel
  private lateinit var binding: FragmentMapBottomSheetBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    // bottomSheetにViewModelを付与
    viewModel = ViewModelProvider(this).get(MapBottomSheetViewModel::class.java) // AndroidViewModelを継承した場合、インスタンス化はViewModelProvider経由で行う
    binding = DataBindingUtil.inflate(inflater,R.layout.fragment_map_bottom_sheet, container,false) // bindingクラスを生成
    binding.vm = viewModel // viewModelをsetしている
    binding.lifecycleOwner = this // LiveDataが値の更新を検知するように設定する
    viewModel.updateBottomSheetText("test")
    return binding.root
  }
}
