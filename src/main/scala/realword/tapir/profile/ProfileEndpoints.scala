package realword.tapir.profile

import io.circe.generic.auto.*
import sttp.model.Method
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

  val endpoints: Iterable[AnyEndpoint] = List(getProfileByUsername)

  def operationIdGenerator: PartialFunction[(AnyEndpoint, Vector[String], Method), String] = {
    case (_, Vector("profiles", "username"), _) => "GetProfileByUsername"
  }
