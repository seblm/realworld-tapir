package realword.tapir

import realword.tapir.userauthentication.UserAndAuthentication
import sttp.apispec.openapi.circe.yaml.*
import sttp.apispec.openapi.{Contact, Info, License, OpenAPI}
import sttp.tapir.*
import sttp.tapir.docs.openapi.{OpenAPIDocsInterpreter, OpenAPIDocsOptions}

import java.nio.file.{Files, Paths}

object GenerateOpenApiDocs:

  @main def generate(): Unit =
    val options = OpenAPIDocsOptions(UserAndAuthentication.operationIdGenerator.lift(_, _, _).getOrElse("?"))
    val docs: OpenAPI = OpenAPIDocsInterpreter(options)
      .toOpenAPI(
        UserAndAuthentication.endpoints,
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
