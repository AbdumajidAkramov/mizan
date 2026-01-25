package dev.esbi.mizan.feature.newtransaction.amountinput.store.state

data class CameraInputState(
    val isScanning: Boolean = true,
    val cameraRecognitionResult: String = "",
    val cameraPermissionGranted: Boolean = false,
    val receiptScanText: String = "",
    val lastRecognizedAmount: Double = 0.0,
    val cameraScanError: String? = null,
)
