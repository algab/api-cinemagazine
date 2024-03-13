package br.com.cinemagazine.constants

enum class Media(private val value: String) {
    MOVIE("Movie"), TV("TV");

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