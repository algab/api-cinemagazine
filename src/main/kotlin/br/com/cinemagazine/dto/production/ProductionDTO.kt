package br.com.cinemagazine.dto.production

interface ProductionDTO {
    val id: Long
    val title: String
    val originalTitle: String
    val description: String
    val poster: String
    val dateRelease: String
    val companies: List<String>
}