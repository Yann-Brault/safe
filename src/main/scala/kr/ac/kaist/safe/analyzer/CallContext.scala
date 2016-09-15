/**
 * *****************************************************************************
 * Copyright (c) 2016, KAIST.
 * All rights reserved.
 *
 * Use is subject to license terms.
 *
 * This distribution may include materials developed by third parties.
 * ****************************************************************************
 */

package kr.ac.kaist.safe.analyzer

import kr.ac.kaist.safe.analyzer.domain._
import kr.ac.kaist.safe.nodes.cfg.{ CFG, FunctionId }
import kr.ac.kaist.safe.util.Address

import scala.collection.immutable.HashSet

import scala.util.{ Try, Failure, Success }

abstract class CallContext {
  def newCallContext(
    cfg: CFG,
    calleeFid: FunctionId,
    scopeLoc: Loc
  ): CallContext
}

/* Interface */
case class CallContextManager(callsiteDepth: Int = 0) {
  val globalCallContext: CallContext = KCallsite(callsiteDepth, List[Address]())

  private case class KCallsite(depth: Int, callsiteList: List[Address]) extends CallContext {
    def newCallContext(
      cfg: CFG,
      calleeFid: FunctionId,
      scopeLoc: Loc
    ): CallContext = {
      val k: Int =
        cfg.getFunc(calleeFid) match {
          case Some(fun) if fun.isUser => depth
          case _ => depth + 1 // additional depth for built-in calls.
        }
      val newCallsiteList = (scopeLoc.address :: this.callsiteList).take(k)
      KCallsite(depth, newCallsiteList)
    }

    override def toString: String = "(" + callsiteList.mkString(", ") + ")"
  }
}
