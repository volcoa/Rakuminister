package models

import java.text.{DecimalFormat, DecimalFormatSymbols}
import java.util.Locale

import play.api.libs.json.Json

case class ProductInfo(
                        aisle: String,
                        id: Double,
                        urlName: String,
                        adverts: List[Advert],
                        bestOffers: Option[Map[String, BestOffers]],
                        bestPrice: Double,
                        newBestPrice: Double,
                        usedBestPrice: Double,
                        collectibleBestPrice: Double,
                        collapseBestPrice: String,
                        advertsCount: Double,
                        advertsNewCount: Double,
                        advertsUsedCount: Double,
                        advertsCollectibleCount: Double,
                        headline: Option[String],
                        reviewsAverageNote: Double,
                        nbReviews: Double,
                        reviews: List[Review],
                        productDetailTitle: String,
                        description: Option[String],
                        images: List[Images],
                        rspMinimumAmount: Double
                      ){
  val dfs = new DecimalFormatSymbols(Locale.FRANCE);
  dfs.setGroupingSeparator(' ');
  val formatter = new DecimalFormat("#,##0.00", dfs);
  val formattedBestPrice = formatter.format(bestPrice)
  val formattedNewBestPrice = formatter.format(newBestPrice)
  val formattedUsedBestPrice = formatter.format(usedBestPrice)
  val formattedCollectibleBestPrice = formatter.format(collectibleBestPrice)

  var buybox: Advert = null;
  if(bestOffers.isDefined) {
    for (bestOffer <- bestOffers.get.values) {
      if (bestOffer.isBuybox) {
        buybox = bestOffer.adverts(0)
      }
    }
  }
}

object ProductInfo {

  implicit val entryReads = Json.format[Entry]
  implicit val imagesUrlsReads = Json.format[ImagesUrls]
  implicit val imagesReads = Json.format[Images]

  implicit val sellerReads = Json.format[Seller]
  implicit val advertReads = Json.format[Advert]
  implicit val bestOffersReads = Json.format[BestOffers]

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
                   quality: String,
                   //                     type: String,
                   images: List[Images],
                   rspMinimumAmount: Option[Double]
                 ){
  val dfs = new DecimalFormatSymbols(Locale.FRANCE);
  dfs.setGroupingSeparator(' ');
  val formatter = new DecimalFormat("#,##0.00", dfs);
  val formattedSalePrice = formatter.format(salePrice)
  val formattedShippingAmount = formatter.format(shippingAmount)

  var state: String = AdvertQuality.map.getOrElse(quality, "")

}

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
                 ) {
  var fullSizeUrl: String = null
  var smallSizeUrl: String = null
  for(entry <- imagesUrls.entry) {
    if (entry.size.equals("ORIGINAL")){
      fullSizeUrl = entry.url
    }
    if(entry.size.equals("SMALL")){
      smallSizeUrl = entry.url
    }
  }
}

case class ImagesUrls(
                       entry: List[Entry]
                     )

case class Entry(
                  //"SMALL" "MEDIUM" "LARGE" "ORIGINAL"
                  size: String,
                  url: String
                )


case class BestOffers(
                    isBuybox: Boolean,
                    score: Double,
                    adverts: List[Advert]
)

object AdvertQuality {
  var map: Map[String, String] = Map(
  "NEW" -> "Neuf",
  "LIKE_NEW" -> "Comme Neuf",
  "VERY_GOOD" -> "Très bon état",
  "GOOD" -> "Bon état",
  "ACCEPTABLE" -> "Etat Correct",
  "OUT_OF_ORDER" -> "Hors Service")
}