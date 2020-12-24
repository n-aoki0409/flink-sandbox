package util

import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

class TemperatureAverager extends ProcessWindowFunction[SensorReading, SensorReading, String, TimeWindow] {

  override def process(
    sensorId: String,
    context: Context,
    input: Iterable[SensorReading],
    out: Collector[SensorReading]): Unit = {

    val (cnt, sum) = input.foldLeft((0, 0.0)) { case ((cnt, sum), r) => (cnt + 1, sum + r.temperature) }
    val avgTemp = sum / cnt

    out.collect(SensorReading(sensorId, context.window.getEnd, avgTemp))
  }
}
