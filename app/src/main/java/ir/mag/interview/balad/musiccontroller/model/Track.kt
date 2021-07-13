package ir.mag.interview.balad.musiccontroller.model

data class Track(
    val ID: Long,
    val title: String,
    val albumName: String,
    val artistName: String,
    val trackLength: Long,
    var progress: Long = 0,
)