package alex.eros.pokeappdex.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ErroMsgDialog(msg:String,isError:Boolean){
    Row(
        modifier = Modifier
            .background(
                shape = RoundedCornerShape(9.dp),
                color = if(isError) Color(0xFFFF4757) else Color(0xFF595959)
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Icon(
            imageVector = if (isError) ImageVector.vectorResource(id = alex.eros.pokeappdex.R.drawable.ic_error_msg)
            else ImageVector.vectorResource(id = alex.eros.pokeappdex.R.drawable.ic_check),
            contentDescription = "",
            tint = Color.White,
            modifier = Modifier
                .height(28.dp)
                .width(28.dp)
                .padding(start = 8.dp)
        )
        Text(
            textAlign = TextAlign.Center,
            text = msg,
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 8.dp)
                .height(intrinsicSize = IntrinsicSize.Min),
            color = Color.White,
            fontSize = 17.sp,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun ErroMsgDialog() {
    ErroMsgDialog(msg = "Hola",true)
}