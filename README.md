# Spring Boot 기술 적용 프로젝트

## 프로젝트 개요
이 프로젝트는 Spring Boot를 사용하여 다양한 기술과 기능을 적용해보기 위한 샘플 프로젝트입니다. 목표는 Spring Boot의 다양한 기능을 학습하고 실제로 적용해보는 것입니다.

## 개발 환경
| Category           |                  | 
|--------------------|------------------|
| 🛠️ IDE            | IntelliJ         |
| 🏁 Language        | JAVA17           |
| 🔗 Framework       | Springboot 3.3.0 |
| ⚙️ Project         | Gradle           |
| 🌏️ Server port    | 20001            |
| 🗄️ Database       | MySQL 8.3.0      |
| 🔐 Spring Security | 6.3.0            |


## 적용 기술
`SpringBoot` , `Gradle` , `MySQL` , `MyBatis` , `JPA` , 
`Spring Security` , `JWT` , 
`Lombok` , `ModelMapper` , `dotenv` , 
`Swagger`

## 구현 기능
- [x] [Spring Boot 기본 설정](https://tyan-8.tistory.com/16)
- [x] [Local MySQL 데이터베이스와 MyBatis 연동](https://tyan-8.tistory.com/21)
- [x] [Spring Data JPA를 사용한 데이터베이스 연동](https://tyan-8.tistory.com/35)
- [x] [Spring Security를 사용한 회원가입-패스워드인코딩](https://tyan-8.tistory.com/24)
- [x] [Spring Security를 사용한 jwt 인증](https://tyan-8.tistory.com/25)
- [x] [Spring Security를 사용한 Oauth2 소셜 로그인](https://tyan-8.tistory.com/29)
- [x] Spring Security를 사용한 역할 기반 접근 제어 관리
- [x] [Springdoc OpenAPI를 사용한 swagger 연결](https://tyan-8.tistory.com/34)
- [x] RESTful API 구현
- [x] [환경 변수를 통한 보안 정보 관리](https://tyan-8.tistory.com/38)
- [x] Response body 및 응답코드 커스텀
- [x] [.jar 파일을 사용한 로컬 서버로 배포](https://tyan-8.tistory.com/37)

```
- [ ] (파일1) 파일 업로드 및 다운로드  기능 구현
- [ ] (파일2) 멀티파트 업로드
- [ ] (파일3) 대용량 파일 업로드 스레드로 비동기 처리하기 (여러 파일 동시 업로드)
- [ ] (파일4) 대용량 파일 업로드 스케줄링 추가하기
- [ ] 이메일 발송 기능 (비밀번호 재설정)
- [ ] TDD 기반 개발, 테스트 자동화
- [ ] docker로 배포하기
- [ ] Jenkins로 CI/CD 자동화 하기
- [ ] Jenkins와 docker를 연동하여 CI/CD 파이프라인 구축하기
- [ ] docker에 DB 올리기
- [ ] GraphQL
- [ ] 모니터링 (Spring Boot Actuator)
- [ ] 모니터링 (Prometheus와 Grafana)
- [ ] 웹소켓 기반 알림 시스템 (실시간 채팅)
- [ ] SSE 기반 알림 시스템 (실시간 주식)
- [ ] 메시징 (RabbitMQ, Kafka)
```

## 주요 엔드포인트
- #### Swagger 접속 : http://0.0.0.0:20001/swagger-ui/index.html
- #### 소셜 로그인 : http://0.0.0.0:20001/login
- #### 관리자 권한 : http://0.0.0.0:20001/admin/**


## 환경 변수 설정
``` ini
MYSQL_PROJECT_URL=
MYSQL_USERNAME=
MYSQL_PASSWORD=

OAUTH2_GOOGLE_CLIENT_ID=
OAUTH2_GOOGLE_CLIENT_SECRET=
OAUTH2_NAVER_CLIENT_ID=
OAUTH2_NAVER_CLIENT_SECRET=
OAUTH2_KAKAO_CLIENT_ID=
OAUTH2_KAKAO_CLIENT_SECRET=
```

## /keys : 주요 Secret Files
> 이 디렉토리에는 애플리케이션의 보안과 관련된 중요한 파일들이 포함되어 있습니다. 각 파일의 역할과 사용 방법은 다음과 같습니다:
> - **prod.env** : 프로덕션 환경에서 필요한 환경 변수들을 정의한 파일입니다.
> - **secret.key** : JWT 암호화를 위한 비밀 키 파일입니다. 토큰의 생성 및 검증 시 사용됩니다.
