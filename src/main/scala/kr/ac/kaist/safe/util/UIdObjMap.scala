/**
 * *****************************************************************************
 * Copyright (c) 2016-2018, KAIST.
 * All rights reserved.
 *
 * Use is subject to license terms.
 *
 * This distribution may include materials developed by third parties.
 * ****************************************************************************
 */

package kr.ac.kaist.safe.util

import kr.ac.kaist.safe.errors.error.WrongUId
import scala.collection.mutable.{ Map => MMap }
import spray.json._

class UIdObjMap {
  // internal mutable map from unique ids to objects
  private val map: MMap[Int, Object] = MMap()

  // number of unique id
  private var count: Int = 0

  // get new unique id
  private def getUId: Int = {
    val uid = count
    count += 1
    uid
  }

  def size: Int = count
  def keySet: Set[Int] = map.keySet.toSet
  def toJSON(obj: Object): JsValue = {
    val uid = getUId
    val json = JsObject("____SYMBOL" -> JsNumber(uid))
    map(uid) = obj
    json
  }
  def apply[T](uid: Int): T =
    map.getOrElse(uid, throw WrongUId(uid)).asInstanceOf[T]
}
