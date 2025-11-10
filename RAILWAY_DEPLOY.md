# Railway 배포 가이드

## 📋 배포 준비사항

### 1. 필수 파일 확인
- ✅ `Procfile` - Railway 실행 명령어
- ✅ `railway.json` - Railway 배포 설정
- ✅ `nixpacks.toml` - 빌드 설정
- ✅ `application-railway.yml` - Railway 환경 설정

### 2. 의존성 추가 확인
- ✅ `spring-boot-starter-actuator` - 헬스체크 엔드포인트

## 🚀 Railway 배포 단계

### 1단계: Railway 계정 및 프로젝트 생성
1. [Railway](https://railway.app) 회원가입/로그인
2. "New Project" → "Deploy from GitHub repo" 선택
3. Brandler_BE 리포지토리 선택

### 2단계: 데이터베이스 설정
1. Railway 대시보드에서 "Add Service" → "Database" → "MySQL" 선택
2. 데이터베이스가 생성되면 자동으로 환경변수가 설정됩니다:
   - `DATABASE_URL`
   - `DATABASE_USERNAME` 
   - `DATABASE_PASSWORD`

### 3단계: 환경변수 설정
Railway 대시보드 → Variables 탭에서 다음 환경변수 확인:
```
SPRING_PROFILES_ACTIVE=railway
PORT=8080
DATABASE_URL=(자동 설정됨)
DATABASE_USERNAME=(자동 설정됨) 
DATABASE_PASSWORD=(자동 설정됨)
```

### 4단계: 배포 실행
1. Railway가 자동으로 빌드 및 배포를 시작합니다
2. 빌드 로그에서 진행상황을 확인할 수 있습니다
3. 배포 완료 후 제공되는 URL로 접속 가능합니다

## 🔍 배포 후 확인사항

### 헬스체크
```
GET https://your-app.railway.app/actuator/health
```

### API 문서
```
GET https://your-app.railway.app/swagger-ui.html
```

### 주요 엔드포인트 테스트
```
GET https://your-app.railway.app/brands
GET https://your-app.railway.app/products/{brandId}
```

## 🛠️ 트러블슈팅

### 빌드 실패 시
1. Gradle 빌드 로그 확인
2. Java 17 버전 확인
3. 의존성 충돌 확인

### 데이터베이스 연결 실패 시
1. DATABASE_URL 형식 확인
2. MySQL 서비스 상태 확인
3. 네트워크 연결 확인

### 메모리 부족 시
1. Railway 플랜 업그레이드 고려
2. JVM 힙 메모리 설정 조정

## 📝 추가 설정 (선택사항)

### 커스텀 도메인 설정
1. Railway 대시보드 → Settings → Domains
2. 원하는 도메인 추가
3. DNS 설정 업데이트

### 로그 모니터링
1. Railway 대시보드 → Logs 탭에서 실시간 로그 확인
2. 애플리케이션 로그 레벨 조정 가능

### 자동 배포 설정
- GitHub 리포지토리와 연결되어 있어 push 시 자동 배포됩니다
- 특정 브랜치만 배포하려면 Settings에서 설정 가능합니다

## 🔗 유용한 링크
- [Railway 공식 문서](https://docs.railway.app/)
- [Spring Boot Railway 가이드](https://docs.railway.app/guides/spring-boot)
- [MySQL Railway 가이드](https://docs.railway.app/databases/mysql)
