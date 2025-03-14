import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bestmakina.depotakip.component.card.ExpandableCard
import com.bestmakina.depotakip.presentation.ui.view.TransferWithRecete.TransferWithReceteAction
import com.bestmakina.depotakip.presentation.ui.view.TransferWithRecete.viewmodel.TransferWithReceteViewModel

@Composable
fun TransferWithReceteView(navController: NavHostController, transferWithReceteViewModel: TransferWithReceteViewModel = hiltViewModel()) {

    val state = transferWithReceteViewModel.state.collectAsStateWithLifecycle().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(12.dp)
    ) {
        ExpandableCard(
            itemList = state.personnelList,
            selectedItem = state.selectedPersonnel,
            onItemSelected = { selectedItem ->
                transferWithReceteViewModel.handleAction(TransferWithReceteAction.SelectItem("selectedPersonnel", selectedItem))
            }
        )
    }

}