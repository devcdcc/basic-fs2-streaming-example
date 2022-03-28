package com.github.devcdcc

import cats.effect.IO
import fs2.io.file.{Files, Path}
import fs2.{Stream, text}

class SensorIOProcessor extends WalkFolderEvents[IO] {

  def getFiles(folder: String): Stream[IO, Path] =
    Files[IO]
      .list(Path.apply(folder))

  def retrieveFolderEvents(path: Path): Stream[IO, String] =
    Files[IO].readAll(path)
      .through(text.utf8.decode)
      .through(text.lines)
      .drop(1)
      .filter(_.nonEmpty)

}
