package ir.mag.interview.balad.musiccontroller.controller.music.state

import ir.mag.interview.balad.musiccontroller.controller.music.MusicController
import ir.mag.interview.balad.musiccontroller.model.Track

class PauseState(private val musicController: MusicController) : IMusicState {
    override fun play(): Track? {
        musicController.playlist.value?.let {
            if (it.isEmpty()) return null
            // play the first track in the play list
            musicController.state = musicController.playingState
            return it[0]
        }
        return null
    }

    override fun pause(time: Long): Track? {
        // Do nothing
        return null
    }

    override fun stop(): Track? {
        musicController.playingTrack.value?.let {
            it.progress = 0
            return it
        }
        return null
    }
}