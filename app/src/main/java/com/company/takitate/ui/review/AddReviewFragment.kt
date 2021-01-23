package com.company.takitate.ui.review

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.company.takitate.R
import com.company.takitate.data.repository.driver.MyDatabase
import com.company.takitate.databinding.FragmentAddReviewBinding
import com.company.takitate.databinding.FragmentMapBottomSheetBinding
import com.company.takitate.domain.entity.Review
import com.company.takitate.domain.entity.Reviewer
import com.company.takitate.viewmodel.RecruitAPIResponseShopViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddReviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddReviewFragment : Fragment() {
  // TODO: Rename and change types of parameters
  private var param1: String? = null
  private var param2: String? = null

  private val viewModel: RecruitAPIResponseShopViewModel by activityViewModels()
  private lateinit var binding: FragmentAddReviewBinding

  private lateinit var db: MyDatabase

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      param1 = it.getString(ARG_PARAM1)
      param2 = it.getString(ARG_PARAM2)
    }
    // dbインスタンスを用意
    db = Room.databaseBuilder(
      requireContext(),
      MyDatabase::class.java, "room-database"
    ).build()

  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_review, container,false) // bindingクラスを生成
    binding.vm = viewModel
    binding.lifecycleOwner = this // LiveDataが値の更新を検知するように設定する

    val fab: View = binding.fab
    fab.setOnClickListener { view ->
      // launch は Builder
      // GlobalScope はコルーチンスコープ
      GlobalScope.launch {
        val reviewer = Reviewer(birthday = DateTime(), reviewer_id = 0, handle = "john doe")
        db.reviewerDao().insertReviewer(reviewer)
        val review = viewModel.focusedShop.value?.let {
          Review(
            comment = "まずい",
            review_id = UUID.randomUUID().toString(),
            datetime = DateTime(),
            reviewer_id = "1",
            restaurant_id = it.id,
            title = "まず過ぎる"
          )
        }
        if (review != null) {
          db.reviewDao().insertReview(review)
        }
      }
    }

    return binding.root



  }

  companion object {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddReviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    @JvmStatic
    fun newInstance(param1: String, param2: String) =
      AddReviewFragment().apply {
        arguments = Bundle().apply {
          putString(ARG_PARAM1, param1)
          putString(ARG_PARAM2, param2)
        }
      }
  }
}
