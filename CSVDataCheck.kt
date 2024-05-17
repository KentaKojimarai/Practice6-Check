import java.io.File

// CSVデータを処理するクラス
class CsvDataCheck(private val csvFilePath: String, private val targetColumnIndex: Int) {
    private var rowCount = 0
    private var totalSum = 0.0

    // CSVファイルを処理して行数と合計を計算するメソッド
    fun calculateRowCountAndTotalSum() {
        File(csvFilePath).bufferedReader().useLines { lines ->
            lines.drop(1).forEach { line ->
                val columns = line.split(",") // CSVファイルの列を分割
                if (columns.size > targetColumnIndex) {
                    rowCount++
                    totalSum += columns[targetColumnIndex].toDouble()
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

    // 合計を計算する列のインデックス（0から始まる）
    val targetColumnIndex = 2 // 例として4番目の列（0-based index）

    // CsvDataProcessorクラスのインスタンスを作成して処理を行う
    val processor = CsvDataCheck(csvFilePath, targetColumnIndex)
    processor.calculateRowCountAndTotalSum()
    processor.printResult()
}
