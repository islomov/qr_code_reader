package com.epam.qrcodereader.ui.button

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.epam.qrcodereader.common.BaseFragment
import com.epam.qrcodereader.databinding.FragmentButtonBinding

class ButtonFragment : BaseFragment<FragmentButtonBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentButtonBinding
        get() = FragmentButtonBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.btnScan?.setOnClickListener {
            startScan()
        }

    }

    fun startScan(){
        setPermission(object :OnPermission{
            override fun onGranted() {
                activity?.openScanner()
            }

            override fun onDenied() {
                Toast.makeText(requireContext(),"Permission denied",Toast.LENGTH_LONG).show()
            }
        }, arrayOf(Manifest.permission.CAMERA))
    }

    companion object {
        @JvmStatic
        fun newInstance() = ButtonFragment()
    }

}