package realword.tapir

import io.circe.generic.auto.*
import sttp.apispec.openapi.circe.yaml.*
import sttp.apispec.openapi.{Contact, Info, License, OpenAPI}
import sttp.tapir.*
import sttp.tapir.docs.apispec.DocsExtensionAttribute.*
import sttp.tapir.docs.openapi.{OpenAPIDocsInterpreter, OpenAPIDocsOptions}
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.*

import java.nio.file.{Files, Paths}

object GenerateOpenApiDocs:

  case class LoginUser(email: String, password: String)
  case class LoginUserRequest(user: LoginUser)
  case class User(bio: String, email: String, image: String, token: String, username: String)
  case class UserResponse(user: User)

  @main def generate(): Unit =
    val i: EndpointIO.Body[String, LoginUserRequest] = jsonBody[LoginUserRequest]
    val usersLogin =
      endpoint.post
        .in("users" / "login")
        .summary("Existing user login")
        .description("Login for existing user")
        .tag("User and Authentication")
        .in(i.description("Credentials to use").docsExtension("required", true))
        .out(jsonBody[UserResponse])
        .docsExtension("x-codegen-request-body-name", "body")

    val docs: OpenAPI = OpenAPIDocsInterpreter(OpenAPIDocsOptions({
      case (_, Vector("users", "login"), _) => "Login"
      case _                                => "?"
    }))
      .toOpenAPI(
        List(usersLogin),
        Info(
          "RealWorld Conduit API",
          "1.0.0",
          description = Some("Conduit API documentation"),
          contact = Some(Contact(Some("RealWorld"), url = Some("https://realworld.how"))),
          license = Some(License("MIT License", Some("https://opensource.org/licenses/MIT")))
        )
      )
      .addServer("https://api.realworld.io/api")

    Files.write(Paths.get("src/main/resources/realworld.yml"), docs.toYaml.getBytes)
