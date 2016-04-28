// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.prim

import org.nlogo.agent.{ Patch, Turtle }
import org.nlogo.core.Syntax
import org.nlogo.nvm.{ Context, Reporter }

// needed by _patchat.optimize() because regular _patchhere is turtle-only

class _patchhereinternal extends Reporter {


  override def report(context: Context) = report_1(context)

  def report_1(context: Context): Patch = context.agent match {
    case patch: Patch => patch
    case turtle: Turtle => turtle.getPatchHere
    case _ => world.fastGetPatchAt(0, 0)
  }
}
