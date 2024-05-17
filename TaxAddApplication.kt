package com.example.readcsv

import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EnableJpaRepositories(
	basePackages = ["repository"]
)
@SpringBootApplication
class TaxAddApplication(
	val taxProductService: TaxProductService
) {
	val taxProductFilePath = "C:/Users/10260226/Product2.csv"
	val successDir = "C:/Users/10260226/finishedCSV/Success/"
	val errorDir = "C:/Users/10260226/finishedCSV/error/"

	@Bean
	fun applicationRunner() =
		ApplicationRunner {
			taxProductService.importCSVData(taxProductFilePath, successDir, errorDir)
		}
}

fun main(args: Array<String>) {
	val run = runApplication<TaxAddApplication>(*args)
	run.close()
}
