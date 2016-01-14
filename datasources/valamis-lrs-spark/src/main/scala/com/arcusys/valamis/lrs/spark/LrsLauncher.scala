//package com.arcusys.valamis.lrs.spark
//
//import com.arcusys.valamis.lrs.jdbc.SimpleLrs
//import com.arcusys.valamis.lrs.spark.typemap.SparkJodaSupport
//import org.apache.spark.streaming.kafka.KafkaUtils
//import org.apache.spark.streaming.{StreamingContext, Seconds}
//import org.apache.spark.{SparkContext, SparkConf}
//
//import scala.slick.driver.MySQLDriver
//
///**
// * Created by Iliya Tryapitsin on 05.08.15.
// */
//object LrsLauncher {
//  def main(args: Array[String]): Unit = {
//    val conf = new SparkConf().setAppName("Valamis LRS")
//
//    val sc = new SparkContext(conf)
//    val ssc = new StreamingContext(sc, Seconds(5))
//
//    val msg = ssc.socketTextStream("localhost", 9999)
//
//    // Count each word in each batch
//    msg.foreachRDD(m => {
//
//      val lrs = new SparkLrs(
//        sc,
//        new SimpleLrs(
//          MySQLDriver.profile.backend.Database.forURL("jdbc:mysql://localhost:3306/lportal62cega4?user=root"),
//          new SparkExecutionContext(),
//          new SparkJodaSupport(MySQLDriver)))
//      lrs.verbAmount()
//    })
//
//    msg.print()
//    ssc.start()
//    ssc.awaitTermination()
//  }
//
//}
