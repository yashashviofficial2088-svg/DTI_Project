package com.example.dti_project

import android.text.Layout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors

import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle

@Composable
fun login_page(){
    var email_val by remember{ mutableStateOf("")}
    var password_val by remember{ mutableStateOf("")}
    var checkbox_status by remember{mutableStateOf(false)}

    Box(
        modifier = Modifier.fillMaxSize().background(color = Color.Black),

    ){
        Box(
            modifier = Modifier.fillMaxWidth().height(257.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(painter = painterResource(R.drawable.loginpageelement1), contentDescription = "", modifier = Modifier.height(250.dp).width(246.dp))
        }
        Box(
            modifier = Modifier.fillMaxWidth().height(257.dp)
                .background(brush = Brush.verticalGradient(
                    colors=listOf(colorResource(R.color.primaryButton),Color.Transparent)
                ))
        )
        Box(
            modifier = Modifier.fillMaxWidth().height(257.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(painter = painterResource(R.drawable.loginpageelement2), contentDescription = "", modifier = Modifier.height(250.dp).width(246.dp))
        }


    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(257.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome Back!",color=Color.White, fontSize = 31.sp)
            Text("Sign in to Access smart realtime Noise updates",color=Color.White, fontSize = 14.sp)

        }
        Column(
           horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))
            Text("Email address*", fontSize = 12.sp)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(

                colors=OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, unfocusedBorderColor = Color.White, focusedBorderColor = Color.White)
                ,value=email_val,
                onValueChange = {email_val=it},
                singleLine = true,
                modifier = Modifier.width(334.dp),
                shape=RoundedCornerShape(18.dp),
                placeholder = {Text("example@gmail.com",color=Color.White)}
            )
            Spacer(modifier = Modifier.height(8.dp))
            Spacer(Modifier.height(16.dp))
            Text("Password", fontSize = 12.sp)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value=password_val,colors= OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White, unfocusedBorderColor = Color.White, focusedBorderColor = Color.White),
                 onValueChange = {password_val=it},
                singleLine = true,
                modifier = Modifier.width(334.dp),
                shape=RoundedCornerShape(18.dp),
                placeholder = {Text("@Sn123hsn#",color=Color.White)}
            )
            Row(
                modifier = Modifier.width(334.dp),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                Checkbox(colors = CheckboxDefaults.colors(uncheckedColor = Color.White, checkedColor = colorResource(R.color.primaryButton)),checked = checkbox_status, onCheckedChange = {checkbox_status=it})
                Text("Remember me",color=Color.White, fontSize =12.sp )
                Spacer(Modifier.weight(1f))
                Text("Forgot Password?",color=Color.White, fontSize = 12.sp, modifier = Modifier.clickable(onClick = {}))
            }
           fadingDividerText("Or Continue with")
            Spacer(Modifier.height(41.dp))
            Button(
                colors= ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primaryButton)),
                onClick={},
                modifier = Modifier.width(334.dp).height(56.dp),
                shape = RoundedCornerShape(36.dp)
            ) {
                Text("Sign in")
            }
            Spacer(Modifier.height(64.dp))
            Row(
                modifier = Modifier.width(334.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    modifier = Modifier.height(56.dp).width(146.dp),
                    onClick = {},
                    colors=ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primaryButton), contentColor = Color.White)
                ) {
                    Text("Google")
                }
                Spacer(Modifier.weight(1f))
                Button(
                    colors=ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primaryButton), contentColor = Color.White),
                    modifier = Modifier.height(56.dp).width(146.dp),
                    onClick = {}
                ) {
                    Text("apple")
                }

            }
            Spacer(Modifier.weight(1f))
            Row(
               modifier = Modifier.width(334.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Don't have an account?",color=Color.White)
                Text("Sign Up",color=Color.White, modifier = Modifier.clickable(onClick = {}))
            }

        }
    }
}




@Composable
fun fadingDividerText(value:String){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, Color.Gray)
                    )
                )
        )
        Text(value,color=Color.White, modifier = Modifier.padding(horizontal = 8.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Gray, Color.Transparent)
                    )
                )
        )
    }


}

@Preview(showBackground = true)
@Composable
fun login_page_preview(){
    login_page()
}


