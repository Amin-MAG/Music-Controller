package ir.mag.interview.balad.musiccontroller.controller.music.state

import ir.mag.interview.balad.musiccontroller.controller.music.MusicController
import ir.mag.interview.balad.musiccontroller.model.Track

class PauseState(private val musicController: MusicController) : IMusicState {
    override fun play(): Track? {
        musicController.playlist.value?.let {
            if (it.isEmpty()) return null
            musicController.state = musicController.playingState
            return if (musicController.playingTrack.value == null) {
                // play the first track in the play list
                it[0]
            } else {
                musicController.playingTrack.value
            }
        }
        return null
    }

    override fun pause(time: Long): Track? {
        // Do nothing for now
        return null
    }
}