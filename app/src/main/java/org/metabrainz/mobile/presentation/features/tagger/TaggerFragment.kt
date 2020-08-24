package org.metabrainz.mobile.presentation.features.tagger

import android.graphics.Bitmap
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.simplecityapps.ktaglib.AudioFile
import kotlinx.android.synthetic.main.fragment_tagger.*
import kotlinx.android.synthetic.main.list_item_document.*
import kotlinx.android.synthetic.main.list_item_document.view.*
import org.metabrainz.mobile.R
import org.metabrainz.mobile.databinding.FragmentTaggerBinding
import java.util.concurrent.TimeUnit

class TaggerFragment : Fragment() {


    internal var expandableListView: ExpandableListView?=null
    internal var adapter:ExpandableListAdapter?= null
    internal var titleList: List<String>?= null
    private lateinit var binding: FragmentTaggerBinding
    private lateinit var viewmodel:KotlinTaggerViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentTaggerBinding.inflate(inflater)
        viewmodel = activity?.run {
            ViewModelProvider(this).get(KotlinTaggerViewModel::class.java)
        }!!
        
        binding.cardView1.setOnClickListener {
            if(binding.root.findViewById<CardView>(R.id.dropdownItems1).visibility == View.GONE){
                Log.i("dropdownitemsVisibilty", binding.root.findViewById<CardView>(R.id.dropdownItems1).visibility.toString())
                TransitionManager.beginDelayedTransition(binding.cardView1, AutoTransition())
                binding.root.findViewById<CardView>(R.id.dropdownItems1).visibility = View.VISIBLE
                binding.arrowBtn1.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_up_24)
            }else{
                Log.i("dropdownitemsVisibilty", binding.root.findViewById<CardView>(R.id.dropdownItems1).visibility.toString())
                TransitionManager.beginDelayedTransition(binding.cardView1, AutoTransition())
                binding.root.findViewById<CardView>(R.id.dropdownItems1).visibility = View.GONE
                binding.arrowBtn1.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_down_24)
            }
        }

        binding.cardView2.setOnClickListener {
            if(binding.root.findViewById<CardView>(R.id.dropdownItems2).visibility == View.GONE){
                Log.i("dropdownitemsVisibilty", binding.root.findViewById<CardView>(R.id.dropdownItems2).visibility.toString())
                TransitionManager.beginDelayedTransition(binding.cardView2, AutoTransition())
                binding.root.findViewById<CardView>(R.id.dropdownItems2).visibility = View.VISIBLE
                binding.arrowBtn1.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_up_24)
            }else{
                Log.i("dropdownitemsVisibilty", binding.root.findViewById<CardView>(R.id.dropdownItems2).visibility.toString())
                TransitionManager.beginDelayedTransition(binding.cardView2, AutoTransition())
                binding.root.findViewById<CardView>(R.id.dropdownItems2).visibility = View.GONE
                binding.arrowBtn2.setBackgroundResource(R.drawable.ic_baseline_arrow_drop_down_24)
            }
        }
//        expandableListView = binding.expandableList
//        if (expandableListView != null) {
//            val listData = ExpandableListData()
//            titleList = ArrayList(listData?.data?.keys)
//            adapter = ExpandableListAdapter(requireContext(), titleList as ArrayList<String>, listData?.data)
//            expandableListView!!.setAdapter(adapter)
//            expandableListView!!.setOnGroupExpandListener { groupPosition -> Toast.makeText(requireContext(), (titleList as ArrayList<String>)[groupPosition] + " List Expanded.", Toast.LENGTH_SHORT).show() }
//            expandableListView!!.setOnGroupCollapseListener { groupPosition -> Toast.makeText(requireContext(), (titleList as ArrayList<String>)[groupPosition] + " List Collapsed.", Toast.LENGTH_SHORT).show() }
//        }

        viewmodel.TaglibFetchedMetadata.observe(viewLifecycleOwner, Observer {
            TFM ->  setTags(TFM)
        })

        viewmodel.bitmap.observe(viewLifecycleOwner, Observer{
            bitmapforalbum -> setAlbumArt(bitmapforalbum)
        } )
        return binding.root
    }
    fun setTags(metadata:AudioFile?){
        var size = if(metadata?.size==null) "null" else "${"%.2f".format((metadata?.size / 1024f / 1024f))}MB"
        binding.root.findViewById<TextView>(R.id.title).text = metadata?.title
        binding.root.findViewById<TextView>(R.id.track).text = metadata?.track.toString()
        binding.root.findViewById<TextView>(R.id.disc).text = metadata?.disc.toString()
        binding.root.findViewById<TextView>(R.id.duration).text = metadata?.duration?.toHms()
        binding.root.findViewById<TextView>(R.id.artist).text = metadata?.artist.toString()
        binding.root.findViewById<TextView>(R.id.album).text = metadata?.disc.toString()
        binding.root.findViewById<TextView>(R.id.year).text = metadata?.date.toString()
        binding.root.findViewById<TextView>(R.id.disc).text = metadata?.disc.toString()
        binding.root.findViewById<TextView>(R.id.size).text = size
    }

    fun setAlbumArt(bitmap: Bitmap){
        var image = binding.root.findViewById<ImageView>(R.id.album_art)
        image.setImageBitmap(Bitmap.createScaledBitmap(bitmap, image.getWidth(),
                image.getHeight(), false));
    }

    fun Int.toHms(defaultValue: String?= null):String {
        if(this==0 && defaultValue!=null)
            return defaultValue
        val hours = TimeUnit.MILLISECONDS.toHours(this.toLong())
        val minutes = TimeUnit.MILLISECONDS.toMinutes(this.toLong())% TimeUnit.HOURS.toMinutes(1)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(this.toLong())% TimeUnit.MINUTES.toSeconds(1)

        return if (hours == 0L) {
            String.format("%2d:%02d", minutes, seconds)
        } else {
            String.format("%2d:%02d:%02d", hours, minutes, seconds)
        }
    }



}