import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import util.{SensorReading, SensorSource, SensorTimeAssigner, TemperatureAverager}

import java.time.Duration


object AverageSensorReadings extends App {

  val env = StreamExecutionEnvironment.getExecutionEnvironment

  env.getConfig.setAutoWatermarkInterval(1000L)

  val sensorData: DataStream[SensorReading] = env.addSource(new SensorSource).assignTimestampsAndWatermarks(
    WatermarkStrategy
      .forBoundedOutOfOrderness[SensorReading](Duration.ofSeconds(5))
      .withTimestampAssigner(new SensorTimeAssigner)
  )

  val avgTemp: DataStream[SensorReading] = sensorData
    .map { r =>
      SensorReading(r.id, r.timestamp, (r.temperature - 32) * (5.0 / 9.0))
    }
    .keyBy(_.id)
    .window(TumblingEventTimeWindows.of(Time.seconds(5)))
    .process(new TemperatureAverager)

  avgTemp.print()

  env.execute("Compute average sensor temperature")
}
