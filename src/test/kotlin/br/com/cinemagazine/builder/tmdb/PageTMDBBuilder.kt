package br.com.cinemagazine.builder.tmdb

import br.com.cinemagazine.dto.tmdb.PageTMDB

fun <T> getPageTMDB(page: Int = 1, list: List<T>): PageTMDB<T> {
    return PageTMDB(page, list)
}