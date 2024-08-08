package com.abhi.test

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.samsung.android.sdk.penremote.*

@Composable
fun SpenButtonHandler() {
    val context = LocalContext.current
    var buttonClicked by remember { mutableStateOf(false) }

    // Initialize SpenRemote
    val spenRemote = SpenRemote.getInstance()
    if (!spenRemote.isConnected()) {
        spenRemote.connect(context, object : SpenRemote.ConnectionResultCallback {
            override fun onSuccess(manager: SpenUnitManager) {
                // Get Button unit
                val buttonUnit = manager.getUnit(SpenUnit.TYPE_BUTTON)
                if (buttonUnit != null) {
                    manager.registerSpenEventListener(object : SpenEventListener {

                        override fun onEvent(p0: SpenEvent?) {
                            val buttonEvent = ButtonEvent(p0)
                            if (buttonEvent.action == ButtonEvent.ACTION_DOWN) {
                                buttonClicked = !buttonClicked
                            }                        }
                    }, buttonUnit)
                }
            }

            override fun onFailure(error: Int) {
                // Handle connection failure
            }
        })
    }

    Box(
        contentAlignment = Alignment.Center
    ) {
        Text(text = if (buttonClicked) "Button Clicked" else "Button Not Clicked")
    }
}
