package br.com.cinemagazine.builder.tmdb

import br.com.cinemagazine.dto.tmdb.TrendingTMDB

fun getTrendingTMDB(title: String = "Test", media: String = "movie"): TrendingTMDB {
    return TrendingTMDB(
        1,
        title,
        "Test",
        "Test",
        "/test.png",
        "2024-02-29",
        media
    )
}