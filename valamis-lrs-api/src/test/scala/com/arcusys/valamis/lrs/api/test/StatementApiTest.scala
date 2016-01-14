package com.arcusys.valamis.lrs.api.test

import java.util.UUID

import com.arcusys.valamis.lrs.api.StatementApi
import org.scalatest.{Ignore, FeatureSpec}

import scala.util.{Failure, Success}

/**
 * Created by Iliya Tryapitsin on 15.06.15.
 */
//@Ignore
class StatementApiTest extends FeatureSpec with BaseFeatureTests {

  feature("get statements") {
    val statementApi = new StatementApi()
    val statements = statementApi.put(
      "3a1b8092-3d9d-43db-8817-a3cc4ec2c094",
      "{\"id\":\"fc16d5a1-2da8-4080-b5dc-10a0c8740893\",\"timestamp\":\"2015-12-17T07:34:07.454Z\",\"actor\":{\"objectType\":\"Agent\",\"account\":{\"name\":\"7698db19-d556-4ad9-a3f1-507ff29e2eae\",\"homePage\":\"http://local.example.com\"},\"name\":\"Test Test\"},\"verb\":{\"id\":\"http://adlnet.gov/expapi/verbs/experienced\",\"display\":{\"und\":\"experienced\"}},\"context\":{\"contextActivities\":{\"parent\":[{\"id\":\"http://tincanapi.com/GolfExample_TCAPI\",\"objectType\":\"Activity\"}],\"grouping\":[{\"id\":\"http://tincanapi.com/GolfExample_TCAPI\",\"objectType\":\"Activity\"}]}},\"object\":{\"id\":\"http://tincanapi.com/GolfExample_TCAPI/Playing/Playing.html\",\"objectType\":\"Activity\",\"definition\":{\"name\":{\"en-US\":\"Playing Golf\"},\"description\":{\"en-US\":\"An overview of the game of golf.\"}}}}"
    ) match {
      case Success(result) => result
      case Failure(ex)     =>
        fail(ex)
    }

//    assert(statements.statements.size == 10)
  }
}
