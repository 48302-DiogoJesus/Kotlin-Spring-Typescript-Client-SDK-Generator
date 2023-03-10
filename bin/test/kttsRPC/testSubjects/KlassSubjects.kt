package kttsRPC.testSubjects

data class PersonWithoutFriends(
    val id: Int,
    val name: String,
    val lastName: String?,
    val age: Int,
    val male: Boolean,
    val favouriteMovies: List<String>,
    val favouriteTvShows: List<String?>,
    val favouriteSeries: List<String>?,
    val favouriteBirds: List<String?>?
)

data class Person(
    val id: Int,
    val name: String,
    val friends: List<PersonWithoutFriends>,
    val lastName: String?
)

data class School(
    val id: Int,
    val students: List<Person>
)

const val PERSON_WITHOUT_FRIENDS_CONVERTED = "export interface PersonWithoutFriends {\n" +
        "\tage: number ,\n" +
        "\tfavouriteBirds?: (string | null)[] | null,\n" +
        "\tfavouriteMovies: string[] ,\n" +
        "\tfavouriteSeries?: string[] | null,\n" +
        "\tfavouriteTvShows: (string | null)[] ,\n" +
        "\tid: number ,\n" +
        "\tlastName?: string | null,\n" +
        "\tmale: boolean ,\n" +
        "\tname: string \n" +
        "}"

const val PERSON_CONVERTED = "export interface Person {\n" +
        "\tfriends: PersonWithoutFriends[] ,\n" +
        "\tid: number ,\n" +
        "\tlastName?: string | null,\n" +
        "\tname: string \n" +
        "}"

const val SCHOOL_CONVERTED = "export interface School {\n" +
        "\tid: number ,\n" +
        "\tstudents: Person[] \n" +
        "}"
