package util

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner

class SensorTimeAssigner extends SerializableTimestampAssigner[SensorReading] {
  override def extractTimestamp(element: SensorReading, recordTimestamp: Long): Long = element.timestamp
}
