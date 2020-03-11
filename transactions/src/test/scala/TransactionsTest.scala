import scala.io.Source

object TransactionsTest {
  def main(args: Array[String]): Unit = {
    val fileName = "Transactions/transactions.txt"
    val transactionLines = Source.fromFile(fileName).getLines().drop(1)
    val transactions: List[Transaction] = TransactionHandler.transactionFromLines(transactionLines)
    val valuePerDay = TransactionHandler.transactionsPerDay(transactions)
    val avgValue = TransactionHandler.avgValuePerAccountPerType(transactions)
    val rollingMaxValue = TransactionHandler.rollingMaxTransactionPerAccount(transactions, 5)
    val rollingAvgValue = TransactionHandler.rollingAvgTransactionPerAccount(transactions, 5)
    val rollingAASumValue = TransactionHandler.rollingSumTransactionPerAccount(transactions, 5,
      category = "AA")
    val rollingBBSumValue = TransactionHandler.rollingSumTransactionPerAccount(transactions, 5,
      category = "CC")
    val rollingFFSumValue = TransactionHandler.rollingSumTransactionPerAccount(transactions, 5,
      category = "FF")

    valuePerDay foreach (x => println("Day: " + x._1 + ", Value: " + x._2))

    for ((x,y) <- avgValue) {for ((a,b) <- y)
      println("Account Id: " + x + ", Transaction Type: " + a + ", Average Value: " + b)}

    val task3 = for {
      (k, v1) <- rollingMaxValue
      (`k`, v2) <- rollingAvgValue
      (`k`, v3) <- rollingAASumValue
      (`k`, v4) <- rollingBBSumValue
      (`k`, v5) <- rollingFFSumValue
    } yield (k, v1, v2, v3, v4, v5)

    def fullOuterJoin[K, V1, V2](as: List[(K, V1)], bs: List[(K, V2)], cs: List[(K, V2)], ds: List[(K, V2)], es: List[(K, V2)]) = {
      val map1 = as.toMap
      val map2 = bs.toMap
      val map3 = cs.toMap
      val map4 = ds.toMap
      val map5 = es.toMap
      val allKeys = map1.keySet ++ map2.keySet ++ map3.keySet ++ map4.keySet ++ map5.keySet
      allKeys.toList.map(k => (k, map1.get(k), map2.get(k), map3.get(k), map4.get(k), map5.get(k)))
    }

    val task3Final = for {
      (k, v1, v2, v3, v4, v5) <- task3
    } yield (k, fullOuterJoin(v1.toList, v2.toList, v3.toList, v4.toList, v5.toList))

    for ((id,y) <- task3Final) {
      for ((day, maxval, avgval, aa, bb, ff) <- y) {
        println("Account Id: " + id + ", Day: " + day + ", Max Value: " + maxval.getOrElse(0.0) + ", Average Value: " + avgval.getOrElse(0.0) + ", AA: " + aa.getOrElse(0) + ", BB: " + bb.getOrElse(0.0) + ", FF: " + ff.getOrElse(0.0))
        assert(maxval.getOrElse(0.0) >= avgval.getOrElse(0.0))
      }
    }
  }
}
