package com.bucharest.qurio.domain.entity

enum class Category(val id: Int, val displayName: String) {
    GENERAL_KNOWLEDGE(9, "General Knowledge"),
    BOOKS(10, "Entertainment: Books"),
    FILM(11, "Entertainment: Film"),
    MUSIC(12, "Entertainment: Music"),
    MUSICALS_THEATRES(13, "Entertainment: Musicals & Theatres"),
    TELEVISION(14, "Entertainment: Television"),
    VIDEO_GAMES(15, "Entertainment: Video Games"),
    BOARD_GAMES(16, "Entertainment: Board Games"),
    SCIENCE_NATURE(17, "Science & Nature"),
    COMPUTERS(18, "Science: Computers"),
    MATHEMATICS(19, "Science: Mathematics"),
    MYTHOLOGY(20, "Mythology"),
    SPORTS(21, "Sports"),
    GEOGRAPHY(22, "Geography"),
    HISTORY(23, "History"),
    POLITICS(24, "Politics"),
    ART(25, "Art"),
    CELEBRITIES(26, "Celebrities"),
    ANIMALS(27, "Animals"),
    VEHICLES(28, "Vehicles"),
    COMICS(29, "Entertainment: Comics"),
    GADGETS(30, "Science: Gadgets"),
    ANIME_MANGA(31, "Entertainment: Japanese Anime & Manga"),
    CARTOON_ANIMATIONS(32, "Entertainment: Cartoon & Animations");
}