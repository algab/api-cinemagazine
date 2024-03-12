package br.com.cinemagazine.builder.tmdb

import br.com.cinemagazine.dto.tmdb.JobTvTMDB

fun getJobTvTMDB(job: String = "Writer"): JobTvTMDB {
    return JobTvTMDB(
        job,
        20
    )
}