package models

import play.api.libs.json.Json

case class PMWsResult(
                   title: String,
                   keyword: String,
                   products: Seq[Product]
                 ) {
  implicit val productReads = Json.reads[Product]
}

case class Product(
                    id: Double,
//                    isDigital: Boolean,
//                    urlName: String,
//                    bestPrice: Double,
//                    newBestPrice: Double,
//                    usedBestPrice: Double,
//                    collectibleBestPrice: Double,
//                    advertsCount: Double,
//                    advertsNewCount: Double,
//                    advertsUsedCount: Double,
//                    advertsCollectibleCount: Double,
//                    headline: String,
                    caption: String,
//                    topic: String,
//                    reviewsAverageNote: Double,
//                    nbReviews: Double,
                    imagesUrls: List[String]
////                    pickupAllowed: Boolean,
////                    isPreOrder: Boolean,
////                    releaseDate: String,
////                    attributes: String,
////                    isMemo: Boolean,
////                    isMevFormAvailable: Boolean,
////                    isNotModifiedSinceLastCrawl: Boolean,
//                    images: List[Images],
//                    rspMinimumAmount: Double
                  )

//case class Entry(
//                  size: String,
//                  url: String
//                )
//case class ImagesUrls(
//                       entry: List[Entry]
//                     )
//case class Images(
//                   imagesUrls: ImagesUrls,
//                   id: Double
//                 )
