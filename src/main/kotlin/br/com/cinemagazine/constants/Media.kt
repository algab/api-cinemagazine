package br.com.cinemagazine.constants

enum class Media(val value: String) {
    MOVIE("movie"), TV("tv");

    companion object {
        fun getMedia(value: String): Media? {
            for (media in Media.entries) {
                if (media.value.uppercase() == value.uppercase()) {
                    return media
                }
            }
            return null
        }
    }
}