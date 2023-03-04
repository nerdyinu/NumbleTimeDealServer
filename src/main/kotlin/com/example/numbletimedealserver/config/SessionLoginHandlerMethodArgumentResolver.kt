package com.example.numbletimedealserver.config




import com.example.numblebankingserverchallenge.config.SessionLogin
import com.example.numbletimedealserver.domain.ROLE
import com.example.numbletimedealserver.dto.CustomerDto
import com.example.numbletimedealserver.exception.CustomException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class SessionLoginHandlerMethodArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
         return CustomerDto::class.javaObjectType.isAssignableFrom(parameter.parameterType) && parameter.hasParameterAnnotation(
            SessionLogin::class.java
        )

    }
    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): CustomerDto {
        val request = webRequest.nativeRequest as? HttpServletRequest

        val member =
            request?.session?.getAttribute("user") as? CustomerDto ?: throw CustomException.NotLoggedInException()
        val isAdmin = parameter.getParameterAnnotation(SessionLogin::class.java)!!.admin
        if(isAdmin && member.role!=ROLE.ADMIN)throw CustomException.ForbiddenException()
        return member
    }
}