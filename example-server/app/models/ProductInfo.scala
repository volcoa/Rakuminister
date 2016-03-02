package models

import play.api.libs.json.Json

case class ProductInfo(
                        aisle: String,
                        id: Double,
                        urlName: String,
                        adverts: List[Advert],
                        bestPrice: Double,
                        newBestPrice: Double,
                        usedBestPrice: Double,
                        collectibleBestPrice: Double,
                        collapseBestPrice: String,
                        advertsCount: Double,
                        advertsNewCount: Double,
                        advertsUsedCount: Double,
                        advertsCollectibleCount: Double,
                        headline: String,
                        reviewsAverageNote: Double,
                        nbReviews: Double,
                        reviews: List[Review],
                        productDetailTitle: String,
                        description: String,
                        images: List[Images],
                        rspMinimumAmount: Double
                      )

object ProductInfo {


  implicit val entryReads = Json.format[Entry]
  implicit val imagesUrlsReads = Json.format[ImagesUrls]
  implicit val imagesReads = Json.format[Images]

  implicit val sellerReads = Json.format[Seller]
  implicit val advertReads = Json.format[Advert]

  implicit val authorReads = Json.format[Author]
  implicit val reviewReads = Json.format[Review]

  implicit val productInfoReads = Json.format[ProductInfo]
}

case class Advert(
                   advertId: Long,
                   productId: Long,
                   salePrice: Double,
                   shippingAmount: Double,
                   seller: Seller,
                   sellerComment: Option[String],
                   //                     shippingTypes: List[String],
                   //                     availableShippingTypes: List[AvailableShippingTypes],
                   isPickupAllowed: Boolean,
                   isAdvertInCircleRange: Boolean,
//                   quality: String,
                   //                     type: String,
                   images: List[Images],
                   rspMinimumAmount: Double
                 )

  case class Seller(
                     id: Double,
                     login: String,
                     //                    type: String,
                     isUserBuyer: Boolean,
                     creationDate: Double,
                     saleCommitCount: Option[Double],
                     totalSaleCount: Option[Double],
                     saleCount: Option[Double],
                     averageScore: Option[Double],
                     isoCountryId: Double,
                     sellerAnswerTime: Option[Double],
                     isMicroCompagny: Boolean
                   )

//  case class AvailableShippingTypes(
//                                     id: Double,
//                                     label: String,
//                                     isAllowedByPlatform: Boolean,
//                                     isAllowedForSeller: Boolean,
//                                     canBeModified: Boolean,
//                                     isMandatory: Boolean,
//                                     isPreselectedByDefault: Boolean,
//                                     isMandatoryForFreeShipping: Boolean,
//                                     isUnsupportedWithFreeShipping: Boolean
//                                   )
//
case class Review(
                    note: Double,
                    title: String,
                    author: Author,
                    date: Double,
                    description: String
                  )

case class Author(
                   login: String
                 )

case class Images(
                   imagesUrls: ImagesUrls,
                   id: Double
                 )

case class ImagesUrls(
                       entry: List[Entry]
                     )

case class Entry(
                  size: String,
                  url: String
                )
