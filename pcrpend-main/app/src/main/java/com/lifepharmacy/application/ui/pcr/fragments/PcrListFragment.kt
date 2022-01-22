package com.lifepharmacy.application.ui.pcr.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.lifepharmacy.application.R
import com.lifepharmacy.application.base.BaseFragment
import com.lifepharmacy.application.databinding.FragmentPcrListBinding
import com.lifepharmacy.application.enums.AddressSelection
import com.lifepharmacy.application.managers.AlertManager
import com.lifepharmacy.application.network.Result
import com.lifepharmacy.application.ui.documents.viewmodel.DocumentsViewModel
import com.lifepharmacy.application.managers.analytics.PcrListScreenOpen
import com.lifepharmacy.application.model.docs.DocumentModel
import com.lifepharmacy.application.model.pcr.pcrlist.Products
import com.lifepharmacy.application.model.product.ProductDetails

import com.lifepharmacy.application.ui.address.AddressSelectionActivity
import com.lifepharmacy.application.ui.address.AddressViewModel
import com.lifepharmacy.application.ui.address.ClickSelectedAddress
import com.lifepharmacy.application.ui.documents.dialog.DocTypSelectionBottomSheet.Companion.TAG
import com.lifepharmacy.application.ui.pcr.adapters.ClickPcrProduct

import com.lifepharmacy.application.ui.pcr.viewmodel.PcrListingViewModel
import com.lifepharmacy.application.ui.prescription.fragments.ClickPrescriptionFiles

import com.lifepharmacy.application.ui.products.fragment.ProductFragment
import com.lifepharmacy.application.ui.profile.viewmodel.ProfileViewModel
import com.lifepharmacy.application.ui.rewards.adapters.PcrAdapter
import com.lifepharmacy.application.ui.utils.topbar.ClickTool
import com.lifepharmacy.application.utils.IntentHandler
import com.lifepharmacy.application.utils.IntentStarter
import com.lifepharmacy.application.utils.universal.RecyclerPagingListener
import com.lifepharmacy.application.utils.universal.RecyclerViewPagingUtil
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass.
 */
@AndroidEntryPoint
class PcrListFragment : BaseFragment<FragmentPcrListBinding>(),
  ClickPcrListFragment, ClickTool, ClickSelectedAddress, ClickPcrProduct,
  ClickPrescriptionFiles, RecyclerPagingListener {
  private val viewModel: PcrListingViewModel by activityViewModels()
  private val profileViewModel: ProfileViewModel by activityViewModels()
  private val docsViewModel: DocumentsViewModel by activityViewModels()
  private val viewModelAddress: AddressViewModel by activityViewModels()
  private lateinit var PcrAdapter: PcrAdapter
  var pcrtitle: String = ""
  private lateinit var recyclerViewPagingUtil: RecyclerViewPagingUtil
  private var layoutManager: GridLayoutManager? = null
  lateinit var intentHandler: IntentHandler
  lateinit var intentStarter: IntentStarter
  private val addressContract =
    registerForActivityResult(AddressSelectionActivity.Contract()) { result ->
      result?.address?.let {
        viewModelAddress.deliveredAddressMut.value = it
        viewModelAddress.addressSelection = AddressSelection.NON
      }
    }


  // Override this method to customize back press

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // Override this method to customize back press
    requireActivity().onBackPressedDispatcher.addCallback(this) {
      handleBackPress()
    }

  }
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    viewModel.appManager.analyticsManagers.PcrListScreenOpen()
    if (view == null) {
      mView = super.onCreateView(inflater, container, savedInstanceState)

      initUI()
      observers()
    }
    return mView
  }

  private fun initUI() {
    binding.viewModel = viewModel
    binding.profileViewModel = profileViewModel
    binding.lifecycleOwner = this
    binding.click = this
    binding.tollBar.click = this
    binding.tollBar.tvToolbarTitle.text = getString(R.string.covidrtpcr)
    binding.btnCheckOut.text = "next"

//    var imageUrl = appManager.storageManagers.config.prescriptionBanner ?: ""
    initProductsListRV()


  }

  private fun initProductsListRV() {
//    PcrAdapter = PcrAdapter(
//      requireActivity(),
//      this,
//      this,
//      viewModel.appManager,
//      viewModel.appManager.storageManagers.config.backOrder ?: "Pre-Order"
//    )
    PcrAdapter = PcrAdapter(requireActivity(), this,viewModel.appManager)
    layoutManager = GridLayoutManager(requireContext(), 1)

    binding.recyclerViewProducts.adapter = PcrAdapter
    recyclerViewPagingUtil = RecyclerViewPagingUtil(
      binding.recyclerViewProducts,
      layoutManager!!, this
    )

    binding.recyclerViewProducts.addOnScrollListener(recyclerViewPagingUtil)
    binding.recyclerViewProducts.post { // Call smooth scroll
      binding.recyclerViewProducts.scrollToPosition(0)
    }
//    resetSkip()
  }



  private fun observers() {
    val gson = Gson()
    viewModel.getPcrProducts().observe(viewLifecycleOwner, Observer {
      it?.let {
        when (it.status) {
          Result.Status.SUCCESS -> {
            hideLoading()
            it.data?.data?.products?.let { data ->
//              pcrtitle = it.data?.data?.
              binding.recyclerViewProducts.adapter = PcrAdapter
              PcrAdapter.setDataChanged(it.data?.data?.products)
//              if (data.isNotEmpty()) {
//                if (productListAdapter.arrayList?.contains(data.first()) == true) {
//                  Toast.makeText(requireContext(), "productExists", Toast.LENGTH_LONG).show()
//                }
//              }
            }
          }
          Result.Status.ERROR -> {
            hideLoading()
            it.message?.let { it1 ->
              AlertManager.showErrorMessage(
                requireActivity(),
                it1
              )
            }
          }
          Result.Status.LOADING -> {
//            recyclerViewPagingUtil.isLoading = true
          }
        }
      }
    })
  }

  override fun getLayoutRes(): Int {
    return R.layout.fragment_pcr_list
  }

  override fun permissionGranted(requestCode: Int) {

  }


  override fun onClickBack() {


  }
  private fun handleBackPress() {
    findNavController().navigateUp()
  }

  override fun onClickClaimInsurance() {
//    viewModel.isClaimInsurance.set(true)
  }

  override fun onClickUploadImage() {

  }

  override fun onClickUploadCardBackImage() {
  }

  override fun onClickUploadCardFrontImage() {

  }

  override fun onClickUploadInsurance() {

  }

  override fun onClickUploadInsuranceBack() {

  }

  override fun onNotClaimInsurance() {
//    viewModel.isNotClaimInsurance.set(true)
  }

  override fun onClickProceed() {
//    findNavController().navigate(R.id.pcrDetailsBottomSheet)
  }

  override fun onClickNext() {
    findNavController().navigate(R.id.pcrdateFragment)
  }

  override fun onClickLogin() {
    findNavController().navigate(R.id.nav_login_sheet)
  }

  override fun onClickMayBeLater() {

  }


  private fun proceedButtonStatus() {
//    viewModel.isProceedEnable.value =
//      !(viewModel.cardFrontUrl.get().isNullOrEmpty() || viewModel.cardBackUrl.get()
//        .isNullOrEmpty() || viewModel.filesListLive.value.isNullOrEmpty())
  }

  override fun onClickChangeAddress() {
    viewModelAddress.isSelecting.set(true)
    addressContract.launch(true)
  }

  private fun docSelected(doc: DocumentModel) {
    docsViewModel.selectedDoc.value = doc
  }

  override fun onProductClicked(productDetails: Products, position: Int) {
//    findNavController().navigate(
//      R.id.pcrDetailsBottomSheet
//
//    )
  }
//  override fun onProductClicked(productDetails: ProductDetails, position: Int) {
//    viewModel.position = position
//    viewModel.previewProductMut.value = productDetails
//    findNavController().navigate(R.id.productPreviewBottomSheet)
////    findNavController().navigate(
////      R.id.productFragment,
////      getBundle(
////        productID = productDetails.id
////      )
////    )
//  }

  override fun onPcrProductClicked(productDetails: Products, position: Int) {
    viewModel.position = position
    viewModel.previewProductMut.value = productDetails
//    findNavController().navigate(
//      R.id.pcrDetailsBottomSheet,
//      PcrDetailsBottomSheet.getBundle(productID = productDetails.id, position)
//    )
    findNavController().navigate(
      R.id.pcrDetailsBottomSheet)
  }

  //  ProductFragment.getBundle(productID = productDetails.id, position)
  override fun onClickAddProduct(productDetails: Products, position: Int) {

  }

  override fun onClickMinus(productDetails: Products, position: Int) {

  }

  override fun onClickPlus(productDetails: Products, position: Int) {

  }

  override fun onClickWishList(productDetails: Products, position: Int) {

  }

  override fun onClickNotify(productDetails: Products, position: Int) {

  }

  override fun onClickOrderOutOfStock(productDetails: Products, position: Int) {

  }

  override fun onNextPage(skip: Int, take: Int) {

  }


}



