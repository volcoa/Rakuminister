package models

import java.text.DecimalFormat
import java.util.{Currency, Locale}

import play.api.libs.json.Json

case class NavigationResult(
                   title: String,
                   keyword: String,
                   products: Seq[Product],
                   resultProductsCount: Long,
                   totalResultProductsCount: Long,
                   hasNewProducts: Boolean,
                   hasUsedProducts: Boolean
                 ){
  var nbPages = 1
  if(totalResultProductsCount > 20){
    nbPages = math.ceil(totalResultProductsCount / 20).toInt
  }
}
object NavigationResult {
  implicit val productJsonFormat = Json.format[Product]
  implicit val navigationResultJsonFormat = Json.format[NavigationResult]
}

case class RequestInfos(title: String,
                        keyword: String)
case class Product(
                    id: Long,
                    urlName: String,
                    bestPrice: Double,
                    newBestPrice: Double,
                    usedBestPrice: Double,
                    collectibleBestPrice: Double,
                    advertsCount: Int,
                    advertsNewCount: Int,
                    advertsUsedCount: Int,
                    advertsCollectibleCount: Int,
                    headline: String,
                    caption: Option[String],
                    topic: String,
                    reviewsAverageNote: Double,
                    nbReviews: Long,
                    imagesUrls: Option[Seq[String]],
                    isMemo: Boolean
                  ){
  val formatter = new DecimalFormat("#.00");
  val formattedBestPrice = formatter.format(bestPrice)
  val formattedNewBestPrice = formatter.format(newBestPrice)
  val formattedUsedBestPrice = formatter.format(usedBestPrice)
  val formattedCollectibleBestPrice = formatter.format(collectibleBestPrice)
}
