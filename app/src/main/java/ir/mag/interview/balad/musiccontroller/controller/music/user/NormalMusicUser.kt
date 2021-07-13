package ir.mag.interview.balad.musiccontroller.controller.music.user

import ir.mag.interview.balad.musiccontroller.model.Track

class NormalMusicUser : IMusicUser {
    override fun updatePlaylist(tracks: List<Track>): List<Track>? {
        if (tracks.size < 5) return null
        return tracks.shuffled()
    }
}