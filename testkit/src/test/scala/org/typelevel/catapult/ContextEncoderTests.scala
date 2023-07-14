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

import cats.syntax.all._
import cats.Eq
import foobar.launchdarklyNodeServerSdk.mod.{LDContext, LDContextCommon, LDSingleKindContext}
import weaver.SimpleIOSuite

object ContextEncoderTests extends SimpleIOSuite {

//  implicit def eqUndefor[A: Eq]: Eq[js.UndefOr[A]] = new Eq[js.UndefOr[A]] {
//    override def eqv(x: UndefOr[A], y: UndefOr[A]): Boolean =
//      if (x.isEmpty && y.isEmpty) true
//      else if (x.isDefined && y.isDefined) x.get == y.get
//      else false
//  }
//
//  implicit val eqContext: cats.Eq[LDContext] = new Eq[LDContext] {
//    override def eqv(x: LDContext, y: LDContext): Boolean =
//      (x, y) match {
//        case (x: LDSingleKindContext, y: LDSingleKindContext) =>
//          x.kind === y.kind &&
//          x.name === y.name // &&
//        // x._meta === y._meta
//      }
//  }

  pureTest("contramap allows changing the input type of ContextEncoder") {
    // This is a trivial example - it would be just as simple to write a new ContextEncoder from scatch.
    case class Country(name: String)

    val enc = ContextEncoder[LDContext].contramap[Country](country =>
      LDContext.LDSingleKindContext(country.name, "country")
    )

    val franceCtx: LDSingleKindContext = LDContext.LDSingleKindContext("France", "country")

    val result = enc.encode(Country("France")).asInstanceOf[LDSingleKindContext]

    expect(result.name == franceCtx.name && result.kind === franceCtx.kind)
  }
}
