package yieon.kakaopay.workplatform.util

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import yieon.kakaopay.workplatform.entity.User
import yieon.kakaopay.workplatform.model.TokenToUserInfo
import java.security.Key
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

/**
 * @author yieon
 * @version default
 * @email parrotbill@naver.com
 * @since 2022-07-23
 * <PRE>
 * ------------------------
 * summary : JwtTokenProvider
 * ------------------------
 * Revision history
 * 2022-07-23. yieon : Initial creation
 * </PRE>
 */
open class JwtTokenProvider(private val secretKey: String) {

    private val PROJECT_NAME = "KAKAOPAY_WORKPLATFORM"

    private var jwtParser: JwtParser = Jwts.parserBuilder()
        .setSigningKey(secretKey.toByteArray())
        .build()

    private fun generateClaim(user: User): Claims {
        val claims = Jwts.claims().setSubject(user.email)
        claims["id"] = user.id
        claims["name"] = user.name
        claims["roles"] = user.authorities.map { r -> r.name }
        claims["project"] = PROJECT_NAME
        return claims
    }

    private fun getSigningKey(key: String): Key {
        return Keys.hmacShaKeyFor(key.toByteArray())
    }

    fun createToken(user: User) =
        buildJwt(user, Date.from(LocalDateTime.now()
            .atZone(ZoneId.systemDefault())
            .plusDays(28)
            .toInstant()))

    private fun buildJwt(user: User, expireDate: Date): String {
        val now = LocalDateTime.now()
        val defaultZonedDateTime = now.atZone(ZoneId.systemDefault())

        val issuedDate = Date.from(defaultZonedDateTime.toInstant())

        return Jwts.builder()
            .setClaims(generateClaim(user))
            .setIssuedAt(issuedDate)
            .setExpiration(expireDate)
            .signWith(getSigningKey(secretKey), SignatureAlgorithm.HS256)
            .compact()
    }

    fun tokenValidate(token: String): Boolean {
        return (jwtParser.parseClaimsJws(token).body.get("project", String::class.java) == PROJECT_NAME)
    }

    fun getUserInfo(token: String): TokenToUserInfo {
        return TokenToUserInfo(
            id = jwtParser.parseClaimsJws(token).body.get("id", Integer::class.java).toLong(),
            name = jwtParser.parseClaimsJws(token).body.get("name", String::class.java),
            roles = jwtParser.parseClaimsJws(token).body["roles"] as List<String>
        )
    }

}