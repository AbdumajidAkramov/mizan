package dev.esbi.mizan.feature.newtransaction.inputtypes

import android.Manifest
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import dev.esbi.mizan.feature.newtransaction2.amountinput.widgets.PermissionDeniedScreen
import dev.esbi.mizan.feature.newtransaction2.amountinput.widgets.PermissionHandler
import dev.esbi.mizan.presentation.feature.temp.AddTransactionStore
import dev.esbi.mizan.ui.theme.MizanTheme
import dev.esbi.mizan.ui.theme.colors.MizanTheme
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun CameraInputStep(
    state: AddTransactionStore.State,
    accept: (AddTransactionStore.Intent) -> Unit,
) {

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun CameraInputStep(
    state: CameraInputState,
    onStartScanning: () -> Unit,
    onStopScanning: () -> Unit,
    onAmountExtracted: (Double) -> Unit,
    onScanResult: (String, Float) -> Unit,
    onQRCodeScanned: (String) -> Unit,
    onError: (String) -> Unit,
    onNext: () -> Unit
) {
    @OptIn(ExperimentalPermissionsApi::class)
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    PermissionHandler(
        permission = Manifest.permission.CAMERA,
        permissionTitle = "Camera Access",
        permissionDescription = "Camera permission is required to scan receipts and extract transaction amounts automatically. This helps you quickly add expenses by taking a picture of your receipt.",
        onPermissionGranted = {
            CameraInputStep(
                isScanning = state.isScanning,
                scannedText = state.qrtext ?: state.receiptScanText,
                recognizedAmount = state.lastRecognizedAmount,
                error = state.cameraScanError,
                onStartScanning = {
                    if (cameraPermissionState.status.isGranted) {
                        onStartScanning()
                    }
                },
                onStopScanning = onStopScanning,
                onAmountExtracted = onAmountExtracted,
                onScanResult = onScanResult,
                onQRCodeScanned = onQRCodeScanned,
                onError = onError,
                onNext = onNext
            )
        },
        onPermissionDenied = { permission ->
            PermissionDeniedScreen(
                permission = permission,
                onRequestAgain = { cameraPermissionState.launchPermissionRequest() }
            )
        }
    )
}

@Composable
internal fun CameraInputStep(
    isScanning: Boolean,
    scannedText: String,
    recognizedAmount: Double,
    error: String?,
    onStartScanning: () -> Unit,
    onStopScanning: () -> Unit,
    onAmountExtracted: (Double) -> Unit,
    onScanResult: (String, Float) -> Unit,
    onQRCodeScanned: (String) -> Unit,
    onError: (String) -> Unit,
    onNext: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(24.dp))

        // Title
        Text(
            text = "Scan Receipt",
            style = MizanTheme.typography.headingLg,
            color = MizanTheme.premium.text.primary,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(MizanTheme.premium.spacing.sm))

        Text(
            text = "Position the receipt within the frame",
            style = MizanTheme.typography.bodyMd,
            color = MizanTheme.premium.text.secondary,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(MizanTheme.premium.spacing.lg))

        // Camera Preview with Overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(horizontal = MizanTheme.premium.spacing.lg)
                .clip(RoundedCornerShape(MizanTheme.premium.radius.lg))
        ) {
            if (isScanning) {
                CameraPreview(
                    onQRCodeDetected = { qrCodeData ->
                        // Immediately notify the store about QR code detection
                        onQRCodeScanned(qrCodeData)

                        // Also provide scan result for backward compatibility
                        onScanResult(qrCodeData, 1.0f)

                        // Extract amount from QR code data
                        extractAmountFromText(qrCodeData)?.let { amount ->
                            onAmountExtracted(amount)
                        }
                    },
                    onError = onError
                )

                // Scanning Overlay
                ScanningOverlay()
            } else {
                // Placeholder when not scanning
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = MizanTheme.premium.colors.surface1,
                            shape = RoundedCornerShape(MizanTheme.premium.radius.lg)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (scannedText.isNotEmpty()) {
                            // Show detected QR code
                            Text(
                                text = "QR Code Detected!",
                                style = MizanTheme.typography.labelLg,
                                color = MizanTheme.premium.colors.success
                            )

                            Spacer(Modifier.height(MizanTheme.premium.spacing.sm))

                            Text(
                                text = scannedText,
                                style = MizanTheme.typography.bodySm,
                                color = MizanTheme.premium.text.secondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = MizanTheme.premium.spacing.md)
                            )
                        } else {
                            // Show camera preview placeholder
                            Text(
                                text = "Camera Preview",
                                style = MizanTheme.typography.bodyLg,
                                color = MizanTheme.premium.text.tertiary
                            )

                            Spacer(Modifier.height(MizanTheme.premium.spacing.md))

                            Button(
                                onClick = onStartScanning,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MizanTheme.premium.colors.primary
                                )
                            ) {
                                Text(
                                    text = "Start QR Scan",
                                    style = MizanTheme.typography.labelLg,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(MizanTheme.premium.spacing.lg))

        // Scanned Text Display
        if (scannedText.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MizanTheme.premium.spacing.lg)
                    .background(
                        color = MizanTheme.premium.colors.surface1,
                        shape = RoundedCornerShape(MizanTheme.premium.radius.md)
                    )
                    .padding(MizanTheme.premium.spacing.md)
            ) {
                Text(
                    text = "Detected: $scannedText",
                    style = MizanTheme.typography.bodySm,
                    color = MizanTheme.premium.text.primary
                )
            }

            Spacer(Modifier.height(MizanTheme.premium.spacing.md))

            // Extracted Amount Display
            if (recognizedAmount > 0) {
                Text(
                    text = "Amount: $${String.format("%.2f", recognizedAmount)}",
                    style = MizanTheme.typography.headingMd,
                    color = MizanTheme.premium.colors.success
                )
            }
        }

        // Error Display
        error?.let { errorMessage ->
            Spacer(Modifier.height(MizanTheme.premium.spacing.md))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MizanTheme.premium.spacing.lg)
                    .background(
                        color = MizanTheme.premium.colors.error.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(MizanTheme.premium.radius.md)
                    )
                    .padding(MizanTheme.premium.spacing.md)
            ) {
                Text(
                    text = errorMessage,
                    style = MizanTheme.typography.bodySm,
                    color = MizanTheme.premium.colors.error,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(Modifier.weight(1f))

        // Action Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MizanTheme.premium.spacing.lg),
            horizontalArrangement = Arrangement.spacedBy(MizanTheme.premium.spacing.sm)
        ) {
            if (isScanning) {
                Button(
                    onClick = onStopScanning,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MizanTheme.premium.colors.error
                    )
                ) {
                    Text(
                        text = "Stop Scan",
                        style = MizanTheme.typography.labelLg,
                        color = Color.White
                    )
                }
            }

            if (recognizedAmount > 0 && !isScanning) {
                Button(
                    onClick = onNext,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MizanTheme.premium.colors.success
                    )
                ) {
                    Text(
                        text = "Next",
                        style = MizanTheme.typography.labelLg,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(Modifier.height(MizanTheme.premium.spacing.lg))
    }
}

@Composable
private fun CameraPreview(
    onQRCodeDetected: (String) -> Unit,
    onError: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }

            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build()
                val imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(Executors.newSingleThreadExecutor()) { imageProxy ->
                            processImage(imageProxy, onQRCodeDetected, onError)
                        }
                    }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalyzer
                    )
                    preview.setSurfaceProvider(previewView.surfaceProvider)
                } catch (exc: Exception) {
                    onError("Camera initialization failed: ${exc.message}")
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}

@androidx.annotation.OptIn(ExperimentalGetImage::class)
private fun processImage(
    imageProxy: ImageProxy,
    onQRCodeDetected: (String) -> Unit,
    onError: (String) -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        // Configure barcode scanner to only detect QR codes for better performance
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()

        val scanner = BarcodeScanning.getClient(options)

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    // Extract the raw value from the detected QR code
                    val rawValue = barcode.rawValue
                    if (!rawValue.isNullOrBlank()) {
                        onQRCodeDetected(rawValue)
                        // Stop processing after finding first valid QR code
                        break
                    }
                }
            }
            .addOnFailureListener { e ->
                onError("QR code scanning failed: ${e.message}")
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    } else {
        imageProxy.close()
    }
}

@Composable
private fun ScanningOverlay() {
    val cornerColor = MizanTheme.premium.colors.success
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        // Scanning frame dimensions
        val frameWidth = canvasWidth * 0.7f
        val frameHeight = canvasHeight * 0.6f
        val frameLeft = (canvasWidth - frameWidth) / 2
        val frameTop = (canvasHeight - frameHeight) / 2

        // Draw corners
        val cornerLength = 30.dp.toPx()
        val cornerWidth = 4.dp.toPx()


        // Top-left corner
        drawLine(
            color = cornerColor,
            start = Offset(frameLeft, frameTop),
            end = Offset(frameLeft + cornerLength, frameTop),
            strokeWidth = cornerWidth
        )
        drawLine(
            color = cornerColor,
            start = Offset(frameLeft, frameTop),
            end = Offset(frameLeft, frameTop + cornerLength),
            strokeWidth = cornerWidth
        )

        // Top-right corner
        drawLine(
            color = cornerColor,
            start = Offset(frameLeft + frameWidth - cornerLength, frameTop),
            end = Offset(frameLeft + frameWidth, frameTop),
            strokeWidth = cornerWidth
        )
        drawLine(
            color = cornerColor,
            start = Offset(frameLeft + frameWidth, frameTop),
            end = Offset(frameLeft + frameWidth, frameTop + cornerLength),
            strokeWidth = cornerWidth
        )

        // Bottom-left corner
        drawLine(
            color = cornerColor,
            start = Offset(frameLeft, frameTop + frameHeight),
            end = Offset(frameLeft + cornerLength, frameTop + frameHeight),
            strokeWidth = cornerWidth
        )
        drawLine(
            color = cornerColor,
            start = Offset(frameLeft, frameTop + frameHeight - cornerLength),
            end = Offset(frameLeft, frameTop + frameHeight),
            strokeWidth = cornerWidth
        )

        // Bottom-right corner
        drawLine(
            color = cornerColor,
            start = Offset(frameLeft + frameWidth - cornerLength, frameTop + frameHeight),
            end = Offset(frameLeft + frameWidth, frameTop + frameHeight),
            strokeWidth = cornerWidth
        )
        drawLine(
            color = cornerColor,
            start = Offset(frameLeft + frameWidth, frameTop + frameHeight - cornerLength),
            end = Offset(frameLeft + frameWidth, frameTop + frameHeight),
            strokeWidth = cornerWidth
        )

        // Semi-transparent overlay outside the frame
        val overlayColor = Color.Black.copy(alpha = 0.5f)

        // Top overlay
        drawRect(
            color = overlayColor,
            topLeft = Offset(0f, 0f),
            size = Size(canvasWidth, frameTop)
        )

        // Bottom overlay
        drawRect(
            color = overlayColor,
            topLeft = Offset(0f, frameTop + frameHeight),
            size = Size(canvasWidth, canvasHeight - frameTop - frameHeight)
        )

        // Left overlay
        drawRect(
            color = overlayColor,
            topLeft = Offset(0f, frameTop),
            size = Size(frameLeft, frameHeight)
        )

        // Right overlay
        drawRect(
            color = overlayColor,
            topLeft = Offset(frameLeft + frameWidth, frameTop),
            size = Size(canvasWidth - frameLeft - frameWidth, frameHeight)
        )
    }
}

private fun extractAmountFromText(text: String): Double? {
    val amountPatterns = listOf(
        Regex("\\$\\s*(\\d+(?:\\.\\d{2})?)"), // $25.99
        Regex(
            "(?:total|amount|sum)\\s*[:=]\\s*\\$?\\s*(\\d+(?:\\.\\d{2})?)",
            RegexOption.IGNORE_CASE
        ), // Total: $25.99
        Regex(
            "(\\d+(?:\\.\\d{2})?)\\s*(?:dollars?|usd)?",
            RegexOption.IGNORE_CASE
        ), // 25.99 dollars
        Regex("\\b(\\d{1,5}(?:\\.\\d{2})?)\\b") // Any number with 2 decimal places
    )

    for (pattern in amountPatterns) {
        val match = pattern.find(text)
        if (match != null) {
            return match.groupValues[1].toDoubleOrNull()
        }
    }

    return null
}

@androidx.compose.ui.tooling.preview.Preview(
    name = "Camera Input - Idle",
    showBackground = true
)
@Composable
fun CameraInputStepIdlePreview() {
    MizanTheme {
        CameraInputStep(
            isScanning = false,
            scannedText = "",
            recognizedAmount = 0.0,
            error = null,
            onStartScanning = {},
            onStopScanning = {},
            onAmountExtracted = {},
            onScanResult = { _, _ -> },
            onQRCodeScanned = {},
            onError = {},
            onNext = {}
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(
    name = "Camera Input - Scanning",
    showBackground = true
)
@Composable
fun CameraInputStepScanningPreview() {
    MizanTheme {
        CameraInputStep(
            isScanning = true,
            scannedText = "Total: $25.99\nTax: $2.60",
            recognizedAmount = 25.99,
            error = null,
            onStartScanning = {},
            onStopScanning = {},
            onAmountExtracted = {},
            onScanResult = { _, _ -> },
            onQRCodeScanned = {},
            onError = {},
            onNext = {}
        )
    }
}
