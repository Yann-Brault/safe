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

import kr.ac.kaist.safe.analyzer.TypeConversionHelper

/* 8.10 The Property Descriptor and Property Identifier Specification Types */

////////////////////////////////////////////////////////////////////////////////
// concrete descriptor type
////////////////////////////////////////////////////////////////////////////////
case class Desc(
  value: Option[Value],
  writable: Option[Bool],
  enumerable: Option[Bool],
  configurable: Option[Bool]
)

////////////////////////////////////////////////////////////////////////////////
// descriptor abstract domain
////////////////////////////////////////////////////////////////////////////////
trait DescDomain extends AbsDomain[Desc] { domain: DescDomain =>
  def apply(
    value: (AbsValue, AbsAbsent),
    writable: (AbsBool, AbsAbsent) = (AbsBool.Bot, AbsAbsent.Top),
    enumerable: (AbsBool, AbsAbsent) = (AbsBool.Bot, AbsAbsent.Top),
    configurable: (AbsBool, AbsAbsent) = (AbsBool.Bot, AbsAbsent.Top)
  ): Elem

  // 8.10.5 ToPropertyDescriptor ( Obj )
  def ToPropertyDescriptor(obj: AbsObj, h: AbsHeap): Elem

  // abstract boolean element
  type Elem <: ElemTrait

  trait ElemTrait extends super.ElemTrait { this: Elem =>
    val value: (AbsValue, AbsAbsent)
    val writable: (AbsBool, AbsAbsent)
    val enumerable: (AbsBool, AbsAbsent)
    val configurable: (AbsBool, AbsAbsent)

    def copyWith(
      value: (AbsValue, AbsAbsent) = this.value,
      writable: (AbsBool, AbsAbsent) = this.writable,
      enumerable: (AbsBool, AbsAbsent) = this.enumerable,
      configurable: (AbsBool, AbsAbsent) = this.configurable
    ): Elem

    // 8.10.1 IsAccessorDescriptor ( Desc )
    // XXX: we do not support accessor descriptor yet
    // def IsAccessorDescriptor: AbsBool

    // 8.10.2 IsDataDescriptor ( Desc )
    def IsDataDescriptor: AbsBool

    // 8.10.3 IsGenericDescriptor ( Desc )
    def IsGenericDescriptor: AbsBool
  }
}

////////////////////////////////////////////////////////////////////////////////
// default descriptor abstract domain
////////////////////////////////////////////////////////////////////////////////
object DefaultDesc extends DescDomain {
  lazy val Bot: Elem = Elem(
    (AbsValue.Bot, AbsAbsent.Bot),
    (AbsBool.Bot, AbsAbsent.Bot),
    (AbsBool.Bot, AbsAbsent.Bot),
    (AbsBool.Bot, AbsAbsent.Bot)
  )
  lazy val Top: Elem = Elem(
    (AbsValue.Top, AbsAbsent.Top),
    (AbsBool.Top, AbsAbsent.Top),
    (AbsBool.Top, AbsAbsent.Top),
    (AbsBool.Top, AbsAbsent.Top)
  )

  private def conversion[V](
    opt: Option[V],
    domain: AbsDomain[V]
  ): (domain.Elem, AbsAbsent) = opt match {
    case Some(v) => (domain(v), AbsAbsent.Bot)
    case None => (domain.Bot, AbsAbsent.Top)
  }
  def alpha(desc: Desc): Elem = Elem(
    conversion(desc.value, AbsValue),
    conversion(desc.writable, AbsBool),
    conversion(desc.enumerable, AbsBool),
    conversion(desc.configurable, AbsBool)
  )

  def apply(
    value: (AbsValue, AbsAbsent),
    writable: (AbsBool, AbsAbsent),
    enumerable: (AbsBool, AbsAbsent),
    configurable: (AbsBool, AbsAbsent)
  ): Elem = Elem(value, writable, enumerable, configurable)

  case class Elem(
      value: (AbsValue, AbsAbsent),
      writable: (AbsBool, AbsAbsent),
      enumerable: (AbsBool, AbsAbsent),
      configurable: (AbsBool, AbsAbsent)
  ) extends ElemTrait {
    def gamma: ConSet[Desc] = ConInf() // TODO more precise

    def getSingle: ConSingle[Desc] = ConMany() // TODO more precise

    def <=(that: Elem): Boolean = {
      val (left, right) = (this, that)
      val (lv, lva) = left.value
      val (lw, lwa) = left.writable
      val (le, lea) = left.enumerable
      val (lc, lca) = left.configurable
      val (rv, rva) = right.value
      val (rw, rwa) = right.writable
      val (re, rea) = right.enumerable
      val (rc, rca) = right.configurable
      lv <= rv && lva <= rva &&
        lw <= rw && lwa <= rwa &&
        le <= re && lea <= rea &&
        lc <= rc && lca <= rca
    }

    def +(that: Elem): Elem = {
      val (left, right) = (this, that)
      val (lv, lva) = left.value
      val (lw, lwa) = left.writable
      val (le, lea) = left.enumerable
      val (lc, lca) = left.configurable
      val (rv, rva) = right.value
      val (rw, rwa) = right.writable
      val (re, rea) = right.enumerable
      val (rc, rca) = right.configurable
      Elem(
        (lv + rv, lva + rva),
        (lw + rw, lwa + rwa),
        (le + re, lea + rea),
        (lc + rc, lca + rca)
      )
    }

    def <>(that: Elem): Elem = {
      val (left, right) = (this, that)
      val (lv, lva) = left.value
      val (lw, lwa) = left.writable
      val (le, lea) = left.enumerable
      val (lc, lca) = left.configurable
      val (rv, rva) = right.value
      val (rw, rwa) = right.writable
      val (re, rea) = right.enumerable
      val (rc, rca) = right.configurable
      Elem(
        (lv <> rv, lva <> rva),
        (lw <> rw, lwa <> rwa),
        (le <> re, lea <> rea),
        (lc <> rc, lca <> rca)
      )
    }

    override def toString: String = {
      if (isBottom) "⊥Desc"
      else {
        val wch = writable.toString
        val ech = enumerable.toString
        val cch = configurable.toString
        s"[$wch$ech$cch] $value"
      }
    }

    def copyWith(
      value: (AbsValue, AbsAbsent) = this.value,
      writable: (AbsBool, AbsAbsent) = this.writable,
      enumerable: (AbsBool, AbsAbsent) = this.enumerable,
      configurable: (AbsBool, AbsAbsent) = this.configurable
    ): Elem = Elem(value, writable, enumerable, configurable)

    def IsDataDescriptor: AbsBool = {
      val (v, va) = value
      val (w, wa) = writable
      val trueV =
        if (v.isBottom && w.isBottom) AbsBool.Bot
        else AbsBool.True
      val falseV =
        if (va.isBottom || wa.isBottom) AbsBool.Bot
        else AbsBool.False
      trueV + falseV
    }

    def IsGenericDescriptor: AbsBool =
      IsDataDescriptor.negate
  }

  def ToPropertyDescriptor(obj: AbsObj, h: AbsHeap): Elem = {
    def get(str: String): (AbsValue, AbsAbsent) = {
      val has = obj.HasProperty(AbsStr(str), h)
      val v =
        if (AbsBool.True <= has) obj.Get(str, h)
        else AbsValue.Bot

      val va =
        if (AbsBool.False <= has) AbsAbsent.Top
        else AbsAbsent.Bot
      (v, va)
    }
    def getB(str: String): (AbsBool, AbsAbsent) = {
      val (v, va) = get(str)
      (TypeConversionHelper.ToBoolean(v), va)
    }

    val v = get("value")
    val w = getB("writable")
    val e = getB("enumerable")
    val c = getB("configurable")

    Elem(v, w, e, c)
  }
}
