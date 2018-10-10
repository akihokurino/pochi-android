package com.wanpaku.pochi.infra.api.response

import proguard.annotation.KeepClassMemberNames

@KeepClassMemberNames
data class ListResponse<out T>(val items: List<T>)