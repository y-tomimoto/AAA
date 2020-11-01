package io.github.reservationbytom.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
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

class RestListFragment : Fragment() {

  private val viewModel by lazy {
    ViewModelProvider(this).get(RestListViewModel::class.java)
  }

  private lateinit var binding: FragmentRestListBinding

  // Adapterをインスタンス化して取得
  private val restAdapter: RestAdapter =
    RestAdapter(object :
      RestClickCallback {
      override fun onClick(rest: Rest) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED) && activity is MainActivity) {
          (activity as MainActivity).show(rest)
        }
      }
    })

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Binding View
    binding = DataBindingUtil.inflate(
      inflater,
      R.layout.fragment_rest_list,
      container,
      false
    )

    binding.apply {
      restList.adapter = restAdapter // bindingのid 『restList』 のViewの型がRecyclerView
      isLoading = true
    }
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel.restListLiveData.observe(viewLifecycleOwner, Observer { rest ->
      if (rest != null) {
        binding.isLoading = false
        restAdapter.setRestList(rest.rest)
      }
    })
  }
}
