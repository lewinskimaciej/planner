package com.atc.planner.presentation.main.adapter

import com.atc.planner.data.models.remote.nearby_places.PhotoReferenceDetails

data class PlaceItemModel(var id: String?,
                          var title: String?,
                          var description: String?,
                          var photo: PhotoReferenceDetails?)