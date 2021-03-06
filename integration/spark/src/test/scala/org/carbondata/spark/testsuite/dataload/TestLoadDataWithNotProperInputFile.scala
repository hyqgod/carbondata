/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.carbondata.spark.testsuite.dataload

import java.io.File

import org.apache.spark.sql.common.util.{CarbonHiveContext, QueryTest}

import org.carbondata.spark.load.CarbonLoadModel
import org.carbondata.spark.util.GlobalDictionaryUtil

import org.scalatest.BeforeAndAfterAll

/**
 * Test class of loading data for carbon table with not proper input file
 *
 */
class TestLoadDataWithNotProperInputFile extends QueryTest with BeforeAndAfterAll {

  var dataPath: String = _
  var carbonLoadModel: CarbonLoadModel = _

  override def beforeAll: Unit = {
    dataPath = new File(this.getClass.getResource("/").getPath + "/../../")
      .getCanonicalPath + "/src/test/resources/nullSample.csv"
    carbonLoadModel = new CarbonLoadModel
    carbonLoadModel.setFactFilePath(dataPath)
  }

  test("test loading data with input path exists but has nothing") {
    try {
      GlobalDictionaryUtil.loadDataFrame(CarbonHiveContext, carbonLoadModel)
    } catch {
      case e: Throwable =>
        assert(e.getMessage.contains("please check your input path and make sure " +
          "that files end with '.csv' and content is not empty"))
    }
  }

  test("test loading data with input file not ends with '.csv'") {
    dataPath = new File(this.getClass.getResource("/").getPath + "/../../")
      .getCanonicalPath + "/src/test/resources/noneCsvFormat.cs"
    carbonLoadModel.setFactFilePath(dataPath)
    try {
      GlobalDictionaryUtil.loadDataFrame(CarbonHiveContext, carbonLoadModel)
    } catch {
      case e: Throwable =>
        assert(e.getMessage.contains("please check your input path and make sure " +
          "that files end with '.csv' and content is not empty"))
    }
  }
}
