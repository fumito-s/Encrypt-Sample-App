package com.fumito.encryptsampleapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fumito.encryptsampleapp.ui.theme.EncryptSampleAppTheme

class MainActivity : ComponentActivity() {

    companion object {
        private const val PLAIN_TEXT = "This is plain text."
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EncryptSampleAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    ModeList(
                        items = listOf(
                            "AES/CBC/PKCS7Padding",
                            "AES/CBC/PKCS7Padding",
                            "AES/CBC/PKCS7Padding",
                            "AES/CBC/PKCS7Padding",
                            "AES/CBC/PKCS7Padding",
                            "AES/CBC/PKCS7Padding",
                            "AES/CBC/PKCS7Padding",
                            "AES/CBC/PKCS7Padding",
                            "AES/CBC/PKCS7Padding",
                            "AES/CBC/PKCS7Padding",
                            "AES/CBC/PKCS7Padding",
                            "AES/CBC/PKCS7Padding",
                            "AES/CBC/PKCS7Padding",
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ModeList(items: List<String>) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        items.forEach {
            ModeItem(it)
            Divider(color = Color.Gray)
        }
    }
}

@Composable
fun ModeItem(name: String) {
    Text(
        text = name,
        modifier = Modifier
            .clickable { Log.d("@@@@@", "clicked") }
            .padding(24.dp)
            .fillMaxWidth()
    )
}

@Composable
fun DisplayColumn(text: String) {
    Column() {
        // plain
        LabelAndText(
            label = stringResource(R.string.label_plain_text),
            value = text
        )

        // encrypted
        val encrypted = SecurityUtil.encrypt(text)
        LabelAndText(
            label = stringResource(R.string.label_encrypted_value),
            value = encrypted
        )

        // decrypted
        val decrypted = SecurityUtil.decrypt(encrypted)
        LabelAndText(
            label = stringResource(R.string.label_decrypted_value),
            value = decrypted
        )

        // result
        LabelAndText(
            label = stringResource(R.string.label_result),
            value = (text == decrypted).toString()
        )
    }
}

@Composable
fun LabelAndText(label: String, value: String) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Label(label = label)
        DisplayValue(value = value)
    }
}

@Composable
fun Label(label: String) {
    Text(fontSize = 16.sp, text = label)
}

@Composable
fun DisplayValue(value: String) {
    Text(fontSize = 24.sp, text = value)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EncryptSampleAppTheme {
        DisplayColumn(text = "This is plain text")
    }
}
