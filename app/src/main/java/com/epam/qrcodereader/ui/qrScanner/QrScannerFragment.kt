package com.epam.qrcodereader.ui.qrScanner

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.epam.qrcodereader.common.BaseFragment
import com.epam.qrcodereader.databinding.FragmentQrScannerBinding
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class QrScannerFragment : BaseFragment<FragmentQrScannerBinding>() {

    private var camera: Camera? = null
    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var imageCapture: ImageCapture? = null

    private val cameraExecutor = Executors.newSingleThreadExecutor()

    private var displayId: Int = -1


    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentQrScannerBinding
        get() = FragmentQrScannerBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.viewFinder?.post {
            displayId = binding?.viewFinder?.display?.displayId ?: -1

            bindCameraUseCases()
        }

    }

    private fun bindCameraUseCases() {
        val metrics = DisplayMetrics().also { binding?.viewFinder?.display?.getRealMetrics(it) }

        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)

        val rotation = binding?.viewFinder?.display?.rotation ?: 0

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            preview = Preview.Builder()
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation)
                .build()

            preview?.setSurfaceProvider(binding?.viewFinder?.surfaceProvider)

            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation)
                .build()

            imageAnalyzer = ImageAnalysis.Builder()
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, QrCodeAnalyzer(onQrCodes = {list ->
                        if (list.isNotEmpty()) {
                            val value = list[0].rawValue

                            if (value != null) {
                                activity?.openScannerResult(value)
                            } else {
                                Toast.makeText(requireContext(),"QR Code is empty",Toast.LENGTH_LONG).show()
                            }

                            cameraExecutor.shutdownNow()
                        }
                    }, onFailure = {
                        Log.e("Islomov","Error on imageanalyzer")
                    }))
                }

            cameraProvider.unbindAll()

            try {
                camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer).also {

                    preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(binding?.viewFinder?.surfaceProvider)
                    }
                }

            } catch (exc: Exception) {
                Log.e("Islomov","Use case binding failed:${exc}")
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)
        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }


    private class QrCodeAnalyzer(val onQrCodes: (List<Barcode>) -> Unit,
                                 val onFailure: () -> Unit) : ImageAnalysis.Analyzer {
        override fun analyze(image: ImageProxy) {
            if (image.image == null) return

            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .build()

            val detector = BarcodeScanning.getClient(options)

            try {
                val mediaImage = image.image
                if (mediaImage != null) {
                    val visionImage = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)
                    detector.process(visionImage)
                        .addOnSuccessListener { barcodes ->
                            onQrCodes(barcodes)
                        }.addOnFailureListener { e ->
                            onFailure
                            Log.e("Islomov","QR Code read error:${e}")
                        }
                        .addOnCompleteListener {
                            image.close()
                        }
                }
            } catch (e: Exception) {
                Log.e("Islomov","FirebaseVisionImage error:${e}")
            }
        }
    }

    override fun onPause() {
        super.onPause()

        cameraExecutor.shutdown()
    }


    companion object {

        @JvmStatic
        fun newInstance() = QrScannerFragment()

        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }

}