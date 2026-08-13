package com.cureerel.android.simplesplash.jetpackcompose

import androidx.compose.ui.tooling.preview.Preview
import com.cureerel.android.simplesplash.ui.theme.SimplesplashTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ChatScreen() {

    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        ChatTopBar()

        /*
         * Main chat area — ChatGPT keeps generous horizontal padding
         * and no background tint, just plain white.
         */
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            UserMessage(
                text = "Write me a Rust code to calculate Fibonacci numbers."
            )

            Spacer(modifier = Modifier.height(20.dp))

            AssistantMessage()

            Spacer(modifier = Modifier.height(24.dp))

            InteractiveDemo()

            Spacer(modifier = Modifier.height(12.dp))
        }

        ChatOptions()

        ChatInput(
            value = message,
            onValueChange = { message = it },
            onSend = {
                message = ""
            }
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}


@Composable
fun ChatTopBar() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        IconButton(onClick = { /* open drawer */ }) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                tint = Color.Black
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ChatGPT",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.padding(start = 2.dp)
            )
        }

        IconButton(onClick = { /* new chat */ }) {
            Icon(
                imageVector = Icons.Outlined.Create,
                contentDescription = "New chat",
                tint = Color.Black
            )
        }
    }
}

@Composable
fun UserMessage(text: String) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFF1F1F1))
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(
                text = text,
                textAlign = TextAlign.End,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}


@Composable
fun AssistantMessage() {

    // ChatGPT renders assistant replies as plain text, no bubble/card.
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = "Sure! Here's a simple Rust implementation:",
            fontSize = 16.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(12.dp))

        CodeBlock(
            language = "rust",
            code = """
                fn fibonacci(n: u32) -> u32 {
                    match n {
                        0 => 0,
                        1 => 1,
                        n => fibonacci(n - 1) + fibonacci(n - 2),
                    }
                }
            """.trimIndent()
        )

        Spacer(modifier = Modifier.height(8.dp))

        MessageActions()
    }
}


@Composable
fun CodeBlock(language: String, code: String) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF1E1E1E))
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2B2B2B))
                .padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = language,
                color = Color(0xFFB4B4B4),
                fontSize = 13.sp
            )

            TextButton(
                onClick = { /* copy only this block */ },
                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy code",
                    tint = Color(0xFFB4B4B4),
                    modifier = Modifier.width(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Copy", color = Color(0xFFB4B4B4), fontSize = 13.sp)
            }
        }

        Text(
            text = code,
            color = Color.White,
            fontFamily = FontFamily.Monospace,
            fontSize = 13.5.sp,
            modifier = Modifier.padding(14.dp)
        )
    }
}

@Composable
fun MessageActions() {

    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {

        IconButton(onClick = { /* Copy */ }) {
            Icon(
                Icons.Default.ContentCopy,
                contentDescription = "Copy",
                tint = Color(0xFF6E6E6E)
            )
        }

        IconButton(onClick = { /* Read aloud */ }) {
            Icon(
                Icons.Default.VolumeUp,
                contentDescription = "Read",
                tint = Color(0xFF6E6E6E)
            )
        }

        IconButton(onClick = { /* Share */ }) {
            Icon(
                Icons.Default.Share,
                contentDescription = "Share",
                tint = Color(0xFF6E6E6E)
            )
        }

        IconButton(onClick = { /* More */ }) {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = "More",
                tint = Color(0xFF6E6E6E)
            )
        }
    }
}


@Composable
fun ChatOptions() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        ChatOptionChip(icon = Icons.Default.Add, label = "Create image")
        ChatOptionChip(label = "Write / edit")
        ChatOptionChip(icon = Icons.Default.Search, label = "Search web")
    }
}

@Composable
fun ChatOptionChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    label: String
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Icon(it, contentDescription = null, tint = Color(0xFF6E6E6E), modifier = Modifier.width(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(label, fontSize = 13.5.sp, color = Color(0xFF3A3A3A))
    }
}

@Composable
fun ChatInput(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(28.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = { /* attach / add */ }) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add",
                tint = Color.Black
            )
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text("Ask anything") },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            singleLine = false,
            maxLines = 5
        )

        IconButton(onClick = { /* mic */ }) {
            Icon(
                Icons.Outlined.Mic,
                contentDescription = "Voice",
                tint = Color.Black
            )
        }

        IconButton(
            onClick = onSend,
            modifier = Modifier
                .padding(end = 6.dp)
                .clip(CircleShape)
                .background(if (value.isBlank()) Color(0xFFD6D6D6) else Color.Black)
        ) {
            Icon(
                Icons.Default.Send,
                contentDescription = "Send",
                tint = Color.White
            )
        }
    }
}


@Composable
fun InteractiveDemo() {

    var value by remember {
        mutableFloatStateOf(50f)
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = "Wind speed: ${value.toInt()} km/h",
            fontSize = 18.sp,
            color = Color.Black
        )

        Slider(
            value = value,
            onValueChange = { value = it },
            valueRange = 0f..100f
        )

        WindIndicator(value)
    }
}


@Composable
fun WindIndicator(value: Float) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF1F1F1)),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "←  WIND  →",
            fontSize = (14 + value / 5).sp,
            color = Color.Black
        )
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ChatScreenPreview() {
    SimplesplashTheme {
        ChatScreen()
    }
}