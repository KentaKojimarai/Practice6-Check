package com.example.readcsv

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import jakarta.transaction.Transactional
import repository.ProductRepository
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

@Service
class ProductService(private val productRepository: ProductRepository) {
    private val logger = LoggerFactory.getLogger(ProductService::class.java)

    @Transactional
    fun readCSV(filePath: String): List<Product> {
        val products = mutableListOf<Product>()
        try {
            File(filePath).useLines { lines ->
                lines.drop(1).forEach { line ->
                    val tokens = line.split(",")
                    if (tokens.size != 4) {
                        throw IllegalArgumentException("Invalid CSV format: $line")
                    }
                    val product = Product(
                        category = tokens[0],
                        name = tokens[1],
                        price = tokens[2].toIntOrNull() ?: throw IllegalArgumentException("Invalid price format: ${tokens[2]}"),
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

    fun importCSV(filePath: String, successDir: String, errorDir: String) {
        try {
            val products = readCSV(filePath)
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
