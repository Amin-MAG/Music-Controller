package ir.mag.interview.balad.musiccontroller.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ir.mag.interview.balad.musiccontroller.R
import ir.mag.interview.balad.musiccontroller.controller.music.MusicController
import ir.mag.interview.balad.musiccontroller.controller.music.user.NormalMusicUser
import ir.mag.interview.balad.musiccontroller.controller.music.user.PremiumMusicUser
import ir.mag.interview.balad.musiccontroller.databinding.ActivityMainBinding
import ir.mag.interview.balad.musiccontroller.model.Track

class MainActivity : AppCompatActivity() {

    private val adapter: TracksRecyclerAdapter = TracksRecyclerAdapter()
    private val sampleTracks: List<Track> = listOf(
        Track(1, "Rescue Me", "Rescue Me", "One Republic", 2 * 60 + 38),
        Track(2, "Notepad", "Mansion", "NF", 3 * 60 + 34),
        Track(3, "When I Grow Up", "The Search", "NF", 3 * 60 + 16),
        Track(4, "Nobody's Love", "Nobody's Love", "Maroon 5", 4 * 60 + 44),
        Track(5, "LOST", "LOST", "NF", 3 * 60 + 12),
        Track(6, "All I Want", "The Kodaline", "Kodaline", 1 * 60 + 34),
        Track(7, "Way Down We Go", "Way Down We Go", "KALEO", 2 * 60 + 47),
        Track(8, "Between Bars", "Either/Or", "Elliot Smith", 2 * 60 + 55),
        Track(9, "The A Team", "The A Team", "Ed Sheeran", 2 * 60 + 16),
        Track(10, "So Close", "So Close", "Olafur Arnalds", 3 * 60 + 52)
    )
    private val aSampleControllerForNormalUser = MusicController(NormalMusicUser(), sampleTracks)
    private val aSampleControllerForPremiumUser = MusicController(PremiumMusicUser(), sampleTracks)
    private var controller = aSampleControllerForNormalUser

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setAdapter()
        setObserversForController()
        setListeners()

    }

    private fun setListeners() {
        binding.isPremium.setOnCheckedChangeListener { _, isPremium ->
            controller =
                if (isPremium) aSampleControllerForPremiumUser else aSampleControllerForNormalUser
            setObserversForController()
        }

        binding.playPauseButton.setOnClickListener {
            controller.playOrResume()
        }

        binding.nextButton.setOnClickListener {
            controller.skip()
        }

        binding.previousButton.setOnClickListener {
            controller.back()
        }
    }

    private fun setObserversForController() {
        controller.playingTrack.observe(this, {
            it?.let {
                binding.titleText.text = it.title
                binding.artistText.text = it.artistName
                binding.progress.text = it.progress.toString()
            }
        })
    }

    private fun setAdapter() {
        adapter.tracks = sampleTracks
        binding.recycler.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    companion object {
        const val TAG = "activity.main"
    }
}