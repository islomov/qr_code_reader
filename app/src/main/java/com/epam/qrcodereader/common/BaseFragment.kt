package com.epam.qrcodereader.common

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T: ViewBinding>: Fragment() {

    protected val binding: T?
        get() = _binding

    private var _binding: T? = null
    private var onPermission: OnPermission? = null
    protected var activity: MainActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as? MainActivity
    }

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> T

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        state: Bundle?
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return requireNotNull(_binding).root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val permReqLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }
            if (granted) {
                onPermission?.onGranted()
            } else {
                onPermission?.onDenied()
            }
        }

    private fun hasPermissions (permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    fun setPermission(onPermission: OnPermission, permissions: Array<String>){
        this.onPermission = onPermission
        if (hasPermissions(permissions)) {
            onPermission.onGranted()
        } else {
            permReqLauncher.launch(permissions)
        }
    }

    interface OnPermission{
        fun onGranted()
        fun onDenied()
    }


}