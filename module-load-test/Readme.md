## 용어 정리

-----

### 처리량 (Throughput - 쓰루풋)
> 부하 테스트에서 서비스가 <u>**1초당 처리할 수 있는 트래픽 양**</u>을 보고 Throughput 이라고 부른다. <br>
> 단위는 `TPS` (Transaction Per Seconds, **1초당 처리한 트랜잭션의 수**) 를 많이 활용한다. <br>
> 예를 들어, 서비스가 1초에 최대 100개의 API 요청을 처리할 수 있다면 해당 서비스의 Throughput 은 100 TPS 라고 말한다.

### 지연 시간 (Latency - 레이턴시)
> 부하 테스트에서의 Latency 는 <u>**요청에 대한 응답 시간**</u>을 의미한다. <br>
> 예를 들어, 서비스가 부하 테스트를 했을 때 평균 응답 시간이 2.5초일 경우 평균 Latency 가 2.5초라고 말한다. <br>
> 즉, 하나의 API 에 요청을 보냈을 때 응답받기까지의 시간이 2.5초 정도 걸린다는 것이다.

### 병목 지점 (Bottleneck Point)
> 전체 시스템에서 <u>**특정 서버 자원(CPU, Memory 등)이 한계에 도달해 전체 성능이 저하되는 구간**</u>을 의미한다. <br>
> 시스템의 성능을 개선하기 위해서는 `병목 지점`을 개선해야한다. <br>
> **병목 지점의 Throughput 이 곧 전체 Throughput 이 된다.** <br>
> 특정 병목 지점을 해소하면 다른 곳에서 새로운 병목 지점이 발생한다.

### 모니터링 (Monitoring)
> 모니터링은 어떤 대상을 지속적으로 감시한다는 뜻이다. <br>
> 부하 테스트 과정 중에 성능을 개선하려면 <u>**병목 지점**</u>을 먼저 파악해야 한다. <br>
> 병목 지점을 파악하기 위해서는 각 컴퓨터의 `CPU 사용률`과 `메모리 사용률`과 같은 값들을 지속적으로 확인할 수 있어야 한다. <br>
 ( 여기서 컴퓨터란 로드 밸런서, 백엔드 서버, DB, 캐시 등등을 말한다. ) <br>
> **즉, 병목 지점을 파악하기 위해서는 부하 테스트를 하면서 컴퓨터의 자원(CPU, 메모리 등)을 모니터링해야 한다.**

### 메트릭 (Metric)
> `수치 데이터` 또는 `측정값`을 의미한다. <br>
> 모니터링을 통해 측정하는 CPU, 메모리 등의 값을 전부 메트릭이라고 부른다.

### CPU
> 명령어(코드)를 해석하고 실행한다. <br>
> 연산 작업을 수행한다.

### 메모리 (Memory)
> **CPU 가 작업하기 위해 임시로 사용하는 공간이다.** <br>
> 디스크보다 메모리로부터 데이터를 가져올 때 **속도가 훨씬 빠르기 때문에 사용**한다. <br>
> CPU 는 명령어를 해석해서 실행하거나 연산 작업을 할 때는 무조건 메모리를 통해서만 데이터를 가져온다. <br>
> CPU 는 디스크에 직접 접근하지 않는다. <br>
> 실행할 프로그램의 코드, 변수(전역 변수, 매개 변수...) 등을 메모리에서 주로 관리하고 있다.
> RAM 이라 부르기도 한다.

### 디스크 (Disk)
> C 드라이브와 같은 **컴퓨터 저장 공간**이다. <br>
> 영구적으로 데이터, 파일 등을 저장한다. <br>
> 데이터를 가져올 때 속도가 상대적으로 느리다.

### ✅ 각 컴퓨터 별로 CPU, 메모리, 디스크를 어떻게 사용할까?
**1. DB**
- `디스크`로부터 데이터를 조회해서 `메모리`에 올린다.
- `메모리`에서 연산, 필터링, 집계 등의 작업을 `CPU`가 처리한다.
- DB의 특성상 많은 양의 데이터를 가지고 작업을 해야 하는 경우가 빈번해서 <u>`CPU`, `메모리` 둘 다를 많이 사용하는 편</u>이다.

**2. 백엔드 서버**
- 백엔드 서버의 로직에 `파일`을 읽어오거나, `파일`을 생성하는 로직이 없다면 `디스크`는 거의 사용할 일이 없다.
  - 파일 업로드나 파일 조회 같은 건 `S3`와 같은 서비스를 활용해 별도로 분리하는 추세
- `DB`로부터 불러오는 데이터의 양이 그렇게 크지 않을 경우 `메모리`를 많이 사용할 일이 없다.
- 많은 요청에 대해 로직을 처리해야 하기 때문에 <u>`CPU`를 많이 사용하는 편</u>이다.
- 복잡한 연산 작업(인코딩, 디코딩, 암호화 등)을 사용할 경우 <u>`CPU`를 많이 사용</u>한다.

**3. 로드밸런서**
- 요청이 들어오면 적절하게 서버들한테 골고루 트래픽을 분배해주는 역할을 한다.
  - 임시로 저장해야 하는 데이터가 많은 게 아니기 때문에 `메모리`를 많이 사용할 일이 없다.
- 많은 요청을 분배해주는 로직(=연산)을 처리하기 때문에 <u>`CPU`를 많이 사용</u>한다.

**4. 캐시(Redis)**
- 캐시의 특성상 빠르게 데이터를 조회해야 하기 때문에 `디스크`에 데이터를 저장하지 않고, `메모리`에 데이터를 저장해둔다.
  - `디스크`보다 `메모리`가 데이터 접근 속도(읽기, 쓰기)가 훨씬 빠르기 때문
- 캐시의 주 역할은 **데이터 조회다**. 연산할만한 작업은 많이 없다.
- 따라서 `CPU` 보다는 <u>`메모리`를 많이 사용하는 편</u>이다.

### 가용성 (Availability)
> 시스템이 서비스를 정상적으로 제공할 수 있는 가능성을 의미한다. <br>
> 즉, 서비스에 장애가 발생할 가능성이 극히 작은 시스템을 가용성이 높든 시스템이라고 말한다. <br>
> 가용성이 높고 낮음은 서비스의 정상 가동률 (%)로 표시된다.

### 시스템 이중화
> 시스템의 일부분을 사용할 수 없게 되어도 다른 시스템을 이용하여 서비스를 계속 이용할 수 있게 만드는걸 말한다. <br>
> 시스템을 이중화 설계하면 **다운 타임을 줄일 수 있게 된다.** <br>
> 즉, 서비스의 **가용성을 높일 수** 있게 된다.

### 수평적 확장 (Scale Out)
> 특정 시스템 성능을 올리기 위해 시스템 개수를 늘리는 걸 말한다. <br>
> **가용성이 증가**하고 **확장에 제한이 없다.** <br>
> 서버 축소/확장이 쉽다 <br>
> 여러 대의 서버를 한 서버처럼 사용하기 위한 추가 작업이 필요하다 <br>
  ex ) 로드 밸런서

### 수직적 확장 (Scale Up)
> 특정 시스템 성능을 올리기 위해 시스템 스펙(CPU, 메모리 등)을 업그레이드 하는 방식을 말한다. <br>
> 여러 대의 서버를 한 서버처럼 사용하기 위한 추가 작업이 필요 없다 <br>
> 심플한 인프라 구성을 할 수 있어 관리 비용(시간,노력,비용)이 적게 들어간다. <br>
> **가용성이 감소**하고, 시스템 스펙에는 제한이 존재하므로 **확장에 제한**이 있다. <br>
> 서버 스펙을 변경할 때마다 **서비스 중단(다운 타임)이 불가피**하다.


### 캐싱 (Caching)
> 데이터를 더 빠르게 엑세스(조회) 할 수 있는 곳에 임시로 저장하는 방식을 의미한다. <br>
> **Redis, CDN(Content Delivery Network)** 등이 있다. 

### ✅ 병목 지점에 따른 성능 개선 방법
**1. EC2(웹 애플리케이션 서버)가 병목 지점일 경우**
- 애플리케이션 로직에서 비효율적인 부분을 개선
- 정적 파일 서버(S3, Cloudfront) 분리하기
- 로드밸런서(ELB)를 활용해 수평적 확장하기
- 수직적 확장하기

**2. RDS(데이터베이스 서버)가 병목 지점일 경우**
- 비효율적인 쿼리 개선하기 (인덱스 활용, 역정규화, SQL 문 튜닝)
- 수직적 확장하기
- 읽기 전용 데이터베이스 (Read Replica) 도입하기
- 캐시 서버 도입하기