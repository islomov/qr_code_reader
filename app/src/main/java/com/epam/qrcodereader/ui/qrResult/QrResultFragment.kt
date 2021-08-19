package com.epam.qrcodereader.ui.qrResult

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.epam.qrcodereader.common.BaseFragment
import com.epam.qrcodereader.databinding.FragmentQrResultBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val ARG_RESULT = "arg_result"

class QrResultFragment : BaseFragment<FragmentQrResultBinding>() {

    val viewModel: QrResultViewModel by viewModel()

    private var result: String? = null

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentQrResultBinding
        get() = FragmentQrResultBinding::inflate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            result = it.getString(ARG_RESULT)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.tvResult?.text = result
        observeViewModel()
    }

    fun observeViewModel() {
        viewModel.uiEvent.observe(viewLifecycleOwner) { uiEvent ->
            when (uiEvent) {
                is UIEvent.UIEventSuccess -> Log.i("Islomov","Uploaded success")
                is UIEvent.UIEventError -> Log.i("Islomov","Upload fail")
                is UIEvent.UIEventLoading -> Log.i("Islomov","In progress")
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param: String?) =
            QrResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_RESULT, param)
                }
            }
    }

}