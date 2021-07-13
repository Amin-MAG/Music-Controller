package ir.mag.interview.balad.musiccontroller.controller.music

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ir.mag.interview.balad.musiccontroller.controller.music.state.IMusicState
import ir.mag.interview.balad.musiccontroller.controller.music.state.PauseState
import ir.mag.interview.balad.musiccontroller.controller.music.state.PlayingState
import ir.mag.interview.balad.musiccontroller.controller.music.user.IMusicUser
import ir.mag.interview.balad.musiccontroller.model.Track

/**
 * [MusicController] is responsible for music player logic.
 */
class MusicController(
    private val _user: IMusicUser,
    tracks: List<Track>
) {
    /**
     * In state design pattern we instantiate the states [pauseState] and
     * [playingState] then we should have a [state] field.
     */
    val pauseState: PauseState = PauseState(this)
    val playingState: PlayingState = PlayingState(this)
    var state: IMusicState = pauseState

    /**
     * Make this fields private to avoid modifying [MutableLiveData] objects.
     */
    private val _selectedTrack: MutableLiveData<Track> = MutableLiveData()
    private var _playlist: MutableLiveData<List<Track>> = MutableLiveData()

    /**
     * [playingTrack] is for task number 1 to get the playing track.
     * It is live data and you can take the advantage of this.
     * Also you can not change the value from outside of controller.
     */
    val playingTrack: LiveData<Track> = _selectedTrack
    val playlist: LiveData<List<Track>> = _playlist

    init {
        _playlist.value = tracks
    }

    /**
     * [playOrResume] is implemented for task number 5 to play or resume the track.
     * It will return null if any errors occurs.Else, It will return the playing track,
     * so that we can get the progress time by track.progress
     * Here we use State design pattern to handle our states. for now two kinds of
     * state are available: playing, pause.
     */
    fun playOrResume(): Track? {
        state.play()?.let {
            _selectedTrack.value = it
            return _selectedTrack.value
        }
        return null
    }

    /**
     * [pause] is implemented for task number 4 to pause the playing track by
     * giving the time.
     * Here we use State design pattern to handle our states. for now two kinds of
     * state are available: playing, pause.
     */
    fun pause(time: Long): Boolean {
        state.pause(time)?.let {
            _selectedTrack.value = it
            return true
        }
        return false
    }

    /**
     * [stop] is implemented for task number 6 to stop playing track.
     * It has the same behavior for playing and pause states.
     */
    fun stop(): Boolean {
        playingTrack.value?.let {
            it.progress = 0
            _selectedTrack.value = it
            state = pauseState
            return true
        }
        return false
    }

    /**
     * [skip] is implemented for task number 7 to skip a track.
     * It has the same behavior for playing and pause states.
     * No need to implement it in states. (for now)
     */
    fun skip(): Track? {
        _playlist.value?.let {
            var idx = it.indexOf(_selectedTrack.value)
            if (idx + 1 >= it.size) idx = 0 else idx++
            it[idx].progress = 0
            _selectedTrack.value = it[idx]
            return _selectedTrack.value
        }
        return null
    }

    /**
     * [back] is implemented for task 8 to go to previous track.
     * This feature is for premium users so they have different behaviors.
     * This is implemented in user that is [IMusicUser]
     */
    fun back(): Track? {
        playlist.value?.let { pl ->
            _selectedTrack.value?.let { st ->
                _user.back(pl, st)?.let {
                    _selectedTrack.value = it
                    return _selectedTrack.value
                }
            }
        }
        return null
    }

    /**
     * [addTrackInstantly] is implemented for task 9 to add a track instantly.
     * This feature is for premium users so they have different behaviors.
     * This is implemented in user that is [IMusicUser]
     */
    fun addTrackInstantly(track: Track): Boolean {
        playlist.value?.let { pl ->
            _selectedTrack.value?.let { st ->
                _user.addTrackInstantly(pl, track, st)?.let {
                    _playlist.value = it
                    return true
                }
            }
        }
        return false
    }

    /**
     * [getNextTracks] is for task number 2 to get next tracks.
     */
    fun getNextTracks(): List<Track> {
        _playlist.value?.let {
            val idx = it.indexOf(_selectedTrack.value)
            return it.subList(idx + 1, it.size - 1)
        }
        return ArrayList()
    }

    /**
     * [updatePlaylist] is for task number 3 to update the playlist.
     * There are two different behavior for updating the playlist.
     * Maybe in future we want to add more users that have other behaviors.
     * So instead of using if conditions There is [IMusicUser] that specify a
     * simple user and and the behaviors that we should define for this user.
     * You can use PremiumMusicUser, NormalMusicUser, or implement your own user.
     */
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