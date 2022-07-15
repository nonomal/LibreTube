package com.github.libretube.preferences

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.libretube.DONATE_URL
import com.github.libretube.GITHUB_URL
import com.github.libretube.PIPED_GITHUB_URL
import com.github.libretube.R
import com.github.libretube.WEBSITE_URL
import com.github.libretube.activities.SettingsActivity
import com.github.libretube.databinding.FragmentAboutBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class AboutFragment : Fragment() {
    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val settingsActivity = activity as SettingsActivity
        settingsActivity.changeTopBarText(getString(R.string.about))

        binding.website.setOnClickListener {
            openLinkFromHref(WEBSITE_URL)
        }
        binding.website.setOnLongClickListener {
            val text = context?.getString(R.string.website_summary)!!
            showSnackBar(text)
            true
        }

        binding.piped.setOnClickListener {
            openLinkFromHref(PIPED_GITHUB_URL)
        }
        binding.piped.setOnLongClickListener {
            val text = context?.getString(R.string.piped_summary)!!
            showSnackBar(text)
            true
        }

        binding.donate.setOnClickListener {
            openLinkFromHref(DONATE_URL)
        }
        binding.donate.setOnLongClickListener {
            val text = context?.getString(R.string.donate_summary)!!
            showSnackBar(text)
            true
        }

        binding.github.setOnClickListener {
            openLinkFromHref(GITHUB_URL)
        }
        binding.github.setOnLongClickListener {
            val text = context?.getString(R.string.contributing_summary)!!
            showSnackBar(text)
            true
        }

        binding.license.setOnClickListener {
            showLicense()
        }
        binding.license.setOnLongClickListener {
            val text = context?.getString(R.string.license_summary)!!
            showSnackBar(text)
            true
        }
    }

    private fun openLinkFromHref(link: String) {
        val uri = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW).setData(uri)
        startActivity(intent)
    }

    private fun showSnackBar(text: String) {
        val snackBar = Snackbar
            .make(binding.root, text, Snackbar.LENGTH_LONG)

        // prevent the text from being partially hidden
        snackBar.setTextMaxLines(3)

        snackBar.show()
    }

    private fun showLicense() {
        val assets = view?.context?.assets
        val licenseString = assets
            ?.open("gpl3.html")
            ?.bufferedReader()
            .use {
                it?.readText()
            }

        val licenseHtml = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(licenseString.toString(), 1)
        } else {
            Html.fromHtml(licenseString.toString())
        }

        MaterialAlertDialogBuilder(requireContext())
            .setPositiveButton(getString(R.string.okay)) { _, _ -> }
            .setMessage(licenseHtml)
            .create()
            .show()
    }
}
