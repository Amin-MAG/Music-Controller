package ir.mag.interview.balad.musiccontroller.controller.music

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ir.mag.interview.balad.musiccontroller.controller.music.state.IMusicState
import ir.mag.interview.balad.musiccontroller.controller.music.state.PauseState
import ir.mag.interview.balad.musiccontroller.controller.music.state.PlayingState
import ir.mag.interview.balad.musiccontroller.controller.music.user.IMusicUser
import ir.mag.interview.balad.musiccontroller.model.Track

class MusicController(
    private val _user: IMusicUser,
    tracks: List<Track>
) {
    // states
    val pauseState: PauseState = PauseState(this)
    val playingState: PlayingState = PlayingState(this)

    private val _selectedTrack: MutableLiveData<Track> = MutableLiveData()
    private var _playlist: MutableLiveData<List<Track>> = MutableLiveData()

    var state: IMusicState = pauseState
    val playingTrack: LiveData<Track> = _selectedTrack
    val playlist: LiveData<List<Track>> = _playlist

    init {
        _playlist.value = tracks
    }

    fun play(): Boolean {
        state.play()?.let {
            _selectedTrack.value = it
            return true
        }
        return false
    }

    fun pause(time: Long): Boolean {
        state.pause(time)?.let {
            _selectedTrack.value = it
            return true
        }
        return false
    }

    fun stop(): Boolean {
        state.stop()?.let {
            _selectedTrack.value = it
            return true
        }
        return false
    }

    fun getNextTracks(): List<Track> {
        _playlist.value?.let {
            val idx = it.indexOf(_selectedTrack.value)
            return it.subList(idx + 1, it.size - 1)
        }
        return ArrayList()
    }

    fun updatePlaylist(tracks: List<Track>): Boolean {
        _user.updatePlaylist(tracks)?.let {
            _playlist.value = it
            _selectedTrack.value = it[0]
            return true
        }
        return false
    }

    companion object {
        const val TAG = "music_controller"
    }
}