package com.wanpaku.pochi.infra.api.request

import proguard.annotation.KeepClassMemberNames

@KeepClassMemberNames
data class RequestBookingRequest(val dogIds: List<Long>,
                                 val startDate: String,
                                 val endDate: String,
                                 val cardToken: String)