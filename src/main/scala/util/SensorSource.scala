package util

import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext

import java.util.Calendar
import scala.util.Random

class SensorSource extends RichParallelSourceFunction[SensorReading] {

  var running: Boolean = true

  override def run(srcCtx: SourceContext[SensorReading]): Unit = {
    val rand = new Random()
    val taskIdx = this.getRuntimeContext.getIndexOfThisSubtask

    var curFTemp = (1 to 10) map { i =>
      ("sensor_" + (taskIdx * 10 + i), 65 + (rand.nextGaussian() * 20))
    }

    while(running) {
      curFTemp = curFTemp.map { case (id, temp) => (id, temp + (rand.nextGaussian() * 0.5)) }
      val curTime = Calendar.getInstance.getTimeInMillis

      curFTemp.foreach { case (id, temp) => srcCtx.collect(SensorReading(id, curTime, temp)) }

      Thread.sleep(100)
    }
  }

  override def cancel(): Unit = {
    running = false
  }
}
