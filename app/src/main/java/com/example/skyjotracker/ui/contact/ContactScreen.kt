package com.example.skyjotracker.ui.contact

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.skyjotracker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.contact_developer)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back_24dp),
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor =
                            MaterialTheme.colorScheme.onPrimaryContainer,
                        navigationIconContentColor =
                            MaterialTheme.colorScheme.onPrimaryContainer
                    )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(id = R.string.contact_name)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(id = R.string.contact_email)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text(stringResource(id = R.string.contact_message)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 5,
                maxLines = 10
            )
            Button(
                onClick = {
                    sendEmail(context, name, email, message)
                    // onBackClick()
                },
                enabled = message.isNotBlank() && email.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) { Text(stringResource(id = R.string.contact_send)) }
        }
    }
}

private fun sendEmail(context: Context, name: String, email: String, message: String) {
    val intent =
        Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf("feedback@skyjotracker.com"))
            putExtra(Intent.EXTRA_SUBJECT, "[APP] SkyjoTracker Feedback")
            putExtra(
                Intent.EXTRA_TEXT,
                """
                        Name: $name
                        Email: $email

                        Message:
                        $message
                        """.trimIndent()
            )
        }
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        // fail silently
    }
}
