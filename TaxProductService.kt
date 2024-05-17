package com.example.readcsv

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import repository.ProductRepository
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

@Service
class TaxProductService(private val productRepository: ProductRepository) {
    private val logger = LoggerFactory.getLogger(TaxProductService::class.java)

    // Foodの税率（8%）
    private val foodTaxRate = 0.08

    // それ以外の税率（10%）
    private val otherTaxRate = 0.1

    @Transactional
    fun TaxAddCSV(filePath: String): List<TaxProduct> {
        val products = mutableListOf<TaxProduct>()
        try {
            File(filePath).useLines { lines ->
                lines.drop(1).forEach { line ->
                    val tokens = line.split(",")
                    if (tokens.size != 4) {
                        throw IllegalArgumentException("Invalid CSV format: $line")
                    }
                    val category = tokens[0]
                    val price = tokens[2].toIntOrNull() ?: throw IllegalArgumentException("Invalid price format: ${tokens[2]}")

                    // カテゴリーごとに適切な税率を適用
                    val taxRate = if (category == "Food") foodTaxRate else otherTaxRate
                    // 税金を計算して新しい金額を設定
                    val taxedPrice = (price * (1 + taxRate)).toInt()

                    val product = TaxProduct(
                        category = tokens[0],
                        name = tokens[1],
                        price = taxedPrice,
                        origin = tokens[3]
                    )
                    products.add(product)
                }
            }
            logger.info("CSV file successfully read from $filePath")
        } catch (e: Exception) {
            logger.error("Error reading CSV file from $filePath: ${e.message}", e)
            throw e
        }
        return products
    }

    fun importCSVData(filePath: String, successDir: String, errorDir: String) {
        try {
            val products = TaxAddCSV(filePath)
            productRepository.saveAll(products)
            logger.info("CSV data imported into database successfully.")
            moveFile(filePath, successDir)
        } catch (e: Exception) {
            logger.error("Error importing CSV data into database: ${e.message}", e)
            moveFile(filePath, errorDir)
        }
    }

    private fun moveFile(filePath: String, destinationDir: String) {
        try {
            val sourcePath = Path.of(filePath)
            val destinationPath = Path.of(destinationDir, sourcePath.fileName.toString())
            Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING)
            logger.info("File moved from $filePath to $destinationPath")
        } catch (e: Exception) {
            logger.error("Error moving file from $filePath to $destinationDir: ${e.message}", e)
        }
    }
}
