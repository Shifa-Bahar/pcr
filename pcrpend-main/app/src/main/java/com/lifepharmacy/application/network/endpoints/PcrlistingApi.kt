package com.lifepharmacy.application.network.endpoints

import com.lifepharmacy.application.model.filters.FilterMainRequest
import com.lifepharmacy.application.model.general.GeneralResponseModel
import com.lifepharmacy.application.model.pcr.pcrlist.PcrData
import com.lifepharmacy.application.model.product.ProductListingMainModel
import retrofit2.Response
import retrofit2.http.*
import com.lifepharmacy.application.utils.URLs


interface PcrlistingApi {
  //    @FormUrlEncoded
  @GET(URLs.PCR_LIST)
  suspend fun requestPcrProducts(): Response<GeneralResponseModel<PcrData>>


}
