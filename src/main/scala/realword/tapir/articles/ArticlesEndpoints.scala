package realword.tapir.articles

import io.circe.generic.auto.*
import sttp.model.Method
import sttp.model.Method.{DELETE, GET, POST, PUT}
import sttp.tapir.*
import sttp.tapir.docs.apispec.DocsExtensionAttribute.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.*

object ArticlesEndpoints:
  private val tag = "Articles"
  private val offsetAndLimit: EndpointInput[(Option[Int], Option[Int])] = query[Option[Int]]("offset")
    .validateOption(Validator.min(0))
    .description("The number of items to skip before starting to collect the result set.")
    .and(
      query[Option[Int]]("limit")
        .description("The numbers of items to return.")
        .validateOption(Validator.min(1))
    )
  private val multipleArticlesResponse = jsonBody[MultipleArticlesResponse]
    .description("Multiple articles")
    .schema(_.modify(_.articles)(_.copy(isOptional = false)))
  private val getArticlesFeed = endpoint
    .in("articles" / "feed")
    .get
    .tag(tag)
    .summary("Get recent articles from users you follow")
    .description("Get most recent articles from users you follow. Use query parameters to limit. Auth is required")
    .in(offsetAndLimit)
    .out(
      multipleArticlesResponse
    )
  private val articlesEndpoint = endpoint.in("articles")
  private val getArticles = articlesEndpoint.get
    .tag(tag)
    .summary("Get recent articles globally")
    .description("Get most recent articles globally. Use query parameters to filter results. Auth is optional")
    .in(
      query[Option[String]]("tag")
        .description("Filter by tag")
        .and(query[Option[String]]("author").description("Filter by author (username)"))
        .and(query[Option[String]]("favorited").description("Filter by favorites of a user (username)"))
        .and(offsetAndLimit)
    )
    .out(multipleArticlesResponse)
  private val singleArticleResponse = jsonBody[SingleArticleResponse].description("Single article")
  private val createArticle = articlesEndpoint.post
    .tag(tag)
    .summary("Create an article")
    .description("Create an article. Auth is required")
    .in(
      jsonBody[NewArticleRequest]
        .description("Article to create")
        .docsExtension("x-codegen-request-body-name", "body")
    )
    .out(singleArticleResponse)
  private val getArticle = endpoint
    .in("articles" / path[String]("slug").description("Slug of the article to get"))
    .get
    .tag(tag)
    .summary("Get an article")
    .description("Get an article. Auth not required")
    .out(singleArticleResponse)
  private val updateArticle = endpoint
    .in("articles" / path[String]("slug").description("Slug of the article to update"))
    .put
    .tag(tag)
    .summary("Update an article")
    .description("Update an article. Auth is required")
    .in(jsonBody[UpdateArticleRequest].description("Article to update"))
    .out(singleArticleResponse)
    .docsExtension("x-codegen-request-body-name", "body")
  private val deleteArticle = endpoint
    .in("articles" / path[String]("slug").description("Slug of the article to delete"))
    .delete
    .tag(tag)
    .summary("Delete an article")
    .description("Delete an article. Auth is required")
    .out(jsonBody[EmptyOkResponse].description("No content"))

  val endpoints: Iterable[AnyEndpoint] =
    Vector(getArticlesFeed, getArticles, createArticle, getArticle, updateArticle, deleteArticle)

  def operationIdGenerator: PartialFunction[(AnyEndpoint, Vector[String], Method), String] =
    case (_, Vector("articles", "feed"), _)      => "GetArticlesFeed"
    case (_, Vector("articles"), GET)            => "GetArticles"
    case (_, Vector("articles"), POST)           => "CreateArticle"
    case (_, Vector("articles", "slug"), GET)    => "GetArticle"
    case (_, Vector("articles", "slug"), PUT)    => "UpdateArticle"
    case (_, Vector("articles", "slug"), DELETE) => "DeleteArticle"
