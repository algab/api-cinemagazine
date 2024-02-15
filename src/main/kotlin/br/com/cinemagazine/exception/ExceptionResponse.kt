package br.com.cinemagazine.exception

data class ExceptionResponse(val status: Int, val error: String, val description: List<String?>)