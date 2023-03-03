package com.example.numbletimedealserver.config

import com.example.numblebankingserverchallenge.config.SessionLogin
import com.example.numblebankingserverchallenge.exception.CustomException
import com.example.numbletimedealserver.domain.ROLE
import com.example.numbletimedealserver.dto.CustomerDto
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class SessionAdminHandlerMethodArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return CustomerDto::class.javaObjectType.isAssignableFrom(parameter.parameterType) && parameter.hasParameterAnnotation(
            SessionAdmin::class.java
        )
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val request = webRequest.nativeRequest as? HttpServletRequest
        val member =
            request?.session?.getAttribute("user") as? CustomerDto ?: throw CustomException.NotLoggedInException()
        if(member.role!=ROLE.ADMIN)throw CustomException.ForbiddenException()
        return member
    }
}