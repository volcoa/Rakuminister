package models

import play.api.libs.json.{JsPath, Reads, Json}

case class PMWsResult(
                   title: String,
                   keyword: String,
                   products: Seq[Product]
                 ) {
  implicit val productReads = Json.reads[Product]
}

case class RequestInfos(title: String,
                        keyword: String)
case class Product(
                    id: Long,
                    //isDigital: Boolean,
                    urlName: String,
                    //bestPrice: Double,
                    //newBestPrice: Double,
                    //usedBestPrice: Double,
                    //collectibleBestPrice: Double,
                    //advertsCount: Double,
                    //advertsNewCount: Double,
                    //advertsUsedCount: Double,
                    //advertsCollectibleCount: Double,
                    headline: String,
                    //caption: String,
                    //topic: String,
                    //reviewsAverageNote: Double,
                    //nbReviews: Double,
                    imagesUrls: Seq[String]
                    //pickupAllowed: Boolean,
                    //isPreOrder: Boolean,
                    //releaseDate: String,
                    //attributes: String,
                    //isMemo: Boolean,
                    //isMevFormAvailable: Boolean,
                    //isNotModifiedSinceLastCrawl: Boolean,
                    //images: Seq[Image],
                    //rspMinimumAmount: Double
                  )

case class Image(imagesUrls: Seq[Entry],
                 id: Double
                )

case class Entry(size: String,
                 url: String)

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
