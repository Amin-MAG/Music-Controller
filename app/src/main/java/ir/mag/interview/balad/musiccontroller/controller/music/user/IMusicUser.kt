package ir.mag.interview.balad.musiccontroller.controller.music.user

import ir.mag.interview.balad.musiccontroller.model.Track


/**
 * [IMusicUser]
 * You can use [PremiumMusicUser] or [NormalMusicUser] to make [MusicController].
 * If you want your customized user model, you should implement this interface and
 * define the characteristic of that kind of user model.
 */
interface IMusicUser {
    fun updatePlaylist(tracks: List<Track>): List<Track>?
}