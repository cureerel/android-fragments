package com.cureerel.android.simplesplash.jetpackcompose

import android.R.attr.name
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CheckboxDefaults.colors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.unit.dp



// make a page
@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextExample()
        Spacer(modifier = Modifier.height(20.dp))
        TextFieldExample()
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextFieldExample()
    }

}


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
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Preview(showBackground = true)
@Composable
fun OutlinedTextFieldExample() {
    var name by remember { mutableStateOf("") }

    OutlinedTextField(
        value = name,
        onValueChange = { name = it },

        label = {
            Text(text = "Do you live at Shire?")
        },

        singleLine = true,
        shape = RoundedCornerShape(25.dp)
    )
}