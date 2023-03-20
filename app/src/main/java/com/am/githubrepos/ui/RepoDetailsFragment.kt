package com.am.githubrepos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.am.githubrepos.databinding.FragmentRepoDetailsBinding
import com.am.githubrepos.ui.viemodels.ReposViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

@AndroidEntryPoint
class RepoDetailsFragment : Fragment() {

    private var parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    private var formatter = SimpleDateFormat("dd/MM/yyyy h:mm a")

    private lateinit var binding: FragmentRepoDetailsBinding
    private val reposViewModel: ReposViewModel by viewModels()
    private val args: RepoDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepoDetailsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        val view = binding.root
        val path = args.repoFullName
        //Hide the layout and show progressbar
        binding.detailsLayoutContent.visibility = View.GONE
        binding.detailsPbRepos.visibility = View.VISIBLE
        reposViewModel.getSingleRepo(path)
        observeViewModel()
        return view
    }

    private fun observeViewModel() {
        reposViewModel.singleRepoResponse.observe(viewLifecycleOwner) {
            //Hide the progress bar
            binding.detailsPbRepos.visibility = View.GONE
            //Display the content
            binding.detailsImgLgUserProfile.load(it.owner.avatarUrl)
            binding.detailsTvRepoFullName.text = it.fullName
            binding.detailsTvStargazersCount.text = it.stargazers_count.toString()
            val creationDate = formatter.format(parser.parse(it.creationDate))
            binding.detailsTvCreationDate.text = creationDate
            binding.detailsLayoutContent.visibility = View.VISIBLE
        }
        //Observer Error Message -> Snackbar
        reposViewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(binding.root, it, Snackbar.LENGTH_INDEFINITE)
                .setAction("Reload", View.OnClickListener() {
                    binding.detailsLayoutContent.visibility = View.GONE
                    binding.detailsPbRepos.visibility = View.VISIBLE
                    reposViewModel.getSingleRepo(args.repoFullName)
                }).show()
        }
    }
}