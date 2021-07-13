package ir.mag.interview.balad.musiccontroller.controller.music.state

import ir.mag.interview.balad.musiccontroller.model.Track

/**
 * [IMusicState] is our interface for state design pattern.
 * Each one of our state should implement this interface.
 */
interface IMusicState {
    fun play(): Track?
    fun pause(time: Long): Track?
}