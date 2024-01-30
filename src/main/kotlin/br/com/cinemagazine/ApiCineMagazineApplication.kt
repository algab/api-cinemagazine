package br.com.cinemagazine

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApiCineMagazineApplication

fun main(args: Array<String>) {
	runApplication<ApiCineMagazineApplication>(*args)
}
