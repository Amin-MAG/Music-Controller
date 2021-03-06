package ir.mag.interview.balad.musiccontroller.controller.music.state

import ir.mag.interview.balad.musiccontroller.controller.music.MusicController
import ir.mag.interview.balad.musiccontroller.model.Track


/**
 * [PlayingState]
 * In state design pattern we have an instance of the object. It is going to
 * be passed by constructor
 */
class PlayingState(private val musicController: MusicController) : IMusicState {
    override fun play(): Track? {
        // Do nothing for now
        return null
    }

    override fun pause(time: Long): Track? {
        musicController.playingTrack.value?.let {
            if (time > it.trackLength) return null
            it.progress = time
            musicController.state = musicController.pauseState
            return it
        }
        return null
    }
}