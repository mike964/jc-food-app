package com.example.gmfastfood.profile
import androidx.appcompat.app.AppCompatDelegate
import com.example.gmfastfood.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import com.example.gmfastfood.LanguageManager
import com.example.gmfastfood.auth.LoginPopup
import com.example.gmfastfood.auth.UserProfile
import com.example.gmfastfood.changeLanguage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profile: UserProfile?,
    isAuthenticated: Boolean,
    onLogout: () -> Unit,
    onLoginSubmitted: (String, String) -> Unit,
    onOrdersClick: () -> Unit,
    onAddressesClick: () -> Unit,
    onBackClick: () -> Boolean,
) {
    val context = LocalContext.current
    // Remember the manager so it isn't recreated on every recomposition
    val languageManager = remember { LanguageManager(context) }

    var showLoginPopup by remember { mutableStateOf(false) }
    // 1. Maintain the toggle state
    var isChecked by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.my_profile), fontWeight = FontWeight.Bold) },
                // Left-side button (e.g., Back or Navigation drawer)
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClick()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle edit profile */ }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF8F9FA)) // Subtle light background grey
                .verticalScroll(rememberScrollState())
        ) {

            LoginPopup(
                isOpen = showLoginPopup,
                onDismiss = { showLoginPopup = false },
                onLoginSubmitted = onLoginSubmitted
            )

            if(!isAuthenticated){
                Row(Modifier.padding(32.dp, 16.dp, )) {
                    Button(
                        onClick = { showLoginPopup = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Log in", modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }

            // 1. Profile Header Section
            if (isAuthenticated && profile != null) {
                ProfileHeader(
                    name = profile.username,
                    email = profile.email,
                    avatarLabel = "AM"
                )

            }

            // 2. Performance/Activity Stats Row
            ProfileStatsRow()

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Action / Settings Options List
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column {
                    ProfileMenuItem(
                        icon = Icons.Default.ShoppingCart,
                        label = stringResource(id = R.string.orders)
                    ) {
                        onOrdersClick()
                    }
                    HorizontalDivider(
                        color = Color(0xFFF1F1F1),
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    ProfileMenuItem(
                        icon = Icons.Default.Home,
                        label = stringResource(id = R.string.address)
                    ) {
                        onAddressesClick()
                    }
                    HorizontalDivider(
                        color = Color(0xFFF1F1F1),
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    ProfileMenuItem(
                        icon = Icons.Default.Notifications,
                        label = stringResource(id = R.string.notifications)
                    ) { /* Navigate */ }
                    HorizontalDivider(
                        color = Color(0xFFF1F1F1),
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    ProfileMenuItem(
                        icon = Icons.Default.Settings,
                        label =  stringResource(id = R.string.preferences)
                    ) { /* Navigate */ }
                    HorizontalDivider(
                        color = Color(0xFFF1F1F1),
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    ProfileMenuItem(
                        icon = Icons.Default.Person,
                        label = stringResource(id = R.string.privacy_and_security)
                    ) { /* Navigate */ }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row{
                Button(onClick = {
//                    changeLanguage("en")
                    languageManager.saveLanguage("en")
                    changeLanguage("en")
                }) {
                    Text("English")
                }

                Button(onClick = {
                    languageManager.saveLanguage("ar")
                    changeLanguage("ar")
                }) {
                    Text("Arabic")
                }
                Switch(
                    checked = isChecked,
                    onCheckedChange = { newValue ->
                        isChecked = newValue
                    }
                )
            }

           if(isAuthenticated){
               TextButton(
                   onClick = {  onLogout() },
                   modifier = Modifier.align(Alignment.CenterHorizontally)
               ) {
                   Text(
                       "Log Out",
                       color = MaterialTheme.colorScheme.error,
                       fontWeight = FontWeight.SemiBold
                   )
               }
           }
        }
    }
}


// ==========================================
// COMPONENT SUB-VIEWS
// ==========================================
@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Navigate Next",
            tint = Color.LightGray
        )
    }
}