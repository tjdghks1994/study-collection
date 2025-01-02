## 도커 네트워크

-------

### 네트워크

여러 개의 장치들이 서로 연결되어서 정보를 주고 받을 수 있는 시스템을 말한다. <br>
물리적인 장치와 매체들로 연결되어 있다. (라우터, 공유기, 랜선, 케이블 등)

### IP (Internet Protocol)

인터넷에서 데이터 전송을 위한 `주소 지정` 및 `라우팅`을 담당하는 프로토콜이다. <br>
네트워크로 연결된 각 장치들은 고유한 IP 주소를 가져, 서로를 식별할 수 있다. <br>
> 📌 **라우팅이란?** <br>데이터 패킷이 출발지에서 목적지까지 효율적으로 전달될 수 있도록 경로를 설정하는 것

- 공인 IP : 전 세계에서 유일한 IP 주소
- 사설 IP : 소속된 장비 내에서 유일한 IP 주소
- 공인망 : 외부 네트워크(공인 IP)를 사용하는 통신망
- 사설망 : 내부 네트워크(사설 IP)를 사용하는 통신망
- **공인 IP에서 사설 IP로 나눌 때 네트워크 장비가 필요** <br>
  ( 일반 가정에서는 공유기를 사용, 기업에서는 라우터를 사용 )
- **즉, 하나의 공인 IP를 통해 사설 IP로 나누어 여러 기기들이 인터넷을 사용할 수 있다.**

### NAT (Network Address Translation)

`내부 -> 외부` **(OutBound)** <br>
**공인 IP와 사설 IP를 맵핑해주는 기술을 말한다.** <br>
공인 IP와 사설 IP의 맵핑 테이블을 만들어 어떤 서버로 전달할지에 대한 정보를 기록한다. <br>
사설망을 구성하는 라우터 장비는 모두 NAT 기능을 포함하고 있다.

### 포트 포워딩

`외부 -> 내부` **(InBound)** <br>
**외부에서 내부(사내망)로 접근할 때 사용하는 기술을 말한다.** <br>
사용자가 직접 NAT 테이블과 같은 맵핑 정보를 관리한다.

### 가상 네트워크

서버 한 대 안에서 여러 네트워크를 구성하는 기술을 말한다.
- 컨테이너의 네트워크를 구성 
- 도커를 설치하고 실행시키면 도커는 가상의 네트워크인 `브릿지 네트워크`와 가상의 공유기인 `docker0(도커 제로 - 브릿지)`를 만든다.
- 브릿지는 가상의 IP 주소를 할당 받는다.
- 도커에서 컨테이너를 실행하면 도커는 브릿지 네트워크의 IP 주소 범위 안에서 가상 IP 주소를 할당해준다.
- **같은 브릿지 네트워크내에서 실행된 컨테이너들은 서로 통신할 수 있다.**
- 반대로 서로 다른 브릿지 네트워크에서 실행된 컨테이너라면 기본적으로 서로 통신할 수 없다.

### 명령어

- `docker network ls` : 네트워크 리스트 조회
- `docker network inspect 네트워크명` : 네트워크 상세정보 조회
- `docker network create 네트워크명` : 네트워크 생성
- `docker network rm 네트워크명` : 네트워크 삭제
- `docker run -p HostOS의 포트:컨테이너의 포트` : 컨테이너 실행 시 포트 포워딩 설정

> 📌 포트 포워딩으로 등록된 포트는 중복해서 설정할 수 없다. (에러 발생 및 컨테이너 실행 실패)

### 도커 DNS 서버

- 도커는 컨테이너들이 기본적으로 사용할 수 있는 DNS 서버를 제공한다.
- DNS 서버덕에 컨테이너들은 IP가 아닌 컨테이너의 이름으로 서로 통신할 수 있다.
- 도커의 DNS 서버는 외부의 DNS 서버와 연동이 되어 있어 외부 도메인을 사용 가능하다.
- <p style="color:pink;">
  단, 기본으로 생성되는 브릿지(bridge) 네트워크는 이 DNS 기능이 제공되지 않는다.
  </p>
  <u><b>( 도커의 DNS 서버를 사용하기 위해서는 새롭게 생성한 브릿지 네트워크로 가능하다. )</b></u>