package com.edsh.contdedup.component

import com.edsh.contdedup.viewmodel.DedupViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edsh.contdedup.state.SimpleState

/**
 * Основной экран приложения дедупликатора контактов.
 * В зависимости от состояния [SimpleState], выводит информацию о текущем состоянии
 * @param viewModel ViewModel с логикой приложения
 */
@Composable
fun DedupScreen(
    viewModel: DedupViewModel
) {
    val state = viewModel.state.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            "Welcome to Contact deduplicator app",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(320.dp))

        when (state.status) {
            SimpleState.Status.IDLE -> {
                Text(
                    "Press button for deduplication process",
                    textAlign = TextAlign.Center
                )
            }

            SimpleState.Status.LOADING -> {
                CircularProgressIndicator()
            }

            SimpleState.Status.SUCCESS -> {
                Text(
                    "Deduplication process complete",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            SimpleState.Status.ERROR -> {
                val error = state.error
                Text(
                    "Error: $error",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.onButtonClicked() },
            enabled = state.status == SimpleState.Status.IDLE,
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
        ) {
            Text(
                "Start",
                fontSize = 24.sp
            )
        }
    }

}
