package com.example.gmfastfood.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gmfastfood.data.UserAddress
import com.example.gmfastfood.map.LeafletMapScreen

@Composable
fun AddressDetailsPopup(
    address: UserAddress,
    onDismiss: () -> Unit
) {

    Dialog(
        onDismissRequest = {
//                showAddAddressDialog = false
            onDismiss()
        }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
//                .padding(4.dp),
            , shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text( "Address Details", style = MaterialTheme.typography.titleLarge  )
                Text("Label: ${address.label}")
                Text("Full Address: ${address.fullAddress}")
                Text("Latitude: ${address.latitude}")
                Text("Longitude: ${address.longitude}")
                Text("Note: ${address.note}")
                Text("Is Default: ${address.isDefault}")

                Box(modifier = Modifier.fillMaxWidth().height(400.dp)) {
//                    LeafletMap( startingLocation = "",
//                        onLocationSelected = { lat, lng -> onSelectedLocation(lat, lng)   } )
                    LeafletMapScreen( latitude = address.latitude, longitude = address.longitude)
                }
            }
        }
    }
}