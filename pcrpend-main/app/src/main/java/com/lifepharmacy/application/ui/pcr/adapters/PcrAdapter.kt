package com.lifepharmacy.application.ui.rewards.adapters

import android.app.Activity
import android.graphics.Paint
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lifepharmacy.application.R
import com.lifepharmacy.application.databinding.ItemPcrListBinding
import com.lifepharmacy.application.managers.AppManager
import com.lifepharmacy.application.model.pcr.pcrlist.Products
import com.lifepharmacy.application.ui.documents.dialog.DocTypSelectionBottomSheet.Companion.TAG
import com.lifepharmacy.application.ui.pcr.adapters.ClickPcrProduct

class PcrAdapter(
  context: Activity?, private val onItemTapped: ClickPcrProduct,
  private val appManager: AppManager
) :
  RecyclerView.Adapter<PcrAdapter.ItemViewHolder>() {
// var rewardMain: GeneralResponseModel<RewardModel>? = null

  var arrayList: ArrayList<Products>? = ArrayList()
  var activity: Activity? = context
  var id: Int = 0

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
    val binding: ItemPcrListBinding = DataBindingUtil.inflate(
      LayoutInflater.from(activity),
      R.layout.item_pcr_list,
      parent, false
    )
    return ItemViewHolder(binding.root)
  }

  override fun getItemCount(): Int {
    //    return if (null != rewardMain) rewardMain!!.data?.data?.size!!
    return if (null != arrayList) arrayList!!.size else 0

  }

  class ItemViewHolder internal constructor(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    var binding: ItemPcrListBinding? = DataBindingUtil.bind(itemView)
  }

  fun setDataChanged(reward: ArrayList<Products>?) {
//    if (reward != null) {
//      rewardMain = reward
//    }
//    notifyDataSetChanged()
    arrayList?.clear()
    if (reward != null) {
      arrayList?.addAll(reward)
    }
    notifyDataSetChanged()
  }

  override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
    val item = arrayList!![position]
    Log.d(TAG, "onBindViewHolder() returned: $item")
//    val item = rewardMain?.data
    holder.binding?.item = item
    holder.binding?.tvAed?.text = "AED"
    holder.binding?.tvCur?.paintFlags =
      holder.binding?.tvCur?.paintFlags!!.or(Paint.STRIKE_THRU_TEXT_FLAG)
    holder.binding?.imgProduct?.setOnClickListener {
      onItemTapped.onPcrProductClicked(item, position)
    }

    holder.binding?.isInCart = appManager.offersManagers.checkIfPcrProductAlreadyExistInCart(item)
    holder.binding?.price?.countryCode
    holder.binding?.tvTags?.text =
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(item.short_description, Html.FROM_HTML_MODE_COMPACT)
      } else {
        Html.fromHtml(item.short_description)
      }


  }


}