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

package kr.ac.kaist.safe.json

import kr.ac.kaist.safe.util._
import kr.ac.kaist.safe.nodes.ir._
import kr.ac.kaist.safe.nodes.cfg._
import kr.ac.kaist.safe.json.NodeProtocol._
import kr.ac.kaist.safe.json.CFGExprProtocol._
import kr.ac.kaist.safe.json.CFGBlockProtocol._
import kr.ac.kaist.safe.errors.error.CFGFunctionParseError

import spray.json._
import DefaultJsonProtocol._

object CFGFunctionProtocol extends DefaultJsonProtocol {

  implicit object CFGFunctionJsonFormat extends RootJsonFormat[CFGFunction] {

    def write(func: CFGFunction): JsValue = func match {
      case CFGFunction(ir, argsName, argVars, localVars, name, isUser) => JsArray(
        ir.toJson,
        JsString(argsName),
        JsArray(argVars.map(_.toJson).to[Vector]),
        JsArray(localVars.map(_.toJson).to[Vector]),
        JsString(name),
        JsBoolean(isUser),
        JsArray(func.getAllBlocks.drop(3).map(_.toJson).to[Vector])
      )
    }

    def read(value: JsValue): CFGFunction = value match {
      case JsArray(Vector(
        ir,
        JsString(argsName),
        JsArray(argVars),
        JsArray(localVars),
        JsString(name),
        JsBoolean(isUser),
        JsArray(blocks)
        )) => {
        val func = CFGFunction(
          ir.convertTo[IRNode],
          argsName,
          argVars.map(_.convertTo[CFGId]).to[List],
          localVars.map(_.convertTo[CFGId]).to[List],
          name,
          isUser
        )
        for (block <- blocks)
          // TODO add blocks to the list in the function
          block.convertTo[CFGBlock]
        func
      }
      case _ => throw CFGFunctionParseError(value)
    }
  }
}
