package ir.mag.interview.balad.musiccontroller.ui

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ir.mag.interview.balad.musiccontroller.R
import ir.mag.interview.balad.musiccontroller.databinding.ViewHolderTrackBinding
import ir.mag.interview.balad.musiccontroller.model.Track

class TracksRecyclerAdapter : RecyclerView.Adapter<TracksRecyclerAdapter.TrackViewHolder>() {
    companion object {
        private const val TAG = "Adapter.MyAppsVH"
    }

    private lateinit var activity: Activity

    var tracks: List<Track> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackViewHolder {
        activity = parent.context as Activity

        val binding: ViewHolderTrackBinding = DataBindingUtil.inflate(
            LayoutInflater.from(activity),
            R.layout.view_holder_track,
            parent,
            false
        )

        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    inner class TrackViewHolder(val binding: ViewHolderTrackBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Track) {
            binding.title.text = track.title
        }
    }
}