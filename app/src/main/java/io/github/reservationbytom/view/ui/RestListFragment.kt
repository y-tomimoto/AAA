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
import io.github.reservationbytom.BuildConfig
import io.github.reservationbytom.R
import io.github.reservationbytom.databinding.FragmentRestListBinding
import io.github.reservationbytom.service.model.GNaviResponse
import io.github.reservationbytom.service.model.Rest
import io.github.reservationbytom.service.repository.GNaviRepository
import io.github.reservationbytom.view.adapter.MyRestListRecyclerViewAdapter
import io.github.reservationbytom.view.adapter.RestAdapter
import io.github.reservationbytom.view.callback.RestClickCallback
import io.github.reservationbytom.view.dummy.DummyContent
import io.github.reservationbytom.viewmodel.RestListViewModel
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
      restList.adapter = restAdapter // bindingのid 『restList』 のViewの型がRecyclerView
      isLoading = true // layout自体にattachしている
      button.setOnClickListener(View.OnClickListener() {
        val repository = GNaviRepository.instance
        CoroutineScope(Dispatchers.IO).launch {
          // runBlocking {
          println("dada")
          val result = repository.getTest(
            BuildConfig.GNAVI_API_KEY,
            1,
            33.3, // TODO: 外部から取得
            33.3 // TODO: 外部から取得
          )
          print(result)

        }
      })
      println("Attached adapter!! ")
    }




    return binding.root // rootタグである <layout> をresしている。
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    println("onActivityCreated!! trying observe ...  ")

    viewModel.restListLiveData.observe(viewLifecycleOwner, Observer { rest ->
      println("No Mod LiveData!!")
      if (rest != null) {
        binding.isLoading = false
        restAdapter.setRestList(rest.rest)
        println("Mod LiveData!!")
      }
    })
  }
}
