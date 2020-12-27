package com.company.takitate.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.company.takitate.BuildConfig
import com.company.takitate.R
import com.company.takitate.databinding.FragmentRestDetailsBinding
import com.company.takitate.viewmodel.RestViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [RestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RestFragment : Fragment() {

    private lateinit var binding: FragmentRestDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO: DataBinding-ktx と比較検討
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rest_details, container, false)
        return binding.root // Binding先のroot viewを返す。getRoot()でも可
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val restID = arguments?.getInt(KEY_REST_ID)

        val factory = RestViewModel.Factory(
            requireActivity().application, restID ?: 0
        )

        val viewModel = ViewModelProvider(this, factory).get(RestViewModel::class.java)

        // apply: https://qiita.com/wakwak/items/7f576b4dbada0995f069
        // applyの採用はアンチパターン。letを採用する: https://www.slideshare.net/RecruitLifestyle/kotlin-87339759
        binding.apply {
            restViewModel = viewModel
            isLoading = true
        }
        observeViewModel(viewModel)
    }

    private fun observeViewModel(viewModel: RestViewModel) {
        viewModel.restLiveData.observe(viewLifecycleOwner, Observer { rest -> // restLiveData.postValue(rest) で発火する

            // ここにliveData更新時の処理を記載
            if (rest != null) {
                binding.isLoading = false
                viewModel.setRest(rest)
            }
        })
    }

    companion object {
        private const val KEY_REST_ID = "rest_id"

        fun forRest(restID: String): RestFragment {
            val fragment = RestFragment()
            val args = Bundle()

            args.putString(KEY_REST_ID, restID)
            fragment.arguments = args

            return fragment
        }
    }
}
