package com.example.letstalk.app.view.widgets

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.letstalk.app.data.model.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawer(
    rooms: List<Room>,
    drawerState: DrawerState,
    scope: CoroutineScope,
    navController: NavController,
    onLeave: (String) -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 48.dp)
            ) {
                item {
                    Text(
                        text = "Your Rooms",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                items(rooms) { room ->
                    RoomItem(
                        room = room,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate("chat/${room.label}")
                        },
                        onLeave = {
                            onLeave(room.label)
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }
        },
        content = content
    )
}