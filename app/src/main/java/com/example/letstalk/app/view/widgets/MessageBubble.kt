package com.example.letstalk.app.view.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.letstalk.app.data.model.Message
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MessageBubble(
    message: Message,
    ownUserName: String
) {
    val isOwnMessage = message.userName == ownUserName
    val bubbleColor = if (isOwnMessage) Color(0xFF6650A4) else Color(0xFF2D9CDB)
    val horizontalArrangement = if (isOwnMessage) Arrangement.End else Arrangement.Start
    val time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(message.timeStamp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = horizontalArrangement
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .fillMaxWidth(0.75f)
        ) {
            Column(
                modifier = Modifier
                    .align(if (isOwnMessage) Alignment.CenterEnd else Alignment.CenterStart)
                    .background(bubbleColor, RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                if (!isOwnMessage) {
                    Text(
                        text = message.userName,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
                Text(
                    text = message.text,
                    color = Color.White,
                    fontSize = 14.sp
                )
                Text(
                    text = time,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 10.sp,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}