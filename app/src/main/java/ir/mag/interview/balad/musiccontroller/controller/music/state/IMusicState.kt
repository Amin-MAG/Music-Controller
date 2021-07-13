package ir.mag.interview.balad.musiccontroller.controller.music.state

import ir.mag.interview.balad.musiccontroller.model.Track

interface IMusicState {
    fun play(): Track?
    fun pause(time: Long): Track?
    fun stop(): Track?
}