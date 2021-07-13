package ir.mag.interview.balad.musiccontroller.controller.music.user

import ir.mag.interview.balad.musiccontroller.model.Track

class PremiumMusicUser : IMusicUser {
    override fun updatePlaylist(tracks: List<Track>): List<Track>? {
        if (tracks.isEmpty()) return null
        return tracks
    }

    override fun back(tracks: List<Track>, playing: Track): Track? {
        if (tracks.isEmpty()) return null

        tracks.let {
            var idx = it.indexOf(playing)
            if (idx - 1 < 0) idx = tracks.size - 1 else idx--
            it[idx].progress = 0
            return it[idx]
        }
    }

    override fun addTrackInstantly(
        tracks: List<Track>,
        track: Track,
        playing: Track
    ): List<Track> {
        tracks.let {
            val idx = it.indexOf(playing)
            val newTracks = ArrayList(tracks)
            newTracks.add(idx + 1, track)
            return newTracks
        }
    }
}