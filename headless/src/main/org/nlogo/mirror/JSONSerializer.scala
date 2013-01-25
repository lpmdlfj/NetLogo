package org.nlogo.mirror

import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

import org.nlogo.{ api, mirror }, api.AgentVariables, mirror._
import Mirrorables._

object JSONSerializer {

  def serialize(update: Update): String = {

    def births: Seq[(Kind, JField)] =
      for {
        Birth(AgentKey(kind, id), values) <- update.births
        varNames = getImplicitVariables(kind)
        varFields = varNames zip values.map(toJValue)
      } yield kind -> JField(id.toString, JObject(varFields: _*))

    def changes: Seq[(Kind, JField)] =
      for {
        (AgentKey(kind, id), changes) <- update.changes
        varNames = getImplicitVariables(kind)
        implicitVars = for {
          Change(varIndex, value) <- changes
          varName = if (varNames.length > varIndex) varNames(varIndex) else varIndex.toString
        } yield varName -> toJValue(value)
      } yield kind -> JField(id.toString, JObject(implicitVars: _*))

    def deaths: Seq[(Kind, JField)] =
      for {
        Death(AgentKey(kind, id)) <- update.deaths
      } yield kind -> JField(id.toString, JObject(JField("WHO", -1)))

    val fieldsByKind: Map[Kind, Seq[JField]] =
      (births ++ changes ++ deaths)
        .groupBy(_._1) // group by kinds
        .mapValues(_.map(_._2)) // remove kind from pairs

    val keysToKind = Seq(
      "turtles" -> Turtle,
      "patches" -> Patch)

    val objectsByKey: Seq[(String, JObject)] =
      for {
        (key, kind) <- keysToKind
        fields = fieldsByKind.getOrElse(kind, Seq())
      } yield key -> JObject(fields: _*)

    compact(render(JObject(objectsByKey: _*)))
  }

  def toJValue(v: AnyRef): JValue = v match {
    case d: java.lang.Double  =>
      if (d.intValue == d.doubleValue)
        JInt(d.intValue)
      else
        JDouble(d.doubleValue)
    case i: java.lang.Integer => JInt(i.intValue)
    case b: java.lang.Boolean => JBool(b)
    case s: java.lang.String  => JString(s)
    case x                    => JString(v.toString)
  }

  def getImplicitVariables(kind: Kind): Seq[String] =
    kind match {
      case Turtle => AgentVariables.getImplicitTurtleVariables(false)
      case Patch  => AgentVariables.getImplicitPatchVariables(false)
      case Link   => AgentVariables.getImplicitLinkVariables
      case _      => Seq() // TODO: raise exception instead?
    }

}
