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

package kr.ac.kaist.safe.cfg_builder

import scala.util.Try
import kr.ac.kaist.safe.analyzer.domain.{ Address, Loc, RecencyTag, Recent, Old }

// Used by cfg_builder/DefaultCFGBuilder.scala
trait AddressManager {
  def addrToLoc(addr: Address, recency: RecencyTag): Loc
  def locToAddr(loc: Loc): Address
  def oldifyLoc(loc: Loc): Loc
  def isRecentLoc(loc: Loc): Boolean
  def isOldLoc(loc: Loc): Boolean
  def compareLoc(a: Loc, b: Loc): Int
  def locName(loc: Loc): String
  def parseLocName(s: String): Try[Loc]
  def registerSystemAddress(addr: Address, name: String): Unit
  def newProgramAddr(): Address
  def newProgramAddr(name: String): Address
  def newRecentLoc(name: String): Loc
  def newRecentLoc(): Loc
  def newSystemRecentLoc(name: String): Loc
  def newSystemLoc(name: String, tag: RecencyTag): Loc
}
