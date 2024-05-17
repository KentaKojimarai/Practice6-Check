import java.io.File

// CSVデータを処理するクラス
class TaxAddCsvDataCheck(private val csvFilePath: String) {
    // 行数
    private var rowCount = 0

    // 合計値
    private var totalSum = 0

    // CSVファイルを処理して行数と合計を計算するメソッド
    fun calculateRowCountAndTotalSum() {
        File(csvFilePath).bufferedReader().useLines { lines ->
            lines.drop(1).forEach { line ->
                val columns = line.split(",") // CSVファイルの列を分割
                if (columns.isNotEmpty() && columns.size >= 3) {
                    rowCount++
                    val value = columns[2].toDoubleOrNull()?.toInt() ?: return@forEach
                    totalSum += if (columns[0] == "Food") (value * 1.08).toInt() else (value * 1.1).toInt()
                }
            }
        }
    }

    // 結果を出力するメソッド
    fun printResult() {
        println("行数: $rowCount")
        println("合計: $totalSum")
    }
}

fun main() {
    // CSVファイルのパス
    val csvFilePath = "C:/Users/10260226/finishedCSV/Success/Product2.csv"

    // CsvDataProcessorクラスのインスタンスを作成して処理を行う
    val processor = TaxAddCsvDataCheck(csvFilePath)
    processor.calculateRowCountAndTotalSum()
    processor.printResult()
}
