package com.wanpaku.pochi.infra.api.response

import proguard.annotation.KeepClassMemberNames

@KeepClassMemberNames
data class PaginationResponse<T>(val nextCursor: String,
                                 val items: List<T>)