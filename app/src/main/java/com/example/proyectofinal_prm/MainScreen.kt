package com.example.proyectofinal_prm

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.proyectofinal_prm.ui.pages.HomePage
import com.example.proyectofinal_prm.ui.pages.NotificationPage
import com.example.proyectofinal_prm.ui.pages.ProfilePage
import com.example.proyectofinal_prm.ui.pages.SettingsPage

@Composable
fun MainScreen (modifier: Modifier = Modifier ){

    val navItemList = listOf(
        NavItem("Home", Icons.Default.Home, 0),
        NavItem("Alerts", Icons.Default.Notifications, 5),
        NavItem("Settings", Icons.Default.Settings, 0),
        NavItem("Profile", Icons.Default.Person, 0)
    )

    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                Box {
                    NavigationBar {
                        navItemList.take(2).forEachIndexed { index, navItem ->
                            NavigationBarItem(
                                selected = selectedIndex == index,
                                onClick = {
                                    selectedIndex = index
                                },
                                icon = {
                                    BadgedBox(badge = {
                                        if (navItem.badgeCount>0)
                                            Badge(){
                                                Text(text = navItem.badgeCount.toString())
                                            }
                                    }) {
                                        Icon(imageVector = navItem.icon , contentDescription = "Icon" )
                                    }
                                },
                                label = {
                                    Text(text = navItem.label)
                                }
                            )
                        }

                        NavigationBarItem(
                            selected = false,
                            onClick = { },
                            icon = {
                                Box {}
                            },
                            label = {
                                Text(text = "")
                            }
                        )

                        navItemList.drop(2).forEachIndexed { index, navItem ->
                            NavigationBarItem(
                                selected = selectedIndex == index + 2,
                                onClick = {
                                    selectedIndex = index + 2
                                },
                                icon = {
                                    BadgedBox(badge = {
                                        if (navItem.badgeCount>0)
                                            Badge(){
                                                Text(text = navItem.badgeCount.toString())
                                            }
                                    }) {
                                        Icon(imageVector = navItem.icon , contentDescription = "Icon" )
                                    }
                                },
                                label = {
                                    Text(text = navItem.label)
                                }
                            )
                        }
                    }

                    FloatingActionButton(
                        onClick = { },
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = (-30).dp)
                            .size(66.dp)
                            .clip(CircleShape)
                            .zIndex(1f),
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add"
                        )
                    }
                }
            }
        ) { innerPadding ->
            ContentScreen(modifier = Modifier.padding(innerPadding), selectedIndex)
        }
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex : Int){
    when(selectedIndex){
        0-> HomePage()
        1-> NotificationPage()
        2-> SettingsPage()
        3-> ProfilePage()
    }
}