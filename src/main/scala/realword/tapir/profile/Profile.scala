package realword.tapir.profile

import sttp.tapir.Schema

case class Profile(username: String, bio: String, image: String, following: Boolean)

object Profile:
  given Schema[Profile] = Schema.derived
