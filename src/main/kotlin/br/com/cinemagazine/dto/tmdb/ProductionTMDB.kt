package br.com.cinemagazine.dto.tmdb

interface ProductionTMDB {
    val id: Long
    val title: String
    val originalTitle: String
    val description: String
    val poster: String?
    val dateRelease: String
    val popularity: Long
}