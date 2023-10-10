package com.mcmouse88.grpccliendtrain

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mcmouse88.grpccliendtrain.ui.theme.GrpcCliendTrainTheme

// code has been used from https://proandroiddev.com/connecting-android-apps-with-server-using-grpc-919719bd9a97

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        val viewModel by viewModels<MainViewModel>()

        super.onCreate(savedInstanceState)
        setContent {
            GrpcCliendTrainTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(title = {
                                Text(text = "GRPC Demo")
                            })
                        },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) { padding ->
                        Column(
                            modifier = Modifier.padding(padding)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Need to input value "10.0.2.2"
                                // If using physical device you need to input ip address your PC
                                OutlinedTextField(
                                    value = viewModel.ip.value,
                                    onValueChange = {
                                        viewModel.onIpChange(it)
                                    },
                                    enabled = viewModel.hostEnabled.value,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    placeholder = {
                                        Text(text = "IP address")
                                    },
                                    label = {
                                        Text(text = "Server")
                                    }
                                )

                                Spacer(modifier = Modifier.size(16.dp))

                                // Need to input value "50051"
                                OutlinedTextField(
                                    value = viewModel.port.value,
                                    onValueChange = {
                                        viewModel.onPortChange(it)
                                    },
                                    enabled = viewModel.portEnabled.value,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    placeholder = {
                                        Text(text = "Port")
                                    },
                                    label = {
                                        Text(text = "Port")
                                    }
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Button(
                                    enabled = viewModel.startEnabled.value,
                                    onClick = {
                                        viewModel.start()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                ) {
                                    Text(text = "Start")
                                }

                                Spacer(modifier = Modifier.size(16.dp))
                                Button(
                                    enabled = viewModel.endEnabled.value,
                                    onClick = {
                                        viewModel.exit()
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                ) {
                                    Text(text = "End")
                                }
                            }

                            Button(
                                enabled = viewModel.buttonsEnabled.value,
                                onClick = {
                                    viewModel.sayHello("Hello!")
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = "Simple RPC: Say Hello")
                            }

                            Text(text = "Result: ${viewModel.result.value}")
                        }
                    }
                }
            }
        }
    }
}