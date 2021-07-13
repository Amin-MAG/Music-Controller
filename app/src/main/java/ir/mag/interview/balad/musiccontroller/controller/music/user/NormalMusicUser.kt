package ir.mag.interview.balad.musiccontroller.controller.music.user

import ir.mag.interview.balad.musiccontroller.model.Track

class NormalMusicUser : IMusicUser {
    override fun updatePlaylist(tracks: List<Track>): List<Track>? {
        if (tracks.size < 5) return null
        return tracks.shuffled()
    }

    override fun back(tracks: List<Track>, playing: Track): Track? {
        // normal user can not back
        return null
    }

    override fun addTrackInstantly(tracks: List<Track>, track: Track, playing: Track): List<Track>? {
        // normal user can not back
        return null
    }
}