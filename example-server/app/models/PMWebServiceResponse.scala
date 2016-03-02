package models

import play.api.libs.json.Json

case class PMWsResult(
                   title: String,
                   keyword: String,
                   products: Seq[Product]
                 )

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
                    imagesUrls: Seq[String],
                    isMemo: Boolean
                  )
object Product {
  implicit val entryJsonFormat = Json.format[Entry]
  implicit val productJsonFormat = Json.format[Product]
}

case class Image(imagesUrls: Seq[Entry],
                 id: Double
                )
