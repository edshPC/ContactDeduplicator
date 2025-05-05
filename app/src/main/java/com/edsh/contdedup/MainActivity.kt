package com.edsh.contdedup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.edsh.contdedup.component.DedupScreen
import com.edsh.contdedup.ui.theme.ContactDeduplicatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContactDeduplicatorTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DedupScreen()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DedupPreview() {
    ContactDeduplicatorTheme {
        DedupScreen()
    }
}
