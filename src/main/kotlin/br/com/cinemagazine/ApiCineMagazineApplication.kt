package br.com.cinemagazine

import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.JwtParserBuilder
import io.jsonwebtoken.Jwts
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@SpringBootApplication
class ApiCineMagazineApplication {
	@Bean
	fun bCryptPasswordEncoder(): PasswordEncoder {
		return BCryptPasswordEncoder()
	}

	@Bean
	fun jwtBuilder(): JwtBuilder {
		return Jwts.builder()
	}

	@Bean
	fun jwtParserBuilder(): JwtParserBuilder {
		return Jwts.parser()
	}
}

fun main(args: Array<String>) {
	runApplication<ApiCineMagazineApplication>(*args)
}
