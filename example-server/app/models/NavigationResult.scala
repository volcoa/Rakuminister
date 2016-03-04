package models

import java.text.{DecimalFormatSymbols, DecimalFormat}
import java.util.{Currency, Locale}

import play.api.libs.json.Json

case class NavigationResult(
                   title: String,
                   keyword: Option[String],
                   products: Seq[Product],
                   categories: Seq[Category],
                   filters: Seq[Filter],
                   resultProductsCount: Long,
                   totalResultProductsCount: Long,
                   hasNewProducts: Boolean,
                   hasUsedProducts: Boolean
                 ){
  var nbPages = 1
  if(totalResultProductsCount > 20){
    nbPages = math.ceil(totalResultProductsCount / 20).toInt
  }
  var isNav = false
  var isSearch = false
  var isLocalSearch = false
  var formattedCategory = ""
}
object NavigationResult {
  implicit val productJsonFormat = Json.format[Product]
  implicit val categoryJsonFormat = Json.format[Category]
  implicit val filterValueJsonFormat = Json.format[FilterValue]
  implicit val filterJsonFormat = Json.format[Filter]
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
                    topic: Option[String],
                    reviewsAverageNote: Double,
                    nbReviews: Long,
                    imagesUrls: Option[Seq[String]],
                    isMemo: Boolean
                  ){
  val dfs = new DecimalFormatSymbols(Locale.FRANCE);
  dfs.setGroupingSeparator(' ');
  val formatter = new DecimalFormat("#,##0.00", dfs);
  val formattedBestPrice = formatter.format(bestPrice)
  val formattedNewBestPrice = formatter.format(newBestPrice)
  val formattedUsedBestPrice = formatter.format(usedBestPrice)
  val formattedCollectibleBestPrice = formatter.format(collectibleBestPrice)
}

case class Category(
                     label: String,
                     name: String,
                     productsCount: Long
                   )

case class Filter(
                     name: String,
                     key: String,
                     filterValues: Seq[FilterValue]
                   )

case class FilterValue(
                   label: String,
                   name: String,
                   value: String,
                   productsCount: Long
                 )
