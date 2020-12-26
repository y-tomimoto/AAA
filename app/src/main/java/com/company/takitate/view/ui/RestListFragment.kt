package com.company.takitate.view.ui

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
import com.company.takitate.BuildConfig
import com.company.takitate.R
import com.company.takitate.databinding.FragmentRestListBinding
import com.company.takitate.service.model.GNaviResponse
import com.company.takitate.service.model.Rest
import com.company.takitate.service.repository.GNaviRepository
import com.company.takitate.view.adapter.MyRestListRecyclerViewAdapter
import com.company.takitate.view.adapter.RestAdapter
import com.company.takitate.view.callback.RestClickCallback
import com.company.takitate.view.dummy.DummyContent
import com.company.takitate.viewmodel.RestListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Response


const val TAG_OF_REST_LIST_FRAGMENT = "RestListFragment"
const val API_URL_PREFIX = "www.googleapis.com"

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
          (activity as MainActivity).show(rest) // Fragmentの遷移を、restを元に実行
        }
      }
    })

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    // Databinding対象viewとして、listViewを指定している。
    binding = DataBindingUtil.inflate(
      inflater,
      R.layout.fragment_rest_list,
      container,
      false
    )

    binding.apply {
      // RecyclerViewにadapterをattachしている
      println("attach")
      restList.adapter = restAdapter // bindingのid 『restList』 のViewの型がRecyclerView
      isLoading = true // layout自体にattachしている
//      button.setOnClickListener(View.OnClickListener() {
//        val repository = GNaviRepository.instance
//        CoroutineScope(Dispatchers.IO).launch {
//          // runBlocking {
//          println("dada")
//          repository.getTest(
//            BuildConfig.GNAVI_API_KEY,
//            1,
//            139.6353565, // TODO: 外部から取得
//            35.6994197 // TODO: 外部から取得
//          )
//        }
//      })
      println("Attached adapter!! ")
    }
    return binding.root // rootタグである <layout> をresしている。
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    println("onActivityCreated!! trying observe ...  ")
    viewModel.restListLiveData.observe(viewLifecycleOwner, Observer { rest ->
      if (rest != null) {
        binding.isLoading = false
        restAdapter.setRestList(rest.rest)
        println("Mod LiveData!!")
      }
    })
  }
}
