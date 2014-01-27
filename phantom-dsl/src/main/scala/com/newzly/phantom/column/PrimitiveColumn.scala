package com.newzly.phantom.column

import scala.annotation.implicitNotFound
import com.newzly.phantom.{CassandraPrimitive, CassandraTable}
import com.datastax.driver.core.Row

@implicitNotFound(msg = "Type ${RR} must be a Cassandra primitive")
class PrimitiveColumn[Owner <: CassandraTable[Owner, Record], Record, @specialized(Int, Double, Float, Long) RR: CassandraPrimitive](t: CassandraTable[Owner, Record]) extends Column[Owner, Record, RR](t) {

  getTable.addColumn(this)

  def cassandraType: String = CassandraPrimitive[RR].cassandraType
  def toCType(v: RR): AnyRef = CassandraPrimitive[RR].toCType(v)

  def optional(r: Row): Option[RR] =
    implicitly[CassandraPrimitive[RR]].fromRow(r, name)
}

class TimeSeries[T]

class TimeColumn[Owner <: CassandraTable[Owner, Record], Record, T](table: CassandraTable[Owner, Record])(implicit ev: TimeSeries[T], pr: CassandraPrimitive[T]) extends PrimitiveColumn[Owner, Record, T](table)(pr)