package br.com.cinemagazine.dto.tmdb

data class PageTMDB<T>(
    val page: Int,
    val results: List<T>
)
