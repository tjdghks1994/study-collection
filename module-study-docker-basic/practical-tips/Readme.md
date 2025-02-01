## 도커 실무 팁's

----

### 레이어 관리

- `Dockerfile`에 작성된 지시어 하나 당 레이어 한 개가 추가된다.
- 불필요한 레이어가 많아지면 이미지 크기가 늘어나고, 빌드 속도 또한 느려진다.
- `Run` 지시어는 `&&` 을 활용해 최대한 하나로 처리하게 하는 것이 좋다. <br>
  ( Run 지시어 하나 당 한 개의 레이어가 추가되기 때문 ) <br>
  **즉, Run 지시어 1개로 N개의 명령어를 수행하도록 할 수 있다.**
- 이미지의 크기는 가능한 작을수록 좋다. (배포 속도와 네트워크 사용량에 유리)
  - 애플리케이션의 크기를 가능한 작게 관리한다 (여러 모듈로 분리)
  - 베이스 이미지는 가능한 작은 이미지를 사용한다. `(일반적으로 alpine os를 활용)`
  - `.dockerignore` 파일을 사용해서 불필요한 파일은 제거한다.

----

### 캐싱 활용 빌드

- `Dockerfile`에 작성된 순서대로 결과 이미지의 레이어가 쌓이며, **도커는 각 단계의 결과 레이어를 캐시 처리해둔다.** <br>
  만약 `Dockerfile`에서 지시어가 변경되지 않으면 다음 빌드에서는 레이어를 **재사용(캐시 활용)** 한다.
- `COPY`, `ADD` 명령의 경우, 빌드 컨텍스트의 파일 내용이 변경되어도 캐시를 사용하지 않는다. <br>
  (지시어는 변경되지 않았지만 파일의 내용이 변경되었기 때문에 캐시를 사용하지 않는 것)
- 명령어가 달라졌다면 <u>**해당 레이어와 이후의 모든 레이어는 캐시를 사용하지 않고**</u> 새로운 레이어가 만들어진다.
- **잘 변경되지 않는 파일들을 아래 레이어에 배치한다면 캐시를 활용하는 빈도를 높일 수 있다**

----

### 3 Tier 아키텍처 구성하기

- 백엔드 애플리케이션이 외부에 노출되어 있을 경우, 개발자가 의도하지 않은 `API`를 호출할 위험이 있다.
- 웹 프론트 서버, 웹 백엔드 서버, 데이터베이스 서버로 나누어 유기적으로 상호작용하는 3 Tier 아키텍처를
  구성하여, 위 문제점을 해결할 수 있다.
- 웹 프론트 서버로 자주 활용하는 `Nginx`는 특정 경로로 온 요청을 지정한 서버로 전달한다.
- `Nginx`의 프록시 기술을 활용하면 백엔드 애플리케이션이 `API` 이외의 경로로 접근하는 것이 물리적으로 차단된다.

----

### 이중화 DB 구성

- 단일서버로만 구성시에 장애가 발생하면 전체 서비스의 장애로 이어지게 된다.
- 이중화 구성 시, 하나의 서버가 실패해도 다른 서버가 동일한 역할을 수행하여 `고가용성`을 보장하게 된다.
- 도커에서 **이중화 구성 방법**으로는 아래와 같은 방법이 있다.
  - 모든 컨테이너가 동시에 같은 볼륨을 사용
  - **각각의 컨테이너에 별도의 볼륨을 사용**
- 동시에 같은 볼륨을 사용하면 구성은 간단하지만 볼륨에 문제가 생길 경우 대처가 어렵다.
- 동시에 같은 볼륨을 사용하면 볼륨의 성능에 부하가 생길 수 있다.
- 각각의 컨테이너에 별도의 볼륨을 사용하면 데이터의 싱크를 맞추는 처리를 별도로 해야한다.
- **각각의 컨테이너가 별도의 볼륨을 가질 경우 데이터를 동기화하는 방법**은 아래와 같다.
  - 프라이머리-스탠바이 복제 구조 (Primary-Standby Replication)
  - 프라이머리-프라이머리 복제 구조 (Primary-Primary Replication)
- `프라이머리-스탠바이 복제 구조`의 경우에는 프라이머리 서버에만 `쓰기 작업`을 수행하며, 
  프라이머리의 상태를 스탠바이에 복제하는 방식이다. <br>
  스탠바이 서버는 `읽기 전용`으로만 사용되며, 읽기 전용 스탠바이 서버를 여러 대 사용할 수 있다.
- `프라이머리-프라이머리 복제 구조`의 경우, 모든 서버에서 읽기/쓰기 작업을 수행한다. <br>
  여러 서버에서 동시에 쓰기 작업이 일어나기 때문에 동기화 구성 작업이 복잡하다.

아래는 이중화 DB 구성 실행 명령어에 대한 예시이다.
```` dockerfile
#1. 테스트용 네트워크 생성
docker network create postgres

#2. 프라이머리 노드 실행
docker run -d \
  --name postgres-primary-0 \
  --network postgres \
  -v postgres_primary_data:/bitnami/postgresql \
  -e POSTGRESQL_POSTGRES_PASSWORD=adminpassword \
  -e POSTGRESQL_USERNAME=myuser \
  -e POSTGRESQL_PASSWORD=mypassword \
  -e POSTGRESQL_DATABASE=mydb \
  -e REPMGR_PASSWORD=repmgrpassword \
  -e REPMGR_PRIMARY_HOST=postgres-primary-0 \
  -e REPMGR_PRIMARY_PORT=5432 \
  -e REPMGR_PARTNER_NODES=postgres-primary-0,postgres-standby-1:5432 \
  -e REPMGR_NODE_NAME=postgres-primary-0 \
  -e REPMGR_NODE_NETWORK_NAME=postgres-primary-0 \
  -e REPMGR_PORT_NUMBER=5432 \
  bitnami/postgresql-repmgr:15

#3. 스탠바이 노드 실행
docker run -d \
  --name postgres-standby-1 \
  --network postgres \
  -v postgres_standby_data:/bitnami/postgresql \
  -e POSTGRESQL_POSTGRES_PASSWORD=adminpassword \
  -e POSTGRESQL_USERNAME=myuser \
  -e POSTGRESQL_PASSWORD=mypassword \
  -e POSTGRESQL_DATABASE=mydb \
  -e REPMGR_PASSWORD=repmgrpassword \
  -e REPMGR_PRIMARY_HOST=postgres-primary-0 \
  -e REPMGR_PRIMARY_PORT=5432 \
  -e REPMGR_PARTNER_NODES=postgres-primary-0,postgres-standby-1:5432 \
  -e REPMGR_NODE_NAME=postgres-standby-1 \
  -e REPMGR_NODE_NETWORK_NAME=postgres-standby-1 \
  -e REPMGR_PORT_NUMBER=5432 \
  bitnami/postgresql-repmgr:15

# 4. SHELL1, SHELL2 각 컨테이너의 로그 확인
docker logs -f postgres-primary-0
docker logs -f postgres-standby-1

# 5. 프라이머리 노드에 테이블 생성 및 데이터 삽입 
docker exec -it -e PGPASSWORD=mypassword postgres-primary-0 psql -U myuser -d mydb -c "CREATE TABLE sample (id SERIAL PRIMARY KEY, name VARCHAR(255));"
docker exec -it -e PGPASSWORD=mypassword postgres-primary-0 psql -U myuser -d mydb -c "INSERT INTO sample (name) VALUES ('John'), ('Jane'), ('Alice');"

#6. 스탠바이 노드에 데이터가 동기화되어 있는지 확인
docker exec -it -e PGPASSWORD=mypassword postgres-standby-1 psql -U myuser -d mydb -c "SELECT * FROM sample;"

#7. 환경 정리
docker rm -f postgres-primary-0 postgres-standby-1
docker volume rm postgres_primary_data postgres_standby_data
docker network rm postgres

````

----

### 컨테이너 애플리케이션 최적화

- **컨테이너가 사용할 수 있는 리소스 사용량을 제한한다.**
   - `docker run --cpus={cpu core 수}` : 컨테이너가 사용할 최대 cpu 코어 수 정의
   - `docker run --memory={메모리 용량}` : 컨테이너가 사용할 최대 메모리 정의 (b,k,m,g 단위로 지정 가능)
   - `docker stats {컨테이너명/ID}` : 컨테이너 리소스 사용량 조회
   - `docker events` : Host OS 에서 발생하는 이벤트 로그 조회
   - 지정한 cpu 코어보다 사용량이 초과할 경우 <u>**cpu 스로틀링**</u> 발생한다. <br>
     ( cpu 스로틀링은 시스템이 애플리케이션의 cpu 사용을 제한하는 것을 의미한다. )
   - 지정한 memory 보다 사용량이 초과할 경우 OOM(Out Of Memory) killer 프로세스가 실행되고 
     컨테이너가 강제로 종료된다. 
  

- **자바 가상 머신 (JVM) 튜닝**
  - 자바 애플리케이션이 사용할 수 있는 메모리 영역인 힙(Heap) 메모리를 별도로 관리해야 한다.
  - 보통 전체 서버 메모리의 `50~80%`를 힙 메모리 사용량으로 설정한다.
  - 자바 애플리케이션을 실행할 때 `-Xmx` 라는 옵션을 통해 힙 메모리의 최대값을 지정할 수 있다.
  - 자바에서는 컨테이너에서 애플리케이션을 실행할 때, 컨테이너의 리미트 값을 
    인식해서 <u>자동으로 힙 메모리를 조정하는 기능을 제공</u>한다. <br>
    **단, 자바 10버전 이상이라면 위 기능은 자동으로 활성화되어 있다.**

