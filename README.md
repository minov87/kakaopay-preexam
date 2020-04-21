## Kakaopay 서버개발 사전과제

> Rest API 기반 쿠폰시스템 개발

### 개발 프레임워크
* Java 1.8
* Spring Boot 2.2.6
* JPA
* JUnit 4
* Gradle
* Lombok

### 문제해결 전략
#### 필수 문제 
- [x] [#1](https://github.com/minov87/kakaopay-preexam/issues/1) 랜덤 쿠폰 N개 생성 API
- [x] [#2](https://github.com/minov87/kakaopay-preexam/issues/2) 사용자에게 쿠폰 지급 API 
- [x] [#3](https://github.com/minov87/kakaopay-preexam/issues/3) 사용자 지급 쿠폰 조회 API
- [x] [#4](https://github.com/minov87/kakaopay-preexam/issues/4) 지급된 쿠폰 사용 API
- [x] [#5](https://github.com/minov87/kakaopay-preexam/issues/5) 지급된 쿠폰 사용 취소 API
- [x] [#6](https://github.com/minov87/kakaopay-preexam/issues/6) 발급된 쿠폰 중 당일 만료된 쿠폰 목록 조회 API

##### 제약사항 필수
- [ ] 단위 테스트 (Unit Test) 코드를 개발하여 각 기능을 검증.
- [ ] README.md 파일을 추가하여, 개발 프레임워크, 문제해결 전략, 빌드 및 실행 방법을 기술.

##### 제약사항 선택
- [x] [#9](https://github.com/minov87/kakaopay-preexam/issues/9) JWT를 이용 Token 기반 API 인증 기능 개발 및 각 API 호출시 HTTP Header에 발급받은 토큰을 가지고 호출.
  - [x] [#7](https://github.com/minov87/kakaopay-preexam/issues/7) signup 계정생성 API: ID, PW를 입력 받아 내부 DB에 계정을 저장하고 토큰을 생성하여 출력.
  - [x] [#8](https://github.com/minov87/kakaopay-preexam/issues/8) signin 로그인 API: 입력으로 생성된 계정 (ID, PW)으로 로그인 요청하면 토큰을 발급.
- [ ] 100억개 이상 쿠폰 관리 저장 관리 가능 하도록 구현 
- [ ] 10만개 이상 벌크 csv Import 기능
- [ ] 대용량 트랙픽(TPS 10K 이상)을 고려한 시스템 구현
- [ ] 성능테스트 결과 / 피드백

### 빌드 및 실행방법