/**
 * *****************************************************************************
 * Copyright (c) 2016-2017, KAIST.
 * All rights reserved.
 *
 * Use is subject to license terms.
 *
 * This distribution may include materials developed by third parties.
 * ****************************************************************************
 */

package kr.ac.kaist.safe.analyzer.domain

////////////////////////////////////////////////////////////////////////////////
// concrete absent type
////////////////////////////////////////////////////////////////////////////////
sealed abstract class Absent
case object Absent extends Absent

////////////////////////////////////////////////////////////////////////////////
// absent abstract domain
////////////////////////////////////////////////////////////////////////////////
trait AbsentDomain extends AbsDomain[Absent] { domain: AbsentDomain =>
  // abstract boolean element
  type Elem <: ElemTrait

  trait ElemTrait extends super.ElemTrait { this: Elem =>
  }
}

////////////////////////////////////////////////////////////////////////////////
// default absent abstract domain
////////////////////////////////////////////////////////////////////////////////
object DefaultAbsent extends AbsentDomain {
  case object Bot extends Elem
  case object Top extends Elem

  def alpha(abs: Absent): Elem = Top

  abstract class Elem extends ElemTrait {
    def gamma: ConSet[Absent] = this match {
      case Bot => ConFin()
      case Top => ConFin(Absent)
    }

    def getSingle: ConSingle[Absent] = this match {
      case Bot => ConZero()
      case Top => ConOne(Absent)
    }

    def <=(that: Elem): Boolean = (this, that) match {
      case (Top, Bot) => false
      case _ => true
    }

    def +(that: Elem): Elem = (this, that) match {
      case (Bot, Bot) => Bot
      case _ => Top
    }

    def <>(that: Elem): Elem = (this, that) match {
      case (Top, Top) => Top
      case _ => Bot
    }

    override def toString: String = this match {
      case Top => "Top(absent)"
      case Bot => "⊥(absent)"
    }
  }
}
