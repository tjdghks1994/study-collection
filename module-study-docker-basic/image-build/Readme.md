## 이미지 빌드

-------

### 이미지 레이어

- 도커 이미지는 `Layered File System` 으로 구성되어 있다. 
- 이미지는 여러개의 레이어로 구성함으로써 재사용하기 유리한 구조가 되었다. 
- `Layered File System` 구조로 인해 효율적인 데이터 저장과 전송이 가능하다. 
- 이미지의 레이어는 이전 레이어의 변경 사항이 저장된다.
- 이미지에서 한 번 저장된 레이어는 변경할 수 없으며, **변경 사항이 있으면 새로운 레이어로 저장**해야한다. 
- **이미지 레이어는 읽기 전용 레이어지만**, 컨테이너 실행 시 읽기/쓰기 가능한 새로운 레이어가 추가된다.
- 각 레이어는 이전 레이어 위에 순차적을 쌓이며, 여러 이미지 간에 공유될 수 있다.
- 레이어를 통해 중복 데이터를 최소화하고, 빌드 속도를 높이며 저장소를 효율적으로 사용할 수 있게된다.
- <u>Copy-on-Write (COW) 전략</u>을 사용한다.

> ✅ COW 전략이란 다음 레이어에서 이전 레이어의 특정 파일을 수정할 때, 
> 해당 파일의 복사본을 만들어서 변경 사항을 적용한다. <br>
> 즉, 원래 레이어는 수정되지 않고 그대로 유지된다.


### 이미지 생성 방법

- 커밋 : 현재 컨테이너의 상태를 이미지로 저장
- 빌드 : `Dockerfile` 을 통해 이미지를 저장 (커밋 방식을 기반으로 동작)

### 명령어

- `docker image history 이미지명` : 이미지의 레이어 이력 조회
- `docker run -it --name 컨테이너명 이미지명 bin/bash` : 컨테이너 실행과 동시에 터미널 접속 <br>
  ( 이미지 내부의 파일 시스템을 확인해보거나 디버깅하는 용도로 많이 사용되는 방법 )
- `docker commit -m 커밋명 실행중인컨테이너명 생성할이미지명` : 실행 중인 컨테이너 상태를 이미지로 생성
- `docker build -t 이미지명 Dockerfile경로` : 도커파일을 통해 이미지 빌드

### 이미지 빌드

- 도커는 IaC 방법을 활용해서 코드로 이미지를 관리하는 방식이며, 이 작업을 이미지 빌드라 한다.
- 도커는 Dockerfile 이라는 소스코드를 사용해서 인프라의 상태를 저장하는 이미지를 만들 수 있다.
- 그래서 도커 이미지를 생성할 때는 대부분 이미지 빌드 방식을 많이 사용
- **빌드 방식은 컨테이너를 생성하고 커밋하는 것을 도커가 대신 수행하는 것이다.**

> ✅ IaC(Infrastructure as Code)는 인프라 상태를 코드로 관리하는 것을 말한다. <br>
> ✅ 코드에 상세 작업 내용이 기재되어 있고, 작업 내용은 프로그램이 대신 수행한다. <br>
> ✅ 상세 작업내용을 GitHub 와 같은 소스코드 레파지토리에 저장하며 인프라의 상태도 버전관리를 할 수 있다.


### 기본 Dockerfile 지시어

- `FROM 이미지명` : 베이스 이미지를 지정 (Dockerfile 에서 필수로 표시)
- `WORKDIR 폴더명` : 작업 디렉토리를 지정 (새로운 레이어 추가)
- `USER 유저명` : 명령을 실행할 사용자 변경 (새로운 레이어 추가)
- `COPY 빌드컨텍스트경로 레이어경로` : 빌드컨텍스트의 파일을 레이어에 복사 (새로운 레이어 추가)
- `ARG 변수명 변수값` : 이미지 빌드 시점의 환경변수 설정
- `ENV 변수명 변수값` : 이미지 빌드 및 컨테이너 실행 시점의 환경변수 설정 (새로운 레이어 추가)
- `RUN 명령어` : 명령어 실행 (새로운 레이어 추가)
- `EXPOSE 포트번호` : 컨테이너가 사용할 포트를 명시
- `ENTRYPOINT ["명령어"]` : 고정된 명령어를 지정
- `CMD ["명령어"]` : 컨테이너 실행 시 명령어 지정

### 빌드 컨텍스트

- **이미지를 빌드할 때 사용되는 폴더**
- 이미지 빌드 방식은 도커 데몬에게 도커 파일(Dockerfile)과 빌드에 사용되는 파일들을 전달해 주어야 하는데, 
  **도커 데몬에게 전달해 주는 폴더가 빌드 컨텍스트이다.**
- 도커 빌드 명령(docker build)을 사용하면 빌드 컨텍스트가 도커 데몬에게 통째로 전달된다.
- 도커 데몬은 빌드 컨텍스트에 있는 파일만 COPY 명령으로 복사할 수 있다.
- 빌드 컨텍스트의 크기가 커질수록 전송 시간이 길어지고 빌드에 문제가 발생할 수 있다.
- 빌드 컨텍스트에는 실제로 이미지 빌드에 필요한 파일들만 넣어놓는 것이 유리하다. **(.dockerignore 활용)**




