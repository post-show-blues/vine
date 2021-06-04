package com.post_show_blues.vine;

import com.post_show_blues.vine.domain.member.MemberRepository;
import com.post_show_blues.vine.dto.Auth.SignupDto;
import com.post_show_blues.vine.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
//@RequiredArgsConstructor
public class VineApplication /*implements CommandLineRunner*/ {
//	private final AuthService authService;

	public static void main(String[] args) {
		SpringApplication.run(VineApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception{
//		//given
//		SignupDto memberEntityA = SignupDto.builder()
//				.name("memberA")
//				.email("a")
//				.nickname("aaaa")
//				.password("a")
//				.phone("010-0000-0000")
//				.university("국민대학교")
//				.build();
//		System.out.println("memberEntityA = " + memberEntityA.toEntity());
//		//when
//		authService.join(memberEntityA.toEntity());
//
//	}

}
