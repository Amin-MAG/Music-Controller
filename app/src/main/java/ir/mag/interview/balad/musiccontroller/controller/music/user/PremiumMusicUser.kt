package ir.mag.interview.balad.musiccontroller.controller.music.user

import ir.mag.interview.balad.musiccontroller.model.Track

class PremiumMusicUser : IMusicUser {
    override fun updatePlaylist(tracks: List<Track>): List<Track>? {
        if (tracks.isEmpty()) return null
        return tracks
    }
}