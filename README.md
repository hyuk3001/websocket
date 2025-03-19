# WebSocket

## 1. 주제
- 반려동물 및 유기 동물 GPS 실시간 위치 추적 시스템 개발

### 2. 목표
- 동물의 현 위치를 효율적으로 모니터링하기 위한 실시간 추적 시스템 구축.
- 데이터를 안정적으로 서버에 저장 및 웹/모바일 클라이언트로 제공.
- 위치 데이터를 기반으로 지도에 실시간으로 시각화.

### 2. 기술 스택
- Backend : Java, Spring Boot, JWT, Spring Security, WebSocket
- Frontend : React, Tailwind CSS, Axios
- Database : Redis (Pub/Sub), PostgreSQL (PostGIS)
- Infra : Docker

### 3. 기능
- GPS 위치 데이터 수집 및 전송
- 실시간 위치 시각화
- 이동 경로 추적
- 안전구역 이탈 시 알림
- 최단 경로 계산 및 이동 시간 예측 

### 4. 아키텍쳐

