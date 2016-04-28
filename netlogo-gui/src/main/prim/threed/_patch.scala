// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.prim.threed

import org.nlogo.agent.World3D
import org.nlogo.api.{ AgentException}
import org.nlogo.core.Syntax
import org.nlogo.core.Nobody
import org.nlogo.nvm.{ Context, Reporter }

class _patch extends Reporter {

  override def report(context: Context) =
    try world.asInstanceOf[World3D].getPatchAt(
      argEvalDoubleValue(context, 0),
      argEvalDoubleValue(context, 1),
      argEvalDoubleValue(context, 2))
    catch { case _: AgentException => Nobody }
}
