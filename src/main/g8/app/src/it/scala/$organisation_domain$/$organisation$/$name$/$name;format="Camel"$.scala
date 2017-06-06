package $organisation_domain$.$organisation$.$name$

import com.google.inject.Inject
import org.scalatest.Tag
import scala.concurrent.ExecutionContext


object Docker extends Tag("Docker")

class $name;format="Camel"$IntegrationTest @Inject() extends RestApiIntegrationTest {
  "When application is running" - {
    "Health-check" - {
      "should always return status okay" taggedAs (Docker) in {
        wsClient
          .url(s"\$appUrl/health")
          .get()
          .map(res => {
            res.status shouldEqual 200
            (res.json \ "status").as[String] shouldEqual "Ok"
          })
      }
    }
    "Build info" - {
      "should return a JSON object with current version" taggedAs (Docker) in {
        wsClient
          .url(s"\$appUrl/version")
          .get()
          .map(res => {
            res.status shouldEqual 200
            (res.json \ "version").as[String] should not be empty
          })
      }
    }
    "OpenAPI specs" - {
      "should return the yaml specs" taggedAs (Docker) in {
        wsClient
          .url(s"\$appUrl/specs.yml")
          .get
          .map(res => {
            res.status shouldEqual 200
          })
      }
      "should redirect to the API docs" taggedAs (Docker) in {
        wsClient
          .url(s"\$appUrl/specs")
          .withFollowRedirects(false)
          .get()
          .map(res => {
            res.status shouldEqual 303
            res.header("Location").get shouldEqual "/specs/index.html?url=/specs.yml"
          })
      }
      "should show the API docs" taggedAs (Docker) in {
        wsClient
          .url(s"\$appUrl/specs?url=/specs.yml")
          .get()
          .map(res => {
            res.status shouldEqual 200
          })
      }
    }
  }
}
