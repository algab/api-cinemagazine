package br.com.cinemagazine.builder.tmdb

import br.com.cinemagazine.dto.tmdb.CrewTvTMDB
import br.com.cinemagazine.dto.tmdb.JobTvTMDB

fun getCrewTvTMDB(jobs: List<JobTvTMDB> = listOf(getJobTvTMDB())): CrewTvTMDB {
    return CrewTvTMDB(
        "Test",
        "http://image.com/writer.png",
        jobs
    )
}