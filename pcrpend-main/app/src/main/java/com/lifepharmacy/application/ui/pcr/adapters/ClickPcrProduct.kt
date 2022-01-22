package com.lifepharmacy.application.ui.pcr.adapters

import com.lifepharmacy.application.model.pcr.pcrlist.Products
import com.lifepharmacy.application.model.product.ProductDetails

/**
 * Created by Zahid Ali
 */
interface ClickPcrProduct {
  fun onProductClicked(productDetails: Products, position: Int)
  fun onPcrProductClicked(productDetails: Products, position: Int)
  fun onClickAddProduct(productDetails: Products, position: Int)
  fun onClickMinus(productDetails: Products, position: Int)
  fun onClickPlus(productDetails: Products, position: Int)
  fun onClickWishList(productDetails: Products, position: Int)
  fun onClickNotify(productDetails: Products, position: Int)
  fun onClickOrderOutOfStock(productDetails: Products, position: Int)
}