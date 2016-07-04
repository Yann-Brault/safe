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

package kr.ac.kaist.safe.analyzer.console.command

import jline.console.ConsoleReader
import kr.ac.kaist.safe.analyzer.console._
import kr.ac.kaist.safe.analyzer.domain._
import kr.ac.kaist.safe.config.Config

// run instructions
case object CmdRunInsts extends Command("run_insts", "Run instruction by instruction.") {
  def help: Unit = println("usage: " + name)
  def run(c: Console, args: List[String]): Option[Target] = {
    args match {
      case Nil => {
        val cp = c.getCurCP
        val st = cp.getState
        val block = cp.node
        val insts = block.getInsts.reverse
        val reader = new ConsoleReader()
        insts match {
          case Nil => println("* no instructions")
          case _ => println(c.getCurCP.node.toString(0))
        }
        val (resSt, resExcSt, _) = insts.foldLeft((st, State.Bot, true)) {
          case ((oldSt, oldExcSt, true), inst) =>
            println
            reader.setPrompt(
              s"inst: [${inst.id}] $inst" + Config.LINE_SEP +
                s"('s': state / 'q': stop / 'n','': next)" + Config.LINE_SEP +
                s"> "
            )
            var line = ""
            while ({
              line = reader.readLine
              line match {
                case "s" => {
                  println("*** state ***")
                  println(showState(c, oldSt))
                  println
                  println("*** exception state ***")
                  println(showState(c, oldExcSt))
                  true
                }
                case "d" => true // TODO diff
                case "n" | "" => false
                case "q" => false
                case _ => true
              }
            }) {}
            line match {
              case "q" => (oldSt, oldExcSt, false)
              case _ =>
                val (st, excSt) = c.semantics.I(cp, inst, oldSt, oldExcSt)
                (st, excSt, true)
            }
          case (old @ (_, _, false), inst) => old
        }
        println("*** state ***")
        println(showState(c, resSt))
        println
        println("*** exception state ***")
        println(showState(c, resExcSt))
      }
      case _ => help
    }
    None
  }
}
