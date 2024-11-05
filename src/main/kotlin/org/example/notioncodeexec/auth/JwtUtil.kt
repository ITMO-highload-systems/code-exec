package org.example.notioncodeexec.auth


import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*

@Service
class JwtUtil {
    @Value("\${application.security.jwt.secret-key}")
    private val secretKey: String? = null

    @Value("\${application.security.jwt.expiration}")
    private val jwtExpiration: Long = 10000

    @Value("\${application.security.jwt.server-expiration}")
    private val jwtServerExpiration: Long = 123456789

    @Value("\${spring.application.name}")
    private val serverName: String = "NOTION-EXEC"

    fun generateServerToken(
    ): String {
        return buildToken(
            mapOf("role" to "ROLE_ADMIN"), object : UserDetails {
                override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
                    return mutableListOf(SimpleGrantedAuthority("ROLE_ADMIN"))
                }

                override fun getPassword(): String {
                    return "password"
                }

                override fun getUsername(): String {
                    return serverName
                }

            },
            jwtServerExpiration
        )
    }

    private fun buildToken(
        extraClaims: Map<String?, Any?>,
        userDetails: UserDetails,
        expiration: Long
    ): String {
        return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + expiration))
            .signWith(signInKey, SignatureAlgorithm.HS256)
            .compact()
    }

    private val signInKey: Key
        get() {
            val keyBytes = Decoders.BASE64.decode(secretKey)
            return Keys.hmacShaKeyFor(keyBytes)
        }
}