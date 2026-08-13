package com.cureerel.android.simplesplash.jetpackcompose

import android.R.attr.font
import android.R.attr.name
import android.R.attr.onClick
import android.R.attr.padding
import android.R.attr.text
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily.Companion.Cursive
import androidx.compose.ui.text.font.FontFamily.Companion.Monospace
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun HobbitScreen() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        // scrollable screen
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
     ) {
        RowExample()
        ColumnExample()
        ColumnWithOutline()
    }
}


@Preview(showBackground = true, showSystemUi = false)
@Composable
fun RowExample() {
    Text(text = "The Characters of Hobbit", fontFamily = Cursive,  fontSize = 30.sp, textAlign = TextAlign.Center, modifier =  Modifier.padding(126.dp).fillMaxWidth())

    val customStyle = LocalTextStyle.current.copy(
        fontFamily = Cursive,
        fontSize = 32.sp
    )

    ProvideTextStyle(value = customStyle) {

    Row(
//       modifier = Modifier.fillMaxWidth().fillMaxHeight()
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement =  Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically

    ){
    Text(text = "Gandalf")
    Text(text = "Bilbo")
    Text(text = "Galadriel")

    }
}
}

@Preview(showBackground = true, showSystemUi = false, )
@Composable
fun ColumnExample() {
    Text(text = "The Ancestry of Characters",  fontSize = 30.sp, fontFamily = Cursive, textAlign = TextAlign.Center, modifier = Modifier.padding(32.dp).fillMaxWidth())

    val customStyle = LocalTextStyle.current.copy(
        fontFamily = Cursive,
        fontSize = 34.sp
    )

    ProvideTextStyle(customStyle) {
    Column (
//       modifier = Modifier.fillMaxWidth().fillMaxHeight()
        modifier = Modifier.fillMaxSize(),
        verticalArrangement =  Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "Magician")
        Text(text = "Hobbit")
        Text(text = "Elf")
    }
}
}
@Preview(showBackground = true)
@Composable
fun ColumnWithOutline() {

    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var phoneId by remember { mutableStateOf("") }

    val customStyle = LocalTextStyle.current.copy(
        fontFamily = Cursive,
        fontSize = 34.sp
    )


        Column(
            modifier = Modifier.fillMaxSize().padding(76.dp)
        ) {
            Text(
                text = "Welcome to the Jungle",
                textAlign = TextAlign.Center,
                fontFamily = Cursive,
                fontSize = 30.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            )
            Spacer(modifier = Modifier.height(76.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = {
                    Text(text = "Enter your name",  style = customStyle)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                placeholder = {
                    Text(text = "Enter your Age",  style = customStyle)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = phoneId,
                onValueChange = { phoneId = it },
                placeholder = {
                    Text(text = "Enter your Phone number", style = customStyle)
                }
            )

            // button
            Button(
                onClick = {
                },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text("Submit", style = customStyle)
            }
        }

}


@Preview(showBackground = true, showSystemUi = false)
@Composable
fun BoxExample() {}
