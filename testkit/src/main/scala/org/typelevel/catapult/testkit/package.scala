/*
 * Copyright 2022 Typelevel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.typelevel.catapult

import cats.effect.{Async, Resource}
import cats.syntax.all._
import facade.launchdarklyNodeServerSdk.integrationsMod.TestData
import facade.launchdarklyNodeServerSdk.mod.LDOptions

import scala.scalajs.js
import scala.scalajs.js.JSON

//import com.launchdarkly.sdk.server.LDConfig
//import com.launchdarkly.sdk.server.integrations.TestData

package object testkit {
  def testClient[F[_]](implicit F: Async[F]): Resource[F, (TestData, LaunchDarklyClient[F])] =
    Resource.eval(F.delay(TestData())).flatMap { td =>
      val opts = LDOptions().setUpdateProcessor(td) // .setOffline(true)
//      println(JSON.stringify(td))
//      println(JSON.stringify(opts.updateProcessor))

      LaunchDarklyClient
        .resource("fake-key", opts)
        .tupleLeft(td)
    }
}
