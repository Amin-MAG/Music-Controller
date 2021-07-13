package ir.mag.interview.balad.musiccontroller.controller.music.state

import ir.mag.interview.balad.musiccontroller.controller.music.MusicController
import ir.mag.interview.balad.musiccontroller.model.Track

class PlayingState(private val musicController: MusicController) : IMusicState {
    override fun play(): Track? {
        // Do nothing
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

    override fun stop(): Track? {
        musicController.playingTrack.value?.let {
            it.progress = 0
            musicController.state = musicController.pauseState
            return it
        }
        return null
    }
}