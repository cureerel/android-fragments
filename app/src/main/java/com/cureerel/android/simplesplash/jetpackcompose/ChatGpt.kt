package com.cureerel.android.simplesplash.jetpackcompose

import androidx.compose.ui.tooling.preview.Preview
import com.cureerel.android.simplesplash.ui.theme.SimplesplashTheme

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalClipboardManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch




// ---------- Content model ----------

sealed class ContentBlock {
    data class Heading(val text: String) : ContentBlock()
    data class Paragraph(val text: String) : ContentBlock()
    data class ListBlock(val items: List<String>) : ContentBlock()
    data class Code(val language: String, val code: String) : ContentBlock()
}

data class ChatTurn(
    val id: Long,
    val userText: String,
    val blocks: List<ContentBlock>
)

// ---------- Demo content ----------

private val demoFibTurn = ChatTurn(
    id = 0L,
    userText = "Write me a Rust code to calculate Fibonacci numbers.",
    blocks = listOf(
        ContentBlock.Paragraph("Sure! Here's a simple Rust implementation:"),
        ContentBlock.Code(
            "rust",
            """
                fn fibonacci(n: u32) -> u32 {
                    match n {
                        0 => 0,
                        1 => 1,
                        n => fibonacci(n - 1) + fibonacci(n - 2),
                    }
                }
            """.trimIndent()
        )
    )
)

// heading + paragraph + flow, shown via the Demo chip
private val demoExplainTurn = ChatTurn(
    id = -1L,
    userText = "Explain how Fibonacci works.",
    blocks = listOf(
        ContentBlock.Heading("How Fibonacci Works"),
        ContentBlock.Paragraph(
            "The Fibonacci sequence builds each number from the two before it, " +
                    "starting from 0 and 1. It grows fast but follows a simple rule."
        ),
        ContentBlock.ListBlock(
            listOf(
                "Start with base cases: 0 and 1.",
                "Each next term is the sum of the previous two.",
                "Repeat until you reach the term you need.",
                "Recursion mirrors this rule directly, one call per term."
            )
        )
    )
)

// ---------- Screen ----------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {

    var inputText by remember { mutableStateOf("") }
    var showAsideSheet by remember { mutableStateOf(false) }
    var isStreaming by remember { mutableStateOf(true) } // true so first demo turn locks input while it types

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val turns = remember { mutableStateListOf(demoFibTurn) }

    fun scrollToBottom() {
        scope.launch { listState.animateScrollToItem((turns.size - 1).coerceAtLeast(0)) }
    }

    fun addTurn(turn: ChatTurn) {
        turns.add(turn)
        isStreaming = true
        scrollToBottom()
    }

    fun sendMessage(text: String) {
        if (text.isBlank() || isStreaming) return
        val nextId = (turns.lastOrNull()?.id ?: 0L) + 1
        addTurn(
            ChatTurn(
                id = nextId,
                userText = text,
                blocks = listOf(ContentBlock.Paragraph("Got it — you said: \"$text\""))
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding() // keeps input above keyboard, screen stays scrollable
                .background(Color.White)
        ) {

            ChatTopBar(onAsideClick = { showAsideSheet = true })

            // single scrollable region — this is the whole chat
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }

                item {
                    InteractiveDemo()
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // container per turn: one user message + its assistant reply
                items(turns, key = { it.id }) { turn ->
                    TurnContainer(
                        turn = turn,
                        onAllDone = { isStreaming = false },
                        onToken = { scrollToBottom() },
                        onCodeCopied = {
                            scope.launch { snackbarHostState.showSnackbar("Code copied") }
                        }
                    )
                }


            }

            ChatOptions(
                enabled = !isStreaming,
                onDemoClick = { addTurn(demoExplainTurn) }
            )

            ChatInput(
                value = inputText,
                enabled = !isStreaming, // new input only opens up after previous reply finishes
                onValueChange = { inputText = it },
                onSend = {
                    sendMessage(inputText)
                    inputText = ""
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        if (showAsideSheet) {
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
            ModalBottomSheet(
                onDismissRequest = { showAsideSheet = false },
                sheetState = sheetState
            ) {
                AsideSheetContent(
                    onDismiss = {
                        scope.launch {
                            sheetState.hide()
                            showAsideSheet = false
                        }
                    }
                )
            }
        }
    }
}

// ---------- Turn container ----------

@Composable
fun TurnContainer(
    turn: ChatTurn,
    onAllDone: () -> Unit,
    onToken: () -> Unit,
    onCodeCopied: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(tween(180)) // container grows smoothly as text streams in
    ) {
        UserMessage(text = turn.userText)
        Spacer(modifier = Modifier.height(16.dp))
        BlockStream(
            blocks = turn.blocks,
            onToken = onToken,
            onAllDone = onAllDone,
            onCodeCopied = onCodeCopied
        )
    }
}

// reveals blocks one after another: heading -> paragraph -> flow -> code
@Composable
fun BlockStream(
    blocks: List<ContentBlock>,
    onToken: () -> Unit,
    onAllDone: () -> Unit,
    onCodeCopied: () -> Unit
) {
    var activeIndex by remember(blocks) { mutableStateOf(0) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        blocks.forEachIndexed { index, block ->
            if (index <= activeIndex) {
                RenderBlock(
                    block = block,
                    onToken = onToken,
                    onCodeCopied = onCodeCopied,
                    onDone = {
                        if (index == activeIndex) {
                            if (index < blocks.lastIndex) activeIndex++ else onAllDone()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun RenderBlock(
    block: ContentBlock,
    onToken: () -> Unit,
    onCodeCopied: () -> Unit,
    onDone: () -> Unit
) {
    when (block) {
        is ContentBlock.Heading -> TypewriterText(
            fullText = block.text,
            stepDelayMs = 22L,
            byChar = true,
            fontSize = 19.sp,
            fontWeight = FontWeight.SemiBold,
            onToken = onToken,
            onDone = onDone
        )

        is ContentBlock.Paragraph -> TypewriterText(
            fullText = block.text,
            stepDelayMs = 45L,
            byChar = false,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            onToken = onToken,
            onDone = onDone
        )

        is ContentBlock.ListBlock -> StreamingFlow(
            items = block.items,
            onToken = onToken,
            onDone = onDone
        )

        is ContentBlock.Code -> {
            CodeBlock(language = block.language, code = block.code, onCopy = { onCodeCopied() })
            LaunchedEffect(block) { onDone() } // code appears whole, then unlocks next block
        }
    }
}

// generic token-by-token / char-by-char reveal
@Composable
fun TypewriterText(
    fullText: String,
    stepDelayMs: Long,
    byChar: Boolean,
    fontSize: androidx.compose.ui.unit.TextUnit,
    fontWeight: FontWeight,
    onToken: () -> Unit,
    onDone: () -> Unit
) {
    var shown by remember(fullText) { mutableStateOf("") }

    LaunchedEffect(fullText) {
        if (byChar) {
            for (i in fullText.indices) {
                shown = fullText.substring(0, i + 1)
                onToken()
                delay(stepDelayMs)
            }
        } else {
            val words = fullText.split(" ")
            for (i in words.indices) {
                shown = words.subList(0, i + 1).joinToString(" ")
                onToken()
                delay(stepDelayMs)
            }
        }
        onDone()
    }

    Text(text = shown, fontSize = fontSize, fontWeight = fontWeight, color = Color.Black)
}

// numbered flow, one item at a time
@Composable
fun StreamingFlow(items: List<String>, onToken: () -> Unit, onDone: () -> Unit) {
    var activeIndex by remember(items) { mutableStateOf(0) }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items.forEachIndexed { i, item ->
            if (i <= activeIndex) {
                Row {
                    Text("${i + 1}.  ", fontSize = 16.sp, color = Color.Black)
                    TypewriterText(
                        fullText = item,
                        stepDelayMs = 40L,
                        byChar = false,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        onToken = onToken,
                        onDone = {
                            if (i == activeIndex) {
                                if (i < items.lastIndex) activeIndex++ else onDone()
                            }
                        }
                    )
                }
            }
        }
    }
}

// ---------- Existing pieces (mostly unchanged) ----------

@Composable
fun AsideSheetContent(onDismiss: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
        Text("Aside", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "Sheet only grows as tall as its content, slides up from the bottom.",
            fontSize = 14.sp,
            color = Color(0xFF6E6E6E)
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextButton(onClick = onDismiss) { Text("Close") }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun ChatTopBar(onAsideClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onAsideClick) {
            Icon(Icons.Default.Menu, contentDescription = "Aside", tint = Color.Black)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("ChatGPT", fontSize = 17.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
            Icon(Icons.Outlined.KeyboardArrowDown, contentDescription = null, tint = Color.Black, modifier = Modifier.padding(start = 2.dp))
        }
        IconButton(onClick = { /* new chat */ }) {
            Icon(Icons.Outlined.Create, contentDescription = "New chat", tint = Color.Black)
        }
    }
}

@Composable
fun UserMessage(text: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFF1F1F1))
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(text = text, textAlign = TextAlign.End, fontSize = 16.sp, color = Color.Black)
        }
    }
}

@Composable
fun CodeBlock(language: String, code: String, onCopy: (String) -> Unit) {
    val clipboardManager = LocalClipboardManager.current
    Column(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).background(Color(0xFF1E1E1E))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().background(Color(0xFF2B2B2B)).padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(language, color = Color(0xFFB4B4B4), fontSize = 13.sp)
            TextButton(
                onClick = { clipboardManager.setText(AnnotatedString(code)); onCopy(code) },
                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
            ) {
                Icon(Icons.Default.ContentCopy, contentDescription = "Copy code", tint = Color(0xFFB4B4B4), modifier = Modifier.width(16.dp))
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
fun ChatOptions(enabled: Boolean, onDemoClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ChatOptionChip(icon = Icons.Default.Add, label = "Create image")
        ChatOptionChip(label = "Write / edit")
        ChatOptionChip(icon = Icons.Default.Search, label = "Search web")
        ChatOptionChip(label = "Demo", onClick = { if (enabled) onDemoClick() }) // shows heading+paragraph+flow
    }
}

@Composable
fun ChatOptionChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    label: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(50))
            .clickable(onClick = onClick)
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
    enabled: Boolean,
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
        IconButton(onClick = { /* attach */ }, enabled = enabled) {
            Icon(Icons.Default.Add, contentDescription = "Add", tint = if (enabled) Color.Black else Color(0xFFC0C0C0))
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled, // locked while assistant is still streaming
            modifier = Modifier.weight(1f),
            placeholder = { Text(if (enabled) "Ask anything" else "Waiting for reply...") },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            singleLine = false,
            maxLines = 5
        )
        IconButton(onClick = { /* mic */ }, enabled = enabled) {
            Icon(Icons.Outlined.Mic, contentDescription = "Voice", tint = if (enabled) Color.Black else Color(0xFFC0C0C0))
        }
        IconButton(
            onClick = onSend,
            enabled = enabled && value.isNotBlank(),
            modifier = Modifier
                .padding(end = 6.dp)
                .clip(CircleShape)
                .background(if (enabled && value.isNotBlank()) Color.Black else Color(0xFFD6D6D6))
        ) {
            Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
        }
    }
}

@Composable
fun InteractiveDemo() {
    var value by remember { mutableFloatStateOf(50f) }
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Wind speed: ${value.toInt()} km/h", fontSize = 18.sp, color = Color.Black)
        Slider(value = value, onValueChange = { value = it }, valueRange = 0f..100f)
        WindIndicator(value)
    }
}

@Composable
fun WindIndicator(value: Float) {
    Box(
        modifier = Modifier.fillMaxWidth().height(80.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFFF1F1F1)),
        contentAlignment = Alignment.Center
    ) {
        Text("←  WIND  →", fontSize = (14 + value / 5).sp, color = Color.Black)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ChatScreenPreview() {
    SimplesplashTheme { ChatScreen() }
}