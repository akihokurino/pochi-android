package com.wanpaku.pochi.infra.api.request

import proguard.annotation.KeepClassMemberNames

@KeepClassMemberNames
data class CreateMessageRequest(val from: String,
                                val content: String)
