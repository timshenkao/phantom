package com.newzly.phantom.dsl

import org.scalatest.concurrent.PatienceConfiguration
import org.scalatest.time.SpanSugar._
import com.newzly.phantom.Implicits._
import com.newzly.phantom.helper.BaseTest
import com.newzly.phantom.tables.{ Article, Articles }
import com.newzly.util.finagle.AsyncAssertionsHelper._

class SkipRecordsByToken extends BaseTest {
  val keySpace: String = "SkippingRecordsByTokenTest"
  implicit val s: PatienceConfiguration.Timeout = timeout(10 seconds)

  it should "allow skipping records " in {
    Articles.insertSchema()
    val article1 = Article.sample
    val article2 = Article.sample
    val article3 = Article.sample
    val article4 = Article.sample

    val result = for {
      i1 <- Articles.insert
        .value(_.name, article1.name).value(_.id, article1.id)
        .value(_.order_id, article1.order_id)
        .future()
      i2 <- Articles.insert
        .value(_.name, article2.name)
        .value(_.id, article2.id)
        .value(_.order_id, article2.order_id)
        .future()
      i3 <- Articles.insert
        .value(_.name, article3.name)
        .value(_.id, article3.id)
        .value(_.order_id, article3.order_id)
        .future()

      i4 <- Articles.insert
        .value(_.name, article4.name)
        .value(_.id, article4.id)
        .value(_.order_id, article4.order_id)
        .future()
      one <- Articles.select.one
      next <- Articles.select.where(_.id gtToken one.get.id ).fetch
    } yield next


    result successful {
      r => {
        assert(r.size === 3)
        info (s"got exactly ${r.size} records")
      }
    }
  }

}
