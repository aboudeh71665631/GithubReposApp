package com.am.githubrepos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.am.githubrepos.data.ConnectivityLiveData
import com.am.githubrepos.data.Constants
import com.am.githubrepos.databinding.FragmentAllReposBinding
import com.am.githubrepos.ui.adapters.ReposRVAdapter
import com.am.githubrepos.ui.viemodels.ReposViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllReposFragment : Fragment() {
    private lateinit var binding: FragmentAllReposBinding
    private val reposViewModel: ReposViewModel by viewModels()
    private val reposRVAdapter by lazy { ReposRVAdapter() }
    private lateinit var connectionStatus: ConnectivityLiveData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAllReposBinding.inflate(inflater, container, false)
        val view = binding.root

        checkConnection()
        initRecyclerView()
        getAllRepos()
        observeViewModel()

        return view
    }

    private fun checkConnection() {
        binding.layoutNoInternet.visibility = View.VISIBLE
        connectionStatus = ConnectivityLiveData(requireActivity().application)
        connectionStatus.observe(viewLifecycleOwner) {
            if (it) {
                //If connection is established
                binding.layoutNoInternet.visibility = View.GONE
                binding.recyclerReposList.visibility = View.GONE
                binding.progressRepos.visibility = View.VISIBLE
                getAllRepos()
            } else if (!it) {
                Toast.makeText(
                    requireContext(),
                    Constants.LOST_INTERNET_CONNECTION,
                    Toast.LENGTH_SHORT
                ).show()
                binding.progressRepos.visibility = View.GONE
                binding.layoutNoInternet.visibility = View.VISIBLE
                binding.recyclerReposList.visibility = View.GONE
            }
        }
    }

    private fun getAllRepos() {
        reposViewModel.getData().observe(viewLifecycleOwner) {
            if (it != null) {
                binding.progressRepos.visibility = View.GONE
                binding.recyclerReposList.visibility = View.VISIBLE
                reposRVAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }
    }

    private fun observeViewModel() {
        //Observer Error Message -> Snackbar
        reposViewModel.error.observe(viewLifecycleOwner) {
            binding.recyclerReposList.visibility = View.GONE
            binding.progressRepos.visibility = View.GONE
            Snackbar.make(binding.root, it, Snackbar.LENGTH_INDEFINITE)
                .setAction("Reload") {
                    binding.progressRepos.visibility = View.VISIBLE
                    getAllRepos()
                }.show()
        }
    }

    private fun initRecyclerView() {
        binding.recyclerReposList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = reposRVAdapter.apply {
                setOnItemClickedListener(object : ReposRVAdapter.OnItemClickedListener {
                    override fun onItemClick(fullName: String) {
                        val action =
                            AllReposFragmentDirections.actionAllReposFragmentToRepoDetailsFragment("repos/$fullName")
                        Navigation.findNavController(binding.root).navigate(action)
                    }
                })
            }
        }
    }


}