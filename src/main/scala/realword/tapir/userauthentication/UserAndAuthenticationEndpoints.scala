package realword.tapir.userauthentication

import io.circe.generic.auto.*
import sttp.model.Method
import sttp.model.Method.{GET, PUT}
import sttp.tapir.*
import sttp.tapir.docs.apispec.DocsExtensionAttribute.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.*

object UserAndAuthenticationEndpoints:
  private val tag = "User and Authentication"
  private val loginUserRequest =
    jsonBody[LoginUserRequest].description("Credentials to use").docsExtension("required", true)
  private val userResponse = jsonBody[UserResponse].description("User")
  private val login =
    endpoint.post
      .in("users" / "login")
      .summary("Existing user login")
      .description("Login for existing user")
      .tag(tag)
      .in(loginUserRequest)
      .out(userResponse)
      .docsExtension("x-codegen-request-body-name", "body")
  private val newUserRequest =
    jsonBody[NewUserRequest].description("Details of the new user to register").docsExtension("required", true)
  private val createUser =
    endpoint.post
      .in("users")
      .description("Register a new user")
      .tag(tag)
      .in(newUserRequest)
      .out(userResponse)
      .docsExtension("x-codegen-request-body-name", "body")
  private val getCurrentUser = endpoint.get
    .in("user")
    .summary("Get current user")
    .description("Gets the currently logged-in user")
    .tag(tag)
    .out(userResponse)
  private val updateUserRequest = jsonBody[UpdateUserRequest]
    .description("User details to update. At least **one** field is required.")
    .docsExtension("required", true)
  private val updateCurrentUser = endpoint.put
    .in("user")
    .tag(tag)
    .summary("Update current user")
    .description("Updated user information for current user")
    .in(updateUserRequest)
    .out(userResponse)
    .docsExtension("x-codegen-request-body-name", "body")

  val endpoints: Iterable[AnyEndpoint] = List(login, createUser, getCurrentUser, updateCurrentUser)

  def operationIdGenerator: PartialFunction[(AnyEndpoint, Vector[String], Method), String] = {
    case (_, Vector("users", "login"), _) => "Login"
    case (_, Vector("users"), _)          => "CreateUser"
    case (_, Vector("user"), GET)         => "GetCurrentUser"
    case (_, Vector("user"), PUT)         => "UpdateCurrentUser"
  }
