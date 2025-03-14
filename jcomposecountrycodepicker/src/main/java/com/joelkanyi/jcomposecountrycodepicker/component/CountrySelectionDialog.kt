/*
 * Copyright 2023 Joel Kanyi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.joelkanyi.jcomposecountrycodepicker.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.joelkanyi.jcomposecountrycodepicker.data.CountryData
import com.joelkanyi.jcomposecountrycodepicker.utils.PickerUtils.getCountryName
import com.joelkanyi.jcomposecountrycodepicker.utils.PickerUtils.getFlags
import com.joelkanyi.jcomposecountrycodepicker.utils.PickerUtils.searchForAnItem

/**
 * [CountrySelectionDialog] is a composable that displays a dialog with a list of countries.
 * [modifier] Modifier to be applied to the layout.
 * [countryList] The list of countries to be displayed in the dialog.
 * [onDismissRequest] Called when the dialog is dismissed.
 * [onSelected] Called when a country is selected.
 * [properties] The properties of the dialog.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountrySelectionDialog(
    modifier: Modifier = Modifier,
    countryList: List<CountryData>,
    onDismissRequest: () -> Unit,
    onSelected: (item: CountryData) -> Unit,
    properties: DialogProperties = DialogProperties().let {
        DialogProperties(
            dismissOnBackPress = it.dismissOnBackPress,
            dismissOnClickOutside = it.dismissOnClickOutside,
            securePolicy = it.securePolicy,
            usePlatformDefaultWidth = false,
        )
    },
    containerColor: Color,
    contentColor: Color,
) {
    var searchValue by remember { mutableStateOf("") }
    var isSearch by remember { mutableStateOf(false) }
    var filteredItems by remember { mutableStateOf(countryList) }

    AlertDialog(
        containerColor = containerColor,
        tonalElevation = 0.dp,
        modifier = Modifier
            .fillMaxSize(),
        shape = RoundedCornerShape(0),
        onDismissRequest = onDismissRequest,
        properties = properties,
        text = {
           Scaffold(
               containerColor = containerColor,
                topBar = {
                    val focusRequester = remember { FocusRequester() }
                    LaunchedEffect(isSearch) {
                        if (isSearch) {
                            focusRequester.requestFocus()
                        }
                    }
                    CenterAlignedTopAppBar(
                        title = {
                            if (isSearch) {
                                TextField(
                                    modifier = Modifier.focusRequester(focusRequester),
                                    value = searchValue,
                                    onValueChange = { searchStr ->
                                        searchValue = searchStr
                                        filteredItems = countryList.searchForAnItem(searchStr)
                                    },
                                    placeholder = {
                                        Text(
                                            text = "Search...",
                                            color = contentColor.copy(alpha = 0.5f),
                                        )
                                    },
                                    colors = TextFieldDefaults.colors(
                                        disabledContainerColor = Color.Transparent,
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        disabledIndicatorColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        unfocusedTextColor = contentColor,
                                        focusedTextColor = contentColor,
                                        focusedPlaceholderColor = contentColor,
                                        unfocusedPlaceholderColor = contentColor,
                                        cursorColor = contentColor,
                                    ),
                                    textStyle = MaterialTheme.typography.labelLarge.copy(
                                        color = contentColor,
                                    ),
                                )
                            } else {
                                Text(
                                    modifier = Modifier.offset(y = (-2).dp),
                                    text = "Select Country",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = contentColor,
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                onDismissRequest()
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = null,
                                    tint = contentColor,
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                isSearch = !isSearch
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = contentColor,
                                )
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = containerColor,
                            titleContentColor = contentColor,
                        )
                    )
                },
            ) { paddingValues ->
                Column {
                    LazyColumn(
                        Modifier
                            .padding(paddingValues)
                            .fillMaxSize(),
                    ) {
                        val countriesData =
                            if (searchValue.isEmpty()) {
                                countryList
                            } else {
                                filteredItems
                            }

                        items(countriesData) { countryItem ->
                            ListItem(
                                colors = ListItemDefaults.colors(
                                    containerColor = containerColor,
                                    trailingIconColor = contentColor,
                                    headlineColor = contentColor,
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onSelected(countryItem)
                                    },
                                leadingContent = {
                                    Image(
                                        modifier = modifier.width(30.dp),
                                        painter = painterResource(
                                            id = getFlags(
                                                countryItem.countryCode,
                                            ),
                                        ),
                                        contentDescription = null,
                                    )
                                },
                                trailingContent = {
                                    Text(
                                        text = countryItem.cCountryPhoneNoCode,
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                },
                                headlineContent = {
                                    Text(
                                        stringResource(id = getCountryName(countryItem.countryCode.lowercase())),
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                },
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {}
    )
}
