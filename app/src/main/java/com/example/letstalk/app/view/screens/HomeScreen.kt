package com.example.letstalk.app.view.screens

import com.example.letstalk.app.view.viewmodel.HomeViewModelImpl
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.letstalk.app.util.Resource
import com.example.letstalk.app.view.widgets.NavigationDrawer
import com.example.letstalk.app.view.widgets.RoomItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModelImpl,
    userName: String
) {
    val context = LocalContext.current
    val rooms by viewModel.rooms.collectAsState()
    val newRoomLabel by viewModel.newRoomLabel.collectAsState()
    val createRoomState by viewModel.createRoomState.collectAsState()

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    // React to create room result
    LaunchedEffect(createRoomState) {
        when (createRoomState) {
            is Resource.Success -> {
                showBottomSheet = false
                Toast.makeText(context, "Room created successfully", Toast.LENGTH_SHORT).show()
            }
            is Resource.Error -> {
                Toast.makeText(
                    context,
                    (createRoomState as Resource.Error).message ?: "Failed to create room",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> Unit
        }
    }

    NavigationDrawer(
        rooms = if (rooms is Resource.Success) (rooms as Resource.Success).data ?: emptyList() else emptyList(),
        drawerState = drawerState,
        scope = scope,
        navController = navController,
        onLeave = { roomLabel -> viewModel.leaveRoom(roomLabel) }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("LetsTalk") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { showBottomSheet = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Create Room")
                }
            }
        ) { paddingValues ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (rooms) {
                    is Resource.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    is Resource.Success -> {
                        val roomList = (rooms as Resource.Success).data ?: emptyList()
                        if (roomList.isEmpty()) {
                            Text(
                                text = "No rooms yet. Create one!",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        } else {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(roomList) { room ->
                                    RoomItem(
                                        room = room,
                                        onClick = {
                                            navController.navigate("chat/${room.label}")
                                        },
                                        onLeave = {
                                            viewModel.leaveRoom(room.label)
                                        }
                                    )
                                }
                            }
                        }
                    }
                    is Resource.Error -> {
                        Text(
                            text = (rooms as Resource.Error).message ?: "Error loading rooms",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    else -> Unit
                }
            }

            // Bottom sheet for creating a room
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(text = "Create a New Room")
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = newRoomLabel,
                            onValueChange = { viewModel.onNewRoomLabelChange(it) },
                            label = { Text("Room Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            if (createRoomState is Resource.Loading) {
                                CircularProgressIndicator()
                            } else {
                                Button(onClick = { viewModel.createRoom() }) {
                                    Text("Create")
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}