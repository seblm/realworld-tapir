package realword.tapir.profile

import io.circe.generic.auto.*
import sttp.model.Method
import sttp.model.Method.{DELETE, POST}
import sttp.tapir.*
import sttp.tapir.docs.apispec.DocsExtensionAttribute.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.*

object ProfileEndpoints:
  private val tag = "Profile"
  private val profileResponse = jsonBody[ProfileResponse].description("Profile").docsExtension("required", true)
  private val getProfileByUsername = endpoint
    .in("profiles" / path[String]("username").description("Username of the profile to get"))
    .get
    .tag(tag)
    .summary("Get a profile")
    .description("Get a profile of a user of the system. Auth is optional")
    .out(profileResponse)
  private val followUserByUsername = endpoint
    .in("profiles" / path[String]("username").description("Username of the profile you want to follow") / "follow")
    .post
    .tag(tag)
    .summary("Follow a user")
    .description("Follow a user by username")
    .out(profileResponse)
  private val unfollowUserByUsername = endpoint
    .in("profiles" / path[String]("username").description("Username of the profile you want to unfollow") / "follow")
    .delete
    .tag(tag)
    .summary("Unfollow a user")
    .description("Unfollow a user by username")
    .out(profileResponse)

  val endpoints: Iterable[AnyEndpoint] = List(getProfileByUsername, followUserByUsername, unfollowUserByUsername)

  def operationIdGenerator: PartialFunction[(AnyEndpoint, Vector[String], Method), String] = {
    case (_, Vector("profiles", "username"), _)                => "GetProfileByUsername"
    case (_, Vector("profiles", "username", "follow"), POST)   => "FollowUserByUsername"
    case (_, Vector("profiles", "username", "follow"), DELETE) => "UnfollowUserByUsername"
  }
