package com.wanpaku.pochi.infra.api.request

import proguard.annotation.KeepClassMemberNames

@KeepClassMemberNames
data class UpdateBookingStatusRequest(val status: String)