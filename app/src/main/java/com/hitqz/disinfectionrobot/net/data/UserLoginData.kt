package com.hitqz.disinfectionrobot.net.data

data class UserLoginData(
        val token: String,//访问令牌
        val refreshToken: String,//刷令牌
        val tokenHead: String,//访问令牌前缀
        val expiresIn: Long//有效时间（秒）
)