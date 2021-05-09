package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.repository.PictureOfDayRepository

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val database = getDatabase(requireActivity().applicationContext)
        val asteroidsRepository = AsteroidsRepository(database.asteroidsDao)
        val pictureOfDayRepository = PictureOfDayRepository(database.pictureOfDayDao)
        val viewModelFactory = MainViewModelFactory(asteroidsRepository, pictureOfDayRepository)
        val viewModel: MainViewModel by viewModels { viewModelFactory }
        this.viewModel = viewModel
        binding.viewModel = viewModel

        val clickListener = AsteroidClickListener { asteroid ->
            viewModel.onAsteroidClicked(asteroid)
        }
        val adapter = AsteroidsAdapter(clickListener)
        binding.asteroidRecycler.adapter = adapter

        viewModel.asteroids.observe(
            viewLifecycleOwner,
            Observer {
                adapter.submitList(it)
            }
        )

        viewModel.navigateToDetail.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                    viewModel.navigateToDetailComplete()
                }
            }
        )

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_week_menu -> viewModel.switchFilterType(AsteroidsFilterType.WEEK)
            R.id.show_today_menu -> viewModel.switchFilterType(AsteroidsFilterType.TODAY)
            R.id.show_all_menu -> viewModel.switchFilterType(AsteroidsFilterType.ALL)
        }
        return true
    }
}
