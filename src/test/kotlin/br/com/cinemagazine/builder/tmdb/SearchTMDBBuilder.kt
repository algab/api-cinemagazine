package br.com.cinemagazine.builder.tmdb

import br.com.cinemagazine.dto.tmdb.SearchTMDB

fun getSearchTMDB(title: String = "Test", popularity: Long = 100): SearchTMDB {
    return SearchTMDB(
        1,
        title,
        "Test",
        "Test",
        "/test.png",
        "2024-02-29",
        popularity
    )
}