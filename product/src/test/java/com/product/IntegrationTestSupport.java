package com.product;

import com.product.global.util.AesUtil;
import com.product.global.util.JsonUtil;
import com.product.global.util.JwtUtil;
import com.product.global.util.SnowflakeGenerator;
import com.product.product.application.port.out.FindProductEsPort;
import com.product.product.application.port.out.FindProductPort;
import com.product.product.application.port.out.ResetProductEsIndex;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(OutputCaptureExtension.class)
public class IntegrationTestSupport {

    @Autowired
    protected CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    protected AesUtil aesUtil;

    @Autowired
    protected JwtUtil jwtUtil;

    @Autowired
    protected JsonUtil jsonUtil;

    @Autowired
    protected RedisTemplate<String, String> redisTemplate;

    @Autowired
    protected KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    protected FindProductPort findProductPort;

    @Autowired
    protected FindProductEsPort findProductEsPort;

    @Autowired
    protected ElasticsearchOperations elasticsearchOperations;

    @Autowired
    protected ResetProductEsIndex resetProductEsIndex;

    @Autowired
    protected SnowflakeGenerator snowflakeGenerator;

    @Value("${jwt.token.secret-key}")
    private String secretKey;

    protected String createAccessToken(String role) {
        Date now = new Date();
        Claims claims = Jwts.claims().setSubject("od@sweettracker.co.kr");
        claims.put("accountId", 1L);
        claims.put("role", role);
        return "Bearer " + Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + 60000))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    protected String createAccessToken(String role, Long accountId) {
        Date now = new Date();
        Claims claims = Jwts.claims().setSubject("od@sweettracker.co.kr");
        claims.put("accountId", accountId);
        claims.put("role", role);
        return "Bearer " + Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + 60000))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }
}
