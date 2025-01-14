package com.example.playlistmaker

class SearchHistory {

    companion object {

        val items = ArrayList<Track>()

        fun read() {
            items.clear()
            items += SharedPrefUtils.getSearchHistory()
        }

        fun clear() {
            items.clear()
        }

        fun update(newTrack: Track) {
            items.removeAll { it.trackId == newTrack.trackId }
            items.add(0, newTrack)
            while (items.size > 10) {
                items.removeLast()
            }
        }

    }

}