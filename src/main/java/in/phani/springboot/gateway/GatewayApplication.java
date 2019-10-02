package in.phani.springboot.gateway;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@SpringBootApplication
//@EnableDiscoveryClient
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder) {
		ZonedDateTime from = LocalDateTime.now().atZone(ZoneId.systemDefault());
		ZonedDateTime to = LocalDateTime.now().plusMinutes(3).atZone(ZoneId.systemDefault());

		return builder.routes()
				.route(r -> r.path("/somepath", "/SomePath").uri("http://localhost:8090/pathpattern"))

				.route(r -> r.method(HttpMethod.DELETE).uri("http://localhost:8090/endpointfordelete"))

				.route(r -> r.header("myheader", "phani").uri("http://localhost:8090/endpointwithheader"))

				.route(r -> r.between(from, to).uri("http://localhost:8070/endpointbetweentime"))

				.route(r -> r.query("name", "phani").uri("http://localhost:8070/endpointwithqueryparams"))

				.route(r -> r.path("/requestheaderpath").filters(f -> f.addRequestHeader("addedheader", "addedvalue"))
						.uri("http://localhost:8090/endpointwithrequestheader"))

				.route(r -> r.path("/responseheaderpath").filters(f -> f.addResponseHeader("addedheader", "addedvalue"))
						.uri("http://localhost:8060/endpointwithresponseheader"))

				.route(r -> r.method(HttpMethod.GET).filters(f -> f.rewritePath("/phanipath", "/kiranpath"))
						.uri("http://localhost:8090")) // call http://localhost:8080/phanipath === redirects to ==> http://localhost:8090/kiranpath

				//.route(r -> r.path("/greeting").uri("lb://eureka-client1"))
				.build();
	}
}

