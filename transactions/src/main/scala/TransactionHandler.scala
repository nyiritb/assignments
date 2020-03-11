import scala.io.Source

object TransactionHandler {

  def linesFromCsv(fileName: String, dropHeader: Boolean = true): Iterator[String] = {
    //The lines of the CSV file (dropping the first to remove the header)
    val source = Source.fromFile(fileName)
    try {
      if (dropHeader) {
        source.getLines().drop(1)
      } else {
        source.getLines()
      }
    }
    finally {
      source.close()
    }
  }

  def transactionFromLines(transactionLines: Iterator[String]): List[Transaction] = {
    //Here we split each line up by commas and construct Transactions
    transactionLines.map { line =>
      val split = line.split(',')
      Transaction(split(0), split(1), split(2).toInt, split(3), split(4).toDouble)
    }.toList
  }

  def transactionsPerDay(transactions: List[Transaction]): List[(Int, Double)] = {
    transactions.map { x =>
      (x.transactionDay, x.transactionAmount)
    }.groupMapReduce(x => x._1)(y => y._2)((z1, z2) =>  z1 + z2)
      .toList
      .sortBy(_._1)
  }

  def avgValuePerAccountPerType(transactions: List[Transaction]) = {
    val mappedTransactions = transactions.map { x =>
      (x.accountId, x.category, x.transactionAmount)
    }.groupMap(x => x._1)(y => (y._2, y._3))

    mappedTransactions.toList
      .map(x => (x._1, x._2.groupMap(_._1)(_._2)
       .toList
       .map(z => (z._1, (z._2).sum / (z._2).size))
        .sortBy(_._1)))
      .sortBy(_._1)
  }

  def rollingTransactionPerAccount(transactions: List[Transaction], startDate: Int, window: Int = 5) = {
    transactions.map { x =>
      (x.accountId, x.transactionDay, x.transactionAmount)
    }.filter(x => (x._2 > startDate) && (x._2 <= startDate + window))
      .groupMap(x => x._1)(y => (y._2, y._3))
  }

  def rollingMaxTransactionPerAccount(transactions: List[Transaction], startDate: Int, window: Int = 5) = {
    val mappedTransactions = rollingTransactionPerAccount(transactions, startDate, window)

    mappedTransactions.toList.map(x => (x._1, x._2
      .groupMapReduce(_._1)(_._2)((x1, x2) => x1 + x2)))
  }

  def rollingAvgTransactionPerAccount(transactions: List[Transaction], startDate: Int, window: Int = 5) = {
    val mappedTransactions = rollingTransactionPerAccount(transactions, startDate, window)

    mappedTransactions.toList.map(x => (x._1, x._2
      .groupMap(_._1)(_._2)
      .toList
      .map(y => (y._1, y._2.sum / y._2.size))))
      .map(z => (z._1, z._2.toMap))
  }

  def rollingSumTransactionPerAccount(transactions: List[Transaction], startDate: Int, category: String, window: Int = 5) = {
    val mappedTransactions = transactions.map { x =>
      (x.accountId, x.transactionDay, x.category, x.transactionAmount)
    }.filter(x => (x._2 > startDate) && (x._2 <= startDate + window)).groupMap(x => x._1)(y => (y._2, y._3, y._4))

    mappedTransactions
      .toList
      .map(x => (x._1, x._2
      .filter(_._2 == category)
      .map(y => (y._1, y._3))
      .groupMapReduce(_._1)(_._2)((z1, z2) => z1 + z2)))
  }
}
