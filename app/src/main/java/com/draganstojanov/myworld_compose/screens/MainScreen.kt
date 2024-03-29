package com.draganstojanov.myworld_compose.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.draganstojanov.myworld_compose.R
import com.draganstojanov.myworld_compose.composables.ButtonStandard
import com.draganstojanov.myworld_compose.composables.CustomTopAppBar
import com.draganstojanov.myworld_compose.ui.theme.colorPrimary
import com.draganstojanov.myworld_compose.ui.theme.colorSecondary
import com.draganstojanov.myworld_compose.ui.theme.colorWhite
import com.draganstojanov.myworld_compose.util.constants.FilterEventType
import com.draganstojanov.myworld_compose.util.constants.FilterEventType.*
import com.draganstojanov.myworld_compose.util.navigation.NavScreens
import com.draganstojanov.myworld_compose.viewModel.MainViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val countries = viewModel.countriesState.value
    if (countries.isNotEmpty()) {
        Scaffold(
            topBar = {
                CustomTopAppBar(
                    title = stringResource(id = R.string.app_name),
                    navController = navController,
                    hasBackButton = false
                )
            }, content = {
                AllCountries(viewModel = viewModel) {
                    var cList: String = Json.encodeToString(viewModel.filteredCountryList.value)
                    cList = cList.replace("/", "*#=@*")
                    navController.navigate(
                        "${NavScreens.CountryListScreen.name}/${cList}/${it}"
                    )
                }
            }
        )
    }
}


@Composable
fun AllCountries(
    viewModel: MainViewModel,
    onSelectItem: (String?) -> Unit
) {
    val allCountries = stringResource(id = R.string.all_countries)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = dimensionResource(id = R.dimen.padding_horizontal))
            .verticalScroll(rememberScrollState())

    ) {
        MainScreenCard(
            title = stringResource(id = R.string.continents),
            list = viewModel.continentsState.value.toList(),
            viewModel = viewModel,
            eventType = CONTINENT,
            onSelectItem = onSelectItem
        )

        MainScreenCard(
            title = stringResource(id = R.string.regions),
            list = viewModel.regionsState.value.toList(),
            viewModel = viewModel,
            eventType = REGION,
            onSelectItem = onSelectItem
        )

        MainScreenCard(
            title = stringResource(id = R.string.subregions),
            list = viewModel.subregionsState.value.toList(),
            viewModel = viewModel,
            eventType = SUBREGION,
            onSelectItem = onSelectItem
        )

        ButtonStandard(
            modifier = Modifier.padding(bottom = 32.dp, top = 16.dp),
            stringRes = R.string.all_countries,
            onCLick = {
                viewModel.filterEvent(ALL, allCountries)
                onSelectItem(allCountries)
            })
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainScreenCard(
    title: String,
    list: List<String>,
    viewModel: MainViewModel,
    eventType: FilterEventType,
    onSelectItem: (String?) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .border(
                width = 2.dp,
                color = colorPrimary,
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.rounded_corner)),
            )
            .fillMaxWidth()
            .background(colorWhite)
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.rounded_corner)))

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorPrimary)
                .height(dimensionResource(id = R.dimen.element_height_standard)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = colorWhite
                )
            )
        }

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            for (item in list) {
                Card(
                    backgroundColor = colorSecondary,
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            viewModel.filterEvent(eventType, item)
                            onSelectItem(item)
                        },
                    elevation = dimensionResource(id = R.dimen.elevation_value),
                ) {
                    Text(
                        text = item,
                        modifier =
                        Modifier.padding(
                            vertical = 4.dp,
                            horizontal = 12.dp
                        ),
                        style = TextStyle(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = colorWhite,
                            textAlign = TextAlign.Center,
                        )
                    )
                }
            }
        }
    }
}









