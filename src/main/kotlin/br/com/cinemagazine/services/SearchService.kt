package br.com.cinemagazine.services

import br.com.cinemagazine.dto.production.SearchDTO

interface SearchService {
    fun search(name: String): List<SearchDTO>
}