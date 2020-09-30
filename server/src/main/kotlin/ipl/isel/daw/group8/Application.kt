package ipl.isel.daw.group8

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import ipl.isel.daw.group8.common.JSON_HOME_MEDIA_TYPE
import ipl.isel.daw.group8.common.JSON_MEDIA_TYPE
import ipl.isel.daw.group8.common.PROBLEM_JSON_MEDIA_TYPE
import ipl.isel.daw.group8.common.SIREN_MEDIA_TYPE
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.core.spi.JdbiPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import org.jdbi.v3.sqlobject.SqlObjectPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.nio.charset.Charset

@SpringBootApplication
class Application

@Configuration
@EnableWebMvc
class ApplicationConfig: WebMvcConfigurer
{
	override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
		val jsonConverter = converters.find { it is MappingJackson2HttpMessageConverter } as MappingJackson2HttpMessageConverter
		jsonConverter.objectMapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
		jsonConverter.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
		jsonConverter.defaultCharset = Charset.forName("UTF-8")
		jsonConverter.supportedMediaTypes = listOf(JSON_MEDIA_TYPE, SIREN_MEDIA_TYPE, PROBLEM_JSON_MEDIA_TYPE, JSON_HOME_MEDIA_TYPE)
	}
}

@Configuration
class DatabaseConfig(
	@Value("jdbc:postgresql:DAW") val url: String,
	@Value("postgres") val username: String,
	@Value("postgres") val password: String
) {
	@Bean
	fun plugins(): List<JdbiPlugin> {
		return listOf(
			PostgresPlugin(),
			SqlObjectPlugin(),
			KotlinPlugin(),
			KotlinSqlObjectPlugin()
		)
	}

	@Bean
	fun dataSource(plugins: List<JdbiPlugin>): Jdbi {
		val dataSource = Jdbi.create(url, username, password)
		plugins.forEach { dataSource.installPlugin(it) }

		return dataSource
	}
}

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}