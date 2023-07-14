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
import cats.{MonadThrow, ~>}
import facade.launchdarklyNodeServerSdk.mod.LDClient

import scala.scalajs.js
import facade.launchdarklyNodeServerSdk.{mod => LaunchDarkly}
import facade.launchdarklyNodeServerSdk.mod.LDOptions

trait LaunchDarklyClient[F[_]] {

  /** @param featureKey the key of the flag to be evaluated
    * @param context the context against which the flag is being evaluated
    * @param defaultValue the value to use if evaluation fails for any reason
    * @tparam Ctx the type representing the context; this can be [[https://javadoc.io/doc/com.launchdarkly/launchdarkly-java-server-sdk/latest/com/launchdarkly/sdk/LDContext.html LDContext]], [[https://javadoc.io/doc/com.launchdarkly/launchdarkly-java-server-sdk/latest/com/launchdarkly/sdk/LDUser.html LDUser]], or any type with a [[ContextEncoder]] instance in scope.
    * @return the flag value, suspended in the `F` effect. If evaluation fails for any reason, or the evaluated value is not of type Boolean, returns the default value.
    * @see [[https://javadoc.io/doc/com.launchdarkly/launchdarkly-java-server-sdk/latest/com/launchdarkly/sdk/server/interfaces/LDClientInterface.html#boolVariation(java.lang.String,com.launchdarkly.sdk.LDContext,boolean) LDClientInterface#boolVariation]]
    */
  def boolVariation[Ctx: ContextEncoder](
      featureKey: String,
      context: Ctx,
      defaultValue: Boolean,
  ): F[Boolean]

  /** @param featureKey   the key of the flag to be evaluated
    * @param context      the context against which the flag is being evaluated
    * @param defaultValue the value to use if evaluation fails for any reason
    * @tparam Ctx the type representing the context; this can be [[https://javadoc.io/doc/com.launchdarkly/launchdarkly-java-server-sdk/latest/com/launchdarkly/sdk/LDContext.html LDContext]], [[https://javadoc.io/doc/com.launchdarkly/launchdarkly-java-server-sdk/latest/com/launchdarkly/sdk/LDUser.html LDUser]], or any type with a [[ContextEncoder]] instance in scope.
    * @return the flag value, suspended in the `F` effect. If evaluation fails for any reason, or the evaluated value is not of type String, returns the default value.
    * @see [[https://javadoc.io/doc/com.launchdarkly/launchdarkly-java-server-sdk/latest/com/launchdarkly/sdk/server/interfaces/LDClientInterface.html#stringVariation(java.lang.String,com.launchdarkly.sdk.LDContext,string) LDClientInterface#stringVariation]]
    */
  def stringVariation[Ctx: ContextEncoder](
      featureKey: String,
      context: Ctx,
      defaultValue: String,
  ): F[String]

  /** @param featureKey   the key of the flag to be evaluated
    * @param context      the context against which the flag is being evaluated
    * @param defaultValue the value to use if evaluation fails for any reason
    * @tparam Ctx the type representing the context; this can be [[https://javadoc.io/doc/com.launchdarkly/launchdarkly-java-server-sdk/latest/com/launchdarkly/sdk/LDContext.html LDContext]], [[https://javadoc.io/doc/com.launchdarkly/launchdarkly-java-server-sdk/latest/com/launchdarkly/sdk/LDUser.html LDUser]], or any type with a [[ContextEncoder]] instance in scope.
    * @return the flag value, suspended in the `F` effect. If evaluation fails for any reason, or the evaluated value cannot be represented as type Int, returns the default value.
    * @see [[https://javadoc.io/doc/com.launchdarkly/launchdarkly-java-server-sdk/latest/com/launchdarkly/sdk/server/interfaces/LDClientInterface.html#intVariation(java.lang.String,com.launchdarkly.sdk.LDContext,int) LDClientInterface#intVariation]]
    */
  def intVariation[Ctx: ContextEncoder](featureKey: String, context: Ctx, defaultValue: Int): F[Int]

  /** @param featureKey   the key of the flag to be evaluated
    * @param context      the context against which the flag is being evaluated
    * @param defaultValue the value to use if evaluation fails for any reason
    * @tparam Ctx the type representing the context; this can be [[https://javadoc.io/doc/com.launchdarkly/launchdarkly-java-server-sdk/latest/com/launchdarkly/sdk/LDContext.html LDContext]], [[https://javadoc.io/doc/com.launchdarkly/launchdarkly-java-server-sdk/latest/com/launchdarkly/sdk/LDUser.html LDUser]], or any type with a [[ContextEncoder]] instance in scope.
    * @return the flag value, suspended in the `F` effect. If evaluation fails for any reason, or the evaluated value cannot be represented as type Double, returns the default value.
    * @see [[https://javadoc.io/doc/com.launchdarkly/launchdarkly-java-server-sdk/latest/com/launchdarkly/sdk/server/interfaces/LDClientInterface.html#doubleVariation(java.lang.String,com.launchdarkly.sdk.LDContext,double) LDClientInterface#doubleVariation]]
    */
  def doubleVariation[Ctx: ContextEncoder](
      featureKey: String,
      context: Ctx,
      defaultValue: Double,
  ): F[Double]

  /** @param featureKey   the key of the flag to be evaluated
    * @param context      the context against which the flag is being evaluated
    * @param defaultValue the value to use if evaluation fails for any reason
    * @tparam Ctx the type representing the context; this can be [[https://javadoc.io/doc/com.launchdarkly/launchdarkly-java-server-sdk/latest/com/launchdarkly/sdk/LDContext.html LDContext]], [[https://javadoc.io/doc/com.launchdarkly/launchdarkly-java-server-sdk/latest/com/launchdarkly/sdk/LDUser.html LDUser]], or any type with a [[ContextEncoder]] instance in scope.
    * @return the flag value, suspended in the `F` effect. If evaluation fails for any reason, returns the default value.
    * @see [[https://javadoc.io/doc/com.launchdarkly/launchdarkly-java-server-sdk/latest/com/launchdarkly/sdk/server/interfaces/LDClientInterface.html#jsonValueVariation(java.lang.String,com.launchdarkly.sdk.LDContext,com.launchdarkly.sdk.Any) LDClientInterface#jsonValueVariation]]
    */
  def jsonVariation[Ctx: ContextEncoder](
      featureKey: String,
      context: Ctx,
      defaultValue: Any,
  ): F[Any]

  /** @see [[https://javadoc.io/doc/com.launchdarkly/launchdarkly-java-server-sdk/latest/com/launchdarkly/sdk/server/interfaces/LDClientInterface.html#flush() LDClientInterface#flush]]
    */
  def flush: F[Unit]

  def mapK[G[_]: MonadThrow](fk: F ~> G): LaunchDarklyClient[G]
}

object LaunchDarklyClient {
  def resource[F[_]](sdkKey: String, config: LDOptions)(implicit
      F: Async[F]
  ): Resource[F, LaunchDarklyClient[F]] =
    Resource
      .make(F.blocking(LaunchDarkly.init(sdkKey, config)))(cl =>
        F.blocking(cl.close())
      ) // (cl => F.fromPromise(F.delay(cl.flush())) >> F.blocking(cl.close()))
      .map(ldClient => defaultLaunchDarklyClient(ldClient))

  def resource[F[_]](sdkKey: String)(implicit F: Async[F]): Resource[F, LaunchDarklyClient[F]] =
    Resource
      .make(F.blocking(LaunchDarkly.init(sdkKey)))(cl =>
        F.fromPromise(F.delay(cl.flush())) >> F.blocking(cl.close())
      )
      .map(ldClient => defaultLaunchDarklyClient(ldClient))

  private def defaultLaunchDarklyClient[F[_]](
      ldClient: LDClient
  )(implicit F: Async[F]): LaunchDarklyClient.Default[F] =
    new LaunchDarklyClient.Default[F] {

      override def unsafeWithJavaClient[A](f: LDClient => js.Promise[A]): F[A] =
        F.fromPromise(F.delay(f(ldClient)))

    }

  private abstract class Default[F[_]: MonadThrow] extends LaunchDarklyClient[F] {
    self =>

    protected def unsafeWithJavaClient[A](f: LDClient => js.Promise[A]): F[A]

    override def boolVariation[Ctx](
        featureKey: String,
        context: Ctx,
        default: Boolean,
    )(implicit ctxEncoder: ContextEncoder[Ctx]): F[Boolean] =
      unsafeWithJavaClient(_.variation(featureKey, ctxEncoder.encode(context), default)).flatMap {
        case b: Boolean => b.pure[F]
        case _ => ???
      }

    override def stringVariation[Ctx](
        featureKey: String,
        context: Ctx,
        default: String,
    )(implicit ctxEncoder: ContextEncoder[Ctx]): F[String] =
      unsafeWithJavaClient(_.variation(featureKey, ctxEncoder.encode(context), default)).flatMap {
        case s: String => s.pure[F]
        case _ => ???
      }

    override def intVariation[Ctx](featureKey: String, context: Ctx, default: Int)(implicit
        ctxEncoder: ContextEncoder[Ctx]
    ): F[Int] =
      unsafeWithJavaClient(_.variation(featureKey, ctxEncoder.encode(context), default)).flatMap {
        case i: Int => i.pure[F]
        case _ => ???
      }

    override def doubleVariation[Ctx](
        featureKey: String,
        context: Ctx,
        default: Double,
    )(implicit ctxEncoder: ContextEncoder[Ctx]): F[Double] =
      unsafeWithJavaClient(_.variation(featureKey, ctxEncoder.encode(context), default)).flatMap {
        case d: Double => d.pure[F]
        case _ => ???
      }

    override def jsonVariation[Ctx](
        featureKey: String,
        context: Ctx,
        default: Any,
    )(implicit ctxEncoder: ContextEncoder[Ctx]): F[Any] =
      unsafeWithJavaClient(_.variation(featureKey, ctxEncoder.encode(context), default))

    override def flush: F[Unit] = unsafeWithJavaClient(_.flush())

    override def mapK[G[_]: MonadThrow](fk: F ~> G): LaunchDarklyClient[G] =
      new LaunchDarklyClient.Default[G] {
        override def unsafeWithJavaClient[A](f: LDClient => js.Promise[A]): G[A] = fk(
          self.unsafeWithJavaClient(f)
        )

        override def flush: G[Unit] = fk(self.flush)
      }
  }
}
