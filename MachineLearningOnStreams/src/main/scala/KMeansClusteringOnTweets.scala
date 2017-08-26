import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.clustering.StreamingKMeans
import scala.concurrent.duration.Duration


/**
  * Apllying the k-means clustering on tweets data
  * data residing in the data/training will be read by the stream
  * and k-means applied to it
  */
object KMeansClusteringOnTweets {
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("KMeans Clustering on Tweets")
    conf.setMaster("local[*]")
    val ssc = new StreamingContext(conf, Seconds(10))
    ssc.checkpoint("/home/nadeem/sparkcheckpoint")

    val trainingStream = ssc.textFileStream("/home/nadeem/tweets/training")
          .map(line => parseTrainingData(line))

    val model = new StreamingKMeans()
                .setDecayFactor(1.0)//change decay factor to 0, to use just the latest batch of tweets
                .setK(2)
                .setRandomCenters(2, 1.0, 0)

    println("Initial centers: " + model.latestModel().clusterCenters.mkString)

    model.trainOn(trainingStream)
    ssc.start()

    //periodically print the clusters

    val actorSystem = ActorSystem()
    val scheduler = actorSystem.scheduler
    val task = new Runnable { def run() { println("Cluster centers" + model.latestModel().clusterCenters.mkString)  } }

    implicit val executor = actorSystem.dispatcher

    scheduler.schedule(
      initialDelay = Duration(5, TimeUnit.SECONDS),
      interval = Duration(10, TimeUnit.SECONDS),
      runnable = task)

    ssc.awaitTermination()

  }

  def parseTrainingData(line:String): Vector ={
    val arr = line.split(",")
    return Vectors.dense(arr(0).toDouble,arr(1).toDouble)
  }
}