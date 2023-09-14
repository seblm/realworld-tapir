package realword.tapir.articles

import realword.tapir.profile.Profile
import sttp.tapir.Schema

import java.time.Instant

case class Article(
    slug: String,
    title: String,
    description: String,
    body: String,
    tagList: Vector[String],
    createdAt: Instant,
    updatedAt: Instant,
    favorited: Boolean,
    favoritesCount: Int,
    author: Profile
)

object Article:
  given Schema[Article] = Schema.derived
