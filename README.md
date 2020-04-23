## Kakaopay 서버개발 사전과제

> Rest API 기반 쿠폰시스템 개발

### 개발 프레임워크
* Java 1.8
* Spring Boot 2.2.6
* JPA
* JUnit5
* Gradle
* Lombok

### 문제해결 전략
#### 필수 문제 
- [x] [#1](https://github.com/minov87/kakaopay-preexam/issues/1) 랜덤 쿠폰 N개 생성 API
  - 랜덤 쿠폰 형식은 지정된 형태가 없으므로 ####-KP연도(2자리)-####-#### 형식으로 만든다.  
    (KPXX 의 경우 16자리 랜덤 쿠폰의 경우 약 1600만개 정도 등록시 중복되는 문제가 발생하므로  
    구분 코드를 삽입하여 중복 문제 해결 및 앞 코드를 용도별로 정하거나, 연도로 구분할 수 있도록 구상)
  - N 개가 요청되지 않을 경우에는 에러 출력.
  - 옵션으로 만료일자를 지정할 수 있도록 추가.   
    (yyyy-MM-dd HH:mm:ss 형식으로 요청. 단, 만료일자는 이전으로 해도 에러처리하지 않는다.)
- [x] [#2](https://github.com/minov87/kakaopay-preexam/issues/2) 사용자에게 쿠폰 지급 API 
  - 지급할 쿠폰이 존재하지 않을 경우 에러를 반환한다.
  - 해당하는 사용자가 존재하지 않을 경우 에러를 반환한다.
- [x] [#3](https://github.com/minov87/kakaopay-preexam/issues/3) 사용자 지급 쿠폰 조회 API
  - 지급된 쿠폰이 없을 경우 빈 배열 [] 형태로 반환한다.
  - 지급된 쿠폰이 있을 경우 (쿠폰 번호, 쿠폰 상태, 쿠폰 만료일) 데이터를 반환한다.
  - 해당하는 사용자가 존재하지 않을 경우 에러를 반환한다.
- [x] [#4](https://github.com/minov87/kakaopay-preexam/issues/4) 지급된 쿠폰 사용 API
  - 사용자 정보와 쿠폰 코드 정보를 받아 일치하는 쿠폰 보관함의 데이터만 사용처리.
  - 쿠폰 코드와 사용자 정보가 매칭되는 데이터가 없을 경우 에러를 반환한다.
  - 해당하는 사용자가 존재하지 않을 경우 에러를 반환한다.
- [x] [#5](https://github.com/minov87/kakaopay-preexam/issues/5) 지급된 쿠폰 사용 취소 API
  - 사용자 정보와 쿠폰 코드 정보를 받아 일치하는 쿠폰 보관함의 데이터만 사용 취소 처리.
  - 쿠폰 코드와 사용자 정보가 매칭되는 데이터가 없을 경우 에러를 반환한다.
  - 해당하는 사용자가 존재하지 않을 경우 에러를 반환한다.
  - 쿠폰의 기간이 만료되었을 경우 사용취소 처리하지 않고 에러를 반환한다.
- [x] [#6](https://github.com/minov87/kakaopay-preexam/issues/6) 발급된 쿠폰 중 당일 만료된 쿠폰 목록 조회 API
  - 사용자에게 발급된 기준으로 사용/미사용 구분을 하지 않고 당일 만료 기준 데이터를 조회한다.
  - 조회 데이터에는 (쿠폰 번호, 쿠폰 상태, 쿠폰 만료일) 를 반환한다.
  - 조회 데이터가 없을 경우 빈 배열 [] 형태로 반환한다.

##### 제약사항 필수
- [x] [#10](https://github.com/minov87/kakaopay-preexam/issues/10) 단위 테스트 (Unit Test) 코드를 개발하여 각 기능을 검증.
  - 각 서비스 및 유틸리티 테스트 케이스 적용.
- [x] README.md 파일을 추가하여, 개발 프레임워크, 문제해결 전략, 빌드 및 실행 방법을 기술.

##### 제약사항 선택
- [x] [#9](https://github.com/minov87/kakaopay-preexam/issues/9) JWT를 이용 Token 기반 API 인증 기능 개발 및 각 API 호출시 HTTP Header에 발급받은 토큰을 가지고 호출.
  - Token 이 존재하지 않을 경우 에러를 반환한다.
  - Token 정보에 문제가 있을 경우 에러를 반환한다.
  - ※ 단, 아래의 회원가입 및 로그인 API의 경 Token 인증에서 제외한다. (토큰을 받아야 하기 때문에)
  - [x] [#7](https://github.com/minov87/kakaopay-preexam/issues/7) signup 계정생성 API: ID, PW를 입력 받아 내부 DB에 계정을 저장하고 토큰을 생성하여 출력.
    - 이미 가입된 정보일 경우 에러를 반환한다.
    - 회원 가입이 완료되면 해당 사용자의 JWT 토큰 (accessToken, refreshToken) 을 반환한다.
    - JWT 토큰의 만료시간은 accessToken 의 경우 10분, refreshToken 의 경우 60분 으로 정한다.
    - 비밀번호는 Springfreamework Security 를 활용하여 암호화 저장한다.
  - [x] [#8](https://github.com/minov87/kakaopay-preexam/issues/8) signin 로그인 API: 입력으로 생성된 계정 (ID, PW)으로 로그인 요청하면 토큰을 발급.
    - 일치하는 회원이 존재하지 않을 경우 에러를 반환한다.
    - 비밀번호가 일치하지 않을 경우 에러를 반환한다.
    - 로그인이 완료되면 해당 사용자의 JWT 토큰 (accessToken, refreshToken) 을 반환한다.
    - JWT 토큰의 만료시간은 accessToken 의 경우 10분, refreshToken 의 경우 60분 으로 정한다.
- [ ] 100억개 이상 쿠폰 관리 저장 관리 가능 하도록 구현
- [ ] 10만개 이상 벌크 csv Import 기능
- [x] 대용량 트랙픽(TPS 10K 이상)을 고려한 시스템 구현
  - 최대한 메소드를 역할별로 쪼개고 재사용 부분에 포인트를 맞추었고,   
    DB의 경우 재사용성 및 관리의 장점을 살리기 위하여 JPA 기반으로    
    개발 작업을 하였지만, 짧은 시간에 해결을 하려다 보니 부족한 부분이 많으리라 생각된다.
  - 좀더 MSA 취지에 맞게 다듬는다면 좋은 성능을 낼 수 있을 것으로 예상한다.
- [ ] 성능테스트 결과 / 피드백

### 빌드 및 실행방법