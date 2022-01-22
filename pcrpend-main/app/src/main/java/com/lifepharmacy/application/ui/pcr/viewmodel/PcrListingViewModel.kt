package com.lifepharmacy.application.ui.pcr.viewmodel

import android.app.Application
import android.net.Uri
import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.lifepharmacy.application.base.BaseViewModel
import com.lifepharmacy.application.enums.ProductDetailSelectedState
import com.lifepharmacy.application.enums.ScrollingState
import com.lifepharmacy.application.managers.AppManager
import com.lifepharmacy.application.network.performNwOperation
import com.lifepharmacy.application.ui.utils.AppScrollState
import com.lifepharmacy.application.managers.FiltersManager
import com.lifepharmacy.application.managers.OffersManagers
import com.lifepharmacy.application.model.category.Section
import com.lifepharmacy.application.model.filters.FilterModel
import com.lifepharmacy.application.model.pcr.pcrlist.Price
import com.lifepharmacy.application.model.pcr.pcrlist.Prices
import com.lifepharmacy.application.model.pcr.pcrlist.Products
import com.lifepharmacy.application.model.product.ProductDetails
import com.lifepharmacy.application.repository.PcrListingRepository
import org.imaginativeworld.whynotimagecarousel.CarouselItem

/**
 * Created by Zahid Ali
 */
class PcrListingViewModel
@ViewModelInject
constructor(
  val appManager: AppManager,
  private val repository: PcrListingRepository,
//  val cartUtil: CartManager,
  public val filtersManager: FiltersManager,
  val offersManagers: OffersManagers,
  application: Application, private val scrollState: AppScrollState
) : BaseViewModel(application) {

  var titleString: String? = ""
  var id: String? = null
  var type: String? = null
  var listing_type: String? = null
  var slug: String? = null
  var imageUrl: String? = ""
  var isSearch: Boolean = false
  var selectedDetails = MutableLiveData<ProductDetailSelectedState>()
  var previewProductMut = MutableLiveData<Products>()
  var position: Int = 0


  var previewPrice = MutableLiveData<Prices>()
  var previewQTY = MutableLiveData<String>()
  var selectedFilteredItems = ArrayList<FilterModel>()
  var isGirdSelected = ObservableField<Boolean>()
  var isSubOption = MutableLiveData<Boolean>()
  var isFilterApplied = MutableLiveData<Boolean>()
  var isRangeSelected = MutableLiveData<Boolean>()
  var isMainOptions = MutableLiveData<Boolean>()
  var title = MutableLiveData<String>()
  var image = MutableLiveData<String>()
  var rangeFrom = MutableLiveData<String>()
  var rangeTo = MutableLiveData<String>()
  var isSubClicked = false

  var isList = ObservableField<Boolean>()
  var isThereFilter = ObservableField<Boolean>()
  var parentSelectedQuickSection = Section()
  var skip = 0
  var take = 30
  fun getScrollStateRepo(): AppScrollState {
    return scrollState
  }
  fun getPcrProductSliderList(productDetail: Products): List<CarouselItem> {
    val list = arrayListOf<CarouselItem>()
    list.add(
      CarouselItem(
        imageUrl = productDetail.images.featured_image

      )
    )
    for (item in productDetail.images.gallery_images) {
      list.add(
        CarouselItem(
          imageUrl = item

        )
      )
    }
    return list
  }


  fun getScrollStateMut(): MutableLiveData<ScrollingState> {
    return scrollState.getScrollingState()
  }
//    fun getDiscounts() =
//        performNwOperation { superSellerRepository.getDiscounts() }

  fun getPcrProducts() =
    performNwOperation {
      repository.getPcrProducts()
    }

//    fun getFilters() =
//        performNwOperation { superSellerRepository.getFilters() }

//    fun getQuickOptions() =
//        performNwOperation { superSellerRepository.getQuickOptions() }

  fun setFilterSelectedItems(item: FilterModel) {
    if (selectedFilteredItems.contains(item)) {
      selectedFilteredItems.remove(item)
    } else {
      selectedFilteredItems.add(item)
    }
  }


  fun saveSelectedRange(from: String, to: String) {
    filtersManager.fromPrice = from
    filtersManager.toPrice = to
  }

  fun addParentFilter() {
    if (id != null && type != null) {
      appManager.filtersManager.addFirstFilter(id!!, type!!)
    }
    if (listing_type != null && slug != null) {
      appManager.filtersManager.addFirstFilter(slug!!, listing_type!!)
    }
  }

  fun setValuesFromURL(url: String) {
    try {
      val uri: Uri = Uri.parse(url)
      val args = uri.queryParameterNames
      if (!args.isNullOrEmpty()) {
        listing_type = args.first() + "Slug"
        slug = uri.getQueryParameter(args.first())
      }
    } catch (e: Exception) {
      e.printStackTrace()
    }
  }
}