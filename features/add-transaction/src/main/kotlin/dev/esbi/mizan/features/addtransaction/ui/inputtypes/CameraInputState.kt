package dev.esbi.mizan.features.addtransaction.ui.inputtypes

data class CameraInputState(
    val isScanning: Boolean = false,
    val cameraRecognitionResult: String = "",
    val cameraPermissionGranted: Boolean = false,
    val receiptScanText: String = "",
    val lastRecognizedAmount: Double = 0.0,
    val cameraScanError: String? = null,
    val qrtext: String? = null
)