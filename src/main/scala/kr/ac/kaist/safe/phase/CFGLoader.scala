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

package kr.ac.kaist.safe.phase

import scala.util.{ Try, Success }
import kr.ac.kaist.safe.{ LINE_SEP, SafeConfig }
import kr.ac.kaist.safe.cfg_builder.DotWriter
import kr.ac.kaist.safe.nodes.cfg.CFG
import kr.ac.kaist.safe.util._

// CFGLoader phase
case object CFGLoader extends PhaseObj[Unit, CFGLoaderConfig, CFG] {
  val name: String = "cfgLoader"
  val help: String =
    "Loads a control flow graph from a given JSON file."
  def apply(
    unit: Unit,
    safeConfig: SafeConfig,
    config: CFGLoaderConfig
  ): Try[CFG] = {
    val fileNames = safeConfig.fileNames
    // TODO only one existing file is inserted.
    // TODO check if it has the extension ".json"
    val cfg = new CFG(null, null) // TODO load CFG from JSON file
    // TODO if it has not well-formed JSON then throw an SAFE exception.

    // Pretty print to file.
    config.outFile.map(out => {
      val (fw, writer) = Useful.fileNameToWriters(out)
      writer.write(cfg.toString(0))
      writer.close
      fw.close
      println("Dumped CFG to " + out)
    })

    // print dot file: {dotName}.gv, {dotName}.pdf
    config.dotName.map(name => {
      DotWriter.spawnDot(cfg, None, None, None, s"$name.gv", s"$name.pdf")
    })

    Success(cfg)
  }

  def defaultConfig: CFGLoaderConfig = CFGLoaderConfig()
  val options: List[PhaseOption[CFGLoaderConfig]] = List(
    ("silent", BoolOption(c => c.silent = true),
      "messages during CFG loading are muted."),
    ("out", StrOption((c, s) => c.outFile = Some(s)),
      "the loaded CFG will be written to the outfile."),
    ("dot", StrOption((c, s) => c.dotName = Some(s)),
      "the loaded CFG will be drawn to the {name}.gv and {name}.pdf")
  )
}

// CFGLoader phase config
case class CFGLoaderConfig(
  var silent: Boolean = false,
  var outFile: Option[String] = None,
  var dotName: Option[String] = None
) extends Config
