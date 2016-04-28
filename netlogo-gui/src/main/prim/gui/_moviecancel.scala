// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.prim.gui

import org.nlogo.core.Syntax
import org.nlogo.nvm.{ Command, Context, EngineException }
import org.nlogo.window.GUIWorkspace

class _moviecancel extends Command {

  override def perform(context: Context) {
    workspace match {
      case gw: GUIWorkspace =>
        workspace.waitFor(
          new org.nlogo.api.CommandRunnable {
            def run() {
              if (gw.movieEncoder != null) {
                gw.movieEncoder.cancel()
                gw.movieEncoder = null
              }}})
      case _ =>
        throw new EngineException(
          context, this, token.text + " can only be used in the GUI")
    }
    context.ip = next
  }
}
