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

package kr.ac.kaist.safe.analyzer.models.builtin

import kr.ac.kaist.safe.analyzer.domain._
import kr.ac.kaist.safe.analyzer.domain.Utils._
import kr.ac.kaist.safe.analyzer.models._
import kr.ac.kaist.safe.analyzer._
import kr.ac.kaist.safe.util.SystemAddr
import scala.collection.immutable.HashSet

object BuiltinArray extends FuncModel(
  name = "Array",

  // 15.4.1.1 Array([item1[, item2[, ... ]]])
  code = BasicCode(argLen = 1, BuiltinArrayHelper.construct),

  // 15.4.2.1 new Array([item0[, item1[, ... ]]])
  // 15.4.2.2 new Array(len)
  construct = Some(BasicCode(argLen = 1, BuiltinArrayHelper.construct)),

  // 15.4.3.1 Array.prototype
  protoModel = Some((BuiltinArrayProto, F, F, F)),

  props = List(
    // TODO isArray
    NormalProp("isArray", FuncModel(
      name = "Array.isArray",
      code = EmptyCode(argLen = 1)
    ), T, F, T)
  )
)

object BuiltinArrayProto extends ObjModel(
  name = "Array.prototype",
  props = List(
    InternalProp(IClass, PrimModel("Array")),
    NormalProp("length", PrimModel(0.0), T, F, T),

    // TODO toString
    NormalProp("toString", FuncModel(
      name = "Array.prototype.toString",
      code = EmptyCode(argLen = 0)
    ), T, F, T),

    // TODO toLocaleString
    NormalProp("toLocaleString", FuncModel(
      name = "Array.prototype.toLocaleString",
      code = EmptyCode(argLen = 0)
    ), T, F, T),

    // TODO concat
    NormalProp("concat", FuncModel(
      name = "Array.prototype.concat",
      code = EmptyCode(argLen = 1)
    ), T, F, T),

    // TODO join
    NormalProp("join", FuncModel(
      name = "Array.prototype.join",
      code = EmptyCode(argLen = 1)
    ), T, F, T),

    // TODO pop
    NormalProp("pop", FuncModel(
      name = "Array.prototype.pop",
      code = EmptyCode(argLen = 0)
    ), T, F, T),

    // TODO push
    NormalProp("push", FuncModel(
      name = "Array.prototype.push",
      code = EmptyCode(argLen = 1)
    ), T, F, T),

    // TODO reverse
    NormalProp("reverse", FuncModel(
      name = "Array.prototype.reverse",
      code = EmptyCode(argLen = 0)
    ), T, F, T),

    // 15.4.4.9 Array.prototype.shift()
    NormalProp("shift", FuncModel(
      name = "Array.prototype.shift",
      code = BasicCode(argLen = 0, BuiltinArrayHelper.shift)
    ), T, F, T),

    // 15.4.4.10 Array.prototype.slice(start, end)
    NormalProp("slice", FuncModel(
      name = "Array.prototype.slice",
      code = BasicCode(argLen = 2, BuiltinArrayHelper.slice)
    ), T, F, T),

    // TODO sort
    NormalProp("sort", FuncModel(
      name = "Array.prototype.sort",
      code = EmptyCode(argLen = 1)
    ), T, F, T),

    // TODO splice
    NormalProp("splice", FuncModel(
      name = "Array.prototype.splice",
      code = EmptyCode(argLen = 2)
    ), T, F, T),

    // TODO unshift
    NormalProp("unshift", FuncModel(
      name = "Array.prototype.unshift",
      code = EmptyCode(argLen = 1)
    ), T, F, T),

    // TODO indexOf
    NormalProp("indexOf", FuncModel(
      name = "Array.prototype.indexOf",
      code = EmptyCode(argLen = 1)
    ), T, F, T),

    // TODO lastIndexOf
    NormalProp("lastIndexOf", FuncModel(
      name = "Array.prototype.lastIndexOf",
      code = EmptyCode(argLen = 1)
    ), T, F, T),

    // TODO every
    NormalProp("every", FuncModel(
      name = "Array.prototype.every",
      code = EmptyCode(argLen = 1)
    ), T, F, T),

    // TODO some
    NormalProp("some", FuncModel(
      name = "Array.prototype.some",
      code = EmptyCode(argLen = 1)
    ), T, F, T),

    // TODO forEach
    NormalProp("forEach", FuncModel(
      name = "Array.prototype.forEach",
      code = EmptyCode(argLen = 1)
    ), T, F, T),

    // TODO map
    NormalProp("map", FuncModel(
      name = "Array.prototype.map",
      code = EmptyCode(argLen = 1)
    ), T, F, T),

    // TODO filter
    NormalProp("filter", FuncModel(
      name = "Array.prototype.filter",
      code = EmptyCode(argLen = 1)
    ), T, F, T),

    // TODO reduce
    NormalProp("reduce", FuncModel(
      name = "Array.prototype.reduce",
      code = EmptyCode(argLen = 1)
    ), T, F, T),

    // TODO reduceRight
    NormalProp("reduceRight", FuncModel(
      name = "Array.prototype.reduceRight",
      code = EmptyCode(argLen = 1)
    ), T, F, T)
  )
)

object BuiltinArrayHelper {
  ////////////////////////////////////////////////////////////////
  // Array
  ////////////////////////////////////////////////////////////////
  def construct(args: AbsValue, st: State): (State, State, AbsValue) = {
    val h = st.heap
    val length = Helper.propLoad(args, Set(AbsString("length")), h).pvalue.numval
    val first = Helper.propLoad(args, Set(AbsString("0")), h)
    val argObj = h.get(args.locset)
    val AT = AbsBool.True
    val (retObj: AbsObject, retExcSet: Set[Exception]) = length.getSingle match {
      case ConZero() => (AbsObjectUtil.Bot, ExcSetEmpty)
      case ConOne(Num(1)) => {
        // 15.4.2.2 new Array(len)
        val firstN = first.pvalue.numval
        val (lenObj: AbsObject, excSet: Set[Exception]) = if (!firstN.isBottom) {
          // If the argument len is a Number and ToUint32(len) is equal to len,
          // then the length property of the newly constructed object is set to ToUint32(len).
          val equal = (firstN === firstN.toUInt32)
          val trueV = if (AbsBool.True <= equal) {
            AbsObjectUtil.newArrayObject(firstN)
          } else AbsObjectUtil.Bot
          // If the argument len is a Number and ToUint32(len) is not equal to len,
          // a RangeError exception is thrown.
          val falseV =
            if (AbsBool.False <= equal) HashSet(RangeError)
            else ExcSetEmpty
          (trueV, falseV)
        } else (AbsObjectUtil.Bot, ExcSetEmpty)

        val otherObj = if (!first.pvalue.copyWith(numval = AbsNumber.Bot).isBottom || !first.locset.isBottom) {
          // If the argument len is not a Number, then the length property of the newly constructed object
          // is set to 1 and the 0 property of the newly constructed object is set to len with attributes
          // {[[Writable]]: true, [[Enumerable]]: true, [[Configurable]]: true}.
          val arr = AbsObjectUtil.newArrayObject(AbsNumber(1))
          val dp = AbsDataProp(first, AT, AT, AT)
          arr.initializeUpdate("0", dp)
        } else AbsObjectUtil.Bot

        (lenObj + otherObj, excSet)
      }
      case ConOne(Num(n)) => {
        // 15.4.2.1 new Array([item0[, item1[, ... ]]])
        val length = n.toInt
        val arr = AbsObjectUtil.newArrayObject(AbsNumber(length))
        val obj = (0 until length).foldLeft(arr)((arr, k) => {
          val kStr = k.toString
          val kValue = argObj(kStr).value
          val dp = AbsDataProp(kValue, AT, AT, AT)
          arr.initializeUpdate(kStr, dp)
        })
        (obj, ExcSetEmpty)
      }
      case ConMany() => {
        val len = first.pvalue.numval + length
        val arr = AbsObjectUtil.newArrayObject(len)
        val aKeySet = argObj.amap.abstractKeySet((aKey, _) => aKey <= AbsString.Number)
        val arrObj = aKeySet.foldLeft(arr)((arr, aKey) => {
          val value = argObj(aKey).value
          val dp = AbsDataProp(value, AT, AT, AT)
          arr.update(aKey, dp)
        })
        (arrObj, HashSet(RangeError))
      }
    }
    val arrAddr = SystemAddr("Array<instance>")
    val state = st.oldify(arrAddr)
    val arrLoc = Loc(arrAddr, Recent)
    val retH = state.heap.update(arrLoc, retObj)
    val excSt = state.raiseException(retExcSet)
    (State(retH, state.context), excSt, AbsLoc(arrLoc))
  }

  ////////////////////////////////////////////////////////////////
  // Array.prototype
  ////////////////////////////////////////////////////////////////
  def shift(args: AbsValue, st: State): (State, State, AbsValue) = {
    val h = st.heap
    val thisLoc = st.context.thisBinding
    val (retH, retV, excSet) = thisLoc.foldLeft((h, AbsValue.Bot, ExcSetEmpty)) {
      case ((h, value, excSet), loc) => {
        // XXX: 1. Let O be the result of calling ToObject passing the this value as the argument.
        // TODO current "this" value only have location. we should change!
        // 2. Let lenVal be the result of calling the [[Get]] internal method of O with argument "length".
        val obj = h.get(loc)
        val lenVal = obj.Get("length", h)
        // 3. Let len be ToUint32(lenVal).
        val len = TypeConversionHelper.ToUInt32(lenVal)
        val (retObj: AbsObject, retV: AbsValue, retExcSet: Set[Exception]) = len.getSingle match {
          case ConZero() => (AbsObjectUtil.Bot, AbsValue.Bot, ExcSetEmpty)
          // 4. If len is zero, then
          case ConOne(Num(0)) => {
            // a. Call the [[Put]] internal method of O with arguments "length", 0, and true.
            val (retObj, retExcSet) = obj.Put(AbsString("length"), AbsValue(0), true, h)
            // b. Return undefined.
            (retObj, AbsValue(AbsUndef.Top), retExcSet)
          }
          case ConOne(Num(n)) => {
            val len = n.toInt
            // 5. Let first be the result of calling the [[Get]] internal method of O with argument "0".
            val first = obj.Get("0", h)
            // 6. Let k be 1.
            // 7. Repeat, while k < len
            var excSet = ExcSetEmpty
            val retObj = (1 until len).foldLeft(obj)((obj, k) => {
              // a. Let from be ToString(k).
              val from = AbsString(k.toString)
              // b. Let to be ToString(k–1).
              val to = AbsString((k - 1).toString)
              // c. Let fromPresent be the result of calling the [[HasProperty]] internal method of O with argument from.
              val fromPresent = obj.HasProperty(from, h)
              val trueV = if (AbsBool.True <= fromPresent) {
                // d. If fromPresent is true, then
                // i. Let fromVal be the result of calling the [[Get]] internal method of O with argument from.
                val fromVal = obj.Get(from, h)
                // ii. Call the [[Put]] internal method of O with arguments to, fromVal, and true.
                val (retObj, retExcSet) = obj.Put(to, fromVal, true, h)
                excSet ++= retExcSet
                retObj
              } else AbsObjectUtil.Bot
              val falseV = if (AbsBool.False <= fromPresent) {
                // e. Else, fromPresent is false
                // i. Call the [[Delete]] internal method of O with arguments to and true.
                val (retObj, _) = obj.Delete(to) //XXX: missing second argument Throw = true.
                // f. Increase k by 1.
                retObj
              } else AbsObjectUtil.Bot
              trueV + falseV
            })
            // 8. Call the [[Delete]] internal method of O with arguments ToString(len–1) and true.
            val (delObj, _) = retObj.Delete(AbsString((len - 1).toString)) //XXX: missing second argument Throw = true.
            // 9. Call the [[Put]] internal method of O with arguments "length", (len–1) , and true.
            val (putObj, putExcSet) = delObj.Put(AbsString("length"), AbsNumber(len - 1), true, h)
            // 10. Return first.
            val retExcSet = excSet ++ putExcSet
            (putObj, first, retExcSet)
          }
          // XXX: very imprecise ConMany case
          case ConMany() => (obj.update(AbsString.Top, AbsDataProp.Top), AbsValue.Top, HashSet(TypeError))
        }
        val retH = h.update(loc, retObj)
        (retH, value + retV, excSet ++ retExcSet)
      }
    }
    val excSt = st.raiseException(excSet)
    (State(retH, st.context), excSt, retV)
  }

  def slice(args: AbsValue, st: State): (State, State, AbsValue) = {
    val h = st.heap
    val thisLoc = st.context.thisBinding
    val start = Helper.propLoad(args, Set(AbsString("0")), h)
    val end = Helper.propLoad(args, Set(AbsString("1")), h)

    // XXX: 1. Let O be the result of calling ToObject passing the this value as the argument.
    // TODO current "this" value only have location. we should change!
    val obj = h.get(thisLoc)
    // 2. Let A be a new array created as if by the expression new Array().
    val arr = AbsObjectUtil.newArrayObject()
    // 3. Let lenVal be the result of calling the [[Get]] internal method of O with argument "length".
    val lenVal = obj.Get("length", h)
    // 4. Let len be ToUint32(lenVal).
    val len = TypeConversionHelper.ToUInt32(lenVal)
    // 5. Let relativeStart be ToInteger(start).
    val relativeStart = TypeConversionHelper.ToInteger(start)
    // 6. If end is undefined, let relativeEnd be len; else let relativeEnd be ToInteger(end).
    val undefLen =
      if (end.pvalue.undefval.isBottom) AbsNumber.Bot
      else len
    val numLen =
      if (end.pvalue.copyWith(undefval = AbsUndef.Bot).isBottom && end.locset.isBottom) AbsNumber.Bot
      else TypeConversionHelper.ToInteger(end)
    val relativeEnd = undefLen + numLen
    val (retObj: AbsObject, retExcSet: Set[Exception]) = (len.getSingle, relativeStart.getSingle, relativeEnd.getSingle) match {
      case (ConZero(), _, _) | (_, ConZero(), _) | (_, _, ConZero()) => (AbsObjectUtil.Bot, ExcSetEmpty)
      case (ConOne(Num(l)), ConOne(Num(from)), ConOne(Num(to))) => {
        val len = l.toInt
        val relativeStart = from.toInt
        val relativeEnd = to.toInt
        def toU(num: Int): Int =
          if (num < 0) Math.max((len + num), 0)
          else Math.min(num, len)
        // 7. If relativeStart is negative, let k be max((len + relativeStart),0); else let k be min(relativeStart, len).
        val k = toU(relativeStart)
        // 8. If relativeEnd is negative, let final be max((len + relativeEnd),0); else let final be min(relativeEnd, len).
        val finalN = toU(relativeEnd)
        // 9. Let n be 0.
        // 10. Repeat, while k < final
        val start = k
        // XXX: It is not in the spec: but it is needed because we did not modeling the aliasing of 'length' for Array obects.
        val length =
          if (start > finalN) 0
          else finalN - start
        val (initArr, _) = arr.Put(AbsString("length"), AbsNumber(length), false, h)
        (start until finalN).foldLeft((initArr, ExcSetEmpty)) {
          case ((arr, excSet), k) => {
            val n = k - start
            // a. Let Pk be ToString(k).
            val Pk = AbsString(k.toString)
            // b. Let kPresent be the result of calling the [[HasProperty]] internal method of O with argument Pk.
            val kPresent = obj.HasProperty(Pk, h)
            // c. If kPresent is true, then
            val (retObj, retExcSet) = if (AbsBool.True <= kPresent) {
              // i. Let kValue be the result of calling the [[Get]] internal method of O with argument Pk.
              val kValue = obj.Get(Pk, h)
              // ii. Call the [[DefineOwnProperty]] internal method of A with arguments ToString(n), Property Descriptor
              //     {[[Value]]: kValue, [[Writable]]: true, [[Enumerable]]: true, [[Configurable]]: true}, and false.
              val AT = (AbsBool.True, AbsAbsent.Bot)
              val desc = AbsDesc((kValue, AbsAbsent.Bot), AT, AT, AT)
              val (retObj, _, excSet) = arr.DefineOwnProperty(AbsString(n.toString), desc, false)
              (retObj, excSet)
            } else (AbsObjectUtil.Bot, ExcSetEmpty)
            val falseObj = if (AbsBool.False <= kPresent) obj else AbsObjectUtil.Bot
            // d. Increase k by 1.
            // e. Increase n by 1.
            (retObj + falseObj, excSet ++ retExcSet)
          }
        }
      }
      case _ => (arr.update(AbsString.Top, AbsDataProp.Top), HashSet(TypeError))
    }
    // 11. Return A.
    val arrAddr = SystemAddr("Array.prototype.slice<array>")
    val state = st.oldify(arrAddr)
    val arrLoc = Loc(arrAddr, Recent)
    val retH = state.heap.update(arrLoc, retObj)
    val excSt = state.raiseException(retExcSet)
    (State(retH, state.context), excSt, AbsLoc(arrLoc))
  }
}
