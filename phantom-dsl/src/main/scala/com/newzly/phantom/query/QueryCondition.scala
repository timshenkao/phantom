/*
 * Copyright 2013 newzly ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.newzly.phantom.query

import com.datastax.driver.core.querybuilder.{QueryBuilder, Assignment, Clause}
import com.newzly.phantom.column.AbstractColumn

case class QueryCondition(clause: Clause)

case class QueryAssignment(assignment: Assignment)

class IndexedColumn[T](col: AbstractColumn[T]) {

  /**
   * The equals operator. Will return a match if the value equals the database value.
   * @param value The value to search for in the database.
   * @return A QueryCondition, wrapping a QueryBuilder clause.
   */
  def eqs(value: T): QueryCondition = {
    QueryCondition(QueryBuilder.eq(col.name, col.toCType(value)))
  }

  def lt(value: T): QueryCondition = {
    QueryCondition(QueryBuilder.lt(col.name, col.toCType(value)))
  }

  def gt(value: T): QueryCondition = {
    QueryCondition(QueryBuilder.gt(col.name, col.toCType(value)))
  }

}