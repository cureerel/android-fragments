package com.cureerel.android.simplesplash.jetpackcompose

import android.R.attr.name
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true, showSystemUi = false )
@Composable
fun TextExample() {
    Text(
        text = "Good Morning, Sauron!",
        color = Color.Blue,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace,
        letterSpacing = 1.sp,
        textAlign = TextAlign.End,

    )
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun TextFieldExample () {
    var name = remember{ mutableStateOf("") }
    TextField(
        value = name.value,
        onValueChange = { name.value = it},
//        label = { Text(text = "How are you?")}
        placeholder = {Text( text = "How are You?")},
        singleLine = true,
         // other : leadingIcon, trailingIcon
         shape = RoundedCornerShape(34.dp ),
        // colors = TextFieldDefaults.color(focused | unfocused - ContainerColor, TextColor, IndicatorColor, cursorColor, PlaceholderColor, LeadingIconColor, TrailingIconColor, LableColor, )
    )
}
