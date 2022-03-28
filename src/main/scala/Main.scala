package com.github.devcdcc

import cats.effect.{IO, IOApp}
import fs2.{Stream, text}
import fs2.io.file.{Files, Path}

object Main extends IOApp.Simple {
  val defaultSensorProcessor: SensorDefaultProcessor[IO] = new SensorDefaultProcessor[IO]
  val ioSensorProcessor: SensorIOProcessor = new SensorIOProcessor

  def run: IO[Unit] = {
    val inputFiles = ioSensorProcessor.getFiles("input")
    val inputData = inputFiles.flatMap(ioSensorProcessor.retrieveFolderEvents)
    val decodedLines = inputData.map(defaultSensorProcessor.decode)
    val metrics = decodedLines.fold(Map.empty)(defaultSensorProcessor.reduceMetrics)


    val numOfFiles = inputFiles.fold(0) {
      case (acc, value) => acc + 1
    }
      .map(value => s"Num of processed files: $value")

    val numOfRecords = decodedLines.fold(0) {
      case (acc, value) => acc + 1
    }
      .map(value => s"Num of processed measurements: $value")

    val numOfFailedRecords = decodedLines
      .filter(_.value.isEmpty)
      .fold(0) {
        case (acc, value) => acc + 1
      }
      .map(value => s"Num of failed measurements: $value")


    // get basic encoded metrics
    val encoded = metrics
      .flatMap(mapValue => fs2.Stream.apply(mapValue.values.toSeq: _*))
      .map(defaultSensorProcessor.encode)


    // Output Formatter
    val output = numOfFiles ++
      numOfRecords ++
      numOfFailedRecords ++
      Stream("") ++
      Stream("Sensors with highest avg humidity:") ++
      Stream("") ++
      Stream("sensor-id,min,avg,max") ++
      encoded
    // run
    output
      .intersperse("\n")
      //      .through(text.utf8.encode)
      .through(fs2.io.stdoutLines())
      .compile
      .drain
  }
}