package ir.mag.interview.balad.musiccontroller

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import ir.mag.interview.balad.musiccontroller.controller.music.MusicController
import ir.mag.interview.balad.musiccontroller.controller.music.state.PauseState
import ir.mag.interview.balad.musiccontroller.controller.music.state.PlayingState
import ir.mag.interview.balad.musiccontroller.controller.music.user.NormalMusicUser
import ir.mag.interview.balad.musiccontroller.controller.music.user.PremiumMusicUser
import ir.mag.interview.balad.musiccontroller.model.Track
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MusicControllerTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val testTrack01 = Track(1, "Rescue Me", "Rescue Me", "One Republic", 2 * 60 + 38)
    private val testTrack02 = Track(2, "Notepad", "Mansion", "NF", 3 * 60 + 34)
    private val testTrack03 = Track(3, "When I Grow Up", "The Search", "NF", 3 * 60 + 16)
    private val testTrack04 = Track(4, "Nobody's Love", "Nobody's Love", "Maroon 5", 4 * 60 + 44)
    private val testTrack05 = Track(5, "LOST", "LOST", "NF", 3 * 60 + 12)
    private val testTrack06 = Track(6, "All I Want", "The Kodaline", "Kodaline", 1 * 60 + 34)
    private val testTrack07 = Track(7, "Way Down We Go", "Way Down We Go", "KALEO", 2 * 60 + 47)
    private val testTrack08 = Track(8, "Between Bars", "Either/Or", "Elliot Smith", 2 * 60 + 55)
    private val testTrack09 = Track(9, "The A Team", "The A Team", "Ed Sheeran", 2 * 60 + 16)
    private val testTrack10 = Track(10, "So Close", "So Close", "Olafur Arnalds", 3 * 60 + 52)

    private val playList1: List<Track> = listOf(
        testTrack01,
        testTrack02,
        testTrack03,
        testTrack04,
        testTrack05,
        testTrack06,
    )
    private val playList2: List<Track> = listOf(
        testTrack04,
        testTrack05,
        testTrack06,
        testTrack07,
        testTrack08,
        testTrack09,
        testTrack10,
    )
    private val playList3: List<Track> = listOf(
        testTrack01,
        testTrack03,
        testTrack04,
        testTrack05,
        testTrack06,
        testTrack09,
        testTrack10,
    )
    private val playList4: List<Track> = listOf(
        testTrack01,
        testTrack09,
        testTrack10,
    )

    @Test
    fun playForInvalidPlaylist() {
        val controller = MusicController(NormalMusicUser(), ArrayList())
        val isPlayed = controller.playOrResume()

        assertThat(isPlayed).isNull()
    }

    @Test
    fun playForValidPlaylist() {
        val controller = MusicController(NormalMusicUser(), playList1)
        val isPlayed = controller.playOrResume()

        assertThat(isPlayed).isNotNull()
    }

    @Test
    fun getPlayingTrack() {
        val controller = MusicController(NormalMusicUser(), playList1)
        assertThat(controller.playingTrack.value).isEqualTo(null)

        val isPlayed = controller.playOrResume()
        assertThat(isPlayed).isNotNull()

        val playingTrack = controller.playingTrack.value
        assertThat(playingTrack).isEqualTo(playList1[0])
    }


    @Test
    fun getNextTracks() {
        val controller = MusicController(NormalMusicUser(), playList2)
        assertThat(controller.playingTrack.value).isEqualTo(null)

        val isPlayed = controller.playOrResume()
        assertThat(isPlayed).isNotNull()

        val playingTrack = controller.playingTrack.value
        assertThat(playingTrack).isEqualTo(playList2[0])

        val nextTracks = controller.getNextTracks()
        assertThat(nextTracks).isEqualTo(playList2.subList(1, playList2.size - 1))
    }

    @Test
    fun updateInvalidPlaylistForNormalUser() {
        val controller = MusicController(NormalMusicUser(), playList2)
        assertThat(controller.playingTrack.value).isEqualTo(null)

        val isPlayed = controller.playOrResume()
        assertThat(isPlayed).isNotNull()

        val playingTrack = controller.playingTrack.value
        assertThat(playingTrack).isEqualTo(playList2[0])

        val isReplaced = controller.updatePlaylist(playList4)
        assertThat(isReplaced).isFalse()
    }

    @Test
    fun updateValidPlaylistForNormalUser() {
        val controller = MusicController(NormalMusicUser(), playList2)
        assertThat(controller.playingTrack.value).isEqualTo(null)

        val isPlayed = controller.playOrResume()
        assertThat(isPlayed).isNotNull()

        val playingTrack = controller.playingTrack.value
        assertThat(playingTrack).isEqualTo(playList2[0])

        val isReplaced = controller.updatePlaylist(playList3)
        assertThat(isReplaced).isTrue()

        val nextTracks = controller.getNextTracks()
        assertThat(nextTracks).isNotEqualTo(playList3.subList(1, playList3.size - 1))
    }

    @Test
    fun updateInvalidPlaylistForPremiumUser() {
        val controller = MusicController(PremiumMusicUser(), playList1)
        assertThat(controller.playingTrack.value).isEqualTo(null)

        val isReplaced = controller.updatePlaylist(ArrayList())
        assertThat(isReplaced).isFalse()
    }

    @Test
    fun updateValidPlaylistForPremiumUser() {
        val controller = MusicController(PremiumMusicUser(), playList1)
        assertThat(controller.playingTrack.value).isEqualTo(null)

        val isReplaced = controller.updatePlaylist(playList4)
        assertThat(isReplaced).isTrue()

        val playingTrack = controller.playingTrack.value
        assertThat(playingTrack).isEqualTo(playList4[0])

        val nextTracks = controller.getNextTracks()
        assertThat(nextTracks).isEqualTo(playList4.subList(1, playList4.size - 1))
    }

    @Test
    fun pauseValidWhilePlaying() {
        val controller = MusicController(NormalMusicUser(), playList2)
        assertThat(controller.playingTrack.value).isEqualTo(null)

        var isPlayed = controller.playOrResume()
        assertThat(isPlayed).isNotNull()

        var playingTrack = controller.playingTrack.value
        assertThat(playingTrack).isEqualTo(playList2[0])
        assertThat(controller.state).isInstanceOf(PlayingState::class.java)

        var isPaused = controller.pause(30)
        assertThat(isPaused).isTrue()

        var pausedTrack = controller.playingTrack.value
        assertThat(pausedTrack?.progress).isEqualTo(30)
        assertThat(controller.state).isInstanceOf(PauseState::class.java)

        isPlayed = controller.playOrResume()
        assertThat(isPlayed).isNotNull()

        playingTrack = controller.playingTrack.value
        assertThat(playingTrack).isEqualTo(playList2[0])
        assertThat(controller.state).isInstanceOf(PlayingState::class.java)

        isPaused = controller.pause(79)
        assertThat(isPaused).isTrue()

        pausedTrack = controller.playingTrack.value
        assertThat(pausedTrack?.progress).isEqualTo(79)
        assertThat(controller.state).isInstanceOf(PauseState::class.java)
    }

    @Test
    fun pauseInvalidWhilePlaying() {
        val controller = MusicController(NormalMusicUser(), playList2)
        assertThat(controller.playingTrack.value).isEqualTo(null)

        val isPlayed = controller.playOrResume()
        assertThat(isPlayed).isNotNull()

        val playingTrack = controller.playingTrack.value
        assertThat(playingTrack).isEqualTo(playList2[0])
        assertThat(controller.state).isInstanceOf(PlayingState::class.java)

        val isPaused = controller.pause(10 * 60)
        assertThat(isPaused).isFalse()
        assertThat(controller.state).isEqualTo(controller.playingState)
    }

    @Test
    fun pauseWhilePause() {
        val controller = MusicController(NormalMusicUser(), playList2)
        assertThat(controller.playingTrack.value).isEqualTo(null)

        val isPlayed = controller.playOrResume()
        assertThat(isPlayed).isNotNull()

        val playingTrack = controller.playingTrack.value
        assertThat(playingTrack).isEqualTo(playList2[0])
        assertThat(controller.state).isInstanceOf(PlayingState::class.java)

        val isPaused = controller.pause(3)
        assertThat(isPaused).isTrue()
        assertThat(controller.state).isEqualTo(controller.pauseState)

        val isPausedAgain = controller.pause(12)
        assertThat(isPausedAgain).isFalse()
        assertThat(controller.state).isEqualTo(controller.pauseState)
    }

    @Test
    fun stopWhilePlaying() {
        val controller = MusicController(NormalMusicUser(), playList2)
        assertThat(controller.playingTrack.value).isEqualTo(null)

        val isPlayed = controller.playOrResume()
        assertThat(isPlayed).isNotNull()

        var playingTrack = controller.playingTrack.value
        assertThat(playingTrack).isEqualTo(playList2[0])
        assertThat(controller.state).isInstanceOf(PlayingState::class.java)

        val isStopped = controller.stop()
        assertThat(isStopped).isTrue()
        assertThat(controller.state).isEqualTo(controller.pauseState)

        playingTrack = controller.playingTrack.value
        assertThat(playingTrack?.progress).isEqualTo(0)
    }

    @Test
    fun stopWhilePause() {
        val controller = MusicController(NormalMusicUser(), playList2)
        assertThat(controller.playingTrack.value).isEqualTo(null)

        val isPlayed = controller.playOrResume()
        assertThat(isPlayed).isNotNull()

        val playingTrack = controller.playingTrack.value
        assertThat(playingTrack).isEqualTo(playList2[0])
        assertThat(controller.state).isInstanceOf(PlayingState::class.java)

        val isPaused = controller.pause(3)
        assertThat(isPaused).isTrue()
        assertThat(controller.state).isEqualTo(controller.pauseState)

        val isStopped = controller.stop()
        assertThat(isStopped).isTrue()
        assertThat(controller.state).isEqualTo(controller.pauseState)
    }

    @Test
    fun resumeWhilePlaying() {
        val controller = MusicController(NormalMusicUser(), playList2)
        assertThat(controller.playingTrack.value).isEqualTo(null)

        val isPlayed = controller.playOrResume()
        assertThat(isPlayed).isNotNull()

        val playingTrack = controller.playingTrack.value
        assertThat(playingTrack).isEqualTo(playList2[0])
        assertThat(controller.state).isInstanceOf(PlayingState::class.java)

        val isResumed = controller.playOrResume()
        assertThat(isResumed).isNull()
        assertThat(controller.state).isInstanceOf(PlayingState::class.java)
    }

    @Test
    fun resumeWhilePause() {
        val controller = MusicController(NormalMusicUser(), playList1)
        assertThat(controller.playingTrack.value).isEqualTo(null)

        val isPlayed = controller.playOrResume()
        assertThat(isPlayed).isNotNull()

        var playingTrack = controller.playingTrack.value
        assertThat(playingTrack).isEqualTo(playList1[0])
        assertThat(controller.state).isInstanceOf(PlayingState::class.java)

        val isPaused = controller.pause(3)
        assertThat(isPaused).isTrue()
        assertThat(controller.state).isEqualTo(controller.pauseState)

        val isResumed = controller.playOrResume()
        assertThat(isResumed).isNotNull()

        playingTrack = controller.playingTrack.value
        assertThat(playingTrack?.progress).isEqualTo(3)
        assertThat(controller.state).isEqualTo(controller.playingState)

        val isStopped = controller.stop()
        assertThat(isStopped).isTrue()
        assertThat(controller.state).isEqualTo(controller.pauseState)

        val isResumedAgain = controller.playOrResume()
        assertThat(isResumedAgain).isNotNull()

        playingTrack = controller.playingTrack.value
        assertThat(playingTrack?.progress).isEqualTo(0)
        assertThat(controller.state).isEqualTo(controller.playingState)
    }

    @Test
    fun skipTwoTrack() {
        val controller = MusicController(NormalMusicUser(), playList1)
        assertThat(controller.playingTrack.value).isEqualTo(null)

        val isPlayed = controller.playOrResume()
        assertThat(isPlayed).isNotNull()

        var playingTrack = controller.playingTrack.value
        assertThat(playingTrack).isEqualTo(playList1[0])
        assertThat(controller.state).isInstanceOf(PlayingState::class.java)

        var isSkipped = controller.skip()
        assertThat(isSkipped).isNotNull()
        assertThat(isSkipped).isEqualTo(playList1[1])

        playingTrack = controller.playingTrack.value
        assertThat(playingTrack).isEqualTo(playList1[1])
        assertThat(playingTrack?.progress).isEqualTo(0)
        assertThat(controller.state).isInstanceOf(PlayingState::class.java)

        val isPaused = controller.pause(3)
        assertThat(isPaused).isTrue()
        assertThat(controller.state).isEqualTo(controller.pauseState)

        isSkipped = controller.skip()
        assertThat(isSkipped).isNotNull()
        assertThat(isSkipped).isEqualTo(playList1[2])

        playingTrack = controller.playingTrack.value
        assertThat(playingTrack).isEqualTo(playList1[2])
        assertThat(playingTrack?.progress).isEqualTo(0)
        assertThat(controller.state).isInstanceOf(PauseState::class.java)
    }

    @Test
    fun backForNormalUser() {
        val controller = MusicController(NormalMusicUser(), playList1)
        assertThat(controller.playingTrack.value).isEqualTo(null)

        val isPlayed = controller.playOrResume()
        assertThat(isPlayed).isNotNull()

        val playingTrack = controller.playingTrack.value
        assertThat(playingTrack).isEqualTo(playList1[0])
        assertThat(controller.state).isInstanceOf(PlayingState::class.java)

        val isBacked = controller.back()
        assertThat(isBacked).isNull()
    }

    @Test
    fun backForPremiumUser() {
        val controller = MusicController(PremiumMusicUser(), playList1)
        assertThat(controller.playingTrack.value).isEqualTo(null)

        val isPlayed = controller.playOrResume()
        assertThat(isPlayed).isNotNull()

        var playingTrack = controller.playingTrack.value
        assertThat(playingTrack).isEqualTo(playList1[0])
        assertThat(controller.state).isInstanceOf(PlayingState::class.java)

        val isSkipped = controller.skip()
        assertThat(isSkipped).isNotNull()
        assertThat(isSkipped).isEqualTo(playList1[1])

        playingTrack = controller.playingTrack.value
        assertThat(playingTrack).isEqualTo(playList1[1])
        assertThat(playingTrack?.progress).isEqualTo(0)
        assertThat(controller.state).isInstanceOf(PlayingState::class.java)

        val isBacked = controller.back()
        assertThat(isBacked).isNotNull()
        assertThat(isBacked).isEqualTo(playList1[0])

        playingTrack = controller.playingTrack.value
        assertThat(playingTrack).isEqualTo(playList1[0])
        assertThat(playingTrack?.progress).isEqualTo(0)
        assertThat(controller.state).isInstanceOf(PlayingState::class.java)

        val isBackedAgain = controller.back()
        assertThat(isBackedAgain).isNotNull()
        assertThat(isBackedAgain).isEqualTo(playList1[playList1.size - 1])

        playingTrack = controller.playingTrack.value
        assertThat(playingTrack).isEqualTo(playList1[playList1.size - 1])
        assertThat(playingTrack?.progress).isEqualTo(0)
        assertThat(controller.state).isInstanceOf(PlayingState::class.java)
    }

    @Test
    fun addTrackInstantlyForNormalUser() {
        val controller = MusicController(NormalMusicUser(), playList1)
        assertThat(controller.playingTrack.value).isEqualTo(null)

        val isPlayed = controller.playOrResume()
        assertThat(isPlayed).isNotNull()

        val playingTrack = controller.playingTrack.value
        assertThat(playingTrack).isEqualTo(playList1[0])
        assertThat(controller.state).isInstanceOf(PlayingState::class.java)

        val isAdded = controller.addTrackInstantly(testTrack10)
        assertThat(isAdded).isFalse()
    }

    @Test
    fun addTrackInstantlyForPremiumUser() {
        val controller = MusicController(PremiumMusicUser(), playList1)
        assertThat(controller.playingTrack.value).isEqualTo(null)

        val isPlayed = controller.playOrResume()
        assertThat(isPlayed).isNotNull()

        var playingTrack = controller.playingTrack.value
        assertThat(playingTrack).isEqualTo(playList1[0])
        assertThat(controller.state).isInstanceOf(PlayingState::class.java)

        val isAdded = controller.addTrackInstantly(testTrack10)
        assertThat(isAdded).isTrue()

        val isSkipped = controller.skip()
        assertThat(isSkipped).isNotNull()
        assertThat(isSkipped).isEqualTo(testTrack10)

        playingTrack = controller.playingTrack.value
        assertThat(playingTrack).isEqualTo(testTrack10)
        assertThat(playingTrack?.progress).isEqualTo(0)
        assertThat(controller.state).isInstanceOf(PlayingState::class.java)

        val isSkippedAgain = controller.skip()
        assertThat(isSkippedAgain).isNotNull()
        assertThat(isSkippedAgain).isEqualTo(playList1[1])

        playingTrack = controller.playingTrack.value
        assertThat(playingTrack).isEqualTo(playList1[1])
        assertThat(playingTrack?.progress).isEqualTo(0)
        assertThat(controller.state).isInstanceOf(PlayingState::class.java)
    }
}